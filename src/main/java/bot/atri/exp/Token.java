package bot.atri.exp;

/**
 * @author : IMG
 * @create : 2025/3/1
 */
public class Token {
    private String value;
    private TokenType type;

    public Token(String value, TokenType type) {
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public enum TokenType {
        NUMBER, FUNCTION, VAR, OPERATOR, STRING
    }

    @Override
    public String toString() {
        return "Token{" +
                "value='" + value + '\'' +
                ", type=" + type +
                '}';
    }
}
