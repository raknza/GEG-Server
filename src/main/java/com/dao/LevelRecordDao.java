package com.dao;

import com.model.LevelRecord;
import com.mongodb.client.AggregateIterable;
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
public class LevelRecordDao implements BaseDao {

    private final MongoDb mongoDb;

    @Value("${key.id}")
    String idString;
    @Value("${key.username}")
    String usernameString;
    @Value("${key.timeCost}")
    String timeCostString;
    @Value("${key.lineCost}")
    String lineCostString;
    @Value("${key.time}")
    String timeString;
    @Value("${level.record.collection.prefix}")
    String collectionPrefix;
    @Value("${level.record.collection.postfix}")
    String collectionPostfix;

    public LevelRecordDao(MongoDb mongoDb){
        this.mongoDb = mongoDb;
    }


    public LevelRecord findByUsername(String username){
        return getLevelRecord(usernameString, username);
    }

    private LevelRecord getLevelRecord(String fiterKey, String filterValue) {
        List<Document> levelRecordDocuments = mongoDb.getCollection(CollectionNameHolder.get(), fiterKey,filterValue);
        if(levelRecordDocuments != null){
            Document doc = levelRecordDocuments.get(0);
            LevelRecord levelRecord  = new LevelRecord(doc.getString(usernameString), doc.getString(timeString),
                    doc.getInteger(timeCostString), doc.getInteger(lineCostString));
            levelRecord.setId(doc.getObjectId(idString).toString());
            return levelRecord;
        }
        return null;
    }

    @Override
    public List<LevelRecord> findAll(){
        String collectionName = CollectionNameHolder.get();
        Document sort = new Document("$sort", new Document(timeCostString, 1).append(lineCostString,1).append(timeString,-1));
        List<Document> aggregateList = new ArrayList<>();
        aggregateList.add(sort);
        AggregateIterable<Document> documents = mongoDb.getCollection(collectionName).aggregate(aggregateList);
        MongoCursor<Document> mongoCursor = documents.iterator();

        List<LevelRecord> levelRecords = new ArrayList<>();
        while (mongoCursor.hasNext()) {
            Document doc = mongoCursor.next();
            LevelRecord levelRecord  = new LevelRecord(doc.getString(usernameString), doc.getString(timeString),
                    doc.getInteger(timeCostString), doc.getInteger(lineCostString));
            levelRecord.setId(doc.getObjectId(idString).toString());
            levelRecords.add(levelRecord);

        }

        return levelRecords;

    }

    @Override
    public LevelRecord findById(String id) {
        return getLevelRecord(idString, id);
    }

    @Override
    public Object save(Object entity) {
        LevelRecord newLevelRecord = (LevelRecord)entity;
        List<Document> oldLevelRecord = mongoDb.getCollection(CollectionNameHolder.get(), usernameString, newLevelRecord.getUsername());
        if(oldLevelRecord != null){
            Document newLevelRecordDoc = new Document(usernameString, newLevelRecord.getUsername())
                    .append(timeString, newLevelRecord.getTime())
                    .append(timeCostString, newLevelRecord.getTimeCost())
                    .append(lineCostString, newLevelRecord.getLineCost());
            return mongoDb.updateOne(CollectionNameHolder.get(), oldLevelRecord.get(0), newLevelRecordDoc);
        }
        return null;
    }

    @Override
    public Object insert(Object entity){
        LevelRecord newLevelRecord = (LevelRecord)entity;
        Document newLevelRecordDoc = new Document(usernameString, newLevelRecord.getUsername())
                .append(timeString, newLevelRecord.getTime())
                .append(timeCostString, newLevelRecord.getTimeCost())
                .append(lineCostString, newLevelRecord.getLineCost());

        return mongoDb.getCollection(CollectionNameHolder.get()).insertOne(newLevelRecordDoc);
    }

    public void setLevel(int level){
        CollectionNameHolder.set(collectionPrefix + level  + collectionPostfix);
    }
}
