package com.losmessias.leherer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeworkCreationDto {
    private String assignment;
    private LocalDateTime deadline;
    private Long classReservationId;
    private Long professorId;
    private MultipartFile file;
}
