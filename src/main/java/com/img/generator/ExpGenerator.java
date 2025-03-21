package com.img.generator;

import bot.atri.exp.ATRIExp;
import bot.atri.exp.ExpParser;
import bot.atri.exp.function.Function;
import bot.atri.exp.node.FunctionNode;
import bot.atri.exp.node.Node;
import bot.atri.exp.node.NumberNode;
import bot.atri.exp.number.Fraction;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * @author : IMG
 * @create : 2025/3/19
 */
public class ExpGenerator {
    public static String[] operators = {"+", "-", "×", "÷"};
    public static Random random = new Random();
    public static int max;
    public static int min;
    public static int num;

    public static ATRIExp generateExp(int operatorNum, int min, int max) {
        ExpGenerator.max = max;
        ExpGenerator.min = min;
        ExpGenerator.num = operatorNum;
        return new ATRIExp(generateNode(operatorNum, 1, Integer.MAX_VALUE));
    }

    /**
     * 生成多个表达式
     * @param num 表达式数量
     * @param operatorNum 运算符数量
     * @param min 最小值
     * @param max 最大值
     * @return 表达式数组
     */
    public static ATRIExp[] generateExps(int num, int operatorNum, int min, int max) {
        ATRIExp[] exps = new ATRIExp[num];
        Set<String> expSet = new HashSet<>();
        while (expSet.size() < num) {
            ATRIExp exp = generateExp(operatorNum, min, max);
            String expStr = exp.toString();
            if (!expSet.contains(expStr)) {
                expSet.add(expStr);
                exps[expSet.size() - 1] = exp;
            }
        }
        return exps;
    }

    public static Node generateNode(int num, int min, int max) {
        if (ExpGenerator.num < 2 && (random.nextBoolean() || ExpGenerator.num == 0)) {
            return generateNumberNode(min, max);
        } else {
            int i = random.nextInt(4);
            String operator = operators[i];
            Function function = ExpParser.functions.get(operator);
            FunctionNode functionNode = new FunctionNode(function);
            int left = 0;
            --ExpGenerator.num;
            Node[] temp = new Node[2];
            for (int j = 0; j < 2; j++) {
                Node node;
                if (j == 1) {
                    switch (operator) {
                        case "+":
                            node = generateNode(ExpGenerator.num, min - left, max - left);
                            break;
                        case "×":
//                            if (left == 0) {
//                                node = new NumberNode(0);
//                            } else {
                                // 向上取整
                                node = generateNode(ExpGenerator.num, (int)Math.ceil(((double) min) / left) , (int)Math.ceil(((double) max) / left));
//                            }
                            break;
                        case "-":
                            node = generateNode(ExpGenerator.num, left - max, left - min);
                            break;
                        case "÷":
//                            try {
                            if (left == 1 && min > 1) {
                                max = 1;
                            }
                                node = generateNode(ExpGenerator.num, Math.max(left, (int) Math.ceil((float) left / max)), max);
//                            } catch (Exception e){
//                                node = new NumberNode(1);
//                            }
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid operator");
                    }
                } else {
                    if (operator.equals("-")) {
                        node = generateNode(ExpGenerator.num, min + 1, max);
                    } else {
                        node = generateNode(ExpGenerator.num, min, max);
                    }
                    left = node.getIntValue();
                }
                temp[j] = node;
                if (node instanceof FunctionNode) {
                    ((FunctionNode) node).setParent(functionNode);
                }
            }
            // 交换两个节点的位置
            functionNode.addChild(temp[1]);
            functionNode.addChild(temp[0]);
            functionNode.calculateIntValue();
            return functionNode;
        }
    }

    private static Node generateNumberNode(int min, int max) {
//        System.out.println("source: " + min + " " + max);
        max = Math.min(max, ExpGenerator.max);
        max = Math.max(ExpGenerator.min, max);
        min = Math.max(min, ExpGenerator.min);
//        System.out.println("clip: " + min + " " + max);
        if (min >= max) {
            return new NumberNode(max);
        }
        if (random.nextBoolean()) {
            try {
                NumberNode numberNode = new NumberNode(random.nextInt(max - min + 1) + min);
                return numberNode;
            }catch (Exception e){
                return Fraction.toNode(Fraction.randomFraction(min, max));
            }
        } else {
            return Fraction.toNode(Fraction.randomFraction(min, max));
        }
    }
}
