package com.losmessias.leherer.domain.enumeration;

import lombok.Getter;

@Getter
public enum ReservationStatus {
//    PENDING("PENDING"),
    CONFIRMED("CONFIRMED"),
    CONCLUDED("CONCLUDED");

    private final String status;

    ReservationStatus(String status) {
        this.status = status;
    }

}
