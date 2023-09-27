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

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
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

        initializeViewModel();
        //Initialize the views
        inspirationalQuote = findViewById(R.id.text_quote);
        inspirationalAuthor = findViewById(R.id.text_quote_author);
        setDailyQuote(inspirationalQuote, inspirationalAuthor);
    }

    private void initializeViewModel() {
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
        homeModel.termsList.observe(this, termsObserver);
        homeModel.coursesList.observe(this, coursesObserver);
        homeModel.assessmentsList.observe(this, assessmentsObserver);
        homeModel.mentorsList.observe(this, mentorsObserver);
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

    /**
     * On navigation item selected boolean.
     *
     * @param item the menu item
     * @return the boolean
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        int termsNavId = R.id.nav_terms;
        int coursesNavId = R.id.nav_course;
        int assessmentsNavId = R.id.nav_assessments;
        int mentorsNavId = R.id.nav_mentors;
        if(itemId == termsNavId) {
            Intent intent = new Intent(this, TermsListActivity.class);
            startActivity(intent);
        } else if(itemId == coursesNavId) {
            Intent intent = new Intent(this, CoursesListActivity.class);
            startActivity(intent);
        } else if(itemId == assessmentsNavId) {
            Intent intent = new Intent(this, AssessmentsListActivity.class);
            startActivity(intent);
        } else if(itemId == mentorsNavId) {
            Intent intent = new Intent(this, MentorsListActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * On create options menu boolean.
     *
     * @param menu the menu
     * @return the boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu, add items to the action bar if it is present in the layout
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * On options item selected boolean.
     *
     * @param item the menu item
     * @return the boolean
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        int deleteAllDataId = R.id.action_deleteAll;
        int addSampleDataId = R.id.action_add_sampleDataSet;
        int settingsId = R.id.action_settings;
        int aboutId = R.id.action_about;
        int helpId = R.id.action_help;
        if(id == deleteAllDataId) {
            deleteAllData();
        } else if(id == addSampleDataId) {
            addSampleData();
        } else if(id == settingsId) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else if(id == aboutId) {
            Intent intent = new Intent(this, InfoActivity.class);
            startActivity(intent);
        } else if(id == helpId) {
            Intent intent = new Intent(this, HelpActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Add sample data.
     */
    private void addSampleData() {
        homeModel.addSampleDataset();
    }

    /**
     * Delete all data.
     */
    private void deleteAllData() {
        homeModel.deleteAllData();
    }

    /**
     * Show terms.
     *
     * @param view the view
     */
    public void showTerms(View view) {
        Intent intent = new Intent(this, TermsListActivity.class);
        startActivity(intent);
    }

    /**
     * Show courses.
     *
     * @param view the view
     */
    public void showCourses(View view) {
        Intent intent = new Intent(this, CoursesListActivity.class);
        startActivity(intent);
    }

    /**
     * Show assessments.
     *
     * @param view the view
     */
    public void showAssessments(View view) {
        Intent intent = new Intent(this, AssessmentsListActivity.class);
        startActivity(intent);
    }

    /**
     * Show mentors.
     *
     * @param view the view
     */
    public void showMentors(View view) {
        Intent intent = new Intent(this, MentorsListActivity.class);
        startActivity(intent);
    }
}