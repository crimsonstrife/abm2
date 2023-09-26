package com.pbarnhardt.abm2task1.Utils;

import java.text.SimpleDateFormat;

public class Formatting {
    private static String pattern = "MM/dd/yyyy";
    public static SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
    public static String email = "^[A-Za-z0-9+_.-]+@(.+)$";
}
