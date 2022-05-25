package com.controller;

import com.service.ActivityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/activity")
public class ActivityController {
    private final ActivityService activityService;

    public ActivityController(ActivityService activityService){
        this.activityService = activityService;
    }

    @GetMapping("/getUsersInGamePerformance")
    public ResponseEntity<Object> getUsersInGamePerformance() {
        return new ResponseEntity<Object>(activityService.getUsersInGamePerformance(), HttpStatus.OK);
    }

    @GetMapping("/getUserGameTime")
    public ResponseEntity<Object> getUserGameTime(@RequestParam String username){
        return new ResponseEntity<Object>(activityService.getUserGameTime(username), HttpStatus.OK);
    }

    @GetMapping("/getAllUserGameTime")
    public ResponseEntity<Object> getAllUserGameTime(){
        return new ResponseEntity<Object>(activityService.getAllUserGameTime(), HttpStatus.OK);
    }

    @GetMapping("/getLevelPassedTimeCostStatistics")
    public ResponseEntity<Object> getLevelPassedTimeCostStatistics(){
        return new ResponseEntity<Object>(activityService.getLevelPassedTimeCostStatistics(), HttpStatus.OK);
    }

    @GetMapping("/getAllUserActivityByDate")
    public ResponseEntity<Object> getAllUserActivityByDate(){
        return new ResponseEntity<Object>(activityService.getAllUserActivityByDate(), HttpStatus.OK);
    }
}
