package org.rdlinux.ezmybatis.demo.ezlistener;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityFieldInfo;
import org.rdlinux.ezmybatis.core.interceptor.listener.EzMybatisQueryRetListener;
import org.rdlinux.ezmybatis.demo.enc.DbEncrypt;
import org.rdlinux.ezmybatis.utils.ReflectionUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import static org.rdlinux.ezmybatis.demo.ezlistener.AbstractEnc.BASE64_ECY_PREFIX;

/**
 * 数据库列解密
 */
@Component
@Slf4j
public class QueryRetDecrypt implements EzMybatisQueryRetListener {
    protected final Configuration configuration;

    public QueryRetDecrypt(Configuration configuration) {
        this.configuration = configuration;
    }

    public static String textDecrypt(String decData) {
        if (decData.startsWith(BASE64_ECY_PREFIX)) {
            decData = decData.substring(2);
        } else {
            return decData;
        }
        byte[] bytes = Base64.getDecoder().decode(decData.getBytes());
        return new String(bytes, StandardCharsets.UTF_8);
    }

    @Override
    public void onBatchBuildDone(List<Object> entities) {
        Class<?> aClass = entities.iterator().next().getClass();
        EntityClassInfo classInfo = EzEntityClassInfoFactory.forClass(this.configuration, aClass);
        List<EntityFieldInfo> fieldInfos = classInfo.getFieldInfos();
        for (Object entity : entities) {
            for (EntityFieldInfo fieldInfo : fieldInfos) {
                Field field = fieldInfo.getField();
                if (field.isAnnotationPresent(DbEncrypt.class)) {
                    Object value = ReflectionUtils.invokeMethod(entity, fieldInfo.getFieldGetMethod());
                    if (value instanceof String) {
                        if (this.isNeedDec((String) value)) {
                            value = textDecrypt((String) value);
                            ReflectionUtils.setFieldValue(entity, field, value);
                        }
                    }

                }
            }
        }
    }

    private boolean isNeedDec(String strValue) {
        return strValue != null && strValue.startsWith(BASE64_ECY_PREFIX);
    }
}
