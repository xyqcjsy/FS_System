package com.fs.system.exception;

/**
 * 业务异常类
 *
 * @author FS_System
 * @since 2025-12-20
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}

