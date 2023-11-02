package com.example.finmanagerbackend.application_user;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class ApplicationUserController {
    private final ApplicationUserService applicationUserService;

    public ApplicationUserController( ApplicationUserService applicationUserService ) {
        this.applicationUserService = applicationUserService;
    }

    @PostMapping( "/add_user" )
    public void newUser( @RequestBody ApplicationUserDTO applicationUserDTO ) {
        applicationUserService.newUser( applicationUserDTO );
    }

    @GetMapping
    public List<ApplicationUser> showUsers() {
        return applicationUserService.showUsers();
    }
}
