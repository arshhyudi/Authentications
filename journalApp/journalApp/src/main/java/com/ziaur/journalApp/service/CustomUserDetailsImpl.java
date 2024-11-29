package com.ziaur.journalApp.service;

import com.ziaur.journalApp.entity.User;
import com.ziaur.journalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Find user by username (ensure this is case-sensitive or case-insensitive based on your requirements)
        User user = userRepository.findByUserName(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // Build a UserDetails object (Spring Security expects this format)
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserName())
                .password(user.getPassword())  // BCrypt-encoded password
                .roles(user.getRoles().toArray(new String[0]))  // Convert roles to a String[]
                .build();
    }
}
