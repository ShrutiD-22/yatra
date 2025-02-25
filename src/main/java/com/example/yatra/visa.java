package com.example.yatra;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class visa {

    @GetMapping("/myvisa")
    public String getdata() {  // Fixed return type (String)
        return "Please submit your application for visa here";
    }
}

