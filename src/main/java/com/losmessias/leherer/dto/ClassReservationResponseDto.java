package com.losmessias.leherer.dto;

import com.losmessias.leherer.domain.Professor;
import com.losmessias.leherer.domain.Student;
import com.losmessias.leherer.domain.Subject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassReservationResponseDto {
    private Long id;
    private Professor professor;
    private Subject subject;
    private Student student;
    private LocalDate day;
    private LocalTime startingHour;
    private LocalTime endingHour;
    private Integer price;
    private String status;
}
