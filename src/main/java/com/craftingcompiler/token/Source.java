package com.craftingcompiler.token;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Predicate;

public class Source {

    private static final char END = '\0';

    private final Deque<Character> chars;

    public Source(String input) {
        this.chars = new ArrayDeque<>();
        for (char c : input.toCharArray()) {
            chars.addLast(c);
        }
        chars.addLast(END);
    }

    public boolean hasNext() {
        return !chars.isEmpty() && chars.peek() != END;
    }

    public boolean nextIs(char expected) {
        return hasNext() && peek() == expected;
    }

    public char peek() {
        return chars.peekFirst();
    }

    public char consume() {
        return chars.pollFirst();
    }

    public void unconsume(char c) {
        chars.addFirst(c);
    }

    public String consumeWhile(Predicate<Character> predicate) {
        StringBuilder sb = new StringBuilder();
        while (hasNext() && predicate.test(peek())) {
            sb.append(consume());
        }
        return sb.toString();
    }
}
