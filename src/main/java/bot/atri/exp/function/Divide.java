package bot.atri.exp.function;

import bot.atri.exp.number.Fraction;

import java.util.Map;
import java.util.Stack;

/**
 * @author : IMG
 * @create : 2025/3/5
 */
public class Divide extends BaseFunction{

    public Divide() {
        this.parameterNum = 2;
    }

    @Override
    public void call(Map<String, Object> vars, Stack<Object> stack) {
        if (!checkParameterNumEnough(stack.size())) {
            throw new IllegalArgumentException("Parameter number is not enough");
        }
        Object b = stack.pop();
        Object a = stack.pop();
        stack.push(divide(a, b));
    }

    private Object divide(Object a, Object b) {
        if (a instanceof Float && b instanceof Float) {
            return new Fraction(0, ((Float) a).intValue(), ((Float) b).intValue());
        } else if (a instanceof Fraction && b instanceof Float) {
            return ((Fraction) a).divide((float)b);
        } else if (a instanceof Fraction && b instanceof Fraction) {
            return ((Fraction) a).divide((Fraction) b);
        } else if (a instanceof Float && b instanceof Fraction) {
            return new Fraction(((Float) a).intValue(), 0, 1).divide((Fraction) b);
        } else {
            throw new UnsupportedOperationException("Unsupported type: " + a.getClass().getName() + " and " + b.getClass().getName());
        }
    }
}
