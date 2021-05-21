package com.common.rest.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;

/**
 * description: AbstractResult <br>
 * date: 2019/8/15 18:19 <br>
 * version: 1.0 <br>
 *
 * @author: zhangzhe <br>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractResult {
    @ApiModelProperty("结果状态码")
    private int code;
    @ApiModelProperty("结果信息")
    private String info;

    public AbstractResult(int code) {
        this.code = code;
    }

    public AbstractResult(int code, String info) {
        this.code = code;
        this.info = info;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getInfo() {
        return this.info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}