package com.example.finmanagerbackend.limit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ILimitRepository extends JpaRepository<Limit, Long> {
    @Query( "SELECT limitAmount FROM Limit " +
            "WHERE limitType = :limitType" )
    Double getLimitAmountByLimitType( @Param( "limitType" ) LimitType limitType );
}
