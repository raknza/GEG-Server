package com.dao;

import com.model.AchievementRecord;
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
public class AchievementRecordDao implements BaseDao {

    @Value("${key.id}")
    String idString;
    @Value("${key.time}")
    String timeString;
    @Value("${key.achievement}")
    String achievementString;
    @Value("${key.achievement.id}")
    String achievementIdString;
    @Value("${achievement.record.collection.post}")
    String collectionPostfix;

    private final MongoDb mongoDb;

    public AchievementRecordDao(MongoDb mongoDb){
        this.mongoDb = mongoDb;
    }


    @Override
    public List<AchievementRecord> findAll(){
        String collectionName = CollectionNameHolder.get();
        MongoCollection<Document> documents = mongoDb.getCollection(collectionName);
        FindIterable<Document> findIterable = documents.find();
        MongoCursor<Document> mongoCursor = findIterable.iterator();
        List<AchievementRecord> userAchievementRecords = new ArrayList<>();
        while (mongoCursor.hasNext()) {
            Document doc = mongoCursor.next();
            AchievementRecord achievementRecord = new AchievementRecord(doc.getString(timeString),
                    (Document) doc.get(achievementString));
            achievementRecord.setId(doc.getObjectId(idString).toString());
            userAchievementRecords.add(achievementRecord);

        }

        return userAchievementRecords;

    }

    @Override
    public AchievementRecord findById(String id) {
        return getAchievement(idString,id);
    }

    public AchievementRecord findByAchievementId(int id){
        return getAchievement(achievementIdString,id);
    }

    private AchievementRecord getAchievement(String filterKey,Object filterValue){
        List<Document> achievementRecordDocument = mongoDb.getCollection(CollectionNameHolder.get(), filterKey,filterValue);
        if(achievementRecordDocument != null){
            Document doc = achievementRecordDocument.get(0);
            AchievementRecord achievementRecord = new AchievementRecord(doc.getString(timeString),
                    (Document) doc.get(achievementString));
            achievementRecord.setId(doc.getObjectId(idString).toString());
            return achievementRecord;
        }
        return null;
    }

    @Override
    public Object save(Object entity) {
        return insert(entity);
    }

    @Override
    public Object insert(Object entity){
        AchievementRecord achievementRecord = (AchievementRecord) entity;
        Document doc = new Document(achievementString, achievementRecord.getAchievement())
                .append(timeString, achievementRecord.getTime());

        return mongoDb.getCollection(CollectionNameHolder.get()).insertOne(doc);
    }

    public void setUser(String username){
        CollectionNameHolder.set(username + collectionPostfix);
    }
}
