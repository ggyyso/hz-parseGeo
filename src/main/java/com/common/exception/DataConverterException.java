package com.common.exception;

/**
 * description: EsDaoException <br>
 * date: 2019/2/13 9:40 <br>
 * version: 1.0 <br>
 *
 * @author: zhangzhe <br>
 */
public class DataConverterException extends BaseException {
    public DataConverterException(String message) {
        super(message);
    }

    public DataConverterException(String message, Throwable cause) {
        super(message, cause);
    }
}
