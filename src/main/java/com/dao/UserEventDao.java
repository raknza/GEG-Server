package com.dao;

import com.model.UserEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
@Configuration
@Repository
public interface UserEventDao extends MongoRepository<UserEvent, String> {

    @Query(value=("{'event_name':?0}"))
    public List<UserEvent> findByEventName(String eventName);
}

