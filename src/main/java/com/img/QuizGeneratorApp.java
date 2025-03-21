package com.img;

import com.img.evaluator.ExpsEvaluator;
import com.img.manager.ExerciseFileManager;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class QuizGeneratorApp {
    private static final int DEFAULT_COUNT = 10;
    private static final int DEFAULT_MAX = 10;
    private static final int MAX_COUNT = 10000;
    private static final int MAX_VALUE = 1000;

    public static void main(String[] args) {
        commandLineMode(args);
    }

    private static void commandLineMode(String[] args) {
        // 解析命令行参数
        int count = DEFAULT_COUNT;
        int maxValue = DEFAULT_MAX;
        String answerFile = null;
        String exerciseFile = null;

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            switch (arg) {
                case "-n":
                    if (i + 1 < args.length) {
                        try {
                            count = Integer.parseInt(args[++i]);
                            if (count <= 0 || count > MAX_COUNT) {
                                System.err.println("错误：题目数量必须在1到" + MAX_COUNT + "之间");
                                return;
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("错误：无效的题目数量");
                            return;
                        }
                    } else {
                        System.err.println("错误：-n 参数后缺少数值");
                        return;
                    }
                    break;
                case "-r":
                    if (i + 1 < args.length) {
                        try {
                            maxValue = Integer.parseInt(args[++i]);
                            if (maxValue <= 0 || maxValue > MAX_VALUE) {
                                System.err.println("错误：数值范围必须在1到" + MAX_VALUE + "之间");
                                return;
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("错误：无效的数值范围");
                            return;
                        }
                    } else {
                        System.err.println("错误：-r 参数后缺少数值");
                        return;
                    }
                    break;
                case "-e":
                    if (i + 1 < args.length) {
                        exerciseFile = args[++i];
                        Path exercisePath = Paths.get(exerciseFile);
                        if (!Files.exists(exercisePath)) {
                            System.err.println("错误：题目文件不存在: " + exerciseFile);
                            return;
                        }
                    } else {
                        System.err.println("错误：-e 参数后缺少文件名");
                        return;
                    }
                    break;
                case "-a":
                    if (i + 1 < args.length) {
                        answerFile = args[++i];
                        Path answerPath = Paths.get(answerFile);
                        if (!Files.exists(answerPath)) {
                            System.err.println("错误：答案文件不存在: " + answerFile);
                            return;
                        }
                    } else {
                        System.err.println("错误：-a 参数后缺少文件名");
                        return;
                    }
                    break;
                default:
                    System.err.println("错误：未知参数 " + arg);
                    return;
            }
        }

        // 检查参数组合
        ExerciseFileManager manager = new ExerciseFileManager();

        try {
            if (answerFile != null && exerciseFile != null) {
                // 检查答案模式
                System.out.println("正在检查答案...");
                long startTime = System.currentTimeMillis();

                List<String> exerciseLines = manager.readLinesFromFile(Paths.get(exerciseFile));
                List<String> answerLines = manager.readLinesFromFile(Paths.get(answerFile));

                if (exerciseLines.size() != answerLines.size()) {
                    System.err.println("错误：题目数量和答案数量不匹配");
                    return;
                }

                for (int i = 0; i < exerciseLines.size(); i++) {
                    String exercise = exerciseLines.get(i);
                    String answer = answerLines.get(i);
                    exerciseLines.set(i, spilt(exercise));
                    answerLines.set(i, spilt(answer));
                }
                String result = ExpsEvaluator.correctExp(exerciseLines.toArray(new String[exerciseLines.size()]), answerLines.toArray(new String[answerLines.size()]));
                System.out.println(result);

                long endTime = System.currentTimeMillis();
                System.out.println("用时: " + (endTime - startTime) / 1000.0 + " 秒");
            } else if (answerFile != null || exerciseFile != null) {
                System.out.println("错误：答案文件和题目文件必须同时提供");
            } else {
                // 生成题目模式
                System.out.println("正在生成题目...");
                long startTime = System.currentTimeMillis();

                if (manager.generateAndSaveExercises(count, maxValue)) {
                    long endTime = System.currentTimeMillis();
                    System.out.println("题目生成完成");
                    System.out.println("题目已保存到 quiz_files/Exercises.txt");
                    System.out.println("答案已保存到 quiz_files/Answers.txt");
                    System.out.println("用时: " + (endTime - startTime) / 1000.0 + " 秒");
                } else {
                    System.err.println("题目生成失败");
                }
            }
        } catch (Exception e) {
            System.err.println("操作失败: " + e.getMessage());
        }
    }

    private static String spilt(String str) {
        return str.split("\\.")[1].replace(" ", "");
    }
}