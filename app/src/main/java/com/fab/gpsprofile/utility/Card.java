package com.fab.gpsprofile.utility;

public class Card {
    private String line1;
    private String line2;
    private String line3;
    private String line4;
    private String line5;
    private String line6;

    public Card(String line1, String line2) {
        this.line1 = line1;
        this.line2 = line2;
    }
    public Card(String line1, String line2, String line3, String line6) {
        this.line1 = line1;
        this.line2 = line2;
        this.line3 = line3;
        this.line6 = line6;
    }

    public String getLine1() {
        return line1;
    }

    public String getLine2() {
        return line2;
    }

    public String getLine3() {
        return line3;
    }

    public String getLine6() {
        return line6;
    }

}