package com.craftingcompiler.node;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Program {

    private final List<Function> functions;
}
