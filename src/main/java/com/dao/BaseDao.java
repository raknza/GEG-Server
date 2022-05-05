package com.dao;

public interface BaseDao {

    public <T> T findAll();
    public <T> T findById(String id);
    public <T> T insert(Object entity);
    public <T> T save(Object entity);
}
