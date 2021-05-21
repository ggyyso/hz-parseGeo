package com.common.rest.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by zzge163 on 2019/2/14.
 */
@ApiModel("单条数据返回结果")
public class ResultRecord<T> extends Result {
    @ApiModelProperty("返回结果记录")
    private T record;
    @ApiModelProperty("记录ID字段")
    private String identifier;
    @ApiModelProperty("记录描述字段")
    private String label;

    public ResultRecord(int code) {
        super(code);
    }

    public ResultRecord(int code, String info) {
        super(code, info);
    }

    public ResultRecord(int code, T record) {
        super(code);
        this.record = record;
    }

    public ResultRecord(int code, String info, T record) {
        super(code, info);
        this.record = record;
    }

    public T getRecord() {
        return this.record;
    }

    public void setRecord(T record) {
        this.record = record;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
