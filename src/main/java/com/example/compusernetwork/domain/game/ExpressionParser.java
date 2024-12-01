package com.example.compusernetwork.domain.game;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class ExpressionParser {
    public double evaluate(String expression) throws Exception {
        // 안전한 수식 평가를 위한 라이브러리 사용
        Expression e = new ExpressionBuilder(expression).build();
        return e.evaluate();
    }
}