package com.filemanager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExpressionFileManager {
    private static final Logger LOGGER = Logger.getLogger(ExpressionFileManager.class.getName());
    private static final String BASE_DIRECTORY = "data/expressions";
    private static final DateTimeFormatter FILE_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    public ExpressionFileManager() {
        createBaseDirectory();
    }

    /**
     * 创建保存表达式的目录
     */
    private void createBaseDirectory() {
        try {
            Path dirPath = Paths.get(BASE_DIRECTORY);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
                LOGGER.info("Created directory: " + dirPath.toAbsolutePath());
            }
        } catch (IOException e) {
            String errorMsg = "Failed to create expressions directory: " + e.getMessage();
            LOGGER.log(Level.SEVERE, errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }

    /**
     * 保存表达式到文件
     *
     * @param expression 表达式内容
     * @param type       表达式类型
     * @return 文件名
     */
    public String saveExpression(String expression, String type) {
        if (expression == null || expression.trim().isEmpty()) {
            throw new IllegalArgumentException("Expression cannot be null or empty");
        }
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Type cannot be null or empty");
        }

        // 增加毫秒级别避免文件名冲突
        String timestamp = LocalDateTime.now().format(FILE_DATE_FORMAT);
        String filename = String.format("%s_%s.txt", type.trim(), timestamp);
        Path filePath = Paths.get(BASE_DIRECTORY, filename);

        try {
            Files.write(filePath, expression.getBytes(StandardCharsets.UTF_8));
            LOGGER.info("File saved to: " + filePath.toAbsolutePath());
            return filename;
        } catch (IOException e) {
            String errorMsg = "Failed to save expression: " + e.getMessage();
            LOGGER.log(Level.SEVERE, errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }

    /**
     * 读取文件中的表达式内容
     *
     * @param filename 文件名
     * @return 表达式内容
     */
    public String readExpression(String filename) {
        if (filename == null || filename.trim().isEmpty()) {
            throw new IllegalArgumentException("Filename cannot be null or empty");
        }

        Path filePath = Paths.get(BASE_DIRECTORY, filename);
        if (!Files.exists(filePath)) {
            throw new RuntimeException("File does not exist: " + filePath.toAbsolutePath());
        }

        try {
            return new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            String errorMsg = "Failed to read expression file: " + filename + ", error: " + e.getMessage();
            LOGGER.log(Level.SEVERE, errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }

    /**
     * 列出所有表达式文件
     *
     * @return 文件名列表
     */
    public List<String> listExpressionFiles() {
        List<String> files = new ArrayList<>();
        Path dirPath = Paths.get(BASE_DIRECTORY);
        if (!Files.exists(dirPath)) {
            LOGGER.warning("Directory does not exist: " + dirPath.toAbsolutePath());
            return files;
        }
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath, "*.txt")) {
            for (Path path : stream) {
                if (Files.isRegularFile(path)) {
                    files.add(path.getFileName().toString());
                }
            }
        } catch (IOException e) {
            String errorMsg = "Failed to list expression files: " + e.getMessage();
            LOGGER.log(Level.SEVERE, errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
        return files;
    }

    /**
     * 删除指定的表达式文件
     *
     * @param filename 文件名
     */
    public void deleteExpression(String filename) {
        if (filename == null || filename.trim().isEmpty()) {
            throw new IllegalArgumentException("Filename cannot be null or empty");
        }

        Path filePath = Paths.get(BASE_DIRECTORY, filename);
        try {
            if (Files.deleteIfExists(filePath)) {
                LOGGER.info("File deleted: " + filePath.toAbsolutePath());
            } else {
                LOGGER.warning("File does not exist: " + filePath.toAbsolutePath());
            }
        } catch (IOException e) {
            String errorMsg = "Failed to delete expression file: " + filename + ", error: " + e.getMessage();
            LOGGER.log(Level.SEVERE, errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }

    /**
     * 获取表达式存储目录的绝对路径
     *
     * @return 目录绝对路径
     */
    public String getBaseDirectory() {
        return Paths.get(BASE_DIRECTORY).toAbsolutePath().toString();
    }
}
