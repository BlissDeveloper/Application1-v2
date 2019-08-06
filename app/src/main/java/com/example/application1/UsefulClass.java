package com.example.application1;

import java.util.UUID;

public class UsefulClass {
    public UsefulClass() {

    }

    public String getUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
