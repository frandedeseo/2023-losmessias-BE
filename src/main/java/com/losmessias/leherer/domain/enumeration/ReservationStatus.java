package com.losmessias.leherer.domain.enumeration;

import lombok.Getter;

@Getter
public enum ReservationStatus {
    NOT_AVAILABLE("NOT_AVAILABLE"),
    CONFIRMED("CONFIRMED"),
    CONCLUDED("CONCLUDED"),
    CANCELLED("CANCELLED");

    private final String status;

    ReservationStatus(String status) {
        this.status = status;
    }

}
