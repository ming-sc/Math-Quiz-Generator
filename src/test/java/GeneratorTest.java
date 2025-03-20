import bot.atri.exp.ATRIExp;
import bot.atri.exp.ExpEvaluator;
import bot.atri.exp.ExpParser;
import com.img.generator.ExpGenerator;
import org.junit.Test;

import java.util.Stack;

/**
 * @author : IMG
 * @create : 2025/3/20
 */
public class GeneratorTest {
    @Test
    public void generateExpTest() {
        ExpEvaluator evaluator = new ExpEvaluator();
        for (int i = 0; i < 100; i++) {
            ATRIExp atriExp = ExpGenerator.generateExp(3, 1, 10);
            System.out.println(atriExp.getRoot());
            System.out.println("= " + evaluator.evaluate(atriExp));
        }
    }

    @Test
    public void generateExpsTest() {
        ExpEvaluator evaluator = new ExpEvaluator();
        ATRIExp[] exps = ExpGenerator.generateExps(10000, 3, 1, 10);
        for (ATRIExp exp : exps) {
            System.out.println(exp.getRoot());
            System.out.println("= " + evaluator.evaluate(exp));
            System.out.println();
        }
    }
}
