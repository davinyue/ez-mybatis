package org.linuxprobe.crud.model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.linuxprobe.crud.utils.StringHumpTool;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public abstract class BaseModel {
	public BaseModel(String tableName) {
		this.table = new Table(tableName);
	}

	@Getter
	private Table table;
	/** 主键 */
	@Getter
	@Setter
	private String id;
	/** 创建时间 */
	@Getter
	@Setter
	private Date createTime;
	/** 创建人id */
	@Getter
	@Setter
	private String createrId;
	@Getter
	private Sqlr sqlr = new Sqlr();

	@Getter
	@Setter
	public class Sqlr {
		public final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		private List<Attribute> attributes;

		public String toInsertSql() throws Exception {
			StringBuffer sqlBuffer = new StringBuffer("insert into " + BaseModel.this.getTable().getName() + " ");
			StringBuffer clounms = new StringBuffer("(");
			StringBuffer values = new StringBuffer(" values(");
			List<Attribute> attributes = this.getAttributes();
			for (int i = 0; i < attributes.size(); i++) {
				Attribute attribute = attributes.get(i);
				if (i + 1 == attributes.size()) {
					clounms.append(attribute.getName() + ")");
					values.append(attribute.getValue() + ")");
				} else {
					clounms.append(attribute.getName() + ", ");
					values.append(attribute.getValue() + ", ");
				}
			}
			sqlBuffer.append(clounms);
			sqlBuffer.append(values);
			return sqlBuffer.toString();
		}

		public String toGlobalUpdateSql() throws Exception {
			StringBuffer sqlBuffer = new StringBuffer("update " + BaseModel.this.getTable().getName() + " set ");
			List<Attribute> attributes = this.getAttributes();
			for (int i = 0; i < attributes.size(); i++) {
				Attribute attribute = attributes.get(i);
				if (attribute.getName().equals("create_time") || attribute.getName().equals("creater_id")) {
					continue;
				}
				sqlBuffer.append(attribute.getName() + " = " + attribute.getValue() + ", ");
			}
			if (sqlBuffer.indexOf(",") != -1)
				sqlBuffer.replace(sqlBuffer.length() - 2, sqlBuffer.length(), " ");
			if (BaseModel.this.getId() != null) {
				sqlBuffer.append("where id = '" + BaseModel.this.getId() + "'");
			} else {
				sqlBuffer.append("where id = " + BaseModel.this.getId());
			}
			return sqlBuffer.toString();
		}

		public String toLocalUpdateSql() throws Exception {
			StringBuffer sqlBuffer = new StringBuffer("update " + BaseModel.this.getTable().getName() + " set ");
			List<Attribute> attributes = this.getAttributes();
			for (int i = 0; i < attributes.size(); i++) {
				Attribute attribute = attributes.get(i);
				if (attribute.getValue() != null) {
					sqlBuffer.append(attribute.getName() + " = " + attribute.getValue() + ", ");
				}
			}
			if (sqlBuffer.indexOf(",") != -1)
				sqlBuffer.replace(sqlBuffer.length() - 2, sqlBuffer.length(), " ");
			else {
				sqlBuffer.append("id = '" + BaseModel.this.getId() + "' ");
			}
			if (BaseModel.this.getId() != null) {
				sqlBuffer.append("where id = '" + BaseModel.this.getId() + "'");
			} else {
				sqlBuffer.append("where id = " + BaseModel.this.getId());
			}
			return sqlBuffer.toString();
		}

		public List<Attribute> getAttributes() throws Exception {
			if (this.attributes != null) {
				return attributes;
			} else {
				this.attributes = new LinkedList<>();
				List<Field> fields = Arrays.asList(BaseModel.this.getClass().getDeclaredFields());
				fields = new ArrayList<Field>(fields);
				Class<?> superClass = BaseModel.this.getClass().getSuperclass();
				if (superClass != null) {
					for (;;) {
						fields.addAll(Arrays.asList(superClass.getDeclaredFields()));
						if (superClass.equals(BaseModel.class)) {
							break;
						} else {
							superClass = superClass.getSuperclass();
						}
					}
				}
				for (int i = 0; i < fields.size(); i++) {
					Field field = fields.get(i);
					String clounm = field.getName();
					boolean needAppend = true;
					/** 获取本次参数的方法 */
					String afterOfGet = clounm.substring(0, 1).toUpperCase() + clounm.substring(1);
					Method getCurrnetClounm = BaseModel.this.getClass().getMethod("get" + afterOfGet);
					String value = null;
					if (String.class.isAssignableFrom(field.getType())) {
						/** 得到本次参数 */
						String clounmValue = (String) getCurrnetClounm.invoke(BaseModel.this);
						if (clounmValue != null) {
							clounmValue = clounmValue.replaceAll("\\\\", "\\\\\\\\");
							value = "'" + clounmValue + "'";
						} else {
							value = null;
						}
					} else if (Number.class.isAssignableFrom(field.getType())) {
						Number clounmValue = (Number) getCurrnetClounm.invoke(BaseModel.this);
						if (clounmValue != null) {
							value = clounmValue.toString();
						} else {
							value = null;
						}
					} else if (Boolean.class.isAssignableFrom(field.getType())) {
						Boolean clounmValue = (Boolean) getCurrnetClounm.invoke(BaseModel.this);
						if (clounmValue != null) {
							if (clounmValue) {
								value = "1";
							} else {
								value = "0";
							}
						} else {
							value = null;
						}
					} else if (Date.class.isAssignableFrom(field.getType())) {
						Date clounmValue = (Date) getCurrnetClounm.invoke(BaseModel.this);
						if (clounmValue != null) {
							value = "'" + sqlr.dateFormat.format(clounmValue) + "'";
						} else {
							value = null;
						}
					} else if (Enum.class.isAssignableFrom(field.getType())) {
						Enum<?> clounmValue = (Enum<?>) getCurrnetClounm.invoke(BaseModel.this);
						if (clounmValue != null) {
							value = clounmValue.ordinal() + "";
						} else {
							value = null;
						}
					} else {
						needAppend = false;
					}
					if (needAppend) {
						attributes.add(new Attribute(StringHumpTool.humpToLine2(clounm, "_"), value));
					}
				}
				return attributes;
			}

		}
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Attribute {
		private String name;
		private String value;
	}

	@Getter
	@Setter
	public static class Table {
		/** 表名 */
		private String name;

		public Table(String name) {
			this.name = name;
		}
	}
}
