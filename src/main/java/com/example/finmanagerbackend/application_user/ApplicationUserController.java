package com.example.finmanagerbackend.application_user;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
public class ApplicationUserController {
    private final ApplicationUserService applicationUserService;

    public ApplicationUserController( ApplicationUserService applicationUserService ) {
        this.applicationUserService = applicationUserService;
    }

    @PostMapping()
    public void newUser( @RequestBody ApplicationUserDTO applicationUserDTO ) {
        applicationUserService.newUser( applicationUserDTO );
    }
}
