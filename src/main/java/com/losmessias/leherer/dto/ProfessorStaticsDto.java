package com.losmessias.leherer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Dictionary;
import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfessorStaticsDto {
    private Double totalClasses;
    private HashMap<String, Double> classesPerSubject;
    private Double incomes;
    private Double cancelledClasses;
}
