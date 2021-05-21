package com.common.rest.api.param;

/**
 * Created by zzge163 on 2019/2/22.
 */
public class SearchParam {
    private String column;
    private String option;
    private String value;

    public SearchParam() {
    }

    public String getColumn() {
        return this.column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getOption() {
        return this.option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
