package com.example.demoApplication.Controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/api/test")
public class TestController {


    @GetMapping
    public String test() {
        return "Test successful!";
    }
}
