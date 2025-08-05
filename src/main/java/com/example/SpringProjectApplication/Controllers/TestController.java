package com.example.SpringProjectApplication.Controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/api/test")
public class TestController {


    @GetMapping
    public ResponseEntity<String> test() {

        return ResponseEntity.ok("Hello World");
    }
}
