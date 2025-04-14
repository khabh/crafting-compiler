package com.craftingcompiler.code;

public enum Instruction {
    EXIT,
    CALL,
    ALLOC,
    RETURN,
    JUMP,
    CONDITION_JUMP,
    PRINT,
    PRINT_LINE,

    LOGICAL_OR,
    LOGICAL_AND,
    ADD,
    SUB,
    MUL,
    DIV,
    MOD,
    EQ,
    NOT_EQ,
    LESS_THAN,
    GREATER_THAN,
    LESS_OR_EQ,
    GREATER_OR_EQ,
    ABSOLUTE,
    REVERSE_SIGN,

    GET_ELEMENT,
    SET_ELEMENT,
    GET_GLOBAL,
    SET_GLOBAL,
    GET_LOCAL,
    SET_LOCAL,

    PUSH_NULL,
    PUSH_BOOL,
    PUSH_NUM,
    PUSH_STR,
    PUSH_ARRAY,
    POP_OPERAND,
    ;
}
