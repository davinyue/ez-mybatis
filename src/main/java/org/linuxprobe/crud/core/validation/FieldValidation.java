package org.linuxprobe.crud.core.validation;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;

import org.hibernate.validator.HibernateValidator;
import org.linuxprobe.crud.utils.SqlFieldUtil;
import org.linuxprobe.luava.reflection.ReflectionUtils;
import org.springframework.util.StreamUtils;

/**
 * 实体属性校验
 */
public class FieldValidation {
	/**
	 * import org.apache.bval.jsr.ApacheValidationProvider; private static
	 * ValidatorFactory apacheValidatorFactory =
	 * Validation.byProvider(ApacheValidationProvider.class)
	 * .configure().buildValidatorFactory();
	 */
	private static ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class).configure()
			.buildValidatorFactory();
	private static Validator validator = validatorFactory.getValidator();
	// private static Validator defalutValidator =
	// Validation.buildDefaultValidatorFactory().getValidator();

	/**
	 * JSR349验证，校验实体属性是否合法，校验不通过将抛出异常
	 */
	public static <T> void validation(T object) {
		Set<ConstraintViolation<T>> results = validator.validate(object, Default.class);
		if (results != null && !results.isEmpty()) {
			StringBuilder messageBuilder = new StringBuilder("in " + object.getClass().getName() + " class,");
			for (ConstraintViolation<T> result : results) {
				messageBuilder.append(result.getPropertyPath().toString()).append(" ").append(result.getMessage()).append(";");
			}
			throw new ValidationException(messageBuilder.toString());
		}
	}

	/**
	 * JSR349验证，校验实体属性是否合法，校验不通过将抛出异常
	 */
	private static <T> void validationField(T object, Field field) {
		Set<ConstraintViolation<T>> results = validator.validateProperty(object, field.getName(), Default.class);
		if (results != null && !results.isEmpty()) {
			StringBuilder messageBuilder = new StringBuilder("in " + object.getClass().getName() + " class,");
			for (ConstraintViolation<T> result : results) {
				messageBuilder.append(result.getPropertyPath().toString()).append(" ").append(result.getMessage()).append(";");
			}
			throw new ValidationException(messageBuilder.toString());
		}
	}

	/**
	 * blob检查
	 * 
	 * @param record 保存对象
	 * @param field  属性
	 */
	private static void blobValidation(Object record, Field field) {
		Object fieldValue = ReflectionUtils.getFieldValue(record, field);
		if (fieldValue != null) {
			byte[] bin = null;
			if (Blob.class.isAssignableFrom(field.getType())) {
				Blob blob = (Blob) fieldValue;
				try {
					bin = StreamUtils.copyToByteArray(blob.getBinaryStream());
				} catch (IOException | SQLException e) {
					throw new IllegalArgumentException(e);
				}
			} else if (byte[].class.isAssignableFrom(field.getType())) {
				bin = (byte[]) fieldValue;
			} else if (Byte[].class.isAssignableFrom(field.getType())) {
				Byte[] binB = (Byte[]) fieldValue;
				bin = new byte[binB.length];
				for (int i = 0; i < binB.length; i++) {
					bin[i] = binB[i];
				}
			}
			if (field.isAnnotationPresent(Size.class)) {
				Size size = field.getAnnotation(Size.class);
				if (size.min() != 0) {
					if (bin.length < size.min()) {
						throw new ValidationException("in " + record.getClass().getName() + " class," + field.getName()
								+ " minSize is " + size.min());
					}
				}
				if (size.max() != 0) {
					if (bin.length > size.max()) {
						throw new ValidationException("in " + record.getClass().getName() + " class," + field.getName()
								+ " maxSize is " + size.max());
					}
				}
			}
		}
	}

	/** 验证属性是否合法 */
	public static void universalValidation(Object entity, Field field) {
		validationField(entity, field);
		if (SqlFieldUtil.isFacultyOfBlob(field.getType())) {
			blobValidation(entity, field);
		}
	}

	/** 验证全部属性是否合法 */
	public static void universalValidation(Object entity) {
		validation(entity);
	}
}
