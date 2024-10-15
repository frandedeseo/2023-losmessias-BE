package com.losmessias.leherer.dto;

import com.losmessias.leherer.domain.Subject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectPriceDto {
    private Double price;
    private Subject subject;
}
