package bot.atri.exp.function;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * @author : IMG
 * @create : 2025/3/5
 */
public class Sum extends BaseFunction implements VariableLengthParam{
    private final Add add = new Add();
    public static Map<Integer, Sum> sumMap = new HashMap<>();

    public Sum() {
        this.parameterNum = -1;
    }

    private Sum(int parameterNum) {
        this.parameterNum = parameterNum;
    }

    @Override
    public void call(Map<String, Object> vars, Stack<Object> stack) {
        if (parameterNum < 1 || !checkParameterNumEnough(stack.size())) {
            throw new IllegalArgumentException("The number of parameters is not enough.");
        }
        Object[] params = new Object[parameterNum];
        for (int i = 0; i < parameterNum; i++) {
            params[i] = stack.pop();
        }
        stack.push(sum(params));
    }

    private Object sum(Object[] params) {
        Object result = params[0];
        for (int i = 1; i < params.length; i++) {
            result = add.add(result, params[i]);
        }
        return result;
    }


    @Override
    public Function getFunctionByParamNum(int num) {
        if (sumMap.containsKey(num)) {
            return sumMap.get(num);
        }
        return new Sum(num);
    }
}
