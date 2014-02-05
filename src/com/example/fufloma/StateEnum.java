package com.example.fufloma;

import android.annotation.SuppressLint;
import java.util.HashMap;
import java.util.Map;

public enum StateEnum {
    BAD("schlecht", 0),
    OKAY("in Ordnung", 1),
    ALMNEW("wie neu", 2),
    NEW("neu/originalverpackt", 3);
    
    private String stringValue;
    private int code;

    
    private StateEnum(String toString, int value) {
        stringValue = toString;
    }

    @Override
    public String toString() {
        return stringValue;
    }
    
    public static StateEnum getStatus(int i) {
        return StateEnum.values()[i];
    }
}
