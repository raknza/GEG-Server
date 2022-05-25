package com.service;

import com.model.Statistics;
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
    private final LevelService levelService;

    public ActivityService(UserService userService, UserEventService userEventService, LevelService levelService){
        this.userService = userService;
        this.userEventService = userEventService;
        this.levelService = levelService;
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
        int gameCount = 0;
        List<Document> allGameCounts = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startEventTime = null;
        Date pointerEventTime = null;
        List<UserEvent> allEvents = userEventService.getUserEvents(username);

        if( allEvents.size() > 0) {
            // set start
            try {
                UserEvent event = allEvents.get(0);
                startEventTime = sdf.parse(event.getTime());
                pointerEventTime = sdf.parse(event.getTime());
            } catch (Exception error) {
                throw new ParserException(error.getMessage());
            }

            for (int i = 1; i < allEvents.size(); i++) {
                UserEvent event = allEvents.get(i);
                Date nextEventTime = null;
                try {
                    nextEventTime = sdf.parse(event.getTime());
                } catch (Exception error) {
                    throw new ParserException(error.getMessage());
                }
                int diffInSeconds = (int) ((nextEventTime.getTime() - pointerEventTime.getTime())
                        / (1000));
                // if event time diff <= 30 minutes
                if (diffInSeconds <= 1800) {
                    gameTime += diffInSeconds;
                    try {
                        pointerEventTime = sdf.parse(event.getTime());
                    } catch (Exception error) {
                        throw new ParserException(error.getMessage());
                    }
                } else { // event diff > 30 minutes
                    try {
                        Document oneGameCount = new Document();
                        oneGameCount.append("start", sdf.format(startEventTime.getTime()));
                        oneGameCount.append("end", sdf.format(pointerEventTime.getTime()));
                        allGameCounts.add(oneGameCount);
                        gameCount++;

                        startEventTime = sdf.parse(event.getTime());
                        pointerEventTime = sdf.parse(event.getTime());
                    } catch (Exception error) {
                        throw new ParserException(error.getMessage());
                    }
                }
            }
            if( allGameCounts.size() == 0 ){
                Document oneGameCount = new Document();
                oneGameCount.append("start", sdf.format(startEventTime.getTime()));
                oneGameCount.append("end", sdf.format(pointerEventTime.getTime()));
                allGameCounts.add(oneGameCount);
                gameCount++;
            }
        }

        gameTime = gameTime/60;
        Document doc = new Document();
        doc.append("gameTimeWithMin",gameTime);
        doc.append("gameCount",gameCount);
        doc.append("gameCountTime",allGameCounts);
        return doc;
    }

    public Object getAllUserGameTime() {

        List<Document> usersGameTime = new ArrayList<Document>();
        List<User> Users = userService.findAll();
        for(int i=0; i< Users.size(); i++){
            User user = Users.get(i);
            Document userGameTime = (Document)getUserGameTime(user.getUsername());
            int gameTime = userGameTime.getInteger("gameTimeWithMin");
            userGameTime.append("user",user.getUsername());
            usersGameTime.add(userGameTime);

            // Sort
            int uIndex = usersGameTime.size()-1;
            for(int j = uIndex ; j > 0; j--){
                if( gameTime > usersGameTime.get(j-1).getInteger("gameTimeWithMin")){
                    Document tmp = usersGameTime.get(j-1);
                    usersGameTime.set(j-1,usersGameTime.get(j));
                    usersGameTime.set(j,tmp);
                }
            }

        }

        return usersGameTime;
    }

    public Object getLevelPassedTimeCostStatistics(){
        int startLevel = 0;
        int endLevel = 9;
        Document doc = new Document();
        List<Statistics> result = levelService.getLevelPassedTimeCostStatistics(startLevel,endLevel);
        for(int level = startLevel;level<=endLevel;level++){
            doc.append("level" + level, result.get(level) );
        }
        return doc;
    }

    public Object getAllUserActivityByDate(){
        List<Document> usersActivityByDate = new ArrayList<Document>();
        List<User> users = userService.findAll();
        for(int userIndex=0;userIndex<users.size();userIndex++){
            String username = users.get(userIndex).getUsername();
            List<UserEvent> allEvents = userEventService.getUserEvents(username);

            Document doc = new Document();
            doc.append("username", username);
            int activity = 0;
            long count = 0;
            List<String> allActivityDate = new ArrayList<String>();
            for(int j=0; j< allEvents.size();j++){
                String activityDate = allEvents.get(j).getTime().split(" ")[0];
                if( !allActivityDate.contains( activityDate)  ) {
                    activity++;
                    doc.append("activity" + activity, activityDate);
                    allActivityDate.add(activityDate);
                }
                count++;
            }

            doc.append("activity" , activity);
            doc.append("eventCount" , count);
            usersActivityByDate.add(doc);
            // Sort
            int uIndex = usersActivityByDate.size()-1;
            for(int i = uIndex ; i > 0; i--){
                if( activity > usersActivityByDate.get(i-1).getInteger("activity") ){
                    Document tmp = usersActivityByDate.get(i-1);
                    usersActivityByDate.set(i-1,usersActivityByDate.get(i));
                    usersActivityByDate.set(i,tmp);
                }
                else if ( activity == usersActivityByDate.get(i-1).getInteger("activity") &&  count > usersActivityByDate.get(i-1).getLong("eventCount") ){
                    Document tmp = usersActivityByDate.get(i-1);
                    usersActivityByDate.set(i-1,usersActivityByDate.get(i));
                    usersActivityByDate.set(i,tmp);
                }
            }
        }
        return usersActivityByDate;
    }

}
