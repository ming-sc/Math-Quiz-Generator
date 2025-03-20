package bot.atri.exp.node;

import java.util.Map;
import java.util.Stack;

/**
 * @author : IMG
 * @create : 2025/3/14
 */
public class NumberNode implements Node{
    private float value;

    @Override
    public int getIntValue() {
        return (int) value;
    }

    @Override
    public void eval(Map<String, Object> vars, Stack<Object> stack) {
        stack.push(value);
    }

    @Override
    public void addChild(Node node) {
        throw new UnsupportedOperationException("NumberNode can't add child node");
    }

    @Override
    public void visit() {
    }

    public NumberNode(float value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf((int) value);
    }
}
