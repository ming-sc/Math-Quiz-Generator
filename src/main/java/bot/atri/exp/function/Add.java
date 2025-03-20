package bot.atri.exp.function;

import bot.atri.exp.number.Fraction;

import java.util.Map;
import java.util.Stack;

/**
 * @author : IMG
 * @create : 2025/3/5
 */
public class Add extends BaseFunction implements Operator{

    public Add() {
        this.parameterNum = 2;
    }

    @Override
    public void call(Map<String, Object> vars, Stack<Object> stack) {
        if (!checkParameterNumEnough(stack.size())) {
            throw new IllegalArgumentException("Parameter number is not enough");
        }
        Object b = stack.pop();
        Object a = stack.pop();
        stack.push(add(a, b));
    }

    public Object add(Object a, Object b) {
        if (a instanceof Float && b instanceof Float) {
            return (float)a + (float)b;
        } else if (a instanceof String && b instanceof String) {
            return (String)a + b;
        } else if (a instanceof Float && b instanceof String) {
            return a + (String)b;
        } else if (a instanceof String && b instanceof Float) {
            return (String)a + b;
        } else if (a instanceof Float && b instanceof Fraction){
            return ((Fraction) b).add((float)a);
        } else if (a instanceof Fraction && b instanceof Float) {
            return ((Fraction) a).add((float)b);
        } else if (a instanceof Fraction && b instanceof Fraction) {
            return ((Fraction) a).add((Fraction) b);
        } else {
            throw new UnsupportedOperationException("Unsupported type: " + a.getClass().getName() + " and " + b.getClass().getName());
        }
    }

    @Override
    public String getSymbol() {
        return "+";
    }

    @Override
    public int getPriority() {
        return 1;
    }
}
