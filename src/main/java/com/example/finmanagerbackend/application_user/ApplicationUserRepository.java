package com.example.finmanagerbackend.application_user;

import com.github.javafaker.Bool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, String> {
    @Query( """
            SELECT users
            FROM ApplicationUser users
            WHERE users.email = :email
            """)
    Optional<ApplicationUser> findByEmail( @Param( "email" ) String email );

    @Query( """
            SELECT 
            CASE WHEN COUNT( users.email ) > 0 
            THEN true ELSE false
            END 
            FROM ApplicationUser users
            WHERE users.email = :email
            """)
    Boolean existsByUsername( @Param( "email" ) String email );

    @Transactional
    @Modifying
    @Query( """
            UPDATE ApplicationUser user
            SET user.active = :isActive
            WHERE user.email = :email
            """ )
    void setUserActivity( @Param( "email" ) String email,
                          @Param( "isActive" ) boolean isActive );

    @Query( """
            SELECT user.active
            FROM ApplicationUser user
            WHERE user.email = :email
            """ )
    boolean isUserActive( @Param( "email" ) String email );
}
