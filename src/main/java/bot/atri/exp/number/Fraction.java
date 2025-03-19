package bot.atri.exp.number;

/**
 * @author : IMG
 * @create : 2025/3/18
 */
public class Fraction implements Comparable<Fraction>{
    private int integer;
    private int numerator;
    private int denominator;

    public Fraction(int numerator, int denominator) {
        this(0, numerator, denominator);
    }

    public Fraction(int integer, int numerator, int denominator) {
        this.integer = integer;
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public void simplify() {
        int gcd = gcd(numerator, denominator);
        numerator /= gcd;
        denominator /= gcd;
        integer += numerator / denominator;
        numerator %= denominator;
        // 处理负数
        if (integer > 0) {
            if (numerator < 0) {
                integer--;
                numerator += denominator;
            }
        }
    }


    public Object add(Fraction fraction) {
        // 分子
        int newNumerator = numerator * fraction.getDenominator() + fraction.getNumerator() * denominator;
        // 分母
        int newDenominator = denominator * fraction.getDenominator();
        return new Fraction(integer + fraction.getInteger(), newNumerator, newDenominator).getValue();
    }

    public Object add(float num) {
        return this.add(new Fraction((int) num, 0, 1));
    }

    public Object subtract(Fraction fraction) {
        // 分子
        int newNumerator = numerator * fraction.getDenominator() - fraction.getNumerator() * denominator;
        // 分母
        int newDenominator = denominator * fraction.getDenominator();
        return new Fraction(integer - fraction.getInteger(), newNumerator, newDenominator).getValue();
    }

    public Object subtract(float num) {
        return this.subtract(new Fraction((int) num, 0, 1));
    }

    public Object multiply(Fraction fraction) {
        int newNumerator = (integer * denominator + numerator) * (fraction.getInteger() * fraction.getDenominator() + fraction.getNumerator());
        int newDenominator = denominator * fraction.getDenominator();
        return new Fraction(0, newNumerator, newDenominator).getValue();
    }

    public Object multiply(float num) {
        return this.multiply(new Fraction((int) num, 0, 1));
    }

    public Object divide(Fraction fraction) {
        int newNumerator = (integer * denominator + numerator) * fraction.getDenominator();
        int newDenominator = denominator * (fraction.getInteger() * fraction.getDenominator() + fraction.getNumerator());
        return new Fraction(0, newNumerator, newDenominator).getValue();
    }

    public Object divide(float num) {
        return this.divide(new Fraction((int) num, 0, 1));
    }

    public Object getValue() {
        this.simplify();
        if (numerator == 0) {
            return (float)integer;
        } else {
            return this;
        }
    }

    public double getDoubleValue() {
        return (double) integer + (double) numerator / (double) denominator;
    }

    private int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    public int getInteger() {
        return integer;
    }

    public void setInteger(int integer) {
        this.integer = integer;
    }

    public int getNumerator() {
        return numerator;
    }

    public void setNumerator(int numerator) {
        this.numerator = numerator;
    }

    public int getDenominator() {
        return denominator;
    }

    public void setDenominator(int denominator) {
        this.denominator = denominator;
    }

    @Override
    public String toString() {
        if (numerator == 0) {
            return integer + "";
        } else if (integer == 0) {
            return numerator + "/" + denominator;
        } else {
            return integer + "'" + numerator + "/" + denominator;
        }
    }

    @Override
    public int compareTo(Fraction o) {
        return Double.compare(this.getDoubleValue(), o.getDoubleValue());
    }
}
