package bot.atri.exp;

import bot.atri.exp.exception.SyntaxException;
import bot.atri.exp.function.*;
import bot.atri.exp.node.*;

import java.util.*;

/**
 * @author : IMG
 * @create : 2025/3/1
 */
public class ExpParser {
    public static final Map<String, Integer> operatorPriority = new HashMap<String, Integer>(){{
        put("(", 0);
        put(")", 0);
        put("+", 1);
        put("-", 1);
        put("×", 2);
        put("÷", 2);
        put(",", 5);
        put("'", 3);
        put("/", 4);
    }};

    public static final Map<String, Function> functions = new HashMap<String, Function>(){{
        put("+", new Add());
        put("-", new Subtract());
        put("×", new Multiply());
        put("÷", new Divide());
        put("'", new FractionInteger());
        put("/", new FractionProper());
    }};

    private static final Map<String, Float> constants = new HashMap<String, Float>(){{
        put("Math.PI", 3.1415926f);
        put("Math.E", 2.7182818f);
    }};

    private enum TokenizerState {
        INIT,
        NUMBER,
        OPERATOR,
        VAR,
        STRING,
        STRING_END
    }

    public static Set<Character> numbers = new HashSet<>(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.'));
    public static char QUOTATION_MARK = '"';

    public static ATRIExp parse(String exp) {
        // 去除空格
        String replaced = exp.replace(" ", "");

        // Tokenizer
        String token = "";
        TokenizerState currentState = TokenizerState.INIT;
        List<Token> tokens = new ArrayList<>();
        for (int i = 0; i < replaced.length(); i++) {
            char c = replaced.charAt(i);
            switch (currentState) {
                case INIT:{
                    if (numbers.contains(c)) {
                        // 数字
                        token = "" + c;
                        currentState = TokenizerState.NUMBER;
                    } else if (operatorPriority.containsKey("" + c)) {
                        // 操作符
                        if (c == '-' || c == '+') {
                            // 自动补零
                            tokens.add(new Token("0", Token.TokenType.NUMBER));
                        }
                        tokens.add(new Token("" + c, Token.TokenType.OPERATOR));
                        if (c == '(') {
                            token = "";
                            currentState = TokenizerState.INIT;
                        }
                    } else if (c == QUOTATION_MARK) {
                        // 字符串
                        token = "";
                        currentState = TokenizerState.STRING;
                    } else {
                        // 变量
                        token = "" + c;
                        currentState = TokenizerState.VAR;
                    }
                    break;
                }
                case NUMBER:{
                    if (numbers.contains(c)) {
                        // 数字
                        token += c;
                    } else if (operatorPriority.containsKey("" + c)) {
                        // 操作符
                        if (c == ',') {
                            // 逗号
                            tokens.add(new Token(token, Token.TokenType.NUMBER));
                            tokens.add(new Token(",", Token.TokenType.OPERATOR));
                            token = ",";
                            currentState = TokenizerState.INIT;
                        } else {
                            tokens.add(new Token(token, Token.TokenType.NUMBER));
                            token = "" + c;
                            currentState = TokenizerState.OPERATOR;
                        }
                    } else if (c == QUOTATION_MARK) {
                        // 字符串
                        tokens.add(new Token(token, Token.TokenType.NUMBER));
                        token = "";
                        currentState = TokenizerState.STRING;
                    } else {
                        // 遇到字符转变为变量
                        token += c;
                        currentState = TokenizerState.VAR;
                    }
                    break;
                }
                case OPERATOR:{
                    if (numbers.contains(c)) {
                        // 数字
                        tokens.add(new Token(token, Token.TokenType.OPERATOR));
                        token = "" + c;
                        currentState = TokenizerState.NUMBER;
                    } else if (operatorPriority.containsKey("" + c)) {
                        // 操作符
                        tokens.add(new Token(token, Token.TokenType.OPERATOR));
                        token = "" + c;
                        if (c == '(' || c == ',') {
                            tokens.add(new Token(token, Token.TokenType.OPERATOR));
                            token = "";
                            currentState = TokenizerState.INIT;
                        }
                    } else if (c == QUOTATION_MARK) {
                        // 字符串
                        tokens.add(new Token(token, Token.TokenType.OPERATOR));
                        token = "";
                        currentState = TokenizerState.STRING;
                    } else {
                        // 变量
                        tokens.add(new Token(token, Token.TokenType.OPERATOR));
                        token = "" + c;
                        currentState = TokenizerState.VAR;
                    }
                    break;
                }
                case VAR:{
                    if (numbers.contains(c)) {
                        // 数字
                        token += c;
                    } else if (operatorPriority.containsKey("" + c)) {
                        // 操作符
                        if (c == '(') {
                            // 有左括号升级为函数
                            tokens.add(new Token(token, Token.TokenType.FUNCTION));
                            tokens.add(new Token("(", Token.TokenType.OPERATOR));
                            token = "";
                            currentState = TokenizerState.INIT;
                        } else if (c == ',') {
                            // 逗号
                            tokens.add(new Token(token, Token.TokenType.VAR));
                            tokens.add(new Token(",", Token.TokenType.OPERATOR));
                            token = "";
                            currentState = TokenizerState.INIT;
                        } else {
                            // 变量后面接操作符
                            tokens.add(new Token(token, Token.TokenType.VAR));
                            token = "" + c;
                            currentState = TokenizerState.OPERATOR;
                        }
                    } else if (c == QUOTATION_MARK) {
                        tokens.add(new Token(token, Token.TokenType.VAR));
                        token = "";
                        currentState = TokenizerState.STRING;
                    } else {
                        // 变量
                        token += c;
                    }
                    break;
                }
                case STRING: {
                    if (c == QUOTATION_MARK) {
                        tokens.add(new Token(token, Token.TokenType.STRING));
                        token = "";
                        currentState = TokenizerState.STRING_END;
                    } else {
                        token += c;
                    }
                    break;
                }
                case STRING_END:{
                    if (operatorPriority.containsKey("" + c)) {
                        // 操作符
                        if (c == ')' || c == '+') {
                            token = "" + c;
                            currentState = TokenizerState.OPERATOR;
                        } else if (c == ',') {
                            token = "" + c;
                            currentState = TokenizerState.INIT;
                        } else {
                            throw new SyntaxException("语法错误");
                        }
                    } else {
                        throw new SyntaxException("语法错误");
                    }
                    break;
                }
            }
        }
        // 处理最后一个token
        if (currentState != TokenizerState.INIT && currentState != TokenizerState.STRING_END) {
            Token.TokenType type = null;
            if (currentState == TokenizerState.NUMBER) {
                type = Token.TokenType.NUMBER;
            } else if (currentState == TokenizerState.VAR) {
                type = Token.TokenType.VAR;
            } else if (currentState == TokenizerState.OPERATOR) {
                type = Token.TokenType.OPERATOR;
            } else {
                throw new SyntaxException("字符串未闭合");
            }
            tokens.add(new Token(token, type));
        }

        // Parser
        Stack<Token> opStack = new Stack<>();
        Stack<Node> nodeStack = new Stack<>();
        Stack<Integer> functionParamNumStack = new Stack<>();
        for (Token t : tokens) {
            String tokenValue = t.getValue();
            Token.TokenType tokenType = t.getType();
            if (tokenType.equals(Token.TokenType.NUMBER)) {
                // 数字
                nodeStack.push(new NumberNode(Float.parseFloat(tokenValue)));
            } else if (tokenType.equals(Token.TokenType.OPERATOR)) {
                if (!opStack.isEmpty()) {
                    Token topOp = opStack.get(opStack.size() - 1);
                    Integer tokenPriority = operatorPriority.get(tokenValue);
                    if (tokenPriority != null) {
                        // 如果是右括号
                        if (tokenValue.equals(")")) {
                            while (!opStack.isEmpty() && !opStack.peek().getValue().equals("(")) {
                                Token top = opStack.pop();
                                buildFunctionNode(top.getValue(), nodeStack, functionParamNumStack);
                            }
                            opStack.pop();
                        }else if (tokenValue.equals("(")) {
                            opStack.push(t);
                        }else if (tokenValue.equals(",")) {
                            while (!opStack.isEmpty() && !opStack.peek().getValue().equals("(")) {
                                Token top = opStack.pop();
                                buildFunctionNode(top.getValue(), nodeStack, functionParamNumStack);
                            }
                            // 域内元素个数加一
                            if (!functionParamNumStack.isEmpty()) {
                                functionParamNumStack.set(functionParamNumStack.size() - 1, functionParamNumStack.peek() + 1);
                            }
                        }else {
                            if (!topOp.getType().equals(Token.TokenType.FUNCTION) && tokenPriority > operatorPriority.get(topOp.getValue())) {
                                opStack.push(t);
                            }else {
                                while (!opStack.isEmpty() && (opStack.peek().getType().equals(Token.TokenType.FUNCTION) || operatorPriority.get(opStack.peek().getValue()) >= tokenPriority)) {
                                    Token top = opStack.pop();
                                    buildFunctionNode(top.getValue(), nodeStack, functionParamNumStack);
                                }
                                opStack.push(t);
                            }
                        }
                    }else {
                        throw new SyntaxException("不支持的操作符：" + token);
                    }
                }else {
                    opStack.push(t);
                }
            } else if (tokenType.equals(Token.TokenType.FUNCTION)) {
                if (functions.containsKey(tokenValue)) {
                    opStack.push(t);
                    // 开始记录域内元素个数
                    int index = tokens.indexOf(t) + 2;
                    // 函数后括号问题
                    if (index > tokens.size() - 1) {
                        throw new SyntaxException("函数参数错误");
                    }
                    if (tokens.get(index).getValue().equals(")")) {
                        // 空函数
                        functionParamNumStack.push(0);
                    } else {
                        functionParamNumStack.push(1);
                    }
                } else {
                    throw new SyntaxException("未定义的函数：" + tokenValue);
                }
            }
        }
        while (!opStack.isEmpty()) {
            Token top = opStack.pop();
            if (top.getValue().equals("(")) {
                throw new SyntaxException("未闭合的括号");
            }
            buildFunctionNode(top.getValue(), nodeStack, functionParamNumStack);
        }
        if (nodeStack.size() != 1) {
            throw new SyntaxException("表达式错误");
        }
        Node root = nodeStack.pop();
        root.visit();
        return new ATRIExp(root);
    }

    public static void buildFunctionNode(String functionName, Stack<Node> nodeStack, Stack<Integer> functionParamNumStack) {
        Function function = functions.get(functionName);
        if (function == null) {
            throw new RuntimeException("未定义的函数：" + functionName);
        }

        int parameterNum;
        // 处理可变参数函数
        if (function instanceof VariableLengthParam) {
            parameterNum = functionParamNumStack.pop();
            // 可变参数函数参数个数必须大于等于 1
            if (parameterNum < 1) {
                throw new SyntaxException("函数 " + function.getClass().getSimpleName() + " 参数不足");
            }
            function = ((VariableLengthParam) function).getFunctionByParamNum(parameterNum);
        } else {
            parameterNum = function.getParameterNum();
        }

        int i = 0;
        FunctionNode functionNode = new FunctionNode(function);
        boolean hasVarOrFunction = false;
        while (!nodeStack.isEmpty() && i < parameterNum) {
            Node node = nodeStack.pop();
            if (node instanceof FunctionNode) {
                hasVarOrFunction = true;
            }
            functionNode.addChild(node);
            if (node instanceof FunctionNode) {
                // 如果是函数，需要设置父节点
                ((FunctionNode) node).setParent(functionNode);
            }
            i++;
        }
        if (i < parameterNum) {
            throw new RuntimeException("函数 " + function.getClass().getSimpleName() + " 参数不足");
        }
//        if (!hasVarOrFunction) {
//            // 如果没有变量，直接计算
//            Stack<Object> stack = new Stack<>();
//            functionNode.eval(new HashMap<>(), stack);
//            Object result = stack.pop();
//            if (result instanceof Float) {
//                nodeStack.push(new NumberNode((Float) result));
//            } else if (result instanceof String) {
//                nodeStack.push(new StringNode((String) result));
//            }
//        } else {
            nodeStack.push(functionNode);
//        }
    }

    public static void addConstant(String name, float value) {
        constants.put(name, value);
    }

    public static void removeConstant(String name) {
        constants.remove(name);
    }

    public static void addFunction(String name, Function function) {
        functions.put(name, function);
    }

    public static void removeFunction(String name) {
        functions.remove(name);
    }
}