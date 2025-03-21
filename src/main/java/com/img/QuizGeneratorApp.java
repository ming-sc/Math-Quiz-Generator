package com.img;

import com.img.manager.ExerciseFileManager;
import java.util.Scanner;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class QuizGeneratorApp {
    private static final int DEFAULT_COUNT = 10;
    private static final int DEFAULT_MAX = 10;
    private static final int MAX_COUNT = 10000;
    private static final int MAX_VALUE = 1000;

    public static void main(String[] args) {
        if (args.length == 0) {
            // 交互式输入模式
            interactiveMode();
            return;
        }

        // 命令行参数模式
        commandLineMode(args);
    }

    private static void interactiveMode() {
        Scanner scanner = new Scanner(System.in);
        ExerciseFileManager manager = new ExerciseFileManager();

        while (true) {
            System.out.println("\n=== 数学题目生成器 ===");
            System.out.println("1. 生成新题目");
            System.out.println("2. 检查答案");
            System.out.println("3. 退出");
            System.out.print("请选择功能 (1-3): ");

            try {
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    System.out.println("请输入选项");
                    continue;
                }

                int choice = Integer.parseInt(input);
                switch (choice) {
                    case 1:
                        generateExercises(scanner, manager);
                        break;
                    case 2:
                        checkAnswers(scanner, manager);
                        break;
                    case 3:
                        System.out.println("感谢使用！");
                        return;
                    default:
                        System.out.println("无效的选择，请输入1-3之间的数字");
                }
            } catch (NumberFormatException e) {
                System.out.println("请输入有效的数字");
            } catch (Exception e) {
                System.out.println("操作失败: " + e.getMessage());
            }
        }
    }

    private static void generateExercises(Scanner scanner, ExerciseFileManager manager) {
        System.out.println("\n=== 生成新题目 ===");

        // 输入题目数量
        int count = readIntegerInput(scanner,
                "请输入要生成的题目数量 (1-" + MAX_COUNT + "): ",
                1, MAX_COUNT,
                "题目数量必须在1到" + MAX_COUNT + "之间");

        // 输入数值范围
        int maxValue = readIntegerInput(scanner,
                "请输入数值范围 (1-" + MAX_VALUE + "): ",
                1, MAX_VALUE,
                "数值范围必须在1到" + MAX_VALUE + "之间");

        // 生成题目
        System.out.println("\n正在生成题目...");
        long startTime = System.currentTimeMillis();

        if (manager.generateAndSaveExercises(count, maxValue)) {
            long endTime = System.currentTimeMillis();
            System.out.println("题目生成完成！用时: " + (endTime - startTime) / 1000.0 + " 秒");
        } else {
            System.out.println("题目生成失败，请尝试减少题目数量或增加数值范围");
        }
    }

    private static int readIntegerInput(Scanner scanner, String prompt, int min, int max, String errorMessage) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    System.out.println("请输入数字");
                    continue;
                }

                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println(errorMessage);
            } catch (NumberFormatException e) {
                System.out.println("请输入有效的数字");
            }
        }
    }

    private static void checkAnswers(Scanner scanner, ExerciseFileManager manager) {
        System.out.println("\n=== 检查答案 ===");

        while (true) {
            System.out.print("请输入答案文件路径: ");
            String answerFile = scanner.nextLine().trim();

            if (answerFile.isEmpty()) {
                System.out.println("文件路径不能为空，请重新输入");
                continue;
            }

            Path answerPath = Paths.get(answerFile);
            if (!Files.exists(answerPath)) {
                System.out.println("文件不存在: " + answerFile);
                System.out.print("是否重新输入? (y/n): ");
                String choice = scanner.nextLine().trim().toLowerCase();
                if (!choice.equals("y") && !choice.equals("yes")) {
                    return;
                }
                continue;
            }

            System.out.println("正在检查答案...");
            try {
                manager.gradeAnswers(answerFile);
                System.out.println("答案检查完成！");
                break;
            } catch (Exception e) {
                System.out.println("检查答案时出错: " + e.getMessage());
                System.out.print("是否重新输入? (y/n): ");
                String choice = scanner.nextLine().trim().toLowerCase();
                if (!choice.equals("y") && !choice.equals("yes")) {
                    return;
                }
            }
        }
    }

    private static void commandLineMode(String[] args) {
        // 解析命令行参数
        int count = -1;
        int maxValue = -1;
        String answerFile = null;

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
                case "-h":
                case "--help":
                    printHelp();
                    return;
                default:
                    System.err.println("错误：未知参数 " + arg);
                    printHelp();
                    return;
            }
        }

        // 检查参数组合
        ExerciseFileManager manager = new ExerciseFileManager();

        try {
            if (answerFile != null) {
                // 检查答案模式
                System.out.println("正在检查答案...");
                long startTime = System.currentTimeMillis();

                manager.gradeAnswers(answerFile);

                long endTime = System.currentTimeMillis();
                System.out.println("答案检查完成，结果已保存到 Grade.txt");
                System.out.println("用时: " + (endTime - startTime) / 1000.0 + " 秒");
            } else {
                // 生成题目模式
                if (count == -1 || maxValue == -1) {
                    System.err.println("错误：生成题目模式必须提供 -n 和 -r 参数");
                    printHelp();
                    return;
                }

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

    private static void printHelp() {
        System.out.println("数学题目生成器");
        System.out.println("使用方法：");
        System.out.println("  1. 直接运行程序进入交互模式");
        System.out.println("  2. 使用命令行参数：");
        System.out.println("     java -jar quiz-generator.jar -n <题目数量> -r <数值范围>");
        System.out.println("     java -jar quiz-generator.jar -a <答案文件>");
        System.out.println("\n参数说明：");
        System.out.println("  -n <数量>    要生成的题目数量 (1-" + MAX_COUNT + ")");
        System.out.println("  -r <范围>    数值范围 (1-" + MAX_VALUE + ")");
        System.out.println("  -a <文件>    答案文件路径");
        System.out.println("  -h, --help   显示帮助信息");
        System.out.println("\n示例：");
        System.out.println("  java -jar quiz-generator.jar -n 20 -r 10");
        System.out.println("  java -jar quiz-generator.jar -a quiz_files/Answers.txt");
    }
}