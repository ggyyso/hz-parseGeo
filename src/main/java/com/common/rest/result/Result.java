package com.common.rest.result;

import io.swagger.annotations.ApiModel;

/**
 * Created by zzge163 on 2019/2/14.
 */
@ApiModel("θΏεη»ζ")
public class Result extends AbstractResult {
    public Result(int code) {
        super(code);
    }

    public Result(int code, String info) {
        super(code, info);
    }
}
