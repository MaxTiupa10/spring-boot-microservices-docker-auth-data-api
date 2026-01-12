package com.example.authapi.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "processing_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessingLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private UUID userId;
    private String inputText;
    private String outputText;
    private LocalDateTime createdAt;
}
