package ink.dvc.ezmybatis.core.sqlstruct.condition;

public class Operator {
    public static final Operator eq = new Operator("=");
    public static final Operator ne = new Operator("!=");
    public static final Operator gt = new Operator(">");
    public static final Operator ge = new Operator(">=");
    public static final Operator lt = new Operator("<");
    public static final Operator le = new Operator("<=");
    public static final Operator isNull = new Operator("IS NULL");
    public static final Operator isNotNull = new Operator("IS NOT NULL");
    public static final Operator in = new Operator("IN");
    public static final Operator notIn = new Operator("NOT IN");
    public static final Operator like = new Operator("LIKE");
    public static final Operator unlike = new Operator("NOT LIKE");
    public static final Operator between = new Operator("BETWEEN");
    public static final Operator notBetween = new Operator("NOT BETWEEN");
    public static final Operator regexp = new Operator("REGEXP");
    private String operator;

    protected Operator(String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return this.operator;
    }
}
