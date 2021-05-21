package com.common.exception;

/**
 * description: EsDaoException <br>
 * date: 2019/2/15 16:40 <br>
 * version: 1.0 <br>
 *
 * @author: zhangzhe <br>
 */
public class ParamException extends BaseException {
    public ParamException(String message) {
        super(message);
    }

    public ParamException(String message, Throwable cause) {
        super(message, cause);
    }
}
