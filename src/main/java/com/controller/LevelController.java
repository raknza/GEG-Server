package com.controller;

import com.service.LevelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/level")
public class LevelController {

    private final LevelService levelService;

    public LevelController(LevelService levelService){
        this.levelService = levelService;
    }

    @PostMapping("/logLevelRecord")
    public ResponseEntity<Object> logLevelRecord(
            @RequestParam("username") String username,
            @RequestParam("timeCost") int timeCost,
            @RequestParam("lineCost") int lineCost,
            @RequestParam("level") int level
    ) {
        ResponseEntity<Object> response;
        Object result = levelService.logLevelRecord(username, timeCost, lineCost, level);
        response = new ResponseEntity<>(result, HttpStatus.OK);
        return response;
    }

    @GetMapping("/getLevelLeaderboard")
    public ResponseEntity<Object> getLevelLeaderboard(@RequestParam("level") int level) {
        return new ResponseEntity<>(levelService.getLevelLeaderboard(level), HttpStatus.OK);
    }
}
