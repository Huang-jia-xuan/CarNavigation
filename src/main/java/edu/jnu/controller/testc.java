package edu.jnu.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
public class testc {
    @GetMapping("/test")
    public ResponseEntity<String> test(){
        return ResponseEntity.ok().body("fefef");
    }
}
