package com.service;

import com.dto.AddFeatureeData;

/**
 * @author Jason Wong
 * @title: QypgShpUpdateService
 * @projectName hz-mis
 * @description: TODO
 * @date 2021/3/13 11:06
 */
public interface XmhxShpUpdateService {

    void addFeature(AddFeatureeData info) throws Exception;

    String dijiInfoCheck(Object info) throws Exception;
}
