package com.van.takeout.exception;

/**
 * 业务异常-分类有关联异常
 */
public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}
