package com.ziaur.journalApp.service;

import com.ziaur.journalApp.entity.User;
import com.ziaur.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class UserService {
    @Autowired
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public void saveEntry(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER"));
        userRepository.save(user);
    }
    public void saveNewUser(User user) {
        userRepository.save(user);
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public Optional<User> getUserById(@PathVariable ObjectId id) {
        return userRepository.findById(id);
    }
    public void deleteById(@PathVariable ObjectId id) {
        userRepository.deleteById(id);
    }

    public User findByUsername(String userName) {
        return userRepository.findByUserName(userName);
    }
}


//controller ---> service ----> repository