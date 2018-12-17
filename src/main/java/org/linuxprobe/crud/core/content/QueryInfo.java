package org.linuxprobe.crud.core.content;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.linuxprobe.crud.core.annoatation.Column;
import org.linuxprobe.crud.core.annoatation.JoinColumn;
import org.linuxprobe.crud.core.annoatation.Query;
import org.linuxprobe.crud.core.query.BaseQuery;
import org.linuxprobe.crud.core.query.param.QueryParam;
import org.linuxprobe.crud.utils.FieldUtil;
import org.linuxprobe.crud.utils.StringHumpTool;

import lombok.Getter;
import lombok.Setter;

@Getter
public class QueryInfo {
	public QueryInfo(Class<?> queryType) {
		if (queryType == null) {
			throw new IllegalArgumentException("type can't be null");
		}
		if (!BaseQuery.class.isAssignableFrom(queryType)) {
			throw new IllegalArgumentException(
					queryType.getName() + " is not a subclass of org.linuxprobe.crud.core.query.BaseQuery");
		}
		if (!queryType.isAnnotationPresent(Query.class)) {
			throw new IllegalArgumentException(queryType.getName()
					+ " does not have callout org.linuxprobe.crud.core.annoatation.Query annotation");
		} else {
			this.columnMapQueryFieldInfos = new HashMap<>();
			this.queryEntityCalss = queryType.getAnnotation(Query.class).value();
			this.queryFieldInfos = new LinkedList<>();
			this.queryParamFieldInfos = new LinkedList<>();
			this.baseQueryFieldInfos = new LinkedList<>();
			List<Field> fields = FieldUtil.getAllFields(queryType);
			for (Field field : fields) {
				if (!QueryParam.class.isAssignableFrom(field.getType())
						&& !BaseQuery.class.isAssignableFrom(field.getType())) {
					continue;
				} else {
					QueryFieldInfo fieldInfo = new QueryFieldInfo();
					fieldInfo.setField(field);
					/** 如果是基本类型参数 */
					if (QueryParam.class.isAssignableFrom(field.getType())) {
						fieldInfo.setColumnName(StringHumpTool.humpToLine2(field.getName(), "_"));
						if (field.isAnnotationPresent(Column.class)) {
							Column column = field.getAnnotation(Column.class);
							if (column.value() != null) {
								fieldInfo.setColumnName(column.value());
							}
						}
						this.queryParamFieldInfos.add(fieldInfo);
					}
					/** 如果是查询类型参数 */
					else if (BaseQuery.class.isAssignableFrom(field.getType())) {
						fieldInfo.setPrincipalColumn(StringHumpTool.humpToLine2(field.getName() + "Id", "_"));
						EntityInfo querEntityInfo = UniversalCrudContent.getEntityInfo(queryEntityCalss);
						fieldInfo.setSubordinateColumn(querEntityInfo.getPrimaryKey().getFiledColumn());
						if (field.isAnnotationPresent(JoinColumn.class)) {
							JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
							if (!"".equals(joinColumn.value())) {
								fieldInfo.setPrincipalColumn(joinColumn.value().trim());
							} else if (!"".equals(joinColumn.principal())) {
								fieldInfo.setPrincipalColumn(joinColumn.principal().trim());
							}
							if (!"".equals(joinColumn.subordinate())) {
								fieldInfo.setSubordinateColumn(joinColumn.subordinate().trim());
							}
						}
						this.baseQueryFieldInfos.add(fieldInfo);
					}

					this.columnMapQueryFieldInfos.put(fieldInfo.getColumnName(), fieldInfo);
					this.queryFieldInfos.add(fieldInfo);
				}
			}
		}
	}

	private Class<?> queryEntityCalss;

	private Map<String, QueryFieldInfo> columnMapQueryFieldInfos;

	private List<QueryFieldInfo> queryFieldInfos;

	private List<QueryFieldInfo> queryParamFieldInfos;

	private List<QueryFieldInfo> baseQueryFieldInfos;

	@Getter
	@Setter
	public static class QueryFieldInfo {
		private Field field;
		/** 普通参数列对应列名 */
		private String columnName;
		/** 链接查询时，主表链接字段 */
		private String principalColumn;
		/** 链接查询时，从表链接字段 */
		private String subordinateColumn;

		/** 是否是查询类对象 */
		public boolean isQueryClass() {
			if (BaseQuery.class.isAssignableFrom(field.getType())) {
				return true;
			} else {
				return false;
			}
		}
	}
}
