package com.craftingcompiler.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReturnException extends RuntimeException {

    private Object result;
}
