package com.craftingcompiler.node;

import com.craftingcompiler.Kind;
import com.craftingcompiler.util.SyntaxPrinter;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Arithmetic extends Expression {

    private Kind kind;
    private Expression lhs;
    private Expression rhs;

    @Override
    public Object interpret() {
        var lValue = lhs.interpret();
        var rValue = rhs.interpret();
        if (areClass(Number.class, lValue, rValue)) {
            return interpretNumber(lValue, rValue);
        }
        if (areClass(String.class, lValue, rValue) && kind == Kind.Add) {
            return interpretString(lValue, rValue);
        }
        if (kind == Kind.Multiply) {
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

        if (kind == Kind.Add) {
            return lNumber + rNumber;
        }
        if (kind == Kind.Subtract) {
            return lNumber - rNumber;
        }
        if (kind == Kind.Multiply) {
            return lNumber * rNumber;
        }
        if (kind == Kind.Divide) {
            return lNumber / rNumber;
        }
        if (kind == Kind.Modulo) {
            return lNumber % rNumber;
        }
        throw new IllegalArgumentException("invalid Arithmetic");
    }

    private String interpretString(Object lValue, Object rValue) {
        String lString = ((String) lValue);
        String rString = ((String) rValue);
        if (kind == Kind.Add) {
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
