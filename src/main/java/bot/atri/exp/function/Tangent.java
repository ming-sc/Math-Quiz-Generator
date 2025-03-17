package bot.atri.exp.function;

import java.util.Map;
import java.util.Stack;

/**
 * @author : IMG
 * @create : 2025/3/5
 */
public class Tangent extends BaseFunction {
    public Tangent() {
        this.parameterNum = 1;
    }

    @Override
    public void call(Map<String, Object> vars, Stack<Object> stack) {
        if (!checkParameterNumEnough(stack.size())) {
            throw new IllegalArgumentException("The number of parameters is not enough.");
        }
        Object param = stack.pop();
        stack.push(tangent(param));
    }

    private Object tangent(Object param) {
        if (param instanceof Float) {
            return (float) Math.tan((float) param);
        } else {
            throw new IllegalArgumentException("The parameter is not a number.");
        }
    }
}
