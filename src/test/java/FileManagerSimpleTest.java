import com.filemanager.ExpressionFileManager;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileManagerSimpleTest {

    private ExpressionFileManager manager;

    @Before
    public void setup() {
        System.out.println("\n========== 开始测试 ==========");
        manager = new ExpressionFileManager();
        System.out.println("初始化完成 - 文件存储目录: " + manager.getBaseDirectory());
    }

    @Test
    public void testBasicFileOperations() {
        try {
            System.out.println("\n----- 测试基本文件操作 -----");

            // 步骤1：保存表达式
            System.out.println("\n1. 测试保存表达式");
            String content = "1 + 1 = 2";
            String filename = manager.saveExpression(content, "test");
            assertNotNull("文件名不应为空", filename);
            System.out.println("✓ 保存表达式成功: " + filename);

            // 步骤2：验证文件存在
            System.out.println("\n2. 验证文件是否存在");
            assertTrue("文件应该存在", Files.exists(Paths.get(manager.getBaseDirectory(), filename)));
            System.out.println("✓ 文件存在性验证成功");

            // 步骤3：读取文件
            System.out.println("\n3. 测试读取文件内容");
            String readContent = manager.readExpression(filename);
            assertEquals("文件内容应该匹配", content, readContent);
            System.out.println("✓ 文件内容读取成功: " + readContent);

            // 步骤4：列出文件
            System.out.println("\n4. 测试列出所有文件");
            List<String> files = manager.listExpressionFiles();
            assertTrue("文件列表应包含新文件", files.contains(filename));
            System.out.println("✓ 文件列表获取成功:");
            files.forEach(file -> System.out.println("  - " + file));

            // 步骤5：删除文件
            System.out.println("\n5. 测试删除文件");
            manager.deleteExpression(filename);
            assertFalse("文件应该已被删除", Files.exists(Paths.get(manager.getBaseDirectory(), filename)));
            System.out.println("✓ 文件删除成功");

            System.out.println("\n基本文件操作测试全部通过！");

        } catch (Exception e) {
            System.err.println("\n❌ 测试失败:");
            e.printStackTrace();
            fail("测试过程中发生异常: " + e.getMessage());
        }
    }

    @Test
    public void testErrorHandling() {
        System.out.println("\n----- 测试错误处理 -----");

        try {
            System.out.println("\n1. 测试空表达式");
            try {
                manager.saveExpression("  ", "test");
                fail("应该抛出IllegalArgumentException");
            } catch (IllegalArgumentException e) {
                System.out.println("✓ 空表达式测试通过");
            }

            System.out.println("\n2. 测试null表达式");
            try {
                manager.saveExpression(null, "test");
                fail("应该抛出IllegalArgumentException");
            } catch (IllegalArgumentException e) {
                System.out.println("✓ null表达式测试通过");
            }

            System.out.println("\n3. 测试null类型");
            try {
                manager.saveExpression("1 + 1 = 2", null);
                fail("应该抛出IllegalArgumentException");
            } catch (IllegalArgumentException e) {
                System.out.println("✓ null类型测试通过");
            }

            System.out.println("\n4. 测试读取不存在的文件");
            try {
                manager.readExpression("nonexistent.txt");
                fail("应该抛出RuntimeException");
            } catch (RuntimeException e) {
                System.out.println("✓ 读取不存在文件测试通过");
            }

            System.out.println("\n错误处理测试全部通过！");

        } catch (Exception e) {
            System.err.println("\n❌ 测试失败:");
            e.printStackTrace();
            fail("测试过程中发生异常: " + e.getMessage());
        }
    }
}