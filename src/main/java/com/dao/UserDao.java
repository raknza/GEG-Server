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
    @Value("${key.role}")
    String roleString;
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
                return new User(doc.getString(nameString),doc.getString(usernameString),
                        doc.getString(passwordString), doc.getString(roleString));
            }
        }
        return null;

    }

    @Override
    public List<User> findAll(){
        MongoCollection<Document> documents = mongoDb.getCollection(collectionName);
        FindIterable<Document> findIterable = documents.find();
        MongoCursor<Document> mongoCursor = findIterable.iterator();
        List<User> allUsers = new ArrayList<>();
        while (mongoCursor.hasNext()) {
            Document doc = mongoCursor.next();
            User user = new User(doc.getString(nameString),doc.getString(usernameString),
                    doc.getString(passwordString), doc.getString(roleString));
            user.setId(doc.getObjectId(idString).toString());
            allUsers.add(user);
        }
        return allUsers;

    }

    @Override
    public User findById(String id) {
        List<Document> userDocs = mongoDb.getCollection(collectionName, idString,id);
        if(userDocs != null){
            Document userDoc = userDocs.get(0);
            User user = new User(userDoc.getString(nameString),userDoc.getString(usernameString),
                    userDoc.getString(passwordString), userDoc.getString(roleString));
            user.setId(userDoc.getObjectId(idString).toString());
            return user;
        }
        return null;
    }

    @Override
    public Object save(Object entity) {
        User user = (User)entity;
        List<Document> oldUser = mongoDb.getCollection(collectionName, usernameString,user.getUsername());
        if(oldUser != null){
            Document newUserDoc = new Document(
                    idString, oldUser.get(0).getString(idString))
                    .append(usernameString, user.getUsername())
                    .append(nameString, user.getName())
                    .append(passwordString, user.getPassword())
                    .append(roleString, user.getRole());
            return mongoDb.updateOne(collectionName, oldUser.get(0), newUserDoc);
        }
        return null;
    }

    @Override
    public Object insert(Object entity){
        User user = (User)entity;
        Document doc = new Document(usernameString, user.getUsername())
                .append(nameString, user.getName())
                .append(passwordString, user.getPassword())
                .append(roleString, user.getRole());

        mongoDb.createCollection(user.getUsername());
        return mongoDb.getCollection(collectionName).insertOne(doc);
    }

}
