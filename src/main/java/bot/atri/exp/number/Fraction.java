package bot.atri.exp.number;

import bot.atri.exp.ExpParser;
import bot.atri.exp.node.FunctionNode;
import bot.atri.exp.node.Node;
import bot.atri.exp.node.NumberNode;

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
        if (denominator == 0) {
            this.denominator = 1;
        }
    }

    public void simplify() {
        int gcd = gcd(numerator, denominator);
        if (gcd == 0) {
            return;
        }
        numerator /= gcd;
        denominator /= gcd;
        try {
            integer += numerator / denominator;
        } catch (Exception e) {
            return;
        }
        numerator %= denominator;
        // 处理负数
        if (integer > 0) {
            if (numerator < 0) {
                integer--;
                numerator += denominator;
            } else if (denominator < 0) {
                integer--;
                denominator = -denominator;
                numerator = denominator - numerator;
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

    public static Fraction randomFraction(int min, int max) {
        // 分数计算结果必须在 min 和 max 之间
        int integer = (int) (Math.random() * (max - min) + min);
        int numerator = (int) (Math.random() * (max - min + 1) + min);
        int denominator = (int) (Math.random() * (max - min + 1) + numerator);
        return new Fraction(integer, numerator, denominator);
    }

    public static Node toNode(Fraction fraction) {
        fraction.simplify();
//        System.out.println(fraction.integer + " " + fraction.numerator + " " + fraction.denominator);
        if (fraction.getNumerator() == 0) {
            return new NumberNode(fraction.getInteger());
        } else if (fraction.getInteger() == 0) {
            FunctionNode functionNode = new FunctionNode(ExpParser.functions.get("/"));
            functionNode.addChild(new NumberNode(fraction.getNumerator()));
            functionNode.addChild(new NumberNode(fraction.getDenominator()));
            functionNode.setIntValue(fraction.toInt());
            return functionNode;
        } else {
            FunctionNode functionNode = new FunctionNode(ExpParser.functions.get("'"));
            FunctionNode functionNode1 = new FunctionNode(ExpParser.functions.get("/"));
            functionNode1.addChild(new NumberNode(fraction.getDenominator()));
            functionNode1.addChild(new NumberNode(fraction.getNumerator()));
            functionNode.addChild(functionNode1);
            functionNode.addChild(new NumberNode(fraction.getInteger()));
            functionNode.setIntValue(fraction.toInt());
            return functionNode;
        }
    }

    public int toInt() {
        return (int)Math.ceil((float)numerator / (float) denominator + integer);
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
