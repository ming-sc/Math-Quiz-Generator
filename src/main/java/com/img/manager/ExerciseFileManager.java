package com.img.manager;

import com.img.generator.ExpGenerator;
import bot.atri.exp.ATRIExp;
import bot.atri.exp.ExpEvaluator;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class ExerciseFileManager {
    private static final String FOLDER_NAME = "quiz_files";
    private static final String EXERCISE_FILE = "Exercises.txt";
    private static final String ANSWER_FILE = "Answers.txt";
    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private static final ExpEvaluator evaluator = new ExpEvaluator();

    private final Path quizFolder;
    private final Path exerciseFile;
    private final Path answerFile;

    public ExerciseFileManager() {
        // 在当前工作目录下创建quiz_files文件夹
        Path currentDir = Paths.get(System.getProperty("user.dir")).toAbsolutePath().normalize();
        this.quizFolder = currentDir.resolve(FOLDER_NAME);
        this.exerciseFile = quizFolder.resolve(EXERCISE_FILE);
        this.answerFile = quizFolder.resolve(ANSWER_FILE);

        // 创建文件夹
        try {
            Files.createDirectories(quizFolder);
        } catch (IOException e) {
            System.err.println("创建文件夹失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean generateAndSaveExercises(int count, int maxValue) {
        try {
            ATRIExp[] atriExps = ExpGenerator.generateExps(count, 3, 1, maxValue);
            List<String> exerciseLines = new ArrayList<>();
            List<String> answerLines = new ArrayList<>();
            int i = 1;
            for (ATRIExp atriExp : atriExps) {
                exerciseLines.add(i + ". " + atriExp.getRoot().toString());
                answerLines.add(i + ". " + evaluator.evaluate(atriExp).toString());
                i++;
            }
            saveToFile(exerciseFile, exerciseLines);
            saveToFile(answerFile, answerLines);
            return true;
        } catch (Exception e) {
            System.err.println("生成题目失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<String> readLinesFromFile(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path, CHARSET);
        if (lines.isEmpty()) {
            throw new IOException("文件内容为空: " + path);
        }
        return lines;
    }

    private void saveToFile(Path filePath, List<String> lines) throws IOException {
        if (lines == null || lines.isEmpty()) {
            throw new IOException("没有内容可保存");
        }

        // 确保父目录存在
        Files.createDirectories(filePath.getParent());

        // 使用原子写入操作
        Files.write(filePath, lines, CHARSET, StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);

        System.out.println("文件已保存: " + filePath);
    }
}