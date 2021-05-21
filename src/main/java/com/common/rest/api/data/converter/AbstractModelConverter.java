package com.common.rest.api.data.converter;

import com.common.rest.api.data.AbstractModel;

import java.util.List;

/**
 * description: AbstractModelConverter <br>
 * date: 2019/8/15 18:19 <br>
 * version: 1.0 <br>
 *
 * @author: zhangzhe <br>
 */
public abstract class AbstractModelConverter<T> {
    protected List<T> list;

    public AbstractModelConverter() {
    }

    public AbstractModelConverter(List<T> list) {
        this.list = list;
    }

    public List<T> getList() {
        return this.list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public abstract <X extends AbstractModel> List<X> toList(Class<X> var1)
            throws InstantiationException, IllegalAccessException;
}
