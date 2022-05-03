package com.service;

import com.dao.AchievementRecoardDao;
import com.dao.UserDao;
import com.dao.UserEventDao;
import com.exception.BaseException;
import com.model.*;
import com.utils.CollectionNameHolder;
import com.utils.JwtHandler;
import com.utils.MD5Helper;
import net.minidev.json.JSONObject;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@EnableMongoRepositories
@ComponentScan({"com.utils","com.dao"})
public class UserService {

    private final UserDao userDao;
    private final JwtHandler jwtHandler;
    private final AchievementRecoardDao achievementRecoardDao;
    private final LevelService levelService;
    private final UserEventDao userEventDao;

    public UserService(UserDao userDao, JwtHandler jwtHandler,
                       AchievementRecoardDao achievementRecoardDao,
                       LevelService levelService,
                       UserEventDao userEventDao){
        this.userDao = userDao;
        this.jwtHandler = jwtHandler;
        this.achievementRecoardDao = achievementRecoardDao;
        this.levelService = levelService;
        this.userEventDao = userEventDao;
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
            User user = new User(name, username, MD5Helper.encodeToMD5(password));
            return userDao.insert(user);
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
     * Get User's all point in game
     * @param username the username
     * @return the result as {@link UserPoints}
     */
    public Object getUserPoints(String username){
        int levelPassedCounts = 0;
        int achievementCounts = 0;
        // get achievement_record
        String collectionName = username+ "_achievement_record";
        CollectionNameHolder.set(collectionName);
        List<AchievementRecord> achievementRecords = achievementRecoardDao.findAll();
        if(achievementRecords != null){
            achievementCounts = achievementRecords.size();
        }
        // get level passed record
        CollectionNameHolder.set(username);
        List<UserEvent> userEvents = userEventDao.findByEventName("level_passed");
        List<String> levelPassed = new ArrayList<String>();
        if( userEvents!= null ){
            for(int i = 0 ; i < userEvents.size() ; i++){
                String level = userEvents.get(i).getEventContent().getString("level");
                if( !levelPassed.contains(level) ) {
                    levelPassed.add(level);
                }
            }
            levelPassedCounts = levelPassed.size();
        }
        UserPoints userPoints = new UserPoints(levelPassedCounts,achievementCounts,username);
        return userPoints;
    }

    /**
     * Get all users point in game.
     * @return the result will be a List of {@link UserPoints} and will be descending order.
     */
    public Object getAllUsersPoints(){
        List<User> allUsers = userDao.findAll();
        if (allUsers == null){
            return new JSONObject().appendField("message","Do not have any user.");
        }
        List<UserPoints> allUsersPoints= new ArrayList<UserPoints>();

        for(int i =0 ; i < allUsers.size() ; i++){
            UserPoints userPoints = (UserPoints)getUserPoints(allUsers.get(i).getUsername());
            allUsersPoints.add(userPoints);
            Collections.sort(allUsersPoints);
        }

        return allUsersPoints;
    }


}
