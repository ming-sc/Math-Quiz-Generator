package com.img.manager;

import com.img.generator.ExpGenerator;
import bot.atri.exp.ExpParser;
import bot.atri.exp.ATRIExp;
import bot.atri.exp.ExpEvaluator;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ExerciseFileManager {
    private static final String FOLDER_NAME = "quiz_files";
    private static final String EXERCISE_FILE = "Exercises.txt";
    private static final String ANSWER_FILE = "Answers.txt";
    private static final String GRADE_FILE = "Grade.txt";
    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private static final ExpEvaluator evaluator = new ExpEvaluator();
    private static final double EPSILON = 0.0001; // 用于浮点数比较的精度
    private static final int MAX_GENERATION_ATTEMPTS = 5; // 最大尝试次数倍数

    private final Path quizFolder;
    private final Path exerciseFile;
    private final Path answerFile;
    private final Path gradeFile;

    public ExerciseFileManager() {
        // 在当前工作目录下创建quiz_files文件夹
        Path currentDir = Paths.get(System.getProperty("user.dir")).toAbsolutePath().normalize();
        this.quizFolder = currentDir.resolve(FOLDER_NAME);
        this.exerciseFile = quizFolder.resolve(EXERCISE_FILE);
        this.answerFile = quizFolder.resolve(ANSWER_FILE);
        this.gradeFile = quizFolder.resolve(GRADE_FILE);

        // 创建文件夹
        try {
            Files.createDirectories(quizFolder);
            System.out.println("文件将保存在: " + quizFolder);
        } catch (IOException e) {
            System.err.println("创建文件夹失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean generateAndSaveExercises(int count, int maxValue) {
        if (count <= 0 || maxValue <= 0) {
            System.err.println("题目数量和数值范围必须大于0");
            return false;
        }

        try {
            // 尝试多次生成以提高成功率
            for (int attempt = 1; attempt <= MAX_GENERATION_ATTEMPTS; attempt++) {
                List<String> exercises = new ArrayList<>(count);
                List<String> answers = new ArrayList<>(count);

                if (tryGenerateExercises(count, maxValue, exercises, answers)) {
                    // 保存题目和答案
                    saveToFile(exerciseFile, exercises);
                    saveToFile(answerFile, answers);
                    return true;
                }

                if (attempt < MAX_GENERATION_ATTEMPTS) {
                    System.out.println("尝试重新生成题目 (尝试 " + (attempt + 1) + "/" + MAX_GENERATION_ATTEMPTS + ")");
                }
            }

            System.err.println("多次尝试后仍无法生成足够数量的有效题目");
            return false;
        } catch (IOException e) {
            System.err.println("文件操作失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("生成题目时发生错误: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private boolean tryGenerateExercises(int count, int maxValue, List<String> exercises, List<String> answers)
            throws Exception {
        // 生成题目，尝试生成更多以增加成功率
        int generateCount = Math.min(count * 3, 50000); // 限制最大生成数量以避免内存问题
        ATRIExp[] exps = ExpGenerator.generateExps(generateCount, 3, 1, maxValue);
        if (exps == null || exps.length == 0) {
            System.err.println("生成题目失败");
            return false;
        }

        // 用于存储已生成的表达式，防止重复
        Set<String> generatedExpressions = new HashSet<>(count * 2);
        AtomicInteger validCount = new AtomicInteger(0);

        // 预处理表达式，过滤无效和重复项
        List<ATRIExp> validExps = Arrays.asList(exps);

        if (validExps.size() < count) {
            System.err.println("过滤后的有效题目数量不足 (" + validExps.size() + "/" + count + ")");
            return false;
        }

        // 格式化题目和答案
        for (int i = 0; i < validExps.size(); i++) {
            ATRIExp exp = validExps.get(i);
            String expression = exp.getRoot().toString();

            // 添加到已生成集合
            generatedExpressions.add(normalizeExpression(expression));

            // 添加题目
            exercises.add(String.format("%d. %s =", i + 1, expression));

            // 计算表达式的值并添加答案
            Object result = evaluator.evaluate(exp);
            String answer;
            if (result instanceof Float) {
                answer = String.format("%.1f", (Float) result);
            } else {
                answer = result.toString();
            }
            answers.add(String.format("%d. %s", i + 1, answer));
        }

        return true;
    }

    public void gradeAnswers(String externalAnswerFile) {
        try {
            Path externalAnswerPath = Paths.get(externalAnswerFile).toAbsolutePath().normalize();

            // 检查文件是否存在
            checkFileExists(externalAnswerPath, "外部答案文件");
            checkFileExists(exerciseFile, "题目文件");
            checkFileExists(answerFile, "正确答案文件");

            // 读取题目、正确答案和外部答案
            List<String> exercises = readLinesFromFile(exerciseFile);
            List<String> correctAnswers = readLinesFromFile(answerFile);
            List<String> externalAnswers = readLinesFromFile(externalAnswerPath);

            validateAnswerFiles(exercises, correctAnswers, externalAnswers);

            List<Integer> correctQuestions = new ArrayList<>();
            List<Integer> wrongQuestions = new ArrayList<>();

            // 检查答案
            processAnswers(exercises, correctAnswers, externalAnswers, correctQuestions, wrongQuestions);

            // 生成并保存成绩报告
            saveGradeReport(correctQuestions, wrongQuestions);

        } catch (IOException e) {
            System.err.println("文件操作失败: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("检查答案时发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void checkFileExists(Path path, String fileType) throws IOException {
        if (!Files.exists(path)) {
            throw new IOException(fileType + "不存在: " + path);
        }
    }

    private List<String> readLinesFromFile(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path, CHARSET);
        if (lines.isEmpty()) {
            throw new IOException("文件内容为空: " + path);
        }
        return lines;
    }

    private void validateAnswerFiles(List<String> exercises, List<String> correctAnswers,
            List<String> externalAnswers) throws IOException {
        if (exercises.size() != correctAnswers.size() || exercises.size() != externalAnswers.size()) {
            throw new IOException("题目和答案数量不匹配：题目=" + exercises.size() +
                    ", 标准答案=" + correctAnswers.size() +
                    ", 外部答案=" + externalAnswers.size());
        }
    }

    private void processAnswers(List<String> exercises, List<String> correctAnswers,
            List<String> externalAnswers, List<Integer> correctQuestions,
            List<Integer> wrongQuestions) {
        for (int i = 0; i < exercises.size(); i++) {
            try {
                // 从正确答案中提取答案（去除题号）
                String expectedAnswer = extractAnswer(correctAnswers.get(i));

                // 从外部答案中提取答案（去除题号）
                String actualAnswer = extractAnswer(externalAnswers.get(i));

                // 比较答案（考虑浮点数精度）
                if (compareAnswers(expectedAnswer, actualAnswer)) {
                    correctQuestions.add(i + 1);
                } else {
                    wrongQuestions.add(i + 1);
                }
            } catch (Exception e) {
                System.err.println("处理题目 " + (i + 1) + " 时出错: " + e.getMessage());
                wrongQuestions.add(i + 1);
            }
        }
    }

    private String extractAnswer(String answerLine) {
        int dotIndex = answerLine.indexOf(".");
        if (dotIndex != -1) {
            return answerLine.substring(dotIndex + 1).trim();
        }
        return answerLine.trim();
    }

    private void saveGradeReport(List<Integer> correctQuestions, List<Integer> wrongQuestions) throws IOException {
        List<String> report = new ArrayList<>();
        report.add(String.format("Correct: %d (%s)",
                correctQuestions.size(),
                formatQuestionNumbers(correctQuestions)));
        report.add(String.format("Wrong: %d (%s)",
                wrongQuestions.size(),
                formatQuestionNumbers(wrongQuestions)));

        saveToFile(gradeFile, report);
    }

    private boolean compareAnswers(String expected, String actual) {
        try {
            // 尝试解析为浮点数
            double expectedValue = Double.parseDouble(expected);
            double actualValue = Double.parseDouble(actual);
            return Math.abs(expectedValue - actualValue) < EPSILON;
        } catch (NumberFormatException e) {
            // 如果不是浮点数，则进行字符串比较
            return expected.equals(actual);
        }
    }

    private String formatQuestionNumbers(List<Integer> numbers) {
        if (numbers.isEmpty()) {
            return "";
        }
        return numbers.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
    }

    private int countOperators(String expression) {
        if (expression == null)
            return 0;

        int count = 0;
        for (char c : expression.toCharArray()) {
            if (c == '+' || c == '-' || c == '×' || c == '÷' || c == '*' || c == '/') {
                count++;
            }
        }
        return count;
    }

    private boolean isDuplicate(String expression, Set<String> generatedExpressions) {
        if (expression == null || expression.trim().isEmpty()) {
            return true;
        }
        // 标准化表达式（移除空格，统一运算符）
        String normalized = normalizeExpression(expression);
        return generatedExpressions.contains(normalized);
    }

    private String normalizeExpression(String expression) {
        if (expression == null)
            return "";
        return expression.replaceAll("\\s+", "")
                .replace("×", "*")
                .replace("÷", "/");
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