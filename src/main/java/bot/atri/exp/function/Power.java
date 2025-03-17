package bot.atri.exp.function;

import java.util.Map;
import java.util.Stack;

/**
 * @author : IMG
 * @create : 2025/3/5
 */
public class Power extends BaseFunction {
    public Power() {
        this.parameterNum = 2;
    }

    @Override
    public void call(Map<String, Object> vars, Stack<Object> stack) {
        if (!checkParameterNumEnough(stack.size())) {
            throw new IllegalArgumentException("The number of parameters is not enough.");
        }
        Object param2 = stack.pop();
        Object param1 = stack.pop();
        stack.push(power(param1, param2));
    }

    private Object power(Object param1, Object param2) {
        if (param1 instanceof Float && param2 instanceof Float) {
            return (float) Math.pow((float) param1, (float) param2);
        } else {
            throw new IllegalArgumentException("The parameter is not a number.");
        }
    }
}
