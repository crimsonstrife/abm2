package com.pbarnhardt.abm2task1;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * The type Main activity.
 */
public class MainActivity extends AppCompatActivity {
    /**
     * Variables
     */
    Toast toast;
    String date = null;
    String time = null;

    /**
     * The constant DATE_FORMAT.
     */
    public static final String DATE_FORMAT = "MM/dd/yyyy";
    /**
     * The constant TIME_FORMAT.
     */
    public static final String TIME_FORMAT = "hh:mm a";

    /**
     * The constant timePos
     */
    public static int timePos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}