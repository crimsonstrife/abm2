package com.pbarnhardt.abm2task1.Views;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.navigation.NavigationView;
import com.pbarnhardt.abm2task1.Adapters.TermAdapter;
import com.pbarnhardt.abm2task1.Entity.Assessments;
import com.pbarnhardt.abm2task1.Entity.Courses;
import com.pbarnhardt.abm2task1.Entity.Mentors;
import com.pbarnhardt.abm2task1.Entity.Terms;
import com.pbarnhardt.abm2task1.Enums.RecyclerAdapter;
import com.pbarnhardt.abm2task1.Models.HomeModel;
import com.pbarnhardt.abm2task1.R;
import com.pbarnhardt.abm2task1.Utils.Alerts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The type Main activity.
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    /**
     * Objects
     */
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private TermAdapter termAdapter;
    private HomeModel homeModel;
    private TextView inspirationalQuote, inspirationalAuthor;

    /**
     * Variables
     */
    private List<Terms> termListData = new ArrayList<>();
    private List<Courses> courseListData = new ArrayList<>();
    private List<Assessments> assessmentListData = new ArrayList<>();
    private List<Mentors> mentorListData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_navigation, R.string.close_navigation);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Set the navigation view listener
        navigationView.setNavigationItemSelectedListener(this);

        //Initialize the views
        inspirationalQuote = findViewById(R.id.text_quote);
        inspirationalAuthor = findViewById(R.id.text_quote_author);
    }

    private void initViewModel() {
        //Observer for the terms
        final Observer<List<Terms>> termsObserver = terms -> {
            termListData.clear();
            termListData.addAll(terms);
            if(termAdapter == null) {
                termAdapter = new TermAdapter(termListData, MainActivity.this, RecyclerAdapter.MAIN);
            } else {
                termAdapter.notifyDataSetChanged();
            }
        };
        //Observer for the courses
        final Observer<List<Courses>> coursesObserver = courses -> {
            courseListData.clear();
            courseListData.addAll(courses);
            alertHandler();
        };
        //Observer for the assessments
        final Observer<List<Assessments>> assessmentsObserver = assessments -> {
            assessmentListData.clear();
            assessmentListData.addAll(assessments);
            alertHandler();
        };
        //Observer for the mentors
        final Observer<List<Mentors>> mentorsObserver = mentors -> {
            mentorListData.clear();
            mentorListData.addAll(mentors);
        };

        //Initialize the view model
        homeModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(HomeModel.class);
    }

    /**
     * Set a daily quote
     *
     * Ideally this would be pulled from a database online that could be easily maintained, but for now it is just a random quote from an array
     * @param quote
     * @param author
     */
    private void setDailyQuote(TextView quote, TextView author) {
        //pull a random quote from an array, each item in the array contains the quote and the author separated by a comma
        String[] quotes = getResources().getStringArray(R.array.dailyInspiration);
        String[] quoteAndAuthor = quotes[(int) (Math.random() * quotes.length)].split(",");
        quote.setText(quoteAndAuthor[0]);
        author.setText(quoteAndAuthor[1]);
    }

    /**
     * Handle Alerts
     *
     */
    private void alertHandler() {
        ArrayList<String> alerts = new ArrayList<>();
        //loop through the courses to find reminders set for the start and end dates
        for(Courses course: courseListData) {
            if(DateUtils.isToday(course.getCourseStartDate().getTime())) {
                alerts.add("Course: " + course.getCourseName() + " begins today!");
            } else if(DateUtils.isToday(course.getCourseEndDate().getTime())) {
                alerts.add("Course: " + course.getCourseName() + " ends today!");
            }
        }

        //loop through the assessments to find reminders set for due dates
        for(Assessments assessment: assessmentListData) {
            if(DateUtils.isToday(assessment.getAssessmentDueDate().getTime())) {
                alerts.add("Assessment: " + assessment.getAssessmentName() + " is due today!");
            }
        }

        //Toast the alerts
        if(alerts.size() > 0) {
            for(String alert: alerts) {
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                Alerts alerting = new Alerts();
                IntentFilter filter = new IntentFilter("ALARM_ACTION");
                registerReceiver(alerting, filter);
                Intent intent = new Intent("ALARM_ACTION");
                intent.putExtra("param", alert);
                PendingIntent operation = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + Toast.LENGTH_SHORT, operation);
            }
        }
    }
}