package com.example.finmanagerbackend.limit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Limit entities in the database.
 */
@Repository
public interface LimitRepository extends JpaRepository<Limit, Long> {

    // Retrieves all limits except for the ZERO type associated with a specific account.
    @Query( """
            SELECT limits
            FROM Limit limits
            WHERE limits.limitType != 'ZERO'
            AND limits.account.id = :accountId
            """)
    List<Limit> getAllLimitsWithoutZero( @Param( "accountId" ) Long accountId );

    // Retrieves a specific limit associated with a given account.
    @Query( """
            SELECT limit
            FROM Limit limit
            WHERE limit.account.id = :accountId
            AND limit.id = :limitId
            """)
    Optional<Limit> findLimit( @Param( "limitId" ) Long limitId,
                               @Param( "accountId" ) Long accountId );

    // Retrieves the limit amount for a specific limit type.
    @Query( """
            SELECT limitAmount FROM Limit
            WHERE limitType = :limitType
            """ )
    Double getLimitAmountByLimitType( @Param( "limitType" ) LimitType limitType );

    // Checks if a limit of a specific type exists for a given account.
    @Query( """
          SELECT 
          CASE WHEN COUNT( limitType ) > 0 
          THEN true ELSE false
          END 
          FROM Limit 
          WHERE ( :accountId IS NULL OR account.id = :accountId )
          AND limitType = :limitType
          """ )
    Boolean existsBy( @Param( "accountId" ) Long accountId,
                      @Param( "limitType" ) LimitType limitType );

    // Deletes limits of a specific type.
    @Modifying
    @Query( """
            DELETE 
            FROM Limit lt 
            WHERE lt.limitType = :limitType
            """ )
    void deleteByLimitType( @Param( "limitType" ) LimitType limitType );
}
