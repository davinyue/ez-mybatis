package ink.dvc.ezmybatis.core.sqlstruct;

public class Limit {
    private int skip;
    private int size;

    public Limit(int skip, int size) {
        this.skip = skip;
        this.size = size;
    }

    public Limit() {
        this.skip = 0;
        this.size = 20;
    }

    public int getSkip() {
        return this.skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
