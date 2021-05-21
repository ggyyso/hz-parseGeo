package com.common.exception;

/**
 * description: EsDaoException <br>
 * date: 2019/2/15 15:40 <br>
 * version: 1.0 <br>
 *
 * @author: zhangzhe <br>
 */
public class CanNotDeleteException extends BaseException {
    public CanNotDeleteException(String message) {
        super(message);
    }

    public CanNotDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
