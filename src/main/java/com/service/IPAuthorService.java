package com.service;

/**
 * @author Jason Wong
 * @title: IPAuthorService
 * @projectName hz-parseGeo
 * @description: TODO
 * @date 2021/5/12 15:58
 */
public interface IPAuthorService {

    void add(String ip) throws Exception;

    boolean author(String ip) throws Exception;
}
