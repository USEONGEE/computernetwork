package com.example.front.network_termproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Utility class to evaluate mathematical expressions without using ScriptEngine.
 */
class MathEvaluator {

    /**
     * Evaluates a mathematical expression and returns the result.
     *
     * @param expression The mathematical expression to evaluate.
     * @return The integer result of the evaluated expression.
     * @throws Exception If the expression is invalid or cannot be evaluated.
     */
    public static int evaluate(String expression) throws Exception {
        List<String> tokens = tokenize(expression);
        List<String> rpn = toRPN(tokens);
        return evaluateRPN(rpn);
    }

    /**
     * Tokenizes the input expression into numbers, operators, and parentheses.
     *
     * @param expression The input mathematical expression.
     * @return A list of tokens.
     * @throws Exception If invalid characters are found.
     */
    private static List<String> tokenize(String expression) throws Exception {
        List<String> tokens = new ArrayList<>();
        StringBuilder numberBuffer = new StringBuilder();
        for (char ch : expression.toCharArray()) {
            if (Character.isDigit(ch)) {
                numberBuffer.append(ch);
            } else if ("+-*/()".indexOf(ch) != -1) {
                if (numberBuffer.length() > 0) {
                    tokens.add(numberBuffer.toString());
                    numberBuffer.setLength(0);
                }
                tokens.add(String.valueOf(ch));
            } else if (Character.isWhitespace(ch)) {
                continue; // Ignore whitespace
            } else {
                throw new Exception("Invalid character: " + ch);
            }
        }
        if (numberBuffer.length() > 0) {
            tokens.add(numberBuffer.toString());
        }
        return tokens;
    }

    /**
     * Converts the list of tokens to Reverse Polish Notation using the Shunting Yard algorithm.
     *
     * @param tokens The list of tokens.
     * @return A list of tokens in RPN.
     * @throws Exception If parentheses are mismatched.
     */
    private static List<String> toRPN(List<String> tokens) throws Exception {
        List<String> output = new ArrayList<>();
        Stack<String> operators = new Stack<>();
        Map<String, Integer> precedence = new HashMap<>();
        precedence.put("+", 1);
        precedence.put("-", 1);
        precedence.put("*", 2);
        precedence.put("/", 2);

        for (String token : tokens) {
            if (isNumber(token)) {
                output.add(token);
            } else if ("+-*/".contains(token)) {
                while (!operators.isEmpty() && !"(".equals(operators.peek())
                        && precedence.get(token) <= precedence.get(operators.peek())) {
                    output.add(operators.pop());
                }
                operators.push(token);
            } else if ("(".equals(token)) {
                operators.push(token);
            } else if (")".equals(token)) {
                while (!operators.isEmpty() && !"(".equals(operators.peek())) {
                    output.add(operators.pop());
                }
                if (operators.isEmpty() || !"(".equals(operators.pop())) {
                    throw new Exception("Mismatched parentheses");
                }
            }
        }

        while (!operators.isEmpty()) {
            String op = operators.pop();
            if ("()".contains(op)) {
                throw new Exception("Mismatched parentheses");
            }
            output.add(op);
        }

        return output;
    }

    /**
     * Evaluates a Reverse Polish Notation expression.
     *
     * @param rpn The RPN expression.
     * @return The integer result.
     * @throws Exception If the expression is invalid.
     */
    private static int evaluateRPN(List<String> rpn) throws Exception {
        Stack<Integer> stack = new Stack<>();
        for (String token : rpn) {
            if (isNumber(token)) {
                stack.push(Integer.parseInt(token));
            } else if ("+-*/".contains(token)) {
                if (stack.size() < 2) {
                    throw new Exception("Invalid expression");
                }
                int b = stack.pop();
                int a = stack.pop();
                switch (token) {
                    case "+":
                        stack.push(a + b);
                        break;
                    case "-":
                        stack.push(a - b);
                        break;
                    case "*":
                        stack.push(a * b);
                        break;
                    case "/":
                        if (b == 0) {
                            throw new Exception("Division by zero");
                        }
                        stack.push(a / b);
                        break;
                }
            }
        }
        if (stack.size() != 1) {
            throw new Exception("Invalid expression");
        }
        return stack.pop();
    }

    /**
     * Checks if a string is a number.
     *
     * @param token The string to check.
     * @return True if it's a number, false otherwise.
     */
    private static boolean isNumber(String token) {
        return token.matches("\\d+");
    }
}
