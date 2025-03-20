package com.img.evaluator;

import bot.atri.exp.ATRIExp;
import bot.atri.exp.ExpEvaluator;
import bot.atri.exp.ExpParser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : IMG
 * @create : 2025/3/20
 */
public class ExpsEvaluator {
    public static ExpEvaluator evaluator = new ExpEvaluator();

    public static String correctExp(String[] exp, String[] answer) {
        if (exp.length != answer.length) {
            return "The length of exp and answer is not equal";
        }
        List<Integer> correct = new ArrayList<>();
        List<Integer> wrong = new ArrayList<>();
        ATRIExp atriExp;
        for (int i = 0; i < exp.length; i++) {
            atriExp = ExpParser.parse(exp[i]);
            Object evaluate = evaluator.evaluate(atriExp);
            if (evaluate instanceof Float) {
                evaluate = ((Float) evaluate).intValue();
            }
            String result = evaluate.toString();
            if (result.equals(answer[i])) {
                correct.add(i + 1);
            } else {
                wrong.add(i + 1);
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Correct: (");
        for (int i : correct) {
            sb.append(i).append(", ");
        }
        if (!correct.isEmpty()) {
            sb.delete(sb.length() - 2, sb.length());
        }

        sb.append(")\nWrong: (");
        for (int i : wrong) {
            sb.append(i).append(", ");
        }
        if (!wrong.isEmpty()) {
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append(")");
        return sb.toString();
    }
}
