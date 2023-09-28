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
import android.view.View;
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
import com.pbarnhardt.abm2task1.databinding.ActivityCourseEditBinding;
import com.pbarnhardt.abm2task1.databinding.ContentEditCoursesBinding;

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
    int courseId;
    private EditorModel viewModel;
    private ArrayAdapter<Status> statusAdapter;
    private ActivityCourseEditBinding activityBinding;
    private ContentEditCoursesBinding contentBinding;
    private EditText courseTitle;
    private EditText courseDescription;
    private EditText courseNote;
    private Button courseStartDate;
    private Button courseEndDate;
    private CheckBox courseStartAlert;
    private CheckBox courseEndAlert;
    private Spinner courseStatus;

    /**
     * On create.
     *
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = ActivityCourseEditBinding.inflate(getLayoutInflater());
        View view = activityBinding.getRoot();
        setContentView(view);
        Toolbar toolbar = activityBinding.toolbar;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_action_check);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //check if the activity is being created for a new course or an existing course
        if (savedInstanceState != null) {
            edit = savedInstanceState.getBoolean(EDIT_KEY);
        }

        //initiate the binding
        contentBinding = activityBinding.contentInclude;
        courseTitle = contentBinding.editTextCourseTitle;
        courseDescription = contentBinding.courseDetailDescription;
        courseNote = contentBinding.editTextCourseNote;
        courseStartDate = contentBinding.editTextStartDate;
        courseEndDate = contentBinding.editTextEndDate;
        courseStartAlert = contentBinding.checkBoxStartDate;
        courseEndAlert = contentBinding.checkBoxEndDate;
        courseStatus = contentBinding.spinnerCourseStatus;

        //initiate the view model
        initiateViewModel();
        addStatusToSpinner(courseStatus);

        //set the onclick listener for the start date button
        contentBinding.editTextStartDate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener date = (startView, year, month, day) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                //update the button text with the selected date
                courseStartDate.setText(Formatting.dateFormat.format(calendar.getTime()));
                //set the hint to the selected date
                courseStartDate.setHint(Formatting.dateFormat.format(calendar.getTime()));
            };
            new DatePickerDialog(CourseEditActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        //set the onclick listener for the end date button
        contentBinding.editTextEndDate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener date = (endView, year, month, day) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                //update the button text with the selected date
                courseEndDate.setText(Formatting.dateFormat.format(calendar.getTime()));
                //update the button hint
                courseEndDate.setHint(Formatting.dateFormat.format(calendar.getTime()));
            };
            new DatePickerDialog(CourseEditActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(EDIT_KEY, true);
        super.onSaveInstanceState(outState);
    }

    private Status getStatusFromSpinner(Spinner spinner) {
        return (Status) spinner.getSelectedItem();
    }

    private void addStatusToSpinner(Spinner spinner) {
        statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Status.values());
        spinner.setAdapter(statusAdapter);
    }

    private void initiateViewModel() {
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(EditorModel.class);
        viewModel.liveCourses.observe(this, courses -> {
            if (courses != null && !edit) {
                newCourse = false;
                courseTitle.setText(courses.getCourseName());
                courseDescription.setText(courses.getCourseDescription());
                courseNote.setText(courses.getCourseNote());
                courseStartDate.setText(Formatting.dateFormat.format(courses.getCourseStartDate()));
                courseEndDate.setText(Formatting.dateFormat.format(courses.getCourseEndDate()));
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
            courseId = extras.getInt(COURSE_KEY);
            viewModel.loadCourse(courseId);
        }
    }

    @Override
    public void onBackPressed() {
        saveAndReturn();
    }

    private void saveAndReturn() {
        try {
            String courseNameString = courseTitle.getText().toString().trim();
            String courseDescriptionString = courseDescription.getText().toString().trim();
            String courseNoteString = courseNote.getText().toString().trim();
            boolean bCourseStartAlert = courseStartAlert.isChecked();
            boolean bCourseEndAlert = courseEndAlert.isChecked();
            Status courseStatusValue = getStatusFromSpinner(courseStatus);
            //validate the course start and end dates are within the assigned term start and end dates if a term is assigned
            if (termId != -1) {
                //get the term object from the database
                Terms assignedTerm = viewModel.getTermById(termId);
                //get the start and end dates from the term object
                Date termStartDate = assignedTerm.getTermStartDate();
                Date termEndDate = assignedTerm.getTermEndDate();
                //get the start and end dates from the course object
                Date courseStartDateValue = courseStartDate.getText().toString().isEmpty() ? null : Formatting.dateFormat.parse(courseStartDate.getText().toString());
                Date courseEndDateValue = courseEndDate.getText().toString().isEmpty() ? null : Formatting.dateFormat.parse(courseEndDate.getText().toString());
                if (courseStartDateValue.before(termStartDate) || courseStartDateValue.after(termEndDate) || courseEndDateValue.before(termStartDate) || courseEndDateValue.after(termEndDate)) {
                    //build the alert and show it
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Warning");
                    builder.setMessage("The course start and/or end dates are not within the assigned term start and/or end dates.\nDo you want to continue?");
                    builder.setPositiveButton("Yes", (dialog, which) -> {
                        //if the user clicks yes, save the course and return to the previous activity
                        viewModel.saveCourse(courseNameString, courseDescriptionString, courseStartDateValue, courseEndDateValue, bCourseStartAlert, bCourseEndAlert, courseStatusValue, termId, courseNoteString);
                        finish();
                    });
                    builder.setNegativeButton("No", (dialog, which) -> {
                        //if the user clicks no, close the alert so they can fix the dates
                    });
                } else {
                    //if the course start and end dates are within the term start and end dates, save the course and return to the previous activity
                    viewModel.saveCourse(courseNameString, courseDescriptionString, courseStartDateValue, courseEndDateValue, bCourseStartAlert, bCourseEndAlert, courseStatusValue, termId, courseNoteString);
                    finish();
                }
            } else {
                //if no term is assigned, save the course and return to the previous activity
                Date courseStartDateValue = courseStartDate.getText().toString().isEmpty() ? null : Formatting.dateFormat.parse(courseStartDate.getText().toString());
                Date courseEndDateValue = courseEndDate.getText().toString().isEmpty() ? null : Formatting.dateFormat.parse(courseEndDate.getText().toString());
                viewModel.saveCourse(courseNameString, courseDescriptionString, courseStartDateValue, courseEndDateValue, bCourseStartAlert, bCourseEndAlert, courseStatusValue, termId, courseNoteString);
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
        } else if (item.getItemId() == R.id.action_help) {
            //TODO: add help
        } else if (item.getItemId() == R.id.action_save) {
            saveAndReturn();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
