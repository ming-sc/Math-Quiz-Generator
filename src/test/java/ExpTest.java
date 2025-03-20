import bot.atri.exp.ATRIExp;
import bot.atri.exp.ExpEvaluator;
import bot.atri.exp.ExpParser;
import bot.atri.exp.number.Fraction;
import org.junit.Test;

/**
 * @author : IMG
 * @create : 2025/3/17
 */
public class ExpTest {

    @Test
    public void FractionParserTest() {
        String exp = "(1 - 4 × (4 ' 2 / 5 + 4 ' 2 / 3 ÷ 6 + 6 ' 1 / 5 - (2 + 6) ÷ 6)) ÷ 6 × 6 + 46 ÷ 11 × 10";
        ATRIExp atriExp = ExpParser.parse(exp);
        System.out.println(atriExp.getRoot());
        System.out.println(new ExpEvaluator().evaluate(atriExp));
    }

    @Test
    public void FractionTest() {
        Fraction fraction = new Fraction(1, 2, 4);
        System.out.println(fraction);
        fraction.simplify();
        System.out.println(fraction);
    }
}