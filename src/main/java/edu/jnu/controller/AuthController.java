package edu.jnu.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @PostMapping("/register")
//    public ResponseEntity<?> register(@RequestBody User user) {
//        userService.registerUser(user);
//        return ResponseEntity.ok("User registered successfully");
//    }
//
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> credentials) {
//        String username = credentials.get("username");
//        String password = credentials.get("password");
//
//        Optional<User> user = userService.findByUsername(username);
//        if (user.isPresent() && new BCryptPasswordEncoder().matches(password, user.get().getPassword())) {
//            String token = jwtUtil.generateToken(username);
//            return ResponseEntity.ok(Map.of("token", token));
//        }

        return ResponseEntity.ok().body("qwertyuiop[");
    }
}

