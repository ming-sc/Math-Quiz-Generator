import com.img.evaluator.ExpsEvaluator;
import org.junit.Test;

/**
 * @author : IMG
 * @create : 2025/3/20
 */
public class ExpsEvaluatorTest {
    @Test
    public void evaluateExpsTest() {
        String[] exps = {
            "1 + 2",
            "1 - 2",
            "1 × 2",
            "1 / 2",
            "1 + 2 × 3",
            "1 + 2 × 3",
        };
        String[] results = {
            "3",
            "-1",
            "2",
            "1/2",
            "7",
            "1.6666666666666667",
        };
        System.out.println(ExpsEvaluator.correctExp(exps, results));
    }
}
