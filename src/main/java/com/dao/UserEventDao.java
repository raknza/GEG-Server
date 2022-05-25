package com.dao;

import com.model.UserEvent;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.utils.CollectionNameHolder;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@PropertySource(value = "classpath:mongo_collection.properties")
public class UserEventDao implements BaseDao {

    private final MongoDb mongoDb;

    @Value("${key.username}")
    String usernameString;
    @Value("${key.id}")
    String idString;
    @Value("${key.eventName}")
    String eventNameString;
    @Value("${key.eventContent}")
    String eventContentString;
    @Value("${key.ip}")
    String ipString;
    @Value("${key.time}")
    String timeString;

    public UserEventDao(MongoDb mongoDb){
        this.mongoDb = mongoDb;
    }

    public List<UserEvent> findByEventName(String eventName){
        List<Document> oldEvents = mongoDb.getCollection(CollectionNameHolder.get(), eventNameString,eventName);
        List<UserEvent> allUserEvents = new ArrayList<>();
        if(oldEvents == null){
            return null;
        }
        for(int i = 0 ; i< oldEvents.size() ; i++){
            Document doc = oldEvents.get(i);
            UserEvent event = new UserEvent(doc.getString(usernameString),doc.getString(eventNameString),
                    doc.getString(ipString), doc.getString(timeString), (Document) doc.get(eventContentString));
            event.setId(doc.getObjectId(idString).toString());
            allUserEvents.add(event);
        }
        return allUserEvents;
    }

    @Override
    public List<UserEvent> findAll(){
        String collectionName = CollectionNameHolder.get();
        MongoCollection<Document> documents = mongoDb.getCollection(collectionName);
        FindIterable<Document> findIterable = documents.find();
        MongoCursor<Document> mongoCursor = findIterable.iterator();
        List<UserEvent> allUserEvents = new ArrayList<>();
        while (mongoCursor.hasNext()) {
            Document doc = mongoCursor.next();
            UserEvent event = new UserEvent(doc.getString(usernameString),doc.getString(eventNameString),
                    doc.getString(ipString), doc.getString(timeString), (Document) doc.get(eventContentString));
            event.setId(doc.getObjectId(idString).toString());
            allUserEvents.add(event);

        }

        return allUserEvents;

    }

    @Override
    public UserEvent findById(String id) {
        List<Document> eventDocument = mongoDb.getCollection(CollectionNameHolder.get(), idString,id);
        if(eventDocument != null){
            Document doc = eventDocument.get(0);
            UserEvent event = new UserEvent(doc.getString(usernameString),doc.getString(eventNameString),
                    doc.getString(ipString), doc.getString(timeString), (Document) doc.get(eventContentString));
            event.setId(doc.getObjectId(idString).toString());
            return event;
        }
        return null;
    }

    @Override
    public Object save(Object entity) {
        UserEvent event = (UserEvent)entity;
        List<Document> oldEvent = mongoDb.getCollection(CollectionNameHolder.get(), idString,event.getId());
        if(!oldEvent.isEmpty()){
            Document newEventDoc = new Document(usernameString, event.getUsername())
                    .append(eventNameString, event.getEventName())
                    .append(idString, event.getId())
                    .append(ipString, event.getIp())
                    .append(timeString, event.getTime())
                    .append(eventContentString, event.getEventContent());
            return mongoDb.updateOne(CollectionNameHolder.get(), oldEvent.get(0), newEventDoc);
        }
        return null;
    }

    @Override
    public Object insert(Object entity){
        UserEvent event = (UserEvent)entity;
        Document doc = new Document(usernameString, event.getUsername())
                .append(eventNameString, event.getEventName())
                .append(ipString, event.getIp())
                .append(timeString, event.getTime())
                .append(eventContentString, event.getEventContent());

        return mongoDb.getCollection(event.getUsername()).insertOne(doc);
    }

    public void setUser(String username){
        CollectionNameHolder.set(username);
    }

}

