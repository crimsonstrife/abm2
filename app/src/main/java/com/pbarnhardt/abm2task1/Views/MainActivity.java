package com.pbarnhardt.abm2task1.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.pbarnhardt.abm2task1.R;

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
     * The constant alarmNumber.
     */
    public static int alarmNumber;

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
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     * The Terms Button
     * This method will start the Terms activity used for setting the terms for the students courses
     */
    public void termButton() {
        Intent intent = new Intent(this, TermsListActivity.class);
        startActivity(intent);
    }

    /**
     * The Courses Button
     * This method will start the Courses activity used for setting the courses for the students terms
     */
    public void courseButton() {
        Intent intent = new Intent(this, CoursesListActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Handle item selection
        if (item.getItemId() == R.id.question) {
            Intent intent = new Intent(this, HelpActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.info) {
            Intent intent = new Intent(this, InfoActivity.class);
            startActivity(intent);
            return true;
        } else {
            //If we got here, the user's action was not recognized
            //Invoke the superclass to handle it
            return super.onOptionsItemSelected(item);
        }
    }
}