package com.craftingcompiler.code;

import java.text.DecimalFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Code {

    private Instruction instruction;
    private Object operand;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String instrString = instruction.name();
        sb.append(String.format("%-15s", instrString));

        if (operand instanceof Integer || operand instanceof Long) {
            sb.append("[").append(operand).append("]");
        } else if (operand instanceof Boolean) {
            sb.append(operand);
        } else if (operand instanceof Double || operand instanceof Float) {
            DecimalFormat df = new DecimalFormat("#.######");
            sb.append(df.format(operand));
        } else if (operand instanceof String) {
            sb.append("\"").append(operand).append("\"");
        }

        return sb.toString();
    }
}
