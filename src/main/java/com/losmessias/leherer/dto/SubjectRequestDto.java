package com.losmessias.leherer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectRequestDto {

    private Long professorId;
    private List<Long> subjectIds;

    public String toString() {
        return "SubjectRequestDto{" +
                "professorId=" + professorId +
                ", subjectIds=" + subjectIds +
                "}";
    }
}
