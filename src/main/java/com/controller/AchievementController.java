package com.controller;

import com.service.AchievementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/achievement")
public class AchievementController {

    private final AchievementService achievementService;

    public AchievementController(AchievementService achievementService){
        this.achievementService = achievementService;
    }

    @PostMapping("logAchievement")
    public ResponseEntity<Object> logAchievement(@RequestParam("username") String username,
                                                 @RequestParam("achievement") String achievement) {
        Object result = achievementService.logAchievement(username, achievement);
        return new ResponseEntity<Object>(result, HttpStatus.OK);
    }
}
