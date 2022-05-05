package com.service;


import com.dao.UserDao;
import com.dao.UserEventDao;
import com.model.UserEvent;
import com.utils.CollectionNameHolder;
import com.utils.DateHelper;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;


@Service
@ComponentScan({"com.dao"})
public class UserEventService {

    private final UserEventDao userEventDao;
    private final UserDao userDao;
    public UserEventService(UserEventDao userEventDao, UserDao userDao){
        this.userEventDao = userEventDao;
        this.userDao = userDao;
    }


    /**
     * Log user event
     * @param username the username
     * @param eventName the event name of student's activity
     * @param eventContent the content of event, should be json string
     * @return result
     */
    public Object logUserEvent(HttpServletRequest request, String username, String eventName, String eventContent) {
        if( userDao.findByUsername(username) == null ){
            return null;
        }
        String ip = getClientIpAddress(request);
        String nowTime = DateHelper.getNowTime();
        CollectionNameHolder.set(username);
        UserEvent userEvent = new UserEvent(username, eventName, ip, nowTime, eventContent);
        return userEventDao.insert(userEvent);
    }

    public Object getUserEvents(String username){
        CollectionNameHolder.set(username);
        return userEventDao.findAll();
    }

    /**
     * Get student ip address
     * @return ip address with string
     */
    private static String getClientIpAddress(HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();
        String forwarded = request.getHeader("X-Forwarded-For");
        String realIp = request.getHeader("X-Real-IP");
        String ip = null;

        if (realIp == null) {
            if (forwarded == null) {
                ip = remoteAddr;
            } else {
                ip = remoteAddr + "/" + forwarded.split(",")[0];
            }
        } else {
            if (realIp.equals(forwarded)) {
                ip = realIp;
            } else {
                if(forwarded != null){
                    forwarded = forwarded.split(",")[0];
                }
                ip = realIp + "/" + forwarded;
            }
        }
        return ip;
    }




}
