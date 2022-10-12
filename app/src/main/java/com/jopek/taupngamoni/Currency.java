package com.jopek.taupngamoni;

import android.graphics.Bitmap;

public class Currency {
    public String code;
    public String description;
    public Bitmap img;
    public String symbol;
    public double conversionFactor;

    public static final Currency USD = new Currency("USD", "United States Dollar", null, "$", 0.0);
    public static final Currency GBP = new Currency("GBP", "British Pound Sterling", null, "£", 0.0);
    public static final Currency CHF = new Currency("CHF", "Swiss Franc", null, "Fr", 0.0);
    public static final Currency PLN = new Currency("PLN", "Polish Zloty", null, "zł", 0.0);
    public static final Currency EUR = new Currency("EUR", "Euro", null, "€", 0.0);
    public static final Currency JPY = new Currency("JPY", "Japanese Yen", null, "¥", 0.0);
    public static final Currency CNY = new Currency("CNY", "Chinese Yuan", null, "¥", 0.0);
    public static final Currency UAH = new Currency("UAH", "Ukrainian Hryvnia", null, "₴", 0.0);
    public static final Currency CZK = new Currency("CZK", "Czech Republic Koruna", null, "Kč", 0.0);
    public static final Currency TWD = new Currency("TWD", "New Taiwan Dollar", null, "圓", 0.0);

    public Currency(String code, String description, Bitmap img, String symbol, double conversionFactor) {
        this.code = code;
        this.description = description;
        this.img = img;
        this.symbol = symbol;
        this.conversionFactor = conversionFactor;
    }
}
