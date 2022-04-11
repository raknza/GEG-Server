package com.service;

import com.dao.UserDao;
import com.exception.BaseException;
import com.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@ComponentScan("com.dao")
@Configuration
@EnableMongoRepositories
public class UserService {

    @Autowired
    private UserDao userDao;

    /**
     * Get all user
     *
     * @return response
     */
    public List<User> findAll(){
        return userDao.findAll();
    }

    /**
     * Create new user
     * @param name the name of user
     * @param username the username
     * @param password will encode to MD5
     * @return result
     */
    public Object createUser(String name, String username, String password) throws Exception {
        if ( userDao.findByUsername(username) == null ){
            User user = new User(name, username, password);
            return userDao.save(user);
        }
        else{
            throw new BaseException("Username already exists");
        }
    }
}
