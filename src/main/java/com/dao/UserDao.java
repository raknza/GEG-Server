package com.dao;

import com.model.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Configuration
@Repository
public interface UserDao extends MongoRepository<User, String> {

    @Query(value=("{'username':?0}"))
    public User findByUsername(String username);
}
