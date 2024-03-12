package com.losmessias.leherer.dto;

import com.losmessias.leherer.domain.File;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeworkResponseDto {
    private String response;
    private Long associatedId;
    private File file;
}
