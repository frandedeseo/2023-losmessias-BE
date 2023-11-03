package com.losmessias.leherer.domain;

import com.losmessias.leherer.domain.enumeration.AppUserRole;
import lombok.*;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "file")
@PrimaryKeyJoinColumn(name = "id")
public class File extends LoadedData {

    private String fileName;

    private String fileType;

    @Lob @Basic(fetch = FetchType.LAZY)
    @Column(length=1000000)
    private byte[] data;

    public File(ClassReservation classReservation, LocalDateTime uploadedDateTime, AppUserRole role, Long associatedId, String fileName, String fileType, byte[] data) {
        super(classReservation, uploadedDateTime, role, associatedId);
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
    }
}
