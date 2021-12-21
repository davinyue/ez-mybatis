package ink.dvc.ezmybatis.core.sqlgenerate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

public class MybatisParamEscape {
    public static String getEscapeChar(Object param) {
        if (param instanceof CharSequence || param instanceof Date || param instanceof LocalTime
                || param instanceof LocalDate || param instanceof LocalDateTime || param instanceof Enum) {
            return "#";
        } else {
            return "$";
        }
    }
}
