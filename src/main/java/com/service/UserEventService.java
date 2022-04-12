package com.service;


import com.dao.UserEventDao;
import com.model.UserEvent;
import com.utils.CollectionNameHolder;
import com.utils.DateHelper;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;


@Service
@Configuration
@EnableMongoRepositories
@ComponentScan({"com.utils"})
public class UserEventService {

    @Autowired
    private UserEventDao userEventDao;


    /**
     * Log user event
     * @return result
     */
    public Object logUserEvent(HttpServletRequest request, String username, String eventName, String eventContent) {
        String ip = getClientIpAddress(request);
        String nowTime = DateHelper.getNowTime();
        CollectionNameHolder.set(username);
        Document eventContentDocument = Document.parse(eventContent);
        UserEvent userEvent = new UserEvent(username, eventName, ip, nowTime, eventContentDocument);
        return userEventDao.insert(userEvent);
    }

    /**
     * Get student ip address
     * @return ip address with string
     */
    private static String getClientIpAddress(HttpServletRequest request) {
        return request.getHeader("X-FORWARDED-FOR");
    }




}
