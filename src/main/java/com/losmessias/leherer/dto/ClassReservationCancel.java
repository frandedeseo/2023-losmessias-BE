package com.losmessias.leherer.dto;

import com.losmessias.leherer.domain.enumeration.AppUserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassReservationCancel {
    private Long id;
    private AppUserRole role;
}
