package com.pbarnhardt.abm2task1.Views;

import static com.pbarnhardt.abm2task1.Utils.Constants.COURSE_KEY;
import static com.pbarnhardt.abm2task1.Utils.Constants.EDIT_KEY;
import static com.pbarnhardt.abm2task1.Utils.Constants.TERM_KEY;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.pbarnhardt.abm2task1.Entity.Terms;
import com.pbarnhardt.abm2task1.Enums.Status;
import com.pbarnhardt.abm2task1.Models.EditorModel;
import com.pbarnhardt.abm2task1.R;
import com.pbarnhardt.abm2task1.Utils.Formatting;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class CourseEditActivity extends AppCompatActivity {
    /**
     * Variables.
     */
    private boolean newCourse;
    private boolean edit;
    private int termId = -1;

    private EditorModel viewModel;
    private ArrayAdapter<Status> statusAdapter;

    /**
     * On create.
     *
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_edit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_action_check);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState != null) {
            edit = savedInstanceState.getBoolean(EDIT_KEY);
        }

        initiateViewModel();
        addStatusToSpinner();

        //set the onclick listener for the start date button
        final Button courseStart = findViewById(R.id.editText_start_date);
        courseStart.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener date = (startView, year, month, day) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                //update the button text with the selected date
                courseStart.setText(Formatting.dateFormat.format(calendar.getTime()));
            };
            new DatePickerDialog(CourseEditActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        //set the onclick listener for the end date button
        final Button courseEnd = findViewById(R.id.editText_end_date);
        courseEnd.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener date = (endView, year, month, day) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                //update the button text with the selected date
                courseEnd.setText(Formatting.dateFormat.format(calendar.getTime()));
            };
            new DatePickerDialog(CourseEditActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(EDIT_KEY, true);
        super.onSaveInstanceState(outState);
    }

    private Status getStatusFromSpinner() {
        final Spinner courseStatus = findViewById(R.id.spinner_course_status);
        return (Status) courseStatus.getSelectedItem();
    }

    private void addStatusToSpinner() {
        final Spinner courseStatus = findViewById(R.id.spinner_course_status);
        statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Status.values());
        courseStatus.setAdapter(statusAdapter);
    }

    private void initiateViewModel() {
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(EditorModel.class);
        final EditText courseTitle = findViewById(R.id.editText_course_title);
        final EditText courseDescription = findViewById(R.id.courseDetailDescription);
        final EditText courseNote = findViewById(R.id.editText_course_note);
        final Spinner courseStatus = findViewById(R.id.spinner_course_status);
        final Button courseStart = findViewById(R.id.editText_start_date);
        final Button courseEnd = findViewById(R.id.editText_end_date);
        final CheckBox courseStartAlert = findViewById(R.id.checkBox_start_date);
        final CheckBox courseEndAlert = findViewById(R.id.checkBox_end_date);
        viewModel.liveCourses.observe(this, courses -> {
            if (courses != null && !edit) {
                newCourse = false;
                courseTitle.setText(courses.getCourseName());
                courseDescription.setText(courses.getCourseDescription());
                courseNote.setText(courses.getCourseNote());
                courseStart.setText(Formatting.dateFormat.format(courses.getCourseStartDate()));
                courseEnd.setText(Formatting.dateFormat.format(courses.getCourseEndDate()));
                courseStartAlert.setChecked(courses.getCourseStartAlert());
                courseEndAlert.setChecked(courses.getCourseEndAlert());
                courseStatus.setSelection(statusAdapter.getPosition(courses.getCourseStatus()));
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            setTitle("New Course");
            newCourse = true;
        } else if (extras.containsKey(TERM_KEY)) {
            termId = extras.getInt(TERM_KEY);
            setTitle("New Course");
        } else {
            setTitle("Edit Course");
            int courseId = extras.getInt(COURSE_KEY);
            viewModel.loadCourse(courseId);
        }
    }

    @Override
    public void onBackPressed() {
        saveAndReturn();
    }

    private void saveAndReturn() {
        final EditText courseTitle = findViewById(R.id.editText_course_title);
        final EditText courseDescription = findViewById(R.id.courseDetailDescription);
        final EditText courseNote = findViewById(R.id.editText_course_note);
        final CheckBox courseStartAlert = findViewById(R.id.checkBox_start_date);
        final CheckBox courseEndAlert = findViewById(R.id.checkBox_end_date);
        final Button courseStart = findViewById(R.id.editText_start_date);
        final Button courseEnd = findViewById(R.id.editText_end_date);
        try {
            String courseName = courseTitle.getText().toString().trim();
            String courseDescriptionString = courseDescription.getText().toString().trim();
            String courseNoteString = courseNote.getText().toString().trim();
            boolean bCourseStartAlert = courseStartAlert.isChecked();
            boolean bCourseEndAlert = courseEndAlert.isChecked();
            Status courseStatus = getStatusFromSpinner();
            //validate the course start and end dates are within the assigned term start and end dates if a term is assigned
            if (termId != -1) {
                //get the term object from the database
                Terms assignedTerm = viewModel.getTermById(termId);
                //get the start and end dates from the term object
                Date termStartDate = assignedTerm.getTermStartDate();
                Date termEndDate = assignedTerm.getTermEndDate();
                //get the start and end dates from the course object
                Date courseStartDate = Formatting.dateFormat.parse(courseStart.getText().toString());
                Date courseEndDate = Formatting.dateFormat.parse(courseEnd.getText().toString());
                //check if the course start date or end date is before the term start date or after the term end date, if so throw a warning alert
                assert courseStartDate != null;
                assert courseEndDate != null;
                if (courseStartDate.before(termStartDate) || courseStartDate.after(termEndDate) || courseEndDate.before(termStartDate) || courseEndDate.after(termEndDate)) {
                    //build the alert and show it
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Warning");
                    builder.setMessage("The course start and/or end dates are not within the assigned term start and/or end dates.\nDo you want to continue?");
                    builder.setPositiveButton("Yes", (dialog, which) -> {
                        //if the user clicks yes, save the course and return to the previous activity
                        viewModel.saveCourse(courseName, courseDescriptionString, courseStartDate, courseEndDate, bCourseStartAlert, bCourseEndAlert, courseStatus, termId, courseNoteString);
                        finish();
                    });
                    builder.setNegativeButton("No", (dialog, which) -> {
                        //if the user clicks no, close the alert so they can fix the dates
                    });
                } else {
                    //if the course start and end dates are within the term start and end dates, save the course and return to the previous activity
                    viewModel.saveCourse(courseName, courseDescriptionString, courseStartDate, courseEndDate, bCourseStartAlert, bCourseEndAlert, courseStatus, termId, courseNoteString);
                    finish();
                }
            } else {
                //if no term is assigned, save the course and return to the previous activity
                Date courseStartDate = Formatting.dateFormat.parse(courseStart.getText().toString());
                Date courseEndDate = Formatting.dateFormat.parse(courseEnd.getText().toString());
                viewModel.saveCourse(courseName, courseDescriptionString, courseStartDate, courseEndDate, bCourseStartAlert, bCourseEndAlert, courseStatus, termId, courseNoteString);
                finish();
            }
        } catch (Exception e) {
            Log.v("Exception", Objects.requireNonNull(e.getLocalizedMessage()));
        }
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!newCourse) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_editor, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            saveAndReturn();
            return true;
        } else if (item.getItemId() == R.id.action_delete) {
            viewModel.deleteCourse();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
