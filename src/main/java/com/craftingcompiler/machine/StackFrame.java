package com.craftingcompiler.machine;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StackFrame {

    private List<Object> variables = new ArrayList<>();
    private List<Object> operandStack = new ArrayList<>();
    private int instructionPointer = 0;

    public int addPointer() {
        return ++instructionPointer;
    }

    public Object pushOperand(Object operand) {
        return operandStack.add(operand);
    }

    public Object popOperand() {
        return operandStack.remove(operandStack.size() - 1);
    }

    public Object peekOperand() {
        return operandStack.get(operandStack.size() - 1);
    }

    public Object addVariable(Object variable) {
        return variables.add(variable);
    }

    public Object getVariable(int index) {
        return variables.get(index);
    }

    public Object setVariable(int index, Object variable) {
        return variables.set(index, variable);
    }

    public void resizeVariables(int size) {
        while (variables.size() < size) {
            variables.add(null);
        }
    }
}
