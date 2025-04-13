package com.craftingcompiler.node;

import static com.craftingcompiler.interpreter.Interpreter.local;

import com.craftingcompiler.exception.BreakException;
import com.craftingcompiler.exception.ContinueException;
import com.craftingcompiler.util.SyntaxPrinter;
import java.util.HashMap;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class For extends Statement {

    private Variable variable;
    private Expression condition;
    private Expression expression;
    List<Statement> block;

    @Override
    public void interpret() {
        local.getLast().addFirst(new HashMap<>());
        variable.interpret();
        while (true) {
            boolean result = (boolean) condition.interpret();
            if (!result) {
                break;
            }
            try {
                block.forEach(Statement::interpret);
            } catch (ContinueException ignored) {
            } catch (BreakException e) {
                break;
            }
            expression.interpret();
        }
        local.getLast().pollFirst();
    }

    @Override
    public void print(int depth) {
        SyntaxPrinter.indent(depth);
        System.out.println("FOR:");
        SyntaxPrinter.indent(depth + 1);
        System.out.println("VARIABLE:");
        variable.print(depth + 2);
        SyntaxPrinter.indent(depth + 1);
        System.out.println("CONDITION:");
        condition.print(depth + 2);
        SyntaxPrinter.indent(depth + 1);
        System.out.println("EXPRESSION:");
        expression.print(depth + 2);
        SyntaxPrinter.indent(depth + 1);
        System.out.println("BLOCK:");
        for (Statement stmt : block) {
            stmt.print(depth + 2);
        }
    }
}
