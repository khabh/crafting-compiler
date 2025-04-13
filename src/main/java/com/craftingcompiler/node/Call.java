package com.craftingcompiler.node;

import static com.craftingcompiler.interpreter.Interpreter.local;

import com.craftingcompiler.exception.ReturnException;
import com.craftingcompiler.util.SyntaxPrinter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Call extends Expression {

    private Expression sub;
    List<Expression> arguments;

    @Override
    public Object interpret() {
        var value = sub.interpret();
        if (value instanceof java.util.function.Function<?, ?>) {
            return interpretBuiltinFunction(value);
        }
        if (!(value instanceof Function function)) {
            return null;
        }
        return interpretFunction(function);
    }

    private Object interpretBuiltinFunction(Object value) {
        List<Object> values = arguments.stream()
                .map(Expression::interpret)
                .toList();
        @SuppressWarnings("unchecked")
        java.util.function.Function<List<Object>, Object> func = (java.util.function.Function<List<Object>, Object>) value;
        return func.apply(values);
    }

    private Object interpretFunction(Function function) {
        List<String> paramNames = function.getParameters();
        Map<String, Object> parameters = new HashMap<>();

        if (paramNames.size() != arguments.size()) {
            throw new IllegalArgumentException("paramNames.size() != arguments.size()");
        }
        for (int i = 0; i < paramNames.size(); i++) {
            String name = paramNames.get(i);
            parameters.put(name, arguments.get(i).interpret());
        }

        LinkedList<Map<String, Object>> stack = new LinkedList<>();
        stack.push(parameters);
        local.addLast(new LinkedList<>(stack));

        try {
            function.interpret();
        } catch (ReturnException e) {
            local.removeLast();
            return e.getResult();
        }

        local.removeLast();
        return null;
    }

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println("CALL:");
        SyntaxPrinter.indent(depth + 1);
        System.out.println("EXPRESSION:");
        sub.print(depth + 2);
        for (Expression arg : arguments) {
            SyntaxPrinter.indent(depth + 1);
            System.out.println("ARGUMENT:");
            arg.print(depth + 2);
        }
    }
}
