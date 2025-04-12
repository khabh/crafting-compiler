package com.craftingcompiler;

import java.util.*;

public class TokenScanner {
    private final LinkedList<Character> characters;

    public TokenScanner(String sourceCode) {
        sourceCode += '\0';
        this.characters = stringToIterator(sourceCode);
    }

    private LinkedList<Character> stringToIterator(String sourceCode) {
        LinkedList<Character> characters = new LinkedList<>();
        for (Character character : sourceCode.toCharArray()) {
            characters.addLast(character);
        }
        return characters;
    }

    public List<Token> scan() {
        List<Token> tokens = new ArrayList<>();
        while (!characters.isEmpty() && characters.peekFirst() != '\0') {
            CharType charType = getCharType(characters.peek());
            if (CharType.WhiteSpace == charType) {
                characters.pollFirst();
                continue;
            }
            if (CharType.NumberLiteral == charType) {
                tokens.add(scanNumberLiteral());
                continue;
            }
            if (CharType.StringLiteral == charType) {
                tokens.add(scanStringLiteral());
                continue;
            }
            if (CharType.IdentifierAndKeyWord == charType) {
                tokens.add(scanIdentifierAndKeyword());
                continue;
            }
            if (CharType.OperatorAndPunctuator == charType) {
                tokens.add(scanOperatorAndPunctuator());
            }
        }
        tokens.add(new Token(Kind.EndOfToken, ""));
        return tokens;
    }

    private CharType getCharType(char input) {
        if (CharType.WhiteSpace.isMatched(input))
            return CharType.WhiteSpace;
        if (Character.isDigit(input)) {
            return CharType.NumberLiteral;
        }
        if (input == '\'') {
            return CharType.StringLiteral;
        }
        if ('a' <= input && input <= 'z' || 'A' <= input && 'Z' >= input) {
            return CharType.IdentifierAndKeyWord;
        }
        if (CharType.OperatorAndPunctuator.isMatched(input)) {
            return CharType.OperatorAndPunctuator;
        }
        return CharType.Unknown;
    }

    private Token scanNumberLiteral() {
        String numberLiteral = scan(CharType.NumberLiteral);
        return new Token(Kind.NumberLiteral, numberLiteral);
    }

    private Token scanStringLiteral() {
        characters.pollFirst();
        String stringLiteral = scan(CharType.StringLiteral);
        if (characters.isEmpty() || characters.peekFirst() != '\'') {
            throw new IllegalArgumentException("문자열의 종료 문자가 없습니다.");
        }
        characters.pollFirst();
        return new Token(Kind.StringLiteral, stringLiteral);
    }

    private Token scanIdentifierAndKeyword() {
        String identifierAndKeyword = scan(CharType.IdentifierAndKeyWord);
        Kind kind = StringToKind.toKind(identifierAndKeyword);
        if (kind == Kind.Unknown) {
            kind = Kind.Identifier;
        }
        return new Token(kind, identifierAndKeyword);
    }

    private Token scanOperatorAndPunctuator() {
        String operatorAndPunctuator = scan(CharType.OperatorAndPunctuator);
        int lastIndex = operatorAndPunctuator.length() - 1;
        while (!operatorAndPunctuator.isEmpty() && StringToKind.toKind(operatorAndPunctuator) == Kind.Unknown) {
            characters.addFirst(operatorAndPunctuator.charAt(lastIndex));
            operatorAndPunctuator = operatorAndPunctuator.substring(0, lastIndex);
            lastIndex--;
        }
        Kind kind = StringToKind.toKind(operatorAndPunctuator);
        return new Token(kind, operatorAndPunctuator);
    }

    private String scan(CharType charType) {
        StringBuilder token = new StringBuilder();
        while (!characters.isEmpty() && isCharType(characters.peekFirst(), charType)) {
            token.append(characters.pollFirst());
        }
        return token.toString();
    }

    private boolean isCharType(char c, CharType charType) {
        if (CharType.NumberLiteral == charType) {
            return CharType.NumberLiteral.isMatched(c);
        }
        if (CharType.StringLiteral == charType) {
            return CharType.StringLiteral.isMatched(c);
        }
        if (CharType.IdentifierAndKeyWord == charType) {
            return CharType.IdentifierAndKeyWord.isMatched(c);
        }
        return CharType.OperatorAndPunctuator.isMatched(c);
    }
}
