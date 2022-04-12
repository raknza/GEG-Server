package com.dao;

import com.model.UserEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Configuration
@Repository
public interface UserEventDao extends MongoRepository<UserEvent, String> {

}

