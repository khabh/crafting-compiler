public class Token {
    private final Kind kind;
    private final String value;

    public Token(Kind kind, String value) {
        this.kind = kind;
        this.value = value;
    }

    public Kind getKind() {
        return kind;
    }

    public String getValue() {
        return value;
    }
}
