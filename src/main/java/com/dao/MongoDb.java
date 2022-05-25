package com.dao;

import com.mongodb.client.*;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import static com.mongodb.client.model.Filters.eq;

@PropertySource(value = "classpath:config/mongo_config.properties")
@Component
public class MongoDb {

    private MongoClient mongoClient;
    private MongoDatabase database;

    @Value("${dbConnectionString}")
    String dbConnectionString;
    @Value("${dbSchema}")
    String dbSchema;

    /**
     * Connection to db
     */
    public MongoDatabase getClient() {
        if (mongoClient == null) {
            mongoClient = MongoClients.create(dbConnectionString);
            database = mongoClient.getDatabase(dbSchema);
        }
        return database;
    }

    public List<Document> getCollection(String collection,
                                String filterKey,
                                Object filterValue) {
        MongoCollection<Document> documents = getClient().getCollection(collection);
        FindIterable<Document> findIterable = documents.find().filter(eq(filterKey, filterValue));
        MongoCursor<Document> mongoCursor = findIterable.iterator();
        List<Document> docs = new ArrayList<>();

        if(!mongoCursor.hasNext()){
            return null;
        }

        while (mongoCursor.hasNext()) {
            docs.add(mongoCursor.next());
        }
        return docs;
    }

    public MongoCollection<Document> getCollection(String collection) {
        return getClient().getCollection(collection);
    }

    public UpdateResult updateOne(String collection, Document oldDocument, Document newDocument) {
        Document updateDoc = new Document("$set", newDocument);
        return getCollection(collection).updateOne(oldDocument,updateDoc);
    }

    public void createCollection(String collection) {
        getClient().createCollection(collection);
    }

}
