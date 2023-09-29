package com.pbarnhardt.abm2task1.Views;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
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
import com.pbarnhardt.abm2task1.Models.EditorModel;
import com.pbarnhardt.abm2task1.Models.HomeModel;
import com.pbarnhardt.abm2task1.R;
import com.pbarnhardt.abm2task1.Utils.Alerts;
import com.pbarnhardt.abm2task1.databinding.ActivityMainBinding;
import com.pbarnhardt.abm2task1.databinding.ContentHomeBinding;

import java.util.ArrayList;
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
    private EditorModel editorModel;
    private ActivityMainBinding activityBinding;
    private ContentHomeBinding contentBinding;

    /**
     * Variables
     */
    private final List<Terms> termListData = new ArrayList<>();
    private final List<Courses> courseListData = new ArrayList<>();
    private final List<Assessments> assessmentListData = new ArrayList<>();
    /** @noinspection MismatchedQueryAndUpdateOfCollection*/
    private final List<Mentors> mentorListData = new ArrayList<>();


    /**
     * On create
     *
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = activityBinding.getRoot();
        setContentView(view);
        Toolbar toolbar = activityBinding.toolbarInclude.toolbar;
        setSupportActionBar(toolbar);
        //set toolbar color
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        drawerLayout = activityBinding.drawerLayout;
        navigationView = activityBinding.navigationView;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_navigation, R.string.close_navigation);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Set the navigation view listener
        navigationView.setNavigationItemSelectedListener(this);

        initializeViewModel();

        //Initialize the binding
        contentBinding = activityBinding.toolbarInclude.contentInclude;

        //Set the daily quote
        //pull a random quote from an array, each item in the array contains the quote and the author separated by a comma
        String[] quotes = getResources().getStringArray(R.array.dailyInspiration);
        String[] quoteAndAuthor = quotes[(int) (Math.random() * quotes.length)].split(",");
        contentBinding.textQuote.setText(quoteAndAuthor[0]);
        contentBinding.textQuoteAuthor.setText(quoteAndAuthor[1]);

        //Set the on click listeners for the buttons
        contentBinding.btnTerms.setOnClickListener(v -> {
            Intent intent = new Intent(this, TermsListActivity.class);
            startActivity(intent);
        });
        contentBinding.btnCourses.setOnClickListener(v -> {
            Intent intent = new Intent(this, CoursesListActivity.class);
            startActivity(intent);
        });
        contentBinding.btnAssessments.setOnClickListener(v -> {
            Intent intent = new Intent(this, AssessmentsListActivity.class);
            startActivity(intent);
        });
        contentBinding.btnMentors.setOnClickListener(v -> {
            Intent intent = new Intent(this, MentorsListActivity.class);
            startActivity(intent);
        });
    }

    private void initializeViewModel() {
        //Observer for the terms
        final Observer<List<Terms>> termsObserver = terms -> {
            termListData.clear();
            termListData.addAll(terms);
            if(termAdapter == null) {
                termAdapter = new TermAdapter(termListData, MainActivity.this, RecyclerAdapter.MAIN);
            } else {
                termAdapter.notifyItemRangeChanged(0, termAdapter.getItemCount());
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
     * Handle Alerts
     *
     */
    private void alertHandler() {
        ArrayList<String> alerts = new ArrayList<>();
        boolean courseStartAlert = false;
        boolean courseEndAlert = false;
        boolean assessmentAlert = false;
        ArrayList<Courses> coursesWithAlerts = new ArrayList<>();
        ArrayList<Assessments> assessmentsWithAlerts = new ArrayList<>();
        //loop through the courses to find reminders set for the start and end dates
        for(Courses course: courseListData) {
            if(DateUtils.isToday(course.getCourseStartDate().getTime())) {
                //is the alert set for the start date?
                if(course.getCourseStartAlert()) {
                    courseStartAlert = true;
                    //add the course to the list of courses with alerts if it is not there
                    if(!coursesWithAlerts.contains(course)) {
                        coursesWithAlerts.add(course);
                    }
                    alerts.add("Course: " + course.getCourseName() + " begins today!");
                } else {
                    //make sure the alert is set to false
                    courseStartAlert = false;
                    //remove the course from the list of courses with alerts if it is there and the end date alert is not set
                    if(coursesWithAlerts.contains(course) && !courseEndAlert) {
                        coursesWithAlerts.remove(course);
                    }
                }
            } else if(DateUtils.isToday(course.getCourseEndDate().getTime())) {
                //is the alert set for the end date?
                if(course.getCourseEndAlert()) {
                    courseEndAlert = true;
                    //add the course to the list of courses with alerts if it is not there
                    if(!coursesWithAlerts.contains(course)) {
                        coursesWithAlerts.add(course);
                    }
                    alerts.add("Course: " + course.getCourseName() + " ends today!");
                } else {
                    //make sure the alert is set to false
                    courseEndAlert = false;
                    //remove the course from the list of courses with alerts if it is there and the start date alert is not set
                    if(coursesWithAlerts.contains(course) && !courseStartAlert) {
                        coursesWithAlerts.remove(course);
                    }
                }
            }
        }

        //loop through the assessments to find reminders set for due dates
        for(Assessments assessment: assessmentListData) {
            if(DateUtils.isToday(assessment.getAssessmentDueDate().getTime())) {
                //is the alert set for the due date?
                if(assessment.getAssessmentAlert()) {
                    assessmentAlert = true;
                    //add the assessment to the list of assessments with alerts if it is not there
                    if(!assessmentsWithAlerts.contains(assessment)) {
                        assessmentsWithAlerts.add(assessment);
                    }
                    alerts.add("Assessment: " + assessment.getAssessmentName() + " is due today!");
                } else {
                    //make sure the alert is set to false
                    assessmentAlert = false;
                    //remove the assessment from the list of assessments with alerts if it is there
                    if(assessmentsWithAlerts.contains(assessment)) {
                        assessmentsWithAlerts.remove(assessment);
                    }
                }
            }
        }

        //Toast the alerts
        if(alerts.size() > 0) {
            for(String alert: alerts) {
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                Alerts alerting = new Alerts();
                IntentFilter filter = new IntentFilter("ALARM_ACTION");
                registerReceiver(alerting, filter, Context.RECEIVER_NOT_EXPORTED);
                Intent intent = new Intent("ALARM_ACTION");
                intent.putExtra("param", alert);
                PendingIntent operation = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + Toast.LENGTH_SHORT, operation);

                //for each course with an alert, set the alert to false and remove it from the list of courses with alerts - then update the database
                for(Courses course: coursesWithAlerts) {
                    //if the alert contains the course name, set the alert to false and remove it from the list of courses with alerts
                    if(alert.contains(course.getCourseName())) {
                        //check if the alert is for the start date or the end date
                        if(alert.contains("begins")) {
                            course.setCourseStartAlert(false);
                            //update the database
                            //editorModel.overwriteCourse(course, course.getTermId());
                            //remove the course from the list of courses with alerts if the end date alert is not set
                            if(!course.getCourseEndAlert()) {
                                coursesWithAlerts.remove(course);
                            }
                        } else {
                            course.setCourseEndAlert(false);
                            //update the database
                            //editorModel.overwriteCourse(course, course.getTermId());
                            //remove the course from the list of courses with alerts if the start date alert is not set
                            if(!course.getCourseStartAlert()) {
                                coursesWithAlerts.remove(course);
                            }
                        }
                    }
                }

                //for each assessment with an alert, set the alert to false and remove it from the list of assessments with alerts - then update the database
                for(Assessments assessment: assessmentsWithAlerts) {
                    //if the alert contains the assessment name, set the alert to false and remove it from the list of assessments with alerts
                    if (alert.contains(assessment.getAssessmentName())) {
                        assessment.setAssessmentAlert(false);
                        //update the database
                        //editorModel.overwriteAssessment(assessment, assessment.getCourseId());
                        //remove the assessment from the list of assessments with alerts
                        assessmentsWithAlerts.remove(assessment);
                    }
                }
            }
        } else {
            //these are only day-of notifications, if there are no alerts, clear any existing notifications as they are no longer needed
            //this should mostly be garbage data if there was any at all
            //clear any stored courses with alerts
            coursesWithAlerts.clear();
            //clear any stored assessments with alerts
            assessmentsWithAlerts.clear();
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
            //show an alert dialog to confirm the delete
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete All Data");
            builder.setMessage("Are you sure you want to delete all data?");
            builder.setIcon(ContextCompat.getDrawable(this, android.R.drawable.ic_dialog_alert));
            builder.setPositiveButton("Yes", (dialog, id1) -> {
                deleteAllData();
                dialog.dismiss();
            });
            builder.setNegativeButton("Cancel", (dialog, id12) -> dialog.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();
        } else if(id == addSampleDataId) {
            //show an alert dialog to confirm the delete
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add Sample Data");
            builder.setMessage("Are you sure you want to add sample data? \nThis may conflict with existing data or duplicate data.");
            builder.setIcon(ContextCompat.getDrawable(this, android.R.drawable.ic_dialog_alert));
            builder.setPositiveButton("Yes", (dialog, id13) -> {
                addSampleData();
                dialog.dismiss();
            });
            builder.setNegativeButton("Cancel", (dialog, id14) -> dialog.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();
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
        boolean success = homeModel.addSampleDataset();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Sample Data");
        if(!success) {
            builder.setMessage("Sample data has been added");
            builder.setIcon(ContextCompat.getDrawable(this, android.R.drawable.ic_dialog_info));
            builder.setPositiveButton("OK", (dialog, id) -> dialog.dismiss());
        } else {
            builder.setMessage("There was an error adding sample data");
            builder.setIcon(ContextCompat.getDrawable(this, android.R.drawable.ic_dialog_alert));
            builder.setPositiveButton("OK", (dialog, id) -> dialog.dismiss());
        }
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Delete all data.
     */
    private void deleteAllData() {
        boolean success = homeModel.deleteAllData();
        //log the value of success for debugging
        Log.v("DEBUG", "Delete all data success: " + success);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete All Data");
        if(!success) {
            builder.setMessage("All data has been deleted");
            builder.setIcon(ContextCompat.getDrawable(this, android.R.drawable.ic_dialog_info));
            builder.setPositiveButton("OK", (dialog, id) -> dialog.dismiss());
        } else {
            builder.setMessage("There was an error deleting data");
            builder.setIcon(ContextCompat.getDrawable(this, android.R.drawable.ic_dialog_alert));
            builder.setPositiveButton("OK", (dialog, id) -> dialog.dismiss());
        }
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}