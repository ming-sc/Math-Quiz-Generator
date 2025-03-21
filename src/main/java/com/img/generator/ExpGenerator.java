package com.img.generator;

import com.img.filemanager.ExpressionFileManager;
import java.util.Random;
import java.util.Stack;
import java.util.List;
import java.util.Arrays;

/**
 * @author : IMG
 * @create : 2025/3/19
 */
public class ExpGenerator {
    private static final Random random = new Random();
    private static final ExpressionFileManager fileManager = new ExpressionFileManager();
    private static final String[] operators = { "+", "-", "*", "/" };

    public static String generateAndSaveExp(int operatorNum, int minRange, int maxRange) throws Exception {
        String expression = generateExpression(operatorNum, minRange, maxRange);
        double result = evaluateExpression(expression);
        return fileManager.saveExpression(expression + " = " + result, "basic");
    }

    public static String[] generateAndSaveExps(int n, int operatorNum, int minRange, int maxRange) throws Exception {
        String[] filenames = new String[n];
        for (int i = 0; i < n; i++) {
            filenames[i] = generateAndSaveExp(operatorNum, minRange, maxRange);
        }
        return filenames;
    }

    private static String generateExpression(int operatorNum, int minRange, int maxRange) {
        StringBuilder expression = new StringBuilder();

        // 生成第一个数
        expression.append(generateNumber(minRange, maxRange));

        // 生成剩余的运算符和数字
        for (int i = 0; i < operatorNum; i++) {
            String operator = operators[random.nextInt(operators.length)];
            int number = generateNumber(minRange, maxRange);

            // 如果是除法，确保不会除以0，且结果为整数
            if (operator.equals("/")) {
                while (number == 0 || expression.toString().isEmpty()) {
                    number = generateNumber(minRange, maxRange);
                }
            }

            expression.append(" ").append(operator).append(" ").append(number);
        }

        return expression.toString();
    }

    private static int generateNumber(int minRange, int maxRange) {
        return random.nextInt(maxRange - minRange + 1) + minRange;
    }

    private static double evaluateExpression(String expression) {
        String[] tokens = expression.split(" ");
        Stack<Double> numbers = new Stack<>();
        Stack<String> ops = new Stack<>();

        for (String token : tokens) {
            if (isOperator(token)) {
                while (!ops.empty() && hasPrecedence(token, ops.peek())) {
                    numbers.push(applyOp(ops.pop(), numbers.pop(), numbers.pop()));
                }
                ops.push(token);
            } else {
                numbers.push(Double.parseDouble(token));
            }
        }

        while (!ops.empty()) {
            numbers.push(applyOp(ops.pop(), numbers.pop(), numbers.pop()));
        }

        return numbers.pop();
    }

    private static boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/");
    }

    private static boolean hasPrecedence(String op1, String op2) {
        if (op2.equals("(") || op2.equals(")")) {
            return false;
        }
        return (op1.equals("+") || op1.equals("-")) && (op2.equals("*") || op2.equals("/"));
    }

    private static double applyOp(String op, double b, double a) {
        switch (op) {
            case "+":
                return a + b;
            case "-":
                return a - b;
            case "*":
                return a * b;
            case "/":
                if (b == 0)
                    throw new ArithmeticException("除数不能为0");
                return a / b;
            default:
                return 0;
        }
    }

    public static List<String> listSavedExpressions() {
        try {
            return Arrays.asList(fileManager.listExpressions());
        } catch (Exception e) {
            throw new RuntimeException("列出表达式失败: " + e.getMessage());
        }
    }

    public static String readExpression(String filename) {
        try {
            return fileManager.readExpression(filename);
        } catch (Exception e) {
            throw new RuntimeException("读取表达式失败: " + e.getMessage());
        }
    }

    public static void deleteExpression(String filename) {
        try {
            fileManager.deleteExpression(filename);
        } catch (Exception e) {
            throw new RuntimeException("删除表达式失败: " + e.getMessage());
        }
    }
}
