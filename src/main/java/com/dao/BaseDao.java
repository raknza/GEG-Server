package com.dao;

public interface BaseDao {

    <T> T findAll();
    <T> T findById(String id);
    <T> T insert(Object entity);
    <T> T save(Object entity);
}
