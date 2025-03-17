package bot.atri.exp.node;

import java.util.Map;
import java.util.Stack;

/**
 * @author : IMG
 * @create : 2025/3/14
 */
public class StringNode implements Node{
    private String value;

    @Override
    public void eval(Map<String, Object> vars, Stack<Object> stack) {
        stack.push(value);
    }

    @Override
    public void addChild(Node node) {
        throw new UnsupportedOperationException("StringNode can't add child node");
    }

    @Override
    public void visit() {
    }

    public StringNode(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return '"' + value + '"';
    }
}
