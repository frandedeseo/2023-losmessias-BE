package com.losmessias.leherer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeworkResponseDto {
    private Long id;
    private Long classReservationId;
    private String assignment;
    private String response;
    private String status;
    private String deadline;
    private Long professorId;
    private Long studentId;
}
