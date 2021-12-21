package ink.dvc.ezmybatis.core.sqlstruct.selectitem;

public abstract class AbstractSelectItem implements SelectItem {
    private String alias;

    public String getAlias() {
        return this.alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
