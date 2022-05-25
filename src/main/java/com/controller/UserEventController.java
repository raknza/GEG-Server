package com.controller;

import com.service.UserEventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/user/event")
public class UserEventController {

    private final UserEventService userEventService;

    public UserEventController(UserEventService userEventService){
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
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Get student Event
     * @return Response
     */
    @GetMapping("getUserEvents")
    public ResponseEntity<Object> getUserEvents(@RequestParam("username") String username) {

        Object result = userEventService.getUserEvents(username);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
