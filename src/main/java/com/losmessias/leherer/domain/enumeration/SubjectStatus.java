package com.losmessias.leherer.domain.enumeration;

public enum SubjectStatus {

    PENDING("PENDING"),
    APPROVED("APPROVED"),
    REJECTED("REJECTED");

    private final String status;

    SubjectStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
