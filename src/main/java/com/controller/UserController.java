package com.controller;

import com.exception.BaseException;
import com.service.UserService;
import com.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {


    @Autowired
    UserService userService;


    @GetMapping("/getAllUser")
    public ResponseEntity<Object> getAllUser() {
        ResponseEntity<Object> response;
        try {
            List<User> result = userService.findAll();
            response = new ResponseEntity<Object>(result, HttpStatus.OK);
        } catch (Exception error) {
            System.out.println(error.getMessage());
            response = new ResponseEntity<Object>(error.getMessage(), HttpStatus.FORBIDDEN);
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
            response = new ResponseEntity<Object>(result, HttpStatus.OK);
        } catch (Exception error) {
            response = new ResponseEntity<Object>( ((BaseException)error).toJson(), HttpStatus.FORBIDDEN);
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
        response = new ResponseEntity<Object>(result, HttpStatus.OK);
        return response;
    }

}
