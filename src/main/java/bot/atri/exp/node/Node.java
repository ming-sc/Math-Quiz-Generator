package bot.atri.exp.node;

import java.util.Map;
import java.util.Stack;

/**
 * @author : IMG
 * @create : 2025/3/15
 */
public interface Node {

    int getIntValue();

    void eval(Map<String, Object> vars, Stack<Object> stack);

    void addChild(Node node);

    void visit();
}
