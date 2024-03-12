package com.losmessias.leherer.dto;

import com.losmessias.leherer.domain.enumeration.AppUserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadInformationDto {
    private Long idFile;
    private Long classReservation;
    private LocalDateTime uploadedDateTime;
    private AppUserRole role;
    private Long associatedId;
    private Long homeworkId;
}
