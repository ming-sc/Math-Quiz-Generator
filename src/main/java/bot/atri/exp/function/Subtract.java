package bot.atri.exp.function;

import bot.atri.exp.number.Fraction;

import java.util.Map;
import java.util.Stack;

/**
 * @author : IMG
 * @create : 2025/3/5
 */
public class Subtract extends BaseFunction implements Operator{

    public Subtract() {
        this.parameterNum = 2;
    }

    @Override
    public void call(Map<String, Object> vars, Stack<Object> stack) {
        if (!checkParameterNumEnough(stack.size())) {
            throw new IllegalArgumentException("Parameter number is not enough");
        }
        Object b = stack.pop();
        Object a = stack.pop();
        stack.push(subtract(a, b));
    }

    public Object subtract(Object a, Object b) {
        if (a instanceof Float && b instanceof Float) {
            return (float)a - (float)b;
        } else if (a instanceof Float && b instanceof Fraction){
            return new Fraction(((Float) a).intValue(), 0, 1).subtract((Fraction) b);
        } else if (a instanceof Fraction && b instanceof Float) {
            return ((Fraction) a).subtract((float)b);
        } else if (a instanceof Fraction && b instanceof Fraction) {
            return ((Fraction) a).subtract((Fraction) b);
        } else {
            throw new UnsupportedOperationException("Unsupported type: " + a.getClass().getName() + " and " + b.getClass().getName());
        }
    }

    @Override
    public String getSymbol() {
        return "-";
    }

    @Override
    public int getPriority() {
        return 1;
    }
}
