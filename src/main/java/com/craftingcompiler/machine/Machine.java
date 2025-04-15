package com.craftingcompiler.machine;

import com.craftingcompiler.code.Code;
import com.craftingcompiler.code.Instruction;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Machine {

    private static List<GCObject> objects;
    public static List<StackFrame> callStack;

    public static void execute(Map.Entry<List<Code>, Map<String, Integer>> objectCode) {
        callStack = new ArrayList<>();
        objects = new ArrayList<>();
        Machine.callStack.add(new StackFrame());
        var codes = objectCode.getKey();
        var functionTable = objectCode.getValue();
        while (true) {
            Code code = codes.get(getLastStack().getInstructionPointer());
            try {
                execute(code, functionTable);
            } catch (RuntimeException e) {
                if (e.getMessage().equals("EXIT")) {
                    return;
                } else {
                    throw e;
                }
            }
        }
    }

    private static void execute(Code code, Map<String, Integer> functionTable) {
        Instruction instruction = code.getInstruction();
        if (instruction == Instruction.EXIT) {
            removeLastStack();
            throw new RuntimeException("EXIT");
        }

        if (instruction == Instruction.GET_GLOBAL) {
            String name = (String) code.getOperand();
            pushOperand(functionTable.getOrDefault(name, null));
        } 
        else if (instruction == Instruction.CALL) {
            Object operand = popOperand();
            StackFrame stackFrame = new StackFrame();
            if (operand instanceof Number) {
                stackFrame.setInstructionPointer((int) operand);
                for (int i = 0; i < (int) code.getOperand(); i++) {
                    stackFrame.addVariable(popOperand());
                }
                callStack.add(stackFrame);
                return;
            }
            if (operand instanceof Function<?, ?>) {
                List<Object> arguments = new ArrayList<>();
                for (int i = 0; i < (int) code.getOperand(); i++) {
                    arguments.add(popOperand());
                }
                pushOperand(((Function<List<Object>, Object>) operand).apply(arguments));
            } else {
                pushOperand(null);
            }
        } 
        else if (instruction == Instruction.ALLOC) {
            int extraSize = (int) code.getOperand();
            getLastStack().resizeVariables(extraSize);
        } 
        else if (instruction == Instruction.RETURN) {
            Object result = null;
            if (!getLastStack().getOperandStack().isEmpty()) {
                result = getLastStack().peekOperand();
            }
            removeLastStack();
            getLastStack().pushOperand(result);
            collectGarbage();
        } 
        else if (instruction == Instruction.PRINT) {
            StringBuilder sb = new StringBuilder();
            for (var i = 0; i < (int) code.getOperand(); i++) {
                sb.append(popOperand());
            }
            System.out.print(sb);
        } 
        else if (instruction == Instruction.LOGICAL_OR) {
            if ((boolean) popOperand()) {
                pushOperand(true);
                getLastStack().setInstructionPointer((int) code.getOperand());
                return;
            }
        } 
        else if (instruction == Instruction.LOGICAL_AND) {
            if (!(boolean) popOperand()) {
                pushOperand(false);
                getLastStack().setInstructionPointer((int) code.getOperand());
                return;
            }
        }
        else if (instruction == Instruction.GET_LOCAL) {
            var index = (int) code.getOperand();
            pushOperand(getLastStack().getVariable(index));
        } 
        else if (instruction == Instruction.SET_LOCAL) {
            var index = (int) code.getOperand();
            getLastStack().setVariable(index, peekOperand());
        } 
        else if (instruction == Instruction.ADD || instruction == Instruction.SUB || instruction == Instruction.MUL
                || instruction == Instruction.DIV || instruction == Instruction.MOD) {
            pushOperand(operateArithmetic(instruction));
        } 
        else if (instruction == Instruction.PRINT_LINE) {
            System.out.println();
        } 
        else if (instruction == Instruction.PUSH_STR || instruction == Instruction.PUSH_NUM
                || instruction == Instruction.PUSH_BOOL) {
            pushOperand(code.getOperand());
        } 
        else if (instruction == Instruction.PUSH_NULL) {
            pushOperand(null);
        } 
        else if (instruction == Instruction.JUMP) {
            getLastStack().setInstructionPointer((int) code.getOperand());
            return;
        } 
        else if (instruction == Instruction.CONDITION_JUMP) {
            if (!(boolean) popOperand()) {
                getLastStack().setInstructionPointer((int) code.getOperand());
                return;
            }
        } 
        else if (instruction == Instruction.PUSH_ARRAY) {
            var size = (int) code.getOperand();
            Object[] arr = new Object[size];
            for (int i = 0; i < size; i++) {
                arr[i] = popOperand();
            }
            CustomArray result = new CustomArray(arr);
            pushOperand(result);
            objects.add(result);
        } 
        else if (instruction == Instruction.GET_ELEMENT) {
            var index = popOperand();
            var sub = popOperand();
            if (sub instanceof CustomArray && index instanceof Number) {
                pushOperand(((CustomArray) sub).get((int) index));
            } else {
                pushOperand(null);
            }
        } 
        else if (instruction == Instruction.SET_ELEMENT) {
            var index = popOperand();
            var sub = popOperand();
            if (sub instanceof CustomArray && index instanceof Number) {
                pushOperand(((CustomArray) sub).set((int) index, peekOperand()));
            }
        } 
        else if (instruction == Instruction.EQ || instruction == Instruction.NOT_EQ) {
            Object rValue = popOperand();
            Object lValue = popOperand();
            boolean result;

            if (lValue == null && rValue == null) {
                result = true;
            } else if (lValue == null || rValue == null) {
                result = false;
            } else if (lValue instanceof Boolean && rValue instanceof Boolean) {
                result = ((Boolean) lValue).booleanValue() == ((Boolean) rValue).booleanValue();
            } else if (lValue instanceof Number && rValue instanceof Number) {
                result = Double.compare(((Number) lValue).doubleValue(), ((Number) rValue).doubleValue()) == 0;
            } else if (lValue instanceof String && rValue instanceof String) {
                result = lValue.equals(rValue);
            } else {
                result = false;
            }

            if (instruction == Instruction.NOT_EQ) {
                result = !result;
            }
            pushOperand(result);
        } 
        else if (instruction == Instruction.LESS_THAN ||
                instruction == Instruction.GREATER_THAN ||
                instruction == Instruction.LESS_OR_EQ ||
                instruction == Instruction.GREATER_OR_EQ) {

            Object rValue = popOperand();
            Object lValue = popOperand();
            boolean result = false;

            if (lValue instanceof Number && rValue instanceof Number) {
                double left = ((Number) lValue).doubleValue();
                double right = ((Number) rValue).doubleValue();

                if (instruction == Instruction.LESS_THAN) {
                    result = left < right;
                } else if (instruction == Instruction.GREATER_THAN) {
                    result = left > right;
                } else if (instruction == Instruction.LESS_OR_EQ) {
                    result = left <= right;
                } else {
                    result = left >= right;
                }
            }

            pushOperand(result);
        } 
        else if (instruction == Instruction.ABSOLUTE || instruction == Instruction.REVERSE_SIGN) {
            Object value = popOperand();
            Object result;

            if (value instanceof Number) {
                double number = ((Number) value).doubleValue();

                if (instruction == Instruction.ABSOLUTE) {
                    result = Math.abs(number);
                } else {
                    result = -number;
                }
            } else {
                result = 0.0;
            }

            pushOperand(result);
        }
        getLastStack().addPointer();
    }

    private static Object operateArithmetic(Instruction instruction) {
        var rValue = popOperand();
        var lValue = popOperand();

        if (rValue instanceof Number && lValue instanceof Number) {
            double rNum = ((Number) rValue).doubleValue();
            double lNum = ((Number) lValue).doubleValue();
            if (instruction == Instruction.ADD) {
                return rNum + lNum;
            }
            if (instruction == Instruction.SUB) {
                return lNum - rNum;
            }
            if (instruction == Instruction.MUL) {
                return lNum * rNum;
            }
            if (instruction == Instruction.DIV) {
                return lNum / rNum;
            }
            if (instruction == Instruction.MOD) {
                return lNum % rNum;
            }
        }

        if (rValue instanceof String && lValue instanceof String) {
            return (String) rValue + lValue;
        }

        if (instruction != Instruction.MUL) {
            return 0f;
        }
        if (rValue instanceof Number && lValue instanceof String) {
            return ((String) lValue).repeat((int) rValue);
        }
        if (rValue instanceof String && lValue instanceof Number) {
            return ((String) rValue).repeat((int) lValue);
        }
        return 0f;
    }

    private static void pushOperand(Object operand) {
        getLastStack().pushOperand(operand);
    }

    private static Object popOperand() {
        return getLastStack().popOperand();
    }

    private static Object peekOperand() {
        return getLastStack().peekOperand();
    }

    private static StackFrame getLastStack() {
        return callStack.get(callStack.size() - 1);
    }

    private static StackFrame removeLastStack() {
        return callStack.remove(callStack.size() - 1);
    }

    private static void markObject(Object value) {
        if (value instanceof CustomArray) {
            CustomArray array = (CustomArray) value;
            array.mark();
        }
    }

    private static void collectGarbage() {
        for (StackFrame stackFrame : callStack) {
            for (Object operand : stackFrame.getOperandStack()) {
                markObject(operand);
            }
            for (Object variable : stackFrame.getVariables()) {
                markObject(variable);
            }
        }
        sweepObject();
    }

    private static void sweepObject() {
        objects.removeIf(object -> {
            if (object.isMarked()) {
                object.unmark();
                return false;
            }
            return true;
        });
    }
}
