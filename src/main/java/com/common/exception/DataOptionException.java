package com.common.exception;

/**
 * description: EsDaoException <br>
 * date: 2019/2/14 9:40 <br>
 * version: 1.0 <br>
 *
 * @author: zhangzhe <br>
 */
public class DataOptionException extends BaseException {
    public DataOptionException(String message) {
        super(message);
    }

    public DataOptionException(String message, Throwable cause) {
        super(message, cause);
    }
}