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

import org.linuxprobe.crud.core.annoatation.OneToMany;
import org.linuxprobe.crud.core.annoatation.OneToOne;
import org.linuxprobe.crud.core.content.EntityInfo;
import org.linuxprobe.crud.core.content.EntityInfo.FieldInfo;
import org.linuxprobe.crud.core.content.UniversalCrudContent;
import org.linuxprobe.crud.mybatis.session.SqlSessionExtend;
import org.linuxprobe.crud.utils.FieldUtil;
import org.linuxprobe.crud.utils.StringHumpTool;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

public class ModelCglib implements MethodInterceptor, Serializable {
	private static final long serialVersionUID = 4762059333523060842L;

	private SqlSessionExtend sqlSessionExtend;

	public ModelCglib(SqlSessionExtend sqlSessionExtend) {
		this.sqlSessionExtend = sqlSessionExtend;
	}

	private Set<String> handledMethod = new HashSet<>();

	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		Object result = proxy.invokeSuper(obj, args);
		/** 如果结果不是null,则说明用户已经调用过set方法，则直接返回结果 */
		if (result != null) {
			return result;
		}
		/** 如果已经处理过该方法，直接返回结果 */
		else if (handledMethod.contains(method.getName())) {
			return result;
		}
		/** 如果用户调用了对应属性的set方法，则标记get方法为已经处理，直接返回get的结果 */
		else if (method.getName().startsWith("set")) {
			handledMethod.add(method.getName().replace("set", "get"));
			return result;
		} else if (method.getName().startsWith("get")) {
			Field field = FieldUtil.getFieldByMethod(obj.getClass(), method);
			if (field.isAnnotationPresent(OneToOne.class)) {
				/** 标记该方法已经处理 */
				handledMethod.add(method.getName());
				result = handldeOneToOne(obj, field);
				return result;
			} else if (field.isAnnotationPresent(OneToMany.class)) {
				/** 标记该方法已经处理 */
				handledMethod.add(method.getName());
				result = handldeOneToMany(obj, field);
				return result;
			} else {
				return result;
			}
		} else {
			return result;
		}
	}

	/**
	 * 一对一关系处理
	 * 
	 * @throws Exception
	 */
	private Object handldeOneToOne(Object obj, Field field) throws Exception {
		String columnName = StringHumpTool.humpToLine2(field.getName(), "_") + "_id";
		if (!field.getAnnotation(OneToOne.class).value().isEmpty()) {
			columnName = field.getAnnotation(OneToOne.class).value();
		}
		/** 获取列对应的field info */
		FieldInfo correlationFieldInfo = UniversalCrudContent.getEntityInfo(obj.getClass()).getColumnMapFieldInfo()
				.get(columnName);
		if (correlationFieldInfo == null) {
			throw new IllegalArgumentException(columnName + " column does not have a corresponding field.");
		}
		Object result = sqlSessionExtend.selectByPrimaryKey(
				(Serializable) FieldUtil.getFieldValue(obj, correlationFieldInfo.getField()), field.getType());
		FieldUtil.setField(obj, field, result);
		return result;
	}

	/**
	 * 一对多关系处理
	 * 
	 * @throws Exception
	 */
	private Object handldeOneToMany(Object obj, Field field) throws Exception {
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
		;
		Class<?> subordinateClass = FieldUtil.getFiledGenericclass(field, 0);
		Serializable principalFieldValue = (Serializable) FieldUtil.getFieldValue(obj, principalField);
		@SuppressWarnings("unchecked")
		List<Object> daoResults = (List<Object>) sqlSessionExtend.selectByColumn(subordinateColumn, principalFieldValue,
				subordinateClass);
		Collection<?> result = daoResults;
		if (!Collection.class.isAssignableFrom(field.getType())) {
			throw new IllegalArgumentException(field.getType() + "是不被支持的类型");
		}
		// list
		else if (ArrayList.class.isAssignableFrom(field.getType())) {
			result = new ArrayList<>(daoResults);
		} else if (LinkedList.class.isAssignableFrom(field.getType())) {
			result = new LinkedList<>(daoResults);
		} else if (Stack.class.isAssignableFrom(field.getType())) {
			Stack<Object> stack = new Stack<>();
			stack.addAll(daoResults);
			result = stack;
		} else if (Vector.class.isAssignableFrom(field.getType())) {
			Vector<Object> vector = new Vector<>();
			vector.addAll(daoResults);
			result = vector;
		} else if (List.class.isAssignableFrom(field.getType())) {
			result = new LinkedList<>(daoResults);
		}
		// set
		else if (LinkedHashSet.class.isAssignableFrom(field.getType())) {
			result = new LinkedHashSet<>(daoResults);
		} else if (HashSet.class.isAssignableFrom(field.getType())) {
			result = new HashSet<>(daoResults);
		} else if (TreeSet.class.isAssignableFrom(field.getType())) {
			result = new TreeSet<>(daoResults);
		} else if (NavigableSet.class.isAssignableFrom(field.getType())) {
			result = new TreeSet<>(daoResults);
		} else if (SortedSet.class.isAssignableFrom(field.getType())) {
			result = new TreeSet<>(daoResults);
		} else if (Set.class.isAssignableFrom(field.getType())) {
			result = new HashSet<>(daoResults);
		}
		// queue
		else if (Queue.class.isAssignableFrom(field.getType())) {
			PriorityQueue<Object> priorityQueue = new PriorityQueue<Object>();
			priorityQueue.addAll(daoResults);
			result = priorityQueue;
		}
		// deque
		else if (Deque.class.isAssignableFrom(field.getType())) {
			ArrayDeque<Object> arrayDeque = new ArrayDeque<Object>();
			arrayDeque.addAll(daoResults);
			result = arrayDeque;
		}
		FieldUtil.setField(obj, field, result);
		return result;
	}

	/** 创建代理对象 */
	@SuppressWarnings("unchecked")
	public <T> T getInstance(Class<T> entityType) {
		/** 创建加强器，用来创建动态代理类 */
		Enhancer enhancer = new Enhancer();
		/** 为加强器指定要代理的业务类（即：为下面生成的代理类指定父类） */
		enhancer.setSuperclass(entityType);
		/** 设置回调：对于代理类上所有方法的调用，都会调用CallBack，而Callback则需要实现intercept()方法进行拦 */
		enhancer.setCallback(this);
		/** 创建动态代理类对象并返回 */
		return (T) enhancer.create();
	}

}
