package org.linuxprobe.crud.exception;

import lombok.NoArgsConstructor;

/** 不支持的操作异常 */
@NoArgsConstructor
public class OperationNotSupportedException extends RuntimeException {
	private static final long serialVersionUID = 1046278128541606884L;

	public OperationNotSupportedException(String msg) {
		super(msg);
	}
}
