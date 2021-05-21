package com.common.rest.api.param;

/**
 * Created by zzge163 on 2019/2/14.
 */
public class SortParam {
    private String column;
    private String order;

    public SortParam() {
    }

    public String getColumn() {
        return this.column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getOrder() {
        return this.order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
