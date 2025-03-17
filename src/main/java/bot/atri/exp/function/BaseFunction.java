package bot.atri.exp.function;

import java.util.Map;
import java.util.Stack;

/**
 * @author : IMG
 * @create : 2025/3/5
 */
public class BaseFunction implements Function{
    protected int parameterNum = 0;

    @Override
    public void call(Map<String, Object> vars, Stack<Object> stack) {
        throw new UnsupportedOperationException("This method should be override by subclass: " + this.getClass().getName());
    }

    @Override
    public int getParameterNum() {
        return this.parameterNum;
    }

    @Override
    public boolean checkParameterNumEnough(int num) {
        // -1 即为不限制参数个数
        if (this.parameterNum == -1) {
            return true;
        } else {
            return this.parameterNum <= num;
        }
    }
}
