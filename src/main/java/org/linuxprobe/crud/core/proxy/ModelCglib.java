package org.linuxprobe.crud.core.proxy;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.linuxprobe.crud.core.annoatation.OneToOne;
import org.linuxprobe.crud.mybatis.session.SqlSessionExtend;
import org.linuxprobe.crud.utils.FieldUtil;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

public class ModelCglib implements MethodInterceptor {
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
		String columnName = field.getName() + "Id";
		if (!field.getAnnotation(OneToOne.class).value().isEmpty()) {
			columnName = field.getAnnotation(OneToOne.class).value();
		}
		Object result = sqlSessionExtend.selectByPrimaryKey(
				(Serializable) FieldUtil.getFieldValueByFieldName(obj, columnName), field.getType());
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
