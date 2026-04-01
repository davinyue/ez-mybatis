package org.rdlinux.ezmybatis.demo.ezlistener;

import lombok.extern.slf4j.Slf4j;
import org.rdlinux.ezmybatis.core.FieldAccessScope;
import org.rdlinux.ezmybatis.core.interceptor.listener.EzMybatisOnBuildSqlGetFieldListener;
import org.rdlinux.ezmybatis.demo.enc.DbEncrypt;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

import static org.rdlinux.ezmybatis.demo.ezlistener.AbstractEnc.BASE64_ECY_PREFIX;

/**
 * 数据库列加密
 */
@Component
@Slf4j
public class SqlParamEncrypt implements EzMybatisOnBuildSqlGetFieldListener {
    @Override
    public Object onGet(FieldAccessScope accessScope, Class<?> ntType, Field field, Object value) {
        if (accessScope == FieldAccessScope.ENTITY_PERSIST) {
            return value;
        }
        if (!AbstractEnc.valueIsNotEmptyStr(value)) {
            return value;
        }
        String strValue = (String) value;
        if (field.isAnnotationPresent(DbEncrypt.class)) {
            if (strValue.startsWith(BASE64_ECY_PREFIX)) {
                return strValue;
            }
            log.info("{}类的{}属性需要加密", ntType.getName(), field.getName());
            return AbstractEnc.textEncrypt(strValue);
        } else {
            return value;
        }
    }
}
