package com.losmessias.leherer.dto;

import com.losmessias.leherer.domain.File;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeworkCreationDto {
    private String assignment;
    private LocalDateTime deadline;
    private Long classReservationId;
    private Long professorId;
    private List<File> files;
}
