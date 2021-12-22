package ink.dvc.ezmybatis.core.sqlgenerate.dm;

import ink.dvc.ezmybatis.core.sqlgenerate.oracle.OracleSqlGenerate;

public class DmSqlGenerate extends OracleSqlGenerate {
    private static volatile DmSqlGenerate instance;

    private DmSqlGenerate() {
    }

    public static DmSqlGenerate getInstance() {
        if (instance == null) {
            synchronized (DmSqlGenerate.class) {
                if (instance == null) {
                    instance = new DmSqlGenerate();
                }
            }
        }
        return instance;
    }
}
