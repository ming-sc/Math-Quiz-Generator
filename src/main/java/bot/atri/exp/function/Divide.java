package bot.atri.exp.function;

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
            return (float)a / (float)b;
        } else {
            throw new UnsupportedOperationException("Unsupported type: " + a.getClass().getName() + " and " + b.getClass().getName());
        }
    }
}
