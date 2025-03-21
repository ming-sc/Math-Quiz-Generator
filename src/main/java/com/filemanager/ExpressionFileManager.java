package com.filemanager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExpressionFileManager {
    private static final String BASE_DIRECTORY = "data/expressions";
    private static final DateTimeFormatter FILE_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    public ExpressionFileManager() {
        createBaseDirectory();
    }

    private void createBaseDirectory() {
        try {
            Path dirPath = Paths.get(BASE_DIRECTORY);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
                System.out.println("Created directory: " + dirPath.toAbsolutePath());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create expressions directory: " + e.getMessage(), e);
        }
    }

    public String saveExpression(String expression, String type) {
        if (expression == null || expression.trim().isEmpty()) {
            throw new IllegalArgumentException("Expression cannot be null or empty");
        }
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Type cannot be null or empty");
        }

        String timestamp = LocalDateTime.now().format(FILE_DATE_FORMAT);
        String filename = String.format("%s_%s.txt", type.trim(), timestamp);
        Path filePath = Paths.get(BASE_DIRECTORY, filename);

        try {
            Files.write(filePath, expression.getBytes(StandardCharsets.UTF_8));
            System.out.println("File saved to: " + filePath.toAbsolutePath());
            return filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save expression: " + e.getMessage(), e);
        }
    }

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
            throw new RuntimeException("Failed to read expression file: " + filename + ", error: " + e.getMessage(), e);
        }
    }

    public List<String> listExpressionFiles() {
        try {
            List<String> files = new ArrayList<>();
            Path dirPath = Paths.get(BASE_DIRECTORY);

            if (!Files.exists(dirPath)) {
                System.out.println("Directory does not exist: " + dirPath.toAbsolutePath());
                return files;
            }

            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath, "*.txt")) {
                for (Path path : stream) {
                    if (Files.isRegularFile(path)) {
                        files.add(path.getFileName().toString());
                    }
                }
            }
            return files;
        } catch (IOException e) {
            throw new RuntimeException("Failed to list expression files: " + e.getMessage(), e);
        }
    }

    public void deleteExpression(String filename) {
        if (filename == null || filename.trim().isEmpty()) {
            throw new IllegalArgumentException("Filename cannot be null or empty");
        }

        Path filePath = Paths.get(BASE_DIRECTORY, filename);
        try {
            if (Files.deleteIfExists(filePath)) {
                System.out.println("File deleted: " + filePath.toAbsolutePath());
            } else {
                System.out.println("File does not exist: " + filePath.toAbsolutePath());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete expression file: " + filename + ", error: " + e.getMessage(),
                    e);
        }
    }

    public String getBaseDirectory() {
        return Paths.get(BASE_DIRECTORY).toAbsolutePath().toString();
    }
}