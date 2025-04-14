package com.craftingcompiler;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Object> list = new ArrayList<>(List.of(1, 2, 3));
        printLine(list.get(1));
        list.set(2, 4);
        printLine(list.get(2));
        printLine(Math.sqrt(9));
        printLine(mul("Hello ", 3));
        sayHello(3);

        for (int i = 0; i < 5; i++) {
            if (i == 1) {
                printLine("one");
                continue;
            } else if (i == 2) {
                printLine("two");
                break;
            } else if (i == 3) {
                printLine("three");
            } else {
                printLine(i);
            }
            printLine(i);
        }

        int global = 10;
        global = 4;
        int local = 13;
        printLine("global: " + global);
        printLine("local: " + local);
        printLine("Hello" + " World!");
        printLine(1 + 2 + 3 + 4);
        printLine("potato ".repeat(10));
        printLine(true && true);
        printLine(false || true);
    }

    public static void sayHello(int j) {
        for (int i = 0; i < j; i++) {
            printLine("Ho");
        }
    }

    public static String mul(String a, int b) {
        printLine(a + ", " + b);
        return a.repeat(b);
    }

    public static void printLine(Object obj) {
        System.out.println(obj);
    }

    public static void printLine(Object... objs) {
        for (Object obj : objs) {
            System.out.print(obj);
        }
        System.out.println();
    }
}
