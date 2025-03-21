package com.img;

import com.img.generator.ExpGenerator;

public class Main {
    public static void main(String[] args) {
        try {
            // 生成一个表达式并保存
            String filename = ExpGenerator.generateAndSaveExp(2, 1, 10);
            System.out.println("表达式已保存到文件: " + filename);

            // 读取并显示表达式
            String content = ExpGenerator.readExpression(filename);
            System.out.println("文件内容: " + content);

            // 显示所有保存的文件
            System.out.println("\n所有保存的文件:");
            ExpGenerator.listSavedExpressions().forEach(System.out::println);

        } catch (Exception e) {
            System.err.println("发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
}