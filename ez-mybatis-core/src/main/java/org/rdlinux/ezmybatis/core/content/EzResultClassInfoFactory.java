package org.rdlinux.ezmybatis.core.content;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.utils.HumpLineStringUtils;
import org.rdlinux.ezmybatis.core.utils.SqlReflectionUtils;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EzResultClassInfoFactory {
    private static final Object lockO = new Object();
    private static Map<String, ResultClassInfo> infoMap = new HashMap<>();

    /**
     * @param resultClass   结果对象类型
     * @param configuration mybatis配置
     */
    public static ResultClassInfo forClass(Configuration configuration, Class<?> resultClass) {
        ResultClassInfo resultClassInfo = infoMap.get(resultClass.getName());
        if (resultClassInfo == null) {
            synchronized (lockO) {
                resultClassInfo = infoMap.get(resultClass.getName());
                if (resultClassInfo == null) {
                    resultClassInfo = buildInfo(configuration, resultClass);
                }
            }
        }
        return resultClassInfo;
    }

    private static ResultClassInfo buildInfo(Configuration configuration, Class<?> resultClass) {
        List<Field> fields = SqlReflectionUtils.getSupportFields(resultClass);
        Map<String, String> columnMapProperty = new HashMap<>((int) (fields.size() / 0.75) + 1);
        fields.forEach(field -> {
            field.setAccessible(true);
            String columnName = field.getName();
            if (configuration.isMapUnderscoreToCamelCase()) {
                HumpLineStringUtils.humpToLine(field.getName());
            }
            if (field.isAnnotationPresent(Column.class)) {
                Column ccn = field.getAnnotation(Column.class);
                if (StringUtils.isNotEmpty(ccn.name())) {
                    columnName = ccn.name();
                }
            }
            columnMapProperty.put(columnName, field.getName());
        });

        ResultClassInfo resultClassInfo = new ResultClassInfo(resultClass, columnMapProperty);
        infoMap.put(resultClass.getName(), resultClassInfo);
        return resultClassInfo;

    }
}
