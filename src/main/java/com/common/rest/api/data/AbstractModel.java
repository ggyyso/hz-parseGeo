package com.common.rest.api.data;

/**
 * description: AbstractModel <br>
 * date: 2019/8/15 18:19 <br>
 * version: 1.0 <br>
 *
 * @author: zhangzhe <br>
 */
//@JsonInclude(JsonInclude.Include.NON_NULL) // 属性为NULL 不序列化
public abstract class AbstractModel<T> {
    public AbstractModel() {
    }

    public AbstractModel(T model) {
        this.setValues(model);
    }

    public abstract void setValues(T var1);

//    public abstract void setValues(T var1, Map<String, String> filter);

    public abstract void setModelValues(T var1);
}
