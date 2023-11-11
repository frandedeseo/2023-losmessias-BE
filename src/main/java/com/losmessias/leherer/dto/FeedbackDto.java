package com.losmessias.leherer.dto;

import com.losmessias.leherer.domain.enumeration.AppUserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackDto {
    private Long studentId;
    private Long professorId;
    private AppUserRole roleReceptor;
    private Double rating;
    private Boolean material;
    private Boolean punctuality;
    private Boolean educated;

}
