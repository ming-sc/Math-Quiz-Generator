package bot.atri.exp.function;

import java.util.Map;
import java.util.Stack;

/**
 * @author : IMG
 * @create : 2025/3/5
 */
public interface Function {

    void call(Map<String, Object> vars, Stack<Object> stack);

    int getParameterNum();

    boolean checkParameterNumEnough(int num);
}
