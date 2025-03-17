package bot.atri.exp.node;

import bot.atri.exp.function.Function;

import java.util.*;

/**
 * @author : IMG
 * @create : 2025/3/14
 */
public class FunctionNode implements Node{
    private Function function;
    private List<Node> children;

    @Override
    public void eval(Map<String, Object> vars, Stack<Object> stack) {
        ListIterator<Node> iterator = children.listIterator(children.size());
        while (iterator.hasPrevious()) {
            iterator.previous().eval(vars, stack);
        }
        function.call(vars, stack);
    }

    @Override
    public void addChild(Node node) {
        children.add(node);
    }

    @Override
    public void visit() {
        if (function.checkParameterNumEnough(children.size())) {
            for (Node child : children) {
                child.visit();
            }
        } else {
            throw new IllegalArgumentException("Function " + function.getClass().getSimpleName() + " need " + function.getParameterNum() + " parameters, but got " + children.size());
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(function.getClass().getSimpleName()).append("(");
        ListIterator<Node> iterator = children.listIterator(children.size());
        while (iterator.hasPrevious()) {
            sb.append(iterator.previous().toString());
            if (iterator.hasPrevious()) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    public FunctionNode(Function function) {
        this.function = function;
        this.children = new LinkedList<>();
    }
}
