package com.common.rest.api.data.converter;

import com.common.rest.api.data.AbstractTreeModel;

import java.util.List;
import java.util.Map;

/**
 * Created by zzge163 on 2019/3/7.
 */
public class TreeModelConverter<T> extends ListModelConverter<T> {
    private String topId;

    private Map<String, String> filter;

    public TreeModelConverter(List<T> list) {
        super(list);
    }

    public TreeModelConverter(List<T> list, String topId) {
        super(list);
        this.topId = topId;
    }

    public TreeModelConverter(List<T> list, Map<String, String> filter) {
        super(list);
        this.filter = filter;
    }

    public <X extends AbstractTreeModel> List<X> toTree(Class<X> clazz) {
        List changeModelList = super.toList(clazz);
        DataModelToTree dmtt = new DataModelToTree(changeModelList, this.topId);
        return dmtt.changeByRecursive();
    }
}
