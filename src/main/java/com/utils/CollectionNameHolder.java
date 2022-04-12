package com.utils;

public class CollectionNameHolder {
    private static final ThreadLocal<String> collectionNameThreadLocal = new ThreadLocal<>();

    public static String get(){
        String collectionName = collectionNameThreadLocal.get();
        if(collectionName == null){
            collectionName = "null";
            collectionNameThreadLocal.set(collectionName);
        }
        return collectionName;
    }

    public static void set(String collectionName){
        collectionNameThreadLocal.set(collectionName);
    }

    public static void reset(){
        collectionNameThreadLocal.remove();
    }
}
