package com.dao;

public interface BaseDao {

    public Object findAll();
    public Object findById(String id);
    public Object insert(Object entity);
    public Object save(Object entity);
}
