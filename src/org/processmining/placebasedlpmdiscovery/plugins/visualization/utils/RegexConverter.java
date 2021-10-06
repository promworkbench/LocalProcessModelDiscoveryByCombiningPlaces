package org.processmining.placebasedlpmdiscovery.plugins.visualization.utils;

import java.util.function.BiFunction;

public class RegexConverter {

    public String getRegex(String text) {
        text = text
                .replace("|", "\\|")
                .replace("(", "\\(")
                .replace(")", "\\)");
        return getRegexFromInputRecursive(text);
    }

    private String getRegexFromInputRecursive(String text) {
        text = text.trim();
        if (text.indexOf('{') == 0 && text.indexOf('}') == text.length() - 1)
            text = text.substring(1, text.length() - 1);

        String converted = convertRegexForOperator(text, Operator.OR,
                (ind, part) -> ind == 0 ? "(" + part + ")" : "|(" + part + ")");
        if (!converted.equals(text))
            return converted;
        converted = convertRegexForOperator(text, Operator.AND,
                (ind, part) -> "(?=.*" + part + ")") + ".*";
        return converted;
    }

    private String convertRegexForOperator(String input, Operator op, BiFunction<Integer, String, String> conversionFunc) {
        String splitRegex = (op == Operator.AND ? "and" : "or") + "(?![^{}]*})";
        String[] parts = input.split(splitRegex);
        if (parts.length == 1) // if the text cannot be split using the delimiter
            return parts[0];

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length; ++i) {
            sb.append(conversionFunc.apply(i, getRegexFromInputRecursive(parts[i])));
        }
        return sb.toString();
    }

    private enum Operator {
        AND, OR
    }
}
