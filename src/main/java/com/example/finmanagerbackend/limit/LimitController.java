package com.example.finmanagerbackend.limit;

import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping( "/api/v1/limits" )
public class LimitController {
    LimitService limitService;

    public LimitController( LimitService limitService ) {
        this.limitService = limitService;
    }

    // todo: przerobić na lepsze praktyki tworzenia ścieżek według konwencji RestAPI
    @PostMapping( "/" )
    public void addNewLimit( @RequestBody LimitDTO limitDTO ) {
        limitService.addOrUpdateLimit( limitDTO );
    }

    @DeleteMapping( "/{limitId}" )
    public void deleteLimit( @PathVariable( "limitId" ) Long limitId ) {
        limitService.deleteLimit( limitId );
    }

    @GetMapping( "/" )
    public List<Limit> getLimits() {
        return limitService.getLimits();
    }

//    @PutMapping( "/update" )
//    public void updateLimit( @RequestBody Limit limit ) {
//        limitService.addOrUpdateLimit( limit );
//    }

    @GetMapping("/types")
    public List<String> getLimitTypes() {
        return limitService.getLimitTypes();
    }
}
