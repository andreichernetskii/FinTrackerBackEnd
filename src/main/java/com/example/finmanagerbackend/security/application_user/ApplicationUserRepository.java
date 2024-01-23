package com.example.finmanagerbackend.security.application_user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Repository interface for accessing ApplicationUser entities in the database.
 */
@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, String> {

    // Query to find an ApplicationUser by email
    @Query( """
            SELECT users
            FROM ApplicationUser users
            WHERE users.email = :email
            """ )
    Optional<ApplicationUser> findByEmail( @Param( "email" ) String email );

    // Query to check if an ApplicationUser with a given email exists
    @Query( """
            SELECT
            CASE WHEN COUNT( users.email ) > 0 
            THEN true ELSE false
            END 
            FROM ApplicationUser users
            WHERE users.email = :email
            """ )
    Boolean existsByUsername( @Param( "email" ) String email );

    // Transactional query to update the active status of an ApplicationUser by email
    @Transactional
    @Modifying
    @Query( """
            UPDATE ApplicationUser user
            SET user.active = :isActive
            WHERE user.email = :email
            """ )
    void setUserActivity( @Param( "email" ) String email,
                          @Param( "isActive" ) boolean isActive );

    // todo: token musi być odpowiedzialny wiedzieć, czy jest
    // todo albo dodać datę ostatniego wylogowania
    // Query to check if an ApplicationUser is active by email
    @Query( """
            SELECT user.active
            FROM ApplicationUser user
            WHERE user.email = :email
            """ )
    boolean isUserActive( @Param( "email" ) String email );
}

/*
* login     -> singup i dostaj cookie (z nowym JWT)
*           -> uzytkownik zmieniony w bazie danych na aktywny
*
* refresh   -> front zalacza trzyne cookie z zapytaniem
*           -> backend kontroluje czas i prawdziwość
*               -> jeśli coś nie tak to przestawia w bazie na nieaktywny i zwraca status forbiden
*           -> front przekierowuje na zalowanie jeszcze raz
*
*
* */
