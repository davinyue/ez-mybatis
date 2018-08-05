package org.linuxprobe.crud.exception;

import lombok.NoArgsConstructor;

/** 参数错误异常 */
@NoArgsConstructor
public class ParameterException extends RuntimeException {
	private static final long serialVersionUID = -206036483879506865L;

	public ParameterException(String message) {
		super(message);
	}

	public ParameterException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParameterException(Throwable cause) {
		super(cause);
	}
}
