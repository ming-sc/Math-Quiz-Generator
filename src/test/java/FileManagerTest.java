import com.img.generator.ExpGenerator;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class FileManagerTest {

    @Test
    public void testSingleExpressionFileManagement() {
        // 生成并保存一个表达式
        String filename = ExpGenerator.generateAndSaveExp(2, 1, 10);
        assertNotNull("文件名不应为空", filename);

        // 读取保存的表达式
        String content = ExpGenerator.readExpression(filename);
        assertNotNull("文件内容不应为空", content);
        assertTrue("文件内容应包含等号", content.contains("="));

        // 验证文件列表
        List<String> files = ExpGenerator.listSavedExpressions();
        assertTrue("文件列表应包含新保存的文件", files.contains(filename));

        // 删除表达式文件
        ExpGenerator.deleteExpression(filename);
        files = ExpGenerator.listSavedExpressions();
        assertFalse("文件列表不应包含已删除的文件", files.contains(filename));
    }

    @Test
    public void testBatchExpressionGeneration() {
        // 批量生成表达式
        int numExpressions = 5;
        String[] filenames = ExpGenerator.generateAndSaveExps(numExpressions, 2, 1, 10);
        assertEquals("应生成指定数量的文件", numExpressions, filenames.length);

        // 验证所有文件都被正确保存
        List<String> files = ExpGenerator.listSavedExpressions();
        for (String filename : filenames) {
            assertTrue("文件列表应包含所有生成的文件", files.contains(filename));

            // 验证文件内容
            String content = ExpGenerator.readExpression(filename);
            assertNotNull("文件内容不应为空", content);
            assertTrue("文件内容应包含等号", content.contains("="));
        }

        // 清理测试文件
        for (String filename : filenames) {
            ExpGenerator.deleteExpression(filename);
        }
    }
}