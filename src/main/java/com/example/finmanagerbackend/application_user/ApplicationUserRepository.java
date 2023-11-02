package com.example.finmanagerbackend.application_user;

import com.github.javafaker.Bool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, String> {
    Optional<ApplicationUser> findByEmail( String email );
    Boolean existsByUsername( String email );
}
