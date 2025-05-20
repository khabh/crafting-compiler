package com.craftingcompiler.node;

import com.craftingcompiler.code.Generator;
import com.craftingcompiler.code.Instruction;
import com.craftingcompiler.kind.Kind;
import com.craftingcompiler.util.SyntaxPrinter;
import java.util.EnumMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Arithmetic extends Expression {

    private static final Map<Kind, Instruction> instructions = new EnumMap<>(Kind.class);

    static {
        instructions.put(Kind.ADD, Instruction.ADD);
        instructions.put(Kind.SUBTRACT, Instruction.SUB);
        instructions.put(Kind.MULTIPLY, Instruction.MUL);
        instructions.put(Kind.DIVIDE, Instruction.DIV);
        instructions.put(Kind.MODULO, Instruction.MOD);
    }

    private Kind kind;
    private Expression lhs;
    private Expression rhs;

    @Override
    public void generate() {
        lhs.generate();
        rhs.generate();
        Generator.writeCode(instructions.get(kind));
    }

    @Override
    public Object interpret() {
        var lValue = lhs.interpret();
        var rValue = rhs.interpret();
        if (lValue instanceof Potato) {
            lValue = ((Potato) lValue).getValue();
        }
        if (rValue instanceof Potato) {
            rValue = ((Potato) rValue).getValue();
        }
        if (areClass(Number.class, lValue, rValue)) {
            return interpretNumber(lValue, rValue);
        }
        if (areClass(String.class, lValue, rValue) && kind == Kind.ADD) {
            return interpretString(lValue, rValue);
        }
        if (kind == Kind.MULTIPLY) {
            if (areClass(String.class, Number.class, rValue, lValue)) {
                return interpretStringMultiple(rValue, lValue);
            }
            if (areClass(Number.class, String.class, rValue, lValue)) {
                return interpretStringMultiple(lValue, rValue);
            }
        }
        throw new IllegalArgumentException("invalid Arithmetic");
    }

    private boolean areClass(Class<?> clazz, Object lValue, Object rValue) {
        return areClass(clazz, clazz, lValue, rValue);
    }

    private boolean areClass(Class<?> lClass, Class<?> rClass, Object lValue, Object rValue) {

        return lClass.isInstance(lValue) && rClass.isInstance(rValue);
    }

    private double interpretNumber(Object lValue, Object rValue) {
        double lNumber = ((Number) lValue).doubleValue();
        double rNumber = ((Number) rValue).doubleValue();

        if (kind == Kind.ADD) {
            return lNumber + rNumber;
        }
        if (kind == Kind.SUBTRACT) {
            return lNumber - rNumber;
        }
        if (kind == Kind.MULTIPLY) {
            return lNumber * rNumber;
        }
        if (kind == Kind.DIVIDE) {
            return lNumber / rNumber;
        }
        if (kind == Kind.MODULO) {
            return lNumber % rNumber;
        }
        throw new IllegalArgumentException("invalid Arithmetic");
    }

    private String interpretString(Object lValue, Object rValue) {
        String lString = ((String) lValue);
        String rString = ((String) rValue);
        if (kind == Kind.ADD) {
            return lString + rString;
        }
        throw new IllegalArgumentException("invalid Arithmetic");
    }

    private String interpretStringMultiple(Object stringValue, Object numberValue) {
        String string = ((String) stringValue);
        int number = ((Number) numberValue).intValue();
        return string.repeat(number);
    }

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println(kind.toString() + ":");
        SyntaxPrinter.indent(depth + 1);
        System.out.println("LHS:");
        lhs.print(depth + 2);
        SyntaxPrinter.indent(depth + 1);
        System.out.println("RHS:");
        rhs.print(depth + 2);
    }
}
