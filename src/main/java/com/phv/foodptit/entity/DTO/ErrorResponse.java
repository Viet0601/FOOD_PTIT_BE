package com.phv.foodptit.entity.DTO;

import java.time.LocalDateTime;

import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
     private boolean error;
    private int status;
    private String errorType;
    private String message;
    private String path;
    private LocalDateTime timestamp;
    
}
