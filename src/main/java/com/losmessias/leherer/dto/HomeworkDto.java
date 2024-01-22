package com.losmessias.leherer.dto;

import com.losmessias.leherer.domain.File;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeworkDto {
    private String assignment;
    private Long classReservationId;
    private String deadline;
    private List<File> files;
}
