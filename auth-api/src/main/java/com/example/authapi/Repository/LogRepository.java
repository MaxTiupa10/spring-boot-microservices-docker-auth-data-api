package com.example.authapi.Repository;

import com.example.authapi.Entity.ProcessingLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LogRepository extends JpaRepository<ProcessingLog, UUID> {

}

