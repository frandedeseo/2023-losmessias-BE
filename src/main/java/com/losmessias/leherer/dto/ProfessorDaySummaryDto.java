package com.losmessias.leherer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfessorDaySummaryDto {
    private Long professorId;
    private Long subjectId;
    private Integer totalHours;
    private Integer totalIncome;
}
