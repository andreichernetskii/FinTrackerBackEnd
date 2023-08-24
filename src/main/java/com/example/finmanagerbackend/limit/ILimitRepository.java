package com.example.finmanagerbackend.limit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ILimitRepository extends JpaRepository<Limit, Long> {
}
