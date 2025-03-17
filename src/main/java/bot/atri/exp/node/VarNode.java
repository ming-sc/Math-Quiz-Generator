package bot.atri.exp.node;

import java.util.Map;
import java.util.Stack;

/**
 * @author : IMG
 * @create : 2025/3/14
 */
public class VarNode implements Node{
    private String varName;

    @Override
    public void eval(Map<String, Object> vars, Stack<Object> stack) {
        if (!vars.containsKey(varName)) {
            throw new IllegalArgumentException("Variable not found: " + varName);
        }
        stack.push(vars.get(varName));
    }

    @Override
    public void addChild(Node node) {
        throw new UnsupportedOperationException("VarNode can't add child node");
    }

    @Override
    public void visit() {
    }

    public VarNode(String varName) {
        this.varName = varName;
    }

    @Override
    public String toString() {
        return varName;
    }
}