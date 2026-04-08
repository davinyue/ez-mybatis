package org.rdlinux.ezmybatis.demo.ezlistener;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityFieldInfo;
import org.rdlinux.ezmybatis.demo.enc.DbEncrypt;
import org.rdlinux.ezmybatis.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

public abstract class AbstractEnc {
    public static final String BASE64_ECY_PREFIX = "B$";
    protected final Configuration configuration;

    public AbstractEnc(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * 判断value是否是一个非空字符串
     */
    public static boolean valueIsNotEmptyStr(Object value) {
        if (value == null) {
            return false;
        }
        if (!(value instanceof String)) {
            return false;
        }
        return !((String) value).isEmpty();
    }

    public static String textEncrypt(String encData) {
        return BASE64_ECY_PREFIX + Base64.getEncoder().encodeToString(encData.getBytes());
    }

    protected void enc(Collection<?> entities) {
        Class<?> aClass = entities.iterator().next().getClass();
        EntityClassInfo classInfo = EzEntityClassInfoFactory.forClass(this.configuration, aClass);
        List<EntityFieldInfo> fieldInfos = classInfo.getFieldInfos();
        for (Object entity : entities) {
            for (EntityFieldInfo fieldInfo : fieldInfos) {
                Field field = fieldInfo.getField();
                if (field.isAnnotationPresent(DbEncrypt.class)) {
                    Object value = ReflectionUtils.invokeMethod(entity, fieldInfo.getFieldGetMethod());
                    if (valueIsNotEmptyStr(value)) {
                        //base64编码
                        value = textEncrypt((String) value);
                        ReflectionUtils.setFieldValue(entity, field, value);
                    }
                }
            }
        }
    }
}
