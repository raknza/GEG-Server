package com.dao;

import com.model.User;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDao implements BaseDao {

    private final MongoDb mongoDb;

    public UserDao(MongoDb mongoDb){
        this.mongoDb = mongoDb;
    }

    public User findByUsername(String username){
        MongoCollection<Document> documents = mongoDb.getCollection("Users");
        FindIterable<Document> findIterable = documents.find();
        MongoCursor<Document> mongoCursor = findIterable.iterator();

        while (mongoCursor.hasNext()) {
            Document doc = mongoCursor.next();
            if(doc.getString("username").equals(username)){
                User user = new User(doc.getString("username"),doc.getString("name"),doc.getString("password"));
                return user;
            }
        }
        return null;

    }

    @Override
    public List<User> findAll(){
        MongoCollection<Document> documents = mongoDb.getCollection("Users");
        FindIterable<Document> findIterable = documents.find();
        MongoCursor<Document> mongoCursor = findIterable.iterator();
        List<User> allUsers = new ArrayList<User>();
        while (mongoCursor.hasNext()) {
            Document doc = mongoCursor.next();
            allUsers.add(new User(doc.getString("username"),doc.getString("name"),doc.getString("password")));
        }

        return allUsers;

    }

    @Override
    public Object findById(String id) {
        List<Document> user = mongoDb.getCollection("Users", "_id",id);
        if(!user.isEmpty()){
            return user;
        }
        return null;
    }

    @Override
    public Object save(Object entity) {
        User user = (User)entity;
        List<Document> oldUser = mongoDb.getCollection("Users", "username",user.getUsername());
        if(!oldUser.isEmpty()){
            Document newUserDoc = new Document(
                    "_id", oldUser.get(0).getString("_id"))
                    .append("username", user.getUsername())
                    .append("name", user.getName())
                    .append("password", user.getPassword());
            return mongoDb.updateOne("Users", oldUser.get(0), newUserDoc);
        }
        return null;
    }

    @Override
    public Object insert(Object entity){
        User user = (User)entity;
        Document doc = new Document("username", user.getUsername())
                .append("name", user.getName())
                .append("password", user.getPassword());

        mongoDb.createCollection(user.getUsername());
        return mongoDb.getCollection("Users").insertOne(doc);
    }

}
