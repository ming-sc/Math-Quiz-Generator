package com.img.filemanager;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ExpressionFileManager {
    private static final String DATA_DIR = "data/expressions";

    public ExpressionFileManager() {
        // 确保数据目录存在
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
        } catch (IOException e) {
            System.err.println("创建数据目录失败: " + e.getMessage());
        }
    }

    public String saveExpression(String content, String type) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("表达式内容不能为空");
        }
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("表达式类型不能为空");
        }

        String filename = String.format("%s_%d.txt", type, System.currentTimeMillis());
        Path filePath = Paths.get(DATA_DIR, filename);

        try {
            Files.write(filePath, content.getBytes());
            return filename;
        } catch (IOException e) {
            throw new RuntimeException("保存表达式失败: " + e.getMessage());
        }
    }

    public String readExpression(String filename) throws Exception {
        if (filename == null || filename.trim().isEmpty()) {
            throw new IllegalArgumentException("文件名不能为空");
        }

        Path filePath = Paths.get(DATA_DIR, filename);
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("文件不存在: " + filename);
        }

        try {
            return new String(Files.readAllBytes(filePath));
        } catch (IOException e) {
            throw new Exception("读取表达式失败: " + e.getMessage());
        }
    }

    public String[] listExpressions() throws Exception {
        try {
            return Files.list(Paths.get(DATA_DIR))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .filter(name -> name.endsWith(".txt"))
                    .toArray(String[]::new);
        } catch (IOException e) {
            throw new Exception("列出表达式失败: " + e.getMessage());
        }
    }

    public void deleteExpression(String filename) throws Exception {
        if (filename == null || filename.trim().isEmpty()) {
            throw new IllegalArgumentException("文件名不能为空");
        }

        Path filePath = Paths.get(DATA_DIR, filename);
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("文件不存在: " + filename);
        }

        try {
            Files.delete(filePath);
        } catch (IOException e) {
            throw new Exception("删除表达式失败: " + e.getMessage());
        }
    }
}