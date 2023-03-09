package com.project.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
public enum OptInStatus {
    PENDING(1 , "Pending"),
    REJECTED(2 , "Rejected"),
    ACCEPTED(3 , "Accepted");

    private int key;

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}
