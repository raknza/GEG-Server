package com.service;


import com.dao.UserEventDao;
import com.model.UserEvent;
import com.utils.CollectionNameHolder;
import com.utils.DateHelper;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;


@Service
@EnableMongoRepositories
@ComponentScan({"com.utils"})
public class UserEventService {

    private final UserEventDao userEventDao;

    public UserEventService(UserEventDao userEventDao){
        this.userEventDao = userEventDao;
    }


    /**
     * Log user event
     * @param username the username
     * @param eventName the event name of student's activity
     * @param eventContent the content of event, should be json string
     * @return result
     */
    public Object logUserEvent(HttpServletRequest request, String username, String eventName, String eventContent) {
        String ip = getClientIpAddress(request);
        String nowTime = DateHelper.getNowTime();
        CollectionNameHolder.set(username);
        UserEvent userEvent = new UserEvent(username, eventName, ip, nowTime, eventContent);
        return userEventDao.insert(userEvent);
    }

    public Object getAllUserEvent(String username){
        CollectionNameHolder.set(username);
        return userEventDao.findAll();
    }

    /**
     * Get student ip address
     * @return ip address with string
     */
    private static String getClientIpAddress(HttpServletRequest request) {
        return request.getHeader("X-FORWARDED-FOR");
    }




}
