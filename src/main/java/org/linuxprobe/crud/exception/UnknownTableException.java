package org.linuxprobe.crud.exception;

import lombok.NoArgsConstructor;

/** 未知的表异常 */
@NoArgsConstructor
public class UnknownTableException extends RuntimeException {
	private static final long serialVersionUID = -3615605885092287914L;

	public UnknownTableException(String msg) {
		super(msg);
	}
}
