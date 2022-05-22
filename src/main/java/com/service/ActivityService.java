package com.service;

import com.model.User;
import com.model.UserEvent;
import com.model.UserPoints;
import com.utils.MathHelper;
import jdk.nashorn.internal.runtime.ParserException;
import org.bson.Document;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@ComponentScan({"com.dao"})
public class ActivityService {

    private final UserService userService;
    private final UserEventService userEventService;

    public ActivityService(UserService userService, UserEventService userEventService){
        this.userService = userService;
        this.userEventService = userEventService;
    }

    public Object getUsersInGamePerformance() {
        List<UserPoints> allUsersPoints = (List<UserPoints>) userService.getAllUsersPoints();
        int sumGamePoints = 0;
        int sumLevelPoints = 0;
        int sumAchievementsPoints = 0;
        int sumLevelPassedCount = 0;
        int sumAchievementsCount = 0;
        List<Integer> usersGamePoints = new ArrayList<Integer>();
        List<Integer> usersLevelPoints = new ArrayList<Integer>();
        List<Integer> usersAchievementsPoints = new ArrayList<Integer>();
        List<Integer> usersLevelPassedCount = new ArrayList<Integer>();
        List<Integer> usersAchievementsCount = new ArrayList<Integer>();
        for(int i =0; i< allUsersPoints.size() ; i++){
            usersGamePoints.add(allUsersPoints.get(i).getGamePoints());
            usersLevelPoints.add(allUsersPoints.get(i).getLevelPoints());
            usersAchievementsPoints.add(allUsersPoints.get(i).getAchievementPoints());
            usersLevelPassedCount.add(allUsersPoints.get(i).getLevelPassedCounts());
            usersAchievementsCount.add(allUsersPoints.get(i).getAchievementCounts());

            sumGamePoints += usersGamePoints.get(i);
            sumLevelPoints += usersLevelPoints.get(i);
            sumAchievementsPoints += usersAchievementsPoints.get(i);
            sumLevelPassedCount += usersLevelPassedCount.get(i);
            sumAchievementsCount += usersAchievementsCount.get(i);

        }
        Document averagePoints = new Document();
        averagePoints.append("userCount" , allUsersPoints.size());

        averagePoints.append("sumGamePoints" , sumGamePoints);
        averagePoints.append("sumLevelPoints" , sumLevelPoints );
        averagePoints.append("sumAchievementsPoints" , sumAchievementsPoints );
        averagePoints.append("sumLevelPassedCount" , sumLevelPassedCount );
        averagePoints.append("sumAchievementsCount" , sumAchievementsCount );

        averagePoints.append("averageGamePoints" , sumGamePoints / allUsersPoints.size());
        averagePoints.append("averageLevelPoints" , sumLevelPoints / allUsersPoints.size());
        averagePoints.append("averageAchievementsPoints" , sumAchievementsPoints / allUsersPoints.size());
        averagePoints.append("averageLevelPassedCount" , sumLevelPassedCount / allUsersPoints.size());
        averagePoints.append("averageAchievementsCount" , sumAchievementsCount / allUsersPoints.size());

        averagePoints.append("medianGamePoints" , MathHelper.median(usersGamePoints));
        averagePoints.append("medianLevelPoints" , MathHelper.median(usersLevelPoints));
        averagePoints.append("medianAchievementsPoints" , MathHelper.median(usersAchievementsPoints));
        averagePoints.append("medianLevelPassedCount" , MathHelper.median(usersLevelPassedCount));
        averagePoints.append("medianAchievementsCount" , MathHelper.median(usersAchievementsCount));

        averagePoints.append("modalNumsGamePoints" , MathHelper.getModalNums(usersGamePoints.stream().mapToInt(i->i).toArray() ) );
        averagePoints.append("modalNumsLevelPoints" , MathHelper.getModalNums(usersLevelPoints.stream().mapToInt(i->i).toArray() ));
        averagePoints.append("modalNumsAchievementsPoints" , MathHelper.getModalNums(usersAchievementsPoints.stream().mapToInt(i->i).toArray()));
        averagePoints.append("modalNumsLevelPassedCount" , MathHelper.getModalNums(usersLevelPassedCount.stream().mapToInt(i->i).toArray()));
        averagePoints.append("modalNumsAchievementsCount" , MathHelper.getModalNums(usersAchievementsCount.stream().mapToInt(i->i).toArray()));
        return averagePoints;
    }

    public Object getUserGameTime(String username) {

        int gameTime = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startEventTime = null;
        boolean start = true;
        List<UserEvent> allEvents = userEventService.getUserEvents(username);
        for (int i=0 ; i< allEvents.size(); i++) {
            UserEvent event = allEvents.get(i);
            if(start){
                try {
                    startEventTime = sdf.parse(event.getTime());
                } catch(Exception error){
                    throw new ParserException(error.getMessage());
                }
                start = !start;
            }
            else{
                Date nextEventTime = null;
                try {
                    nextEventTime = sdf.parse(event.getTime());
                } catch(Exception error){
                    throw new ParserException(error.getMessage());
                }
                int diffInSeconds = (int)( (nextEventTime.getTime() - startEventTime.getTime())
                        / (1000 ) );
                if( diffInSeconds <= 3600 ){
                    gameTime += diffInSeconds;
                    start = !start;
                }
                else {
                    try {
                        startEventTime = sdf.parse(event.getTime());
                    } catch(Exception error){
                        throw new ParserException(error.getMessage());
                    }
                }

            }
        }
        gameTime = gameTime/60;
        Document doc = new Document();
        doc.append("game_time",gameTime);
        return doc;
    }

    public Object getAllUserGameTime() {

        List<Document> usersGameTime = new ArrayList<Document>();
        List<User> Users = userService.findAll();
        for(int i=0; i< Users.size(); i++){
            User user = Users.get(i);
            Document userGameTime = (Document)getUserGameTime(user.getUsername());
            int gameTime = userGameTime.getInteger("game_time");
            userGameTime.append("user",user.getUsername());
            usersGameTime.add(userGameTime);

            // Sort
            int uIndex = usersGameTime.size()-1;
            for(int j = uIndex ; j > 0; j--){
                if( gameTime > usersGameTime.get(j-1).getInteger("game_time")){
                    Document tmp = usersGameTime.get(j-1);
                    usersGameTime.set(j-1,usersGameTime.get(j));
                    usersGameTime.set(j,tmp);
                }
            }

        }

        return usersGameTime;
    }



}
