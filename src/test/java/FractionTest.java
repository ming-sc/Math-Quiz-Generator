import bot.atri.exp.number.Fraction;
import org.junit.Test;

/**
 * @author : IMG
 * @create : 2025/3/18
 */
public class FractionTest {
    @Test
    public void simplifyTest() {
        Fraction fraction = new Fraction(2, 4, 2);
        System.out.println(fraction);
        fraction.simplify();
        System.out.println(fraction);
    }

    @Test
    public void addTest() {
        Fraction fraction1 = new Fraction(1, 2, 3);
        Fraction fraction2 = new Fraction(1, 1, 4);
        System.out.println(fraction1.add(fraction2));
        System.out.println(fraction1.add(10));
    }

    @Test
    public void subtractTest() {
        Fraction fraction1 = new Fraction(7, 1, 2);
        Fraction fraction2 = new Fraction(8, 1, 2);
        System.out.println(fraction1.subtract(fraction2));
        System.out.println(fraction1.subtract(10));
    }

    @Test
    public void multiplyTest() {
        Fraction fraction1 = new Fraction(1, 2, 3);
        Fraction fraction2 = new Fraction(1, 1, 4);
        System.out.println(fraction1.multiply(fraction2));
        System.out.println(fraction1.multiply(10));
    }

    @Test
    public void divideTest() {
        Fraction fraction1 = new Fraction(1, 2, 3);
        Fraction fraction2 = new Fraction(1, 1, 4);
        System.out.println(fraction1.divide(fraction2));
        System.out.println(fraction1.divide(10));
    }

}
