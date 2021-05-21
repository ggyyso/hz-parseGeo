package com.common.exception;

/**
 * description: EsDaoException <br>
 * date: 2019/9/24 16:40 <br>
 * version: 1.0 <br>
 *
 * @author: zhangzhe <br>
 */
public class EsDaoException extends BaseException {
    public EsDaoException(String message) {
        super(message);
    }

    public EsDaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
