package com.ziaur.journalApp.controller;

import com.ziaur.journalApp.entity.User;
import com.ziaur.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
     public List<User> getAllUsers() {
         return userService.getAllUsers();
     }
     @PostMapping
     public void createUser(@RequestBody User user) {
         userService.saveEntry(user);
     }
     @PutMapping("/{userName}")
    public ResponseEntity<?> updateUser(@RequestBody User user, @PathVariable String userName) {
         User userInDb = userService.findByUsername(userName);
         if (userInDb != null) {
             userInDb.setUserName(user.getUserName());
             userInDb.setPassword(user.getPassword());
             userService.saveEntry(userInDb);
         }
         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
     }
}
