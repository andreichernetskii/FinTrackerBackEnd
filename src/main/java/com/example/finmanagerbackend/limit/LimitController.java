package com.example.finmanagerbackend.limit;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( "/api/v1/limits" )
public class LimitController {
    LimitService limitService;

    public LimitController( LimitService limitService ) {
        this.limitService = limitService;
    }
}
