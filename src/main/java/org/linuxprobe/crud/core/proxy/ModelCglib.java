package org.linuxprobe.crud.core.proxy;

import org.linuxprobe.crud.core.annoatation.ManyToMany;
import org.linuxprobe.crud.core.annoatation.OneToMany;
import org.linuxprobe.crud.core.annoatation.OneToOne;
import org.linuxprobe.crud.core.content.EntityInfo;
import org.linuxprobe.crud.core.content.EntityInfo.FieldInfo;
import org.linuxprobe.crud.core.content.UniversalCrudContent;
import org.linuxprobe.crud.mybatis.session.SqlSessionExtend;
import org.linuxprobe.luava.proxy.AbstractMethodInterceptor;
import org.linuxprobe.luava.proxy.CglibJoinPoint;
import org.linuxprobe.luava.reflection.ReflectionUtils;
import org.linuxprobe.luava.string.StringUtils;
import org.springframework.cglib.proxy.MethodProxy;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class ModelCglib extends AbstractMethodInterceptor implements Serializable {
    private static final long serialVersionUID = -4926541484203162722L;
    private transient SqlSessionExtend sqlSessionExtend;
    private transient Set<String> handledMethod = new HashSet<>();
    private transient ClassLoader classLoader;

    public ModelCglib(SqlSessionExtend sqlSessionExtend) {
        this.sqlSessionExtend = sqlSessionExtend;
        this.classLoader = this.getClass().getClassLoader();
    }

    public ModelCglib(SqlSessionExtend sqlSessionExtend, ClassLoader classLoader) {
        this(sqlSessionExtend);
        if (classLoader != null) {
            this.classLoader = classLoader;
        }
    }

    @Override
    public boolean preHandle(CglibJoinPoint joinPoint) throws Throwable {
        return true;
    }

    @Override
    public void afterCompletion(CglibJoinPoint joinPoint) throws Throwable {
        if (this.sqlSessionExtend == null || this.handledMethod == null || this.classLoader == null) {
            return;
        }
        Method method = joinPoint.getMethod();
        MethodProxy methodProxy = joinPoint.getMethodProxy();
        Object result = joinPoint.getResult();
        Object obj = joinPoint.getProxy();
        Object[] args = joinPoint.getArgs();
        // 如果没处理过该方法
        if (result == null && !this.handledMethod.contains(method.getName())) {
            // 如果用户调用set方法, 则认为已经处理过get方法
            if (method.getName().startsWith("set")) {
                this.handledMethod.add(method.getName().replace("set", "get"));
            } else if (method.getName().startsWith("get")) {
                Field field = ReflectionUtils.getFieldByMethod(obj.getClass(), method);
                if (field != null) {
                    if (field.isAnnotationPresent(OneToOne.class)) {
                        // 标记该方法已经处理
                        this.handledMethod.add(method.getName());
                        this.handldeOneToOne(obj, field);
                        // 调用目标对象的方法，方便用户对数据进行进一步处理
                        joinPoint.setResult(methodProxy.invokeSuper(obj, args));
                    } else if (field.isAnnotationPresent(OneToMany.class)) {
                        // 标记该方法已经处理
                        this.handledMethod.add(method.getName());
                        this.handldeOneToMany(obj, field);
                        // 调用目标对象的方法，方便用户对数据进行进一步处理
                        joinPoint.setResult(methodProxy.invokeSuper(obj, args));
                    } else if (field.isAnnotationPresent(ManyToMany.class)) {
                        // 标记该方法已经处理
                        this.handledMethod.add(method.getName());
                        this.handldeManyToMany(obj, field);
                        // 调用目标对象的方法，方便用户对数据进行进一步处理
                        joinPoint.setResult(methodProxy.invokeSuper(obj, args));
                    }
                }
            }
        }
    }

    /**
     * 一对一关系处理
     */
    private Object handldeOneToOne(Object obj, Field field) throws Exception {
        String columnName = StringUtils.humpToLine(field.getName()) + "_id";
        if (!field.getAnnotation(OneToOne.class).value().isEmpty()) {
            columnName = field.getAnnotation(OneToOne.class).value();
        }
        // 获取列对应的field info
        FieldInfo correlationFieldInfo = UniversalCrudContent.getEntityInfo(obj.getClass()).getColumnMapFieldInfo()
                .get(columnName);
        if (correlationFieldInfo == null) {
            throw new IllegalArgumentException(columnName + " column does not have a corresponding field.");
        }
        Serializable correlationFieldValue = (Serializable) ReflectionUtils.getFieldValue(obj,
                correlationFieldInfo.getField());
        if (correlationFieldValue != null) {
            Object result = this.sqlSessionExtend.selectByPrimaryKey(correlationFieldValue, field.getType(), this.classLoader);
            ReflectionUtils.setFieldValue(obj, field, result, true);
            return result;
        } else {
            return null;
        }
    }

    /**
     * 一对多关系处理
     */
    private Object handldeOneToMany(Object obj, Field field) throws Exception {
        if (!Collection.class.isAssignableFrom(field.getType())) {
            throw new IllegalArgumentException("in " + obj.getClass().getName() + " " + field.getType().getName()
                    + " must be a subclass of " + Collection.class.getName());
        }
        // 默认的关联属性定义为主键
        Field principalField = UniversalCrudContent.getEntityInfo(obj.getClass()).getPrimaryKey().getField();
        // 从表默认的条件列为主表的表名加‘_id’
        String subordinateColumn = UniversalCrudContent.getEntityInfo(obj.getClass()).getTableName() + "_id";
        // 判断是否需要更加注解改写以上两个变量的值
        OneToMany oneToMany = field.getAnnotation(OneToMany.class);
        EntityInfo entityInfo = UniversalCrudContent.getEntityInfo(obj.getClass());
        if (!"".equals(oneToMany.value())) {
            principalField = entityInfo.getColumnMapFieldInfo().get(oneToMany.value()).getField();
        } else if (!"".equals(oneToMany.principal())) {
            principalField = entityInfo.getColumnMapFieldInfo().get(oneToMany.principal()).getField();
        }
        if (!"".equals(oneToMany.subordinate())) {
            subordinateColumn = oneToMany.subordinate();
        }
        Class<?> subordinateClass = ReflectionUtils.getFiledGenericClass(field, 0);
        Serializable principalFieldValue = (Serializable) ReflectionUtils.getFieldValue(obj, principalField);
        if (principalFieldValue == null) {
            return null;
        }
        List<Object> daoResults = (List<Object>) this.sqlSessionExtend.selectByColumn(subordinateColumn, principalFieldValue,
                subordinateClass, this.classLoader);
        Collection<?> result = this.resultConvert(daoResults, field);
        if (result != null && !result.isEmpty()) {
            ReflectionUtils.setFieldValue(obj, field, result, true);
        } else {
            result = null;
        }
        return result;
    }

    /**
     * 多对多关系处理
     */
    private Object handldeManyToMany(Object obj, Field field) throws Exception {
        if (!Collection.class.isAssignableFrom(field.getType())) {
            throw new IllegalArgumentException("in " + obj.getClass().getName() + " " + field.getType().getName()
                    + " must be a subclass of " + Collection.class.getName());
        }
        EntityInfo entityInfo = UniversalCrudContent.getEntityInfo(obj.getClass());
        String currentTable = entityInfo.getTableName();
        // 中间表条件字段的值
        Serializable primaryKey = (Serializable) ReflectionUtils.getFieldValue(obj,
                entityInfo.getPrimaryKey().getField());
        Class<?> needSelectModelType = ReflectionUtils.getFiledGenericClass(field, 0);
        EntityInfo needSelectEntityInfo = UniversalCrudContent.getEntityInfo(needSelectModelType);
        String needSelectTable = needSelectEntityInfo.getTableName();
        // 中间表
        String middleTable = currentTable + "_" + needSelectTable;
        String joinColumn = needSelectTable + "_id";
        String conditionColumn = entityInfo.getTableName() + "_id";
        ManyToMany manyToMany = field.getAnnotation(ManyToMany.class);
        if (!manyToMany.middleTable().isEmpty()) {
            middleTable = manyToMany.middleTable();
        }
        if (!manyToMany.joinColumn().isEmpty()) {
            joinColumn = manyToMany.joinColumn();
        }
        if (!manyToMany.conditionColumn().isEmpty()) {
            conditionColumn = manyToMany.conditionColumn();
        }
        String sql = UniversalCrudContent.getSelectSqlGenerator().generateManyToManySelectSql(middleTable, joinColumn,
                conditionColumn, primaryKey, needSelectModelType);
        List<Object> datas = (List<Object>) this.sqlSessionExtend.selectBySql(sql, needSelectModelType, this.classLoader);
        Collection<?> result = this.resultConvert(datas, field);
        if (result != null && !result.isEmpty()) {
            ReflectionUtils.setFieldValue(obj, field, result, true);
        } else {
            result = null;
        }
        return result;
    }

    /**
     * 把查询出来的结果转换为实体需要的容器类型
     */
    private Collection<?> resultConvert(List<Object> datas, Field field) {
        Collection<?> result = datas;
        if (!Collection.class.isAssignableFrom(field.getType())) {
            throw new IllegalArgumentException(field.getType() + "是不被支持的类型");
        }
        // list
        else if (ArrayList.class.isAssignableFrom(field.getType())) {
            result = new ArrayList<>(datas);
        } else if (LinkedList.class.isAssignableFrom(field.getType())) {
            result = new LinkedList<>(datas);
        } else if (Stack.class.isAssignableFrom(field.getType())) {
            Stack<Object> stack = new Stack<>();
            stack.addAll(datas);
            result = stack;
        } else if (Vector.class.isAssignableFrom(field.getType())) {
            result = new Vector<>(datas);
        } else if (List.class.isAssignableFrom(field.getType())) {
            result = new LinkedList<>(datas);
        }
        // set
        else if (LinkedHashSet.class.isAssignableFrom(field.getType())) {
            result = new LinkedHashSet<>(datas);
        } else if (HashSet.class.isAssignableFrom(field.getType())) {
            result = new HashSet<>(datas);
        } else if (TreeSet.class.isAssignableFrom(field.getType())) {
            result = new TreeSet<>(datas);
        } else if (NavigableSet.class.isAssignableFrom(field.getType())) {
            result = new TreeSet<>(datas);
        } else if (SortedSet.class.isAssignableFrom(field.getType())) {
            result = new TreeSet<>(datas);
        } else if (Set.class.isAssignableFrom(field.getType())) {
            result = new HashSet<>(datas);
        }
        // queue
        else if (Queue.class.isAssignableFrom(field.getType())) {
            result = new PriorityQueue<>(datas);
        }
        // deque
        else if (Deque.class.isAssignableFrom(field.getType())) {
            result = new ArrayDeque<>(datas);
        }
        return result;
    }

    /**
     * 把传入对象的值复制给代理对象
     */
    public void copy(Object source) {
        Class<?> realCalss = ReflectionUtils.getRealCalssOfProxyClass(this.getInstance().getClass());
        if (!source.getClass().getName().equals(realCalss.getName())) {
            throw new IllegalArgumentException("instance attributes of " + source.getClass().getName()
                    + " cannot be copied to " + realCalss.getName() + " object");
        }
        List<Field> fields = ReflectionUtils.getAllFields(source.getClass());
        for (Field field : fields) {
            int modifiers = field.getModifiers();
            if (Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers)) {
                continue;
            }
            field.setAccessible(true);
            try {
                field.set(this.getInstance(), field.get(source));
            } catch (IllegalArgumentException | IllegalAccessException ignored) {
            }
        }
    }

    /**
     * 清除get和set方法调用标记
     */
    public void cleanMark() {
        this.handledMethod.clear();
    }
}
