package com.bosch.coding.enums;

import java.util.Random;

public enum Update {
    ADD,
    REMOVE;

    public static Update randomEnum(){
        int x = new Random().nextInt(Update.values().length);
        return Update.values()[x];
    }
}
