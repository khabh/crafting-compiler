package com.craftingcompiler.interpreter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class BuiltinFunctionTable {

    public static Map<String, Function<List<Object>, Object>> builtinFunctionTable = new HashMap<>();

    static {
        builtinFunctionTable.put("sqrt", args -> {
            Double number = ((Number) args.get(0)).doubleValue();
            return Math.sqrt(number);
        });
    }
}
