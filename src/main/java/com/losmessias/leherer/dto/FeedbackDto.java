package com.losmessias.leherer.dto;

import com.losmessias.leherer.domain.enumeration.AppUserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackDto {
    private Long senderId;
    private Long receiverId;
    private Long classId;
    private Double rating;
    private Boolean material;
    private Boolean punctuality;
    private Boolean polite;
}
