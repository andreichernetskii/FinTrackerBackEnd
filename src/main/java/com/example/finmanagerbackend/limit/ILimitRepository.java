package com.example.finmanagerbackend.limit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ILimitRepository extends JpaRepository<Limit, Long> {
    @Query( "SELECT limitAmount FROM Limit " +
            "WHERE limitType = :limitType" )
    Double getLimitAmountByLimitType( @Param( "limitType" ) LimitType limitType );


    @Query( """
          SELECT 
          CASE WHEN COUNT( limitType ) > 0 
          THEN true ELSE false
          END 
          FROM Limit 
          WHERE limitType = :limitType""" )
    Boolean existsBy( @Param( "limitType" ) LimitType limitType );

    //todo: dowiedzieć się, czy to można zrobić w stylu
    // void deleteByLimitType( LimitType limitType ); bez @
    // albo od razu update bez usuwania
    @Modifying
    @Query( "DELETE FROM Limit lt WHERE lt.limitType = :limitType" )
    void deleteByLimitType( @Param( "limitType" ) LimitType limitType );
}
