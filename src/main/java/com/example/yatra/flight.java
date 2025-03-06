package com.example.yatra;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flight")  // Optional: Specifies a base path for this controller
public class flight {

    @GetMapping("/myflight")
    public String getdata() {  // Fixed return type (String)
        return "Please Book your flight ticket";
    }
}

