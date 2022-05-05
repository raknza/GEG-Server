package com.dao;

import com.model.User;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@PropertySource(value = "classpath:mongo_collection.properties")
public class UserDao implements BaseDao {

    private final MongoDb mongoDb;

    @Value("${key.username}")
    String usernameString;
    @Value("${key.id}")
    String idString;
    @Value("${key.name}")
    String nameString;
    @Value("${key.password}")
    String passwordString;
    @Value("${user.collection}")
    String collectionName;


    public UserDao(MongoDb mongoDb){
        this.mongoDb = mongoDb;
    }

    public User findByUsername(String username){
        MongoCollection<Document> documents = mongoDb.getCollection(collectionName);
        FindIterable<Document> findIterable = documents.find();
        MongoCursor<Document> mongoCursor = findIterable.iterator();

        while (mongoCursor.hasNext()) {
            Document doc = mongoCursor.next();
            if(doc.getString(usernameString).equals(username)){
                User user = new User(doc.getString(nameString),doc.getString(usernameString),doc.getString(passwordString));
                return user;
            }
        }
        return null;

    }

    @Override
    public List<User> findAll(){
        MongoCollection<Document> documents = mongoDb.getCollection(collectionName);
        FindIterable<Document> findIterable = documents.find();
        MongoCursor<Document> mongoCursor = findIterable.iterator();
        List<User> allUsers = new ArrayList<User>();
        while (mongoCursor.hasNext()) {
            Document doc = mongoCursor.next();
            User user = new User(doc.getString(nameString),doc.getString(usernameString),doc.getString(passwordString));
            user.setId(doc.getObjectId(idString).toString());
            allUsers.add(user);
        }
        return allUsers;

    }

    @Override
    public Object findById(String id) {
        List<Document> user = mongoDb.getCollection(collectionName, idString,id);
        if(!user.isEmpty()){
            return user;
        }
        return null;
    }

    @Override
    public Object save(Object entity) {
        User user = (User)entity;
        List<Document> oldUser = mongoDb.getCollection(collectionName, usernameString,user.getUsername());
        if(!oldUser.isEmpty()){
            Document newUserDoc = new Document(
                    idString, oldUser.get(0).getString(idString))
                    .append(usernameString, user.getUsername())
                    .append(nameString, user.getName())
                    .append(passwordString, user.getPassword());
            return mongoDb.updateOne(collectionName, oldUser.get(0), newUserDoc);
        }
        return null;
    }

    @Override
    public Object insert(Object entity){
        User user = (User)entity;
        Document doc = new Document(usernameString, user.getUsername())
                .append(nameString, user.getName())
                .append(passwordString, user.getPassword());

        mongoDb.createCollection(user.getUsername());
        return mongoDb.getCollection(collectionName).insertOne(doc);
    }

}
