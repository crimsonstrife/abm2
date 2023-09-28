package com.pbarnhardt.abm2task1.Utils;

import java.text.SimpleDateFormat;

public class Formatting {
    private static final String pattern = "MM/dd/yyyy";
    public static SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, java.util.Locale.getDefault());
    public static String email = "^[A-Za-z0-9+_.-]+@(.+)$";  // Regex for email validation, didn't end up needing to use it, was not aware Android has built in validation
}
