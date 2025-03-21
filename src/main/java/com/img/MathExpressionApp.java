package com.img;

import com.img.generator.ExpGenerator;
import java.util.List;
import java.util.Scanner;

public class MathExpressionApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== 数学表达式生成器 ===");
            System.out.println("1. 批量生成表达式");
            System.out.println("2. 查看所有表达式");
            System.out.println("3. 查看指定表达式");
            System.out.println("4. 删除表达式");
            System.out.println("0. 退出程序");
            System.out.print("\n请选择操作 (0-4): ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // 消耗换行符

            switch (choice) {
                case 0:
                    System.out.println("程序已退出");
                    return;

                case 1:
                    generateMultipleExpressions(scanner);
                    break;

                case 2:
                    listAllExpressions();
                    break;

                case 3:
                    viewSpecificExpression();
                    break;

                case 4:
                    deleteExpression();
                    break;

                default:
                    System.out.println("无效的选择，请重试");
            }
        }
    }

    private static void generateMultipleExpressions(Scanner scanner) {
        try {
            System.out.println("\n--- 批量生成表达式 ---");

            // 获取表达式数量
            System.out.print("请输入要生成的表达式数量(n): ");
            int count = scanner.nextInt();
            count = Math.max(1, Math.min(100, count)); // 限制最大生成数量为100

            // 获取数值范围
            System.out.print("请输入数值范围(r): ");
            int range = scanner.nextInt();
            range = Math.max(1, Math.min(100, range)); // 确保范围在1-100之间

            // 获取运算符数量
            System.out.print("请输入运算符数量(1-4): ");
            int operatorNum = scanner.nextInt();
            operatorNum = Math.max(1, Math.min(4, operatorNum)); // 确保运算符数量在1-4之间

            System.out.println("\n开始生成表达式 (数量=" + count + ", 范围=1-" + range + ", 运算符数量=" + operatorNum + ")");
            String[] filenames = ExpGenerator.generateAndSaveExps(count, operatorNum, 1, range);
            System.out.println("\n成功生成 " + filenames.length + " 个表达式:");
            for (String filename : filenames) {
                String content = ExpGenerator.readExpression(filename);
                System.out.println(filename + ": " + content);
            }
        } catch (Exception e) {
            System.err.println("批量生成表达式失败: " + e.getMessage());
        }
    }

    private static void listAllExpressions() {
        try {
            System.out.println("\n--- 所有保存的表达式 ---");
            List<String> files = ExpGenerator.listSavedExpressions();
            if (files.isEmpty()) {
                System.out.println("当前没有保存的表达式");
                return;
            }

            for (String filename : files) {
                String content = ExpGenerator.readExpression(filename);
                System.out.println(filename + ": " + content);
            }
        } catch (Exception e) {
            System.err.println("列出表达式失败: " + e.getMessage());
        }
    }

    private static void viewSpecificExpression() {
        try {
            System.out.println("\n--- 查看指定表达式 ---");
            List<String> files = ExpGenerator.listSavedExpressions();
            if (files.isEmpty()) {
                System.out.println("当前没有保存的表达式");
                return;
            }

            System.out.println("可用的表达式文件:");
            for (int i = 0; i < files.size(); i++) {
                System.out.println((i + 1) + ". " + files.get(i));
            }

            System.out.print("\n请选择要查看的表达式编号 (1-" + files.size() + "): ");
            Scanner scanner = new Scanner(System.in);
            int index = scanner.nextInt() - 1;

            if (index >= 0 && index < files.size()) {
                String filename = files.get(index);
                String content = ExpGenerator.readExpression(filename);
                System.out.println("\n表达式内容: " + content);
            } else {
                System.out.println("无效的选择");
            }
        } catch (Exception e) {
            System.err.println("查看表达式失败: " + e.getMessage());
        }
    }

    private static void deleteExpression() {
        try {
            System.out.println("\n--- 删除表达式 ---");
            List<String> files = ExpGenerator.listSavedExpressions();
            if (files.isEmpty()) {
                System.out.println("当前没有保存的表达式");
                return;
            }

            System.out.println("可删除的表达式文件:");
            for (int i = 0; i < files.size(); i++) {
                System.out.println((i + 1) + ". " + files.get(i));
            }

            System.out.print("\n请选择要删除的表达式编号 (1-" + files.size() + "): ");
            Scanner scanner = new Scanner(System.in);
            int index = scanner.nextInt() - 1;

            if (index >= 0 && index < files.size()) {
                String filename = files.get(index);
                ExpGenerator.deleteExpression(filename);
                System.out.println("表达式已删除: " + filename);
            } else {
                System.out.println("无效的选择");
            }
        } catch (Exception e) {
            System.err.println("删除表达式失败: " + e.getMessage());
        }
    }
}