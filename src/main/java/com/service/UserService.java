package com.service;

import com.dao.AchievementRecoardDao;
import com.dao.UserDao;
import com.exception.BaseException;
import com.model.AchievementRecord;
import com.model.LoginResult;
import com.model.User;
import com.model.UserPoints;
import com.utils.CollectionNameHolder;
import com.utils.JwtHandler;
import com.utils.MD5Helper;
import net.minidev.json.JSONObject;
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
    private final AchievementRecoardDao achievementRecoardDao;
    private final LevelService levelService;

    public UserService(UserDao userDao, JwtHandler jwtHandler,
                       AchievementRecoardDao achievementRecoardDao,
                       LevelService levelService){
        this.userDao = userDao;
        this.jwtHandler = jwtHandler;
        this.achievementRecoardDao = achievementRecoardDao;
        this.levelService = levelService;
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

    /**
     * get User's all point in game
     * @param username the username
     * @return the result as {@link UserPoints}
     */
    public Object getUserPoints(String username){
        User user = userDao.findByUsername(username);
        if (user == null){
            return new JSONObject().appendField("message","Cannot found user");
        }
        String collectionName = username+ "_achievement_record";
        CollectionNameHolder.set(collectionName);
        int levelPassedCounts = 0;
        int achievementCounts = 0;
        List<AchievementRecord> achievementRecords = achievementRecoardDao.findAll();
        if(achievementRecords != null){
            achievementCounts = achievementRecords.size();
        }
        levelPassedCounts = levelService.getUserLevelPassedCount(username,0,9);
        UserPoints userPoints = new UserPoints(levelPassedCounts,achievementCounts,username);
        return userPoints;
    }

}
