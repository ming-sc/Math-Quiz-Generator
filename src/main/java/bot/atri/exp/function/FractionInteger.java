package bot.atri.exp.function;

import bot.atri.exp.number.Fraction;

import java.util.Map;
import java.util.Stack;

/**
 * @author : IMG
 * @create : 2025/3/17
 */
public class FractionInteger extends BaseFunction {

    public FractionInteger() {
        this.parameterNum = 2;
    }

    @Override
    public void call(Map<String, Object> vars, Stack<Object> stack) {
        if (!checkParameterNumEnough(stack.size())) {
            throw new IllegalArgumentException("Parameter number is not enough");
        }
        Object b = stack.pop();
        Object a = stack.pop();
        stack.push(fractionIntegerBuild(a, b));
    }

    private Object fractionIntegerBuild(Object a, Object b) {
        if (a instanceof Float && b instanceof Fraction) {
            ((Fraction) b).setInteger(((Float) a).intValue());
            return b;
        } else {
            throw new UnsupportedOperationException("Unsupported type: " + a.getClass().getName() + " and " + b.getClass().getName());
        }
    }
}
