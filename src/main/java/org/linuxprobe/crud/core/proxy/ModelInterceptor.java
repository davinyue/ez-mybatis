package org.linuxprobe.crud.core.proxy;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NavigableSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;

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

import net.sf.cglib.proxy.MethodProxy;

public class ModelInterceptor extends AbstractMethodInterceptor {
	private SqlSessionExtend sqlSessionExtend;
	private Set<String> handledMethod = new HashSet<>();

	public ModelInterceptor(SqlSessionExtend sqlSessionExtend) {
		this.sqlSessionExtend = sqlSessionExtend;
	}

	@Override
	public boolean preHandle(CglibJoinPoint joinPoint) throws Throwable {
		return true;
	}

	@Override
	public void afterCompletion(CglibJoinPoint joinPoint) throws Throwable {
		Method method = joinPoint.getMethod();
		MethodProxy methodProxy = joinPoint.getMethodProxy();
		Object result = joinPoint.getResult();
		Object obj = joinPoint.getProxy();
		Object[] args = joinPoint.getArgs();
		/** 如果没处理过该方法 */
		if (result != null && !handledMethod.contains(method.getName())) {
			/** 如果用户调用set方法, 则认为已经处理过get方法 */
			if (method.getName().startsWith("set")) {
				handledMethod.add(method.getName().replace("set", "get"));
			} else if (method.getName().startsWith("get")) {
				Field field = ReflectionUtils.getFieldByMethod(obj.getClass(), method);
				if (field.isAnnotationPresent(OneToOne.class)) {
					/** 标记该方法已经处理 */
					handledMethod.add(method.getName());
					handldeOneToOne(obj, field);
					/** 调用目标对象的方法，方便用户对数据进行进一步处理 */
					joinPoint.setResult(methodProxy.invokeSuper(obj, args));
				} else if (field.isAnnotationPresent(OneToMany.class)) {
					/** 标记该方法已经处理 */
					handledMethod.add(method.getName());
					handldeOneToMany(obj, field);
					/** 调用目标对象的方法，方便用户对数据进行进一步处理 */
					joinPoint.setResult(methodProxy.invokeSuper(obj, args));
				} else if (field.isAnnotationPresent(ManyToMany.class)) {
					/** 标记该方法已经处理 */
					handledMethod.add(method.getName());
					handldeManyToMany(obj, field);
					/** 调用目标对象的方法，方便用户对数据进行进一步处理 */
					joinPoint.setResult(methodProxy.invokeSuper(obj, args));
				}
			}
		}
	}

	/**
	 * 一对一关系处理
	 * 
	 * @throws Exception
	 */
	private Object handldeOneToOne(Object obj, Field field) throws Exception {
		String columnName = StringUtils.humpToLine(field.getName()) + "_id";
		if (!field.getAnnotation(OneToOne.class).value().isEmpty()) {
			columnName = field.getAnnotation(OneToOne.class).value();
		}
		/** 获取列对应的field info */
		FieldInfo correlationFieldInfo = UniversalCrudContent.getEntityInfo(obj.getClass()).getColumnMapFieldInfo()
				.get(columnName);
		if (correlationFieldInfo == null) {
			throw new IllegalArgumentException(columnName + " column does not have a corresponding field.");
		}
		Serializable correlationFieldValue = (Serializable) ReflectionUtils.getFieldValue(obj,
				correlationFieldInfo.getField());
		if (correlationFieldValue != null) {
			Object result = sqlSessionExtend.selectByPrimaryKey(correlationFieldValue, field.getType());
			ReflectionUtils.setField(obj, field, result);
			return result;
		} else {
			return null;
		}
	}

	/**
	 * 一对多关系处理
	 * 
	 * @throws Exception
	 */
	private Object handldeOneToMany(Object obj, Field field) throws Exception {
		if (!Collection.class.isAssignableFrom(field.getType())) {
			throw new IllegalArgumentException("in " + obj.getClass().getName() + " " + field.getType().getName()
					+ " must be a subclass of " + Collection.class.getName());
		}
		/** 默认的关联属性定义为主键 */
		Field principalField = UniversalCrudContent.getEntityInfo(obj.getClass()).getPrimaryKey().getField();
		/** 从表默认的条件列为主表的表名加‘_id’ */
		String subordinateColumn = UniversalCrudContent.getEntityInfo(obj.getClass()).getTableName() + "_id";
		/** 判断是否需要更加注解改写以上两个变量的值 */
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
		Class<?> subordinateClass = ReflectionUtils.getFiledGenericclass(field, 0);
		Serializable principalFieldValue = (Serializable) ReflectionUtils.getFieldValue(obj, principalField);
		if (principalFieldValue == null) {
			return null;
		}
		@SuppressWarnings("unchecked")
		List<Object> daoResults = (List<Object>) sqlSessionExtend.selectByColumn(subordinateColumn, principalFieldValue,
				subordinateClass);
		Collection<?> result = this.resultConvert(daoResults, field);
		ReflectionUtils.setField(obj, field, result);
		return result;
	}

	/**
	 * 多对多关系处理
	 * 
	 * @throws Exception
	 */
	private Object handldeManyToMany(Object obj, Field field) throws Exception {
		if (!Collection.class.isAssignableFrom(field.getType())) {
			throw new IllegalArgumentException("in " + obj.getClass().getName() + " " + field.getType().getName()
					+ " must be a subclass of " + Collection.class.getName());
		}
		EntityInfo entityInfo = UniversalCrudContent.getEntityInfo(obj.getClass());
		String currentTable = entityInfo.getTableName();
		/** 中间表条件字段的值 */
		Serializable primaryKey = (Serializable) ReflectionUtils.getFieldValue(obj,
				entityInfo.getPrimaryKey().getField());
		Class<?> needSelectModelType = ReflectionUtils.getFiledGenericclass(field, 0);
		EntityInfo needSelectEntityInfo = UniversalCrudContent.getEntityInfo(needSelectModelType);
		String needSelectTable = needSelectEntityInfo.getTableName();
		/** 中间表 */
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
		@SuppressWarnings("unchecked")
		List<Object> datas = (List<Object>) this.sqlSessionExtend.selectBySql(sql, needSelectModelType);
		Collection<?> result = this.resultConvert(datas, field);
		ReflectionUtils.setField(obj, field, result);
		return result;
	}

	/** 把查询出来的结果转换为实体需要的容器类型 */
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
			Vector<Object> vector = new Vector<>();
			vector.addAll(datas);
			result = vector;
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
			PriorityQueue<Object> priorityQueue = new PriorityQueue<Object>();
			priorityQueue.addAll(datas);
			result = priorityQueue;
		}
		// deque
		else if (Deque.class.isAssignableFrom(field.getType())) {
			ArrayDeque<Object> arrayDeque = new ArrayDeque<Object>();
			arrayDeque.addAll(datas);
			result = arrayDeque;
		}
		return result;
	}

	/** 把传入对象的值复制给代理对象 */
	public void copy(Object source, Object target) {
		Class<?> realCalss = ReflectionUtils.getRealCalssOfProxyClass(target.getClass());
		if (!source.getClass().getName().equals(realCalss.getName())) {
			throw new IllegalArgumentException("instance attributes of " + source.getClass().getName()
					+ " cannot be copied to " + realCalss.getName() + " object");
		}
		List<Field> fields = ReflectionUtils.getAllFields(source.getClass());
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				field.set(target, field.get(source));
			} catch (IllegalArgumentException | IllegalAccessException e) {
			}
		}
		this.cleanMark();
	}

	/** 清除get和set方法调用标记 */
	public void cleanMark() {
		this.handledMethod.clear();
	}
}
