package com.example.finmanagerbackend.limit;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping( "/api/v1/limits" )
public class LimitController {
    LimitService limitService;

    public LimitController( LimitService limitService ) {
        this.limitService = limitService;
    }

    @PostMapping( "/new" )
    public void addNewLimit( @RequestBody LimitDTO limitDTO ) {
        limitService.addNewLimit( limitDTO );
    }

    @DeleteMapping( "/delete/{limitId}" )
    public void deleteLimit( @PathVariable( "limitId" ) Long limitId ) {
        limitService.deleteLimit( limitId );
    }

    @GetMapping( "/all" )
    public List<Limit> getLimits() {
        return limitService.getLimits();
    }

    @PutMapping( "/update" )
    public void updateLimit( @RequestBody Limit limit ) {
        limitService.updateLimit( limit );
    }
}
