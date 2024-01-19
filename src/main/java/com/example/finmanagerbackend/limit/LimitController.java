package com.example.finmanagerbackend.limit;

import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Controller class for handling requests related to limits.
 */
@RestController
@RequestMapping( "/api/v1/limits" )
public class LimitController {
    private final LimitService limitService;

    public LimitController( LimitService limitService ) {
        this.limitService = limitService;
    }

    // Adds a new limit.
    @PostMapping( "/" )
    public void addNewLimit( @RequestBody LimitDTO limitDTO ) {
        // todo not like null on creation date field?
        // todo or something with account id?
        limitService.addLimit( limitDTO );
    }

    // Updates an existing limit.
    @PutMapping( "/{limitId}" )
    public void updateLimit( @PathVariable( "limitId" ) Long limitId, @RequestBody Limit limit ) {
        limitService.updateLimit( limitId, limit );
    }

    // Deletes an existing limit.
    @DeleteMapping( "/{limitId}" )
    public void deleteLimit( @PathVariable( "limitId" ) Long limitId ) {
        limitService.deleteLimit( limitId );
    }

    // Gets a list of all limits.
    @GetMapping( "/" )
    public List<Limit> getLimits() {
        return limitService.getLimits();
    }

    // Gets a list of available limit types.
    @GetMapping( "/types" )
    public List<String> getLimitTypes() {
        return limitService.getLimitTypes();
    }
}
