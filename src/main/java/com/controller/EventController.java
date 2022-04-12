package com.controller;

import com.service.UserEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/user/event")
public class EventController {

    private final UserEventService userEventService;

    public EventController(UserEventService userEventService){
        this.userEventService = userEventService;
    }

    /**
     * Student Event Log
     * @return Response
     */
    @PostMapping("logUserEvent")
    public ResponseEntity<Object> logUserEvent(HttpServletRequest request,
                                                  @RequestParam("username") String username,
                                                  @RequestParam("eventName") String eventName,
                                                  @RequestParam("eventContent") String eventContent) {

        Object result = userEventService.logUserEvent(request, username, eventName, eventContent);
        return new ResponseEntity<Object>(result, HttpStatus.OK);
    }
}
