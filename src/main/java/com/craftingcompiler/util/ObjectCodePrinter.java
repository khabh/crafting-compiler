package com.craftingcompiler.util;

import com.craftingcompiler.code.Code;
import java.util.List;
import java.util.Map;

public class ObjectCodePrinter {

    public static void printObjectCode(Map.Entry<List<Code>, Map<String, Integer>> objectCode) {
        List<Code> codeList = objectCode.getKey();
        Map<String, Integer> functionTable = objectCode.getValue();

        System.out.printf("%-11s%s%n", "FUNCTION", "ADDRESS");
        System.out.println("-".repeat(18));
        for (Map.Entry<String, Integer> entry : functionTable.entrySet()) {
            System.out.printf("%-11s%d%n", entry.getKey(), entry.getValue());
        }

        System.out.println();
        System.out.printf("%-4s %-15s%s%n", "ADDR", "INSTRUCTION", "OPERAND");
        System.out.println("-".repeat(36));

        for (int i = 0; i < codeList.size(); i++) {
            System.out.printf("%4d %s%n", i, codeList.get(i));
        }
    }
}
