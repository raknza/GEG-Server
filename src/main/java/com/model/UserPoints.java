package com.model;


/**
 * User's point in game. According to 2 factors, levelPassedCounts and achievementCounts,
 * which present player's in game performance. These two factors will be convert to
 * levelPoint and achievementPoints. Sum of them are gamePoints.
 *
 * */
public class UserPoints implements Comparable<UserPoints> {

    int gamePoints;
    int levelPoints;
    int achievementPoints;
    int levelPassedCounts;
    int achievementCounts;
    String user;

    public UserPoints(int levelPassedCounts, int achievementCounts, String user){
        this.levelPassedCounts = levelPassedCounts;
        this.achievementCounts = achievementCounts;
        levelPoints = levelPassedCounts * 10;
        achievementPoints = ((1 + achievementCounts) * achievementCounts ) / 2;
        gamePoints = levelPoints + achievementPoints;
        this.user = user;
    }

    public int getGamePoints(){ return gamePoints; }
    public int getLevelPoints(){ return levelPoints; }
    public int getAchievementPoints(){ return achievementPoints; }
    public int getLevelPassedCounts(){ return levelPassedCounts; }
    public int getAchievementCounts(){ return achievementCounts; }
    public String getUser(){ return user; }

    @Override
    public int compareTo(UserPoints userPoints) {
        if(gamePoints == userPoints.gamePoints && levelPoints == userPoints.levelPoints ) {
            return userPoints.achievementPoints - achievementPoints;
        }
        else if ( gamePoints == userPoints.gamePoints ) {
            return userPoints.levelPoints - levelPoints;
        }
        else {
            return userPoints.gamePoints - gamePoints;
        }
    }
}
