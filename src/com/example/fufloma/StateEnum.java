package com.example.fufloma;

public enum StateEnum {
    BAD("schlecht", 0),
    OKAY("in Ordnung", 1),
    ALMNEW("wie neu", 2),
    NEW("neu/originalverpackt", 3);
    
    private String stringValue;
    
    private StateEnum(String toString, int value) {
        stringValue = toString;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}
