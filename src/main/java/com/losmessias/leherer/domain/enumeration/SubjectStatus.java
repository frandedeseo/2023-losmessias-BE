package com.losmessias.leherer.domain.enumeration;

import lombok.Getter;

@Getter
public enum SubjectStatus {

    PENDING("PENDING"),
    APPROVED("APPROVED"),
    REJECTED("REJECTED");

    private final String status;

    SubjectStatus(String status) {
        this.status = status;
    }

}
