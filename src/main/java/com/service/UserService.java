package com.service;

import com.dao.UserDao;
import com.exception.BaseException;
import com.model.LoginResult;
import com.model.User;
import com.utils.JwtHandler;
import com.utils.MD5Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Configuration
@EnableMongoRepositories
@ComponentScan({"com.utils"})
public class UserService {

    private final UserDao userDao;
    private final JwtHandler jwtHandler;

    public UserService(UserDao userDao, JwtHandler jwtHandler){
        this.userDao = userDao;
        this.jwtHandler = jwtHandler;
    }

    /**
     * Get all user
     *
     * @return all data in collection 'Users'
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

    /**
     * Login by username and password
     * @param username the username
     * @param password will encode to MD5
     * @return the result as {@link LoginResult}
     */
    public LoginResult login(String username, String password){
        boolean result = false;
        User user = userDao.findByUsername(username);
        if (user != null) {
            result = user.checkPassword(MD5Helper.encodeToMD5(password));
        }
        else{
            return new LoginResult(false,"","cannot find user");
        }
        if(result){
            return new LoginResult(true,jwtHandler.generateToken(username),"login success");
        }
        else{
            return new LoginResult(false,"","password error");
        }
    }
}
