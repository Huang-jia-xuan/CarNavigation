package edu.jnu.service;

import edu.jnu.dao.UserRepository;
import edu.jnu.entity.UserAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private PasswordEncoder passwordEncoder;

//    public User registerUser(User user) {
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        return userRepository.save(user);
//    }

    public Optional<UserAuth> findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }
}
