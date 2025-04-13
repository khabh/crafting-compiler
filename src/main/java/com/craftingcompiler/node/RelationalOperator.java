package com.craftingcompiler.node;

import com.craftingcompiler.Kind;
import java.util.Map;
import java.util.function.BiPredicate;

public enum RelationalOperator {
    Equal((l, r) -> {
        if (isNull(l, r)) {
            return true;
        }
        if (isBoolean(l, r)) {
            return toBoolean(l) == toBoolean(r);
        }
        if (isNumber(l, r)) {
            return toNumber(l) == toNumber(r);
        }
        if (isString(l, r)) {
            return toString(l).equals(toString(r));
        }
        return false;
    }),
    NotEqual((l, r) -> {
        if (isNull(l, r)) {
            return false;
        }
        if (isNull(l) || isNull(r)) {
            return true;
        }
        if (isBoolean(l, r)) {
            return toBoolean(l) != toBoolean(r);
        }
        if (isNumber(l, r)) {
            return toNumber(l) != toNumber(r);
        }
        if (isString(l, r)) {
            return !toString(l).equals(toString(r));
        }
        return true;
    }),
    LessThan((l, r) -> isNumber(l, r) && toNumber(l) < toNumber(r)),
    GreaterThan((l, r) -> isNumber(l, r) && toNumber(l) > toNumber(r)),
    LessOrEqual((l, r) -> isNumber(l, r) && toNumber(l) <= toNumber(r)),
    GreaterOrEqual((l, r) -> isNumber(l, r) && toNumber(l) >= toNumber(r));

    private static final Map<Kind, RelationalOperator> operatorTable = Map.of(
            Kind.Equal, RelationalOperator.Equal,
            Kind.NotEqual, RelationalOperator.NotEqual,
            Kind.LessThan, RelationalOperator.LessThan,
            Kind.GreaterThan, RelationalOperator.GreaterThan,
            Kind.LessOrEqual, RelationalOperator.LessOrEqual,
            Kind.GreaterOrEqual, RelationalOperator.GreaterOrEqual
    );

    private final BiPredicate<Object, Object> predicate;

    RelationalOperator(BiPredicate<Object, Object> predicate) {
        this.predicate = predicate;
    }

    public static boolean operate(Kind kind, Object lhs, Object rhs) {
        if (!operatorTable.containsKey(kind)) {
            throw new IllegalArgumentException("Unknown operator: " + kind);
        }
        return operatorTable.get(kind).apply(lhs, rhs);
    }

    public boolean apply(Object left, Object right) {
        return predicate.test(left, right);
    }

    private static boolean isNull(Object... values) {
        for (Object v : values) {
            if (v != null) {
                return false;
            }
        }
        return true;
    }

    private static boolean isBoolean(Object... values) {
        for (Object v : values) {
            if (!(v instanceof Boolean)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isNumber(Object... values) {
        for (Object v : values) {
            if (!(v instanceof Number)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isString(Object... values) {
        for (Object v : values) {
            if (!(v instanceof String)) {
                return false;
            }
        }
        return true;
    }

    private static boolean toBoolean(Object value) {
        return (Boolean) value;
    }

    private static double toNumber(Object value) {
        return ((Number) value).doubleValue();
    }

    private static String toString(Object value) {
        return String.valueOf(value);
    }
}
