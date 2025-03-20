package bot.atri.exp.node;

import bot.atri.exp.function.FractionInteger;
import bot.atri.exp.function.FractionProper;
import bot.atri.exp.function.Function;
import bot.atri.exp.function.Operator;
import bot.atri.exp.number.Fraction;

import java.util.*;

/**
 * @author : IMG
 * @create : 2025/3/14
 */
public class FunctionNode implements Node{
    private Function function;
    private List<Node> children;
    private Node parent;
    private int intValue;

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
        if (function instanceof Operator) {
            boolean needSpace = !(function instanceof FractionInteger || function instanceof FractionProper);
            sb.append(children.get(1).toString());
            if (needSpace) {
                sb.append(" ");
            }
            Operator op = (Operator) function;
            String opSymbol = op.getSymbol();
            sb.append(opSymbol);
            if (needSpace) {
                sb.append(" ");
            }
            sb.append(children.get(0).toString());
            if (parent != null && parent instanceof FunctionNode) {
                Operator parentOp = (Operator) ((FunctionNode) parent).function;
                String parentOpSymbol = parentOp.getSymbol();
                if (
                        op.getPriority() < parentOp.getPriority()
                        || (parentOpSymbol.equals("-") && (opSymbol.equals("-") || opSymbol.equals("+")))
                        || (parentOpSymbol.equals("÷") && (opSymbol.equals("÷") || opSymbol.equals("×")))
                ) {
                    sb.insert(0, "(");
                    sb.append(")");
                }
            }
        } else {
            sb.append(function.getClass().getSimpleName()).append("(");
            ListIterator<Node> iterator = children.listIterator(children.size());
            while (iterator.hasPrevious()) {
                sb.append(iterator.previous().toString());
                if (iterator.hasPrevious()) {
                    sb.append(", ");
                }
            }
            sb.append(")");
        }
        return sb.toString();
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public FunctionNode(Function function) {
        this.function = function;
        this.children = new LinkedList<>();
    }

    public void calculateIntValue() {
        Stack<Object> stack = new Stack<>();
        ListIterator<Node> iterator = children.listIterator(children.size());
        while (iterator.hasPrevious()) {
            stack.push((float)iterator.previous().getIntValue());
        }
        function.call(null, stack);
        Object result = stack.pop();
        if (result instanceof Float) {
            intValue = ((Float) result).intValue();
        } else if (result instanceof Fraction) {
            intValue = ((Fraction) result).toInt();
        }
    }

    @Override
    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }
}
