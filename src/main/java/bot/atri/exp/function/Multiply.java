package bot.atri.exp.function;

import bot.atri.exp.number.Fraction;

import java.util.Map;
import java.util.Stack;

/**
 * @author : IMG
 * @create : 2025/3/5
 */
public class Multiply extends BaseFunction implements Operator {

    public Multiply() {
        this.parameterNum = 2;
    }

    @Override
    public void call(Map<String, Object> vars, Stack<Object> stack) {
        if (!checkParameterNumEnough(stack.size())) {
            throw new IllegalArgumentException("Parameter number is not enough");
        }
        Object b = stack.pop();
        Object a = stack.pop();
        stack.push(multiply(a, b));
    }

    public Object multiply(Object a, Object b) {
        if (a instanceof Float) {
            if (b instanceof Float) {
                return (float)a * (float)b;
            } else if (b instanceof Fraction) {
                return ((Fraction) b).multiply((float)a);
            }
        } else if (a instanceof Fraction) {
            if (b instanceof Float) {
                return ((Fraction) a).multiply((float)b);
            } else if (b instanceof Fraction) {
                return ((Fraction) a).multiply((Fraction) b);
            }
        }
        throw new UnsupportedOperationException("Unsupported type: " + a.getClass().getName());
    }

    @Override
    public String getSymbol() {
        return "×";
    }

    @Override
    public int getPriority() {
        return 2;
    }
}
