package com.example.finmanagerbackend.limit;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping( "/api/v1/limits" )
public class LimitController {
    private final LimitService limitService;

    public LimitController( LimitService limitService ) {
        this.limitService = limitService;
    }

    @PostMapping( "/" )
    public void addNewLimit( @RequestBody LimitDTO limitDTO ) {
        limitService.addLimit( limitDTO );
    }

    @PutMapping( "/{limitId}" )
    public void updateLimit( @PathVariable( "limitId" ) Long limitId, @RequestBody Limit limit ) {
        limitService.updateLimit( limitId, limit );
    }

    @DeleteMapping( "/{limitId}" )
    public void deleteLimit( @PathVariable( "limitId" ) Long limitId ) {
        limitService.deleteLimit( limitId );
    }

    @GetMapping( "/" )
    public List<Limit> getLimits() {
        return limitService.getLimits();
    }

    @GetMapping("/types")
    public List<String> getLimitTypes() {
        return limitService.getLimitTypes();
    }
}
