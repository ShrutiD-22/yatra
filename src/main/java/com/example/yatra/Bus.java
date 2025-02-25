package com.example.yatra;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bus")  // Optional: Specifies a base path for this controller
public class Bus {

    @GetMapping("/mybus")
    public String getdata() {  // Fixed return type (String)
        return "Please Book your bus ticket";
    }
}