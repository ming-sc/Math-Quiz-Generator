package bot.atri.exp;

import bot.atri.exp.node.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * @author : IMG
 * @create : 2025/3/15
 */
public class ExpEvaluator {
    private Map<String, Object> vars;

    public ExpEvaluator() {
        vars = new HashMap<>();
    }

    public ExpEvaluator(Map<String, Object> vars) {
        this.vars = vars;
    }

    public Object evaluate(ATRIExp exp) {
        Stack<Object> stack = new Stack<>();
        Node root = exp.getRoot();
        root.eval(vars, stack);
        if (stack.size() != 1) {
            throw new RuntimeException("The expression is invalid");
        }
        return stack.pop();
    }

    public void setVar(String key, Object value) {
        vars.put(key, value);
    }

    public void clearVars() {
        vars.clear();
    }

    public void setVars(Map<String, Object> vars) {
        this.vars = vars;
    }
}