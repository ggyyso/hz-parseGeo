package com.common.rest.api.data.converter;

import com.common.exception.DataConverterException;
import com.common.utils.ClassUtils;
import com.common.rest.api.data.AbstractModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zzge163 on 2019/2/14.
 */
public class ListModelConverter<T> extends AbstractModelConverter<T> {
    public ListModelConverter() {
    }

    public ListModelConverter(List<T> list) {
        super(list);
    }

    @Override
    public <X extends AbstractModel> List<X> toList(Class<X> clazz) {
        ArrayList list = new ArrayList();

        try {
            Iterator e = this.list.iterator();

            while (e.hasNext()) {
                Object model = e.next();
                AbstractModel toModel = (AbstractModel) ClassUtils.createInstance(clazz);
                toModel.setValues(model);
                list.add(toModel);
            }

            return list;
        } catch (ReflectiveOperationException var6) {
            var6.printStackTrace();
            throw new DataConverterException("数据转换时发生异常[" + clazz.getName() + "], 请检查要转换的DataModel中是否有空构造参数");
        }
    }
}
