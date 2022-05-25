package com.controller;

import com.exception.BaseException;
import com.service.UserService;
import com.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }


    @GetMapping("/getAllUser")
    public ResponseEntity<Object> getAllUser() {
        ResponseEntity<Object> response;
        try {
            List<User> result = userService.findAll();
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception error) {
            System.out.println(error.getMessage());
            response = new ResponseEntity<>(error.getMessage(), HttpStatus.FORBIDDEN);
        }

        return response;
    }

    @PostMapping("/createUser")
    public ResponseEntity<Object> createUser(
            @RequestParam("name") String name,
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ) {
        ResponseEntity<Object> response;
        try {
            Object result = userService.createUser(name,username,password);
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception error) {
            response = new ResponseEntity<>( ((BaseException)error).toJson(), HttpStatus.FORBIDDEN);
        }
        return response;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ) {
        ResponseEntity<Object> response;
        Object result = userService.login(username,password);
        response = new ResponseEntity<>(result, HttpStatus.OK);
        return response;
    }

    @GetMapping("/getUserPoints")
    public ResponseEntity<Object> getUserPoints(@RequestParam("username") String username){
        Object result = userService.getUserPoints(username);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/getAllUsersPoints")
    public ResponseEntity<Object> getAllUsersPoints(){
        Object result = userService.getAllUsersPoints();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
