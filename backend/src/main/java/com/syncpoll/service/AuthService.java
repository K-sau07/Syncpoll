package com.syncpoll.service;

import com.syncpoll.model.entity.User;
import com.syncpoll.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User findOrCreateUser(String email, String name, String googleId, String pictureUrl) {
        Optional<User> existingUser = userRepository.findByGoogleId(googleId);
        
        if (existingUser.isPresent()) {
            log.debug("Found existing user with googleId: {}", googleId);
            User user = existingUser.get();
            
            // update profile info in case it changed
            user.setName(name);
            user.setPictureUrl(pictureUrl);
            return userRepository.save(user);
        }

        log.info("Creating new user with email: {}", email);
        User newUser = new User(email, name, googleId);
        newUser.setPictureUrl(pictureUrl);
        return userRepository.save(newUser);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}
