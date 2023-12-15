package com.example.finmanagerbackend.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
 /*   @Query( """
            SELECT accs
            FROM Account accs
            WHERE accs.id = :id
            """ )
    Optional<Account> findById( @Param( "id" ) Long id );*/
}
