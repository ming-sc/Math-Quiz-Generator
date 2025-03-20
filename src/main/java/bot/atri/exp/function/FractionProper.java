package bot.atri.exp.function;

import bot.atri.exp.number.Fraction;

import java.util.Map;
import java.util.Stack;

/**
 * @author : IMG
 * @create : 2025/3/17
 */
public class FractionProper extends BaseFunction implements Operator {

    public FractionProper() {
        this.parameterNum = 2;
    }

    @Override
    public void call(Map<String, Object> vars, Stack<Object> stack) {
        if (!checkParameterNumEnough(stack.size())) {
            throw new IllegalArgumentException("Parameter number is not enough");
        }
        Object b = stack.pop();
        Object a = stack.pop();
        stack.push(fractionProperBuild(a, b));
    }

    private Object fractionProperBuild(Object a, Object b) {
        if (a instanceof Float && b instanceof Float) {
            return new Fraction(((Float) a).intValue(), ((Float) b).intValue());
        } else {
            throw new UnsupportedOperationException("Unsupported type: " + a.getClass().getName() + " and " + b.getClass().getName());
        }
    }

    @Override
    public String getSymbol() {
        return "/";
    }

    @Override
    public int getPriority() {
        return 4;
    }
}
