package com.common.exception;

/**
 * description: EsDaoException <br>
 * date: 2019/2/13 9:40 <br>
 * version: 1.0 <br>
 *
 * @author: zhangzhe <br>
 */
public class BaseException extends RuntimeException {
    private int code = -1;

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}

