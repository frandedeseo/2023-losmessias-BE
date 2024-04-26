package com.losmessias.leherer.dto;

import com.losmessias.leherer.domain.File;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeworkDto {
    private Long id;
    private Long classReservationId;
    private String assignment;
    private String response;
    private String status;
    private String deadline;
    private Long professorId;
    private Long studentId;
    private File assignmentFile;
    private File responseFile;
}
