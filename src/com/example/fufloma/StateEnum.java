package com.example.fufloma;

public enum StateEnum {
	NEW("neu/originalverpackt", 0),
	ALMNEW("wie neu", 1),
	OKAY("in Ordnung", 2),
    BAD("schlecht", 3);

    private String stringValue;
    
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
