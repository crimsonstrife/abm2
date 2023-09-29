package com.pbarnhardt.abm2task1.Views;

import static com.pbarnhardt.abm2task1.Utils.Constants.COURSE_DESCRIPTION;
import static com.pbarnhardt.abm2task1.Utils.Constants.COURSE_END_ALARM;
import static com.pbarnhardt.abm2task1.Utils.Constants.COURSE_END_DATE;
import static com.pbarnhardt.abm2task1.Utils.Constants.COURSE_KEY;
import static com.pbarnhardt.abm2task1.Utils.Constants.COURSE_NAME;
import static com.pbarnhardt.abm2task1.Utils.Constants.COURSE_NOTES;
import static com.pbarnhardt.abm2task1.Utils.Constants.COURSE_START_ALARM;
import static com.pbarnhardt.abm2task1.Utils.Constants.COURSE_START_DATE;
import static com.pbarnhardt.abm2task1.Utils.Constants.COURSE_STATUS;
import static com.pbarnhardt.abm2task1.Utils.Constants.COURSE_TERM_ID;
import static com.pbarnhardt.abm2task1.Utils.Constants.EDIT_KEY;
import static com.pbarnhardt.abm2task1.Utils.Constants.TERM_KEY;

import android.app.DatePickerDialog;
import android.content.Intent;
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
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.pbarnhardt.abm2task1.Entity.Terms;
import com.pbarnhardt.abm2task1.Enums.Status;
import com.pbarnhardt.abm2task1.Models.EditorModel;
import com.pbarnhardt.abm2task1.R;
import com.pbarnhardt.abm2task1.Utils.Converters;
import com.pbarnhardt.abm2task1.Utils.Formatting;
import com.pbarnhardt.abm2task1.databinding.ActivityCourseEditBinding;
import com.pbarnhardt.abm2task1.databinding.ContentEditCoursesBinding;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
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
    private EditText editCourseTitle;
    private EditText editCourseDescription;
    private EditText editCourseNote;
    private Button editCourseStartDate;
    private Button editCourseEndDate;
    private CheckBox editCourseStartAlert;
    private CheckBox editCourseEndAlert;
    private Spinner editCourseStatus;
    private String newCourseTitle;
    private String newCourseDescription;
    private String newCourseNote;
    private Date newCourseStartDate;
    private Date newCourseEndDate;
    private boolean newCourseStartAlert;
    private boolean newCourseEndAlert;
    private Status newCourseStatus;
    private String currentDate = Formatting.dateFormat.format(Calendar.getInstance(Locale.getDefault()).getTime());
    private String endDate;

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
        editCourseTitle = contentBinding.editTextCourseTitle;
        editCourseDescription = contentBinding.courseDetailDescription;
        editCourseNote = contentBinding.editTextCourseNote;
        editCourseStartDate = contentBinding.editTextStartDate;
        editCourseEndDate = contentBinding.editTextEndDate;
        editCourseStartAlert = contentBinding.checkBoxStartDate;
        editCourseEndAlert = contentBinding.checkBoxEndDate;
        editCourseStatus = contentBinding.spinnerCourseStatus;

        //retrieve and set the existing information if there is any
        courseId = getIntent().getIntExtra(COURSE_KEY, -1);
        newCourseTitle = getIntent().getStringExtra(COURSE_NAME);
        newCourseDescription = getIntent().getStringExtra(COURSE_DESCRIPTION);
        newCourseNote = getIntent().getStringExtra(COURSE_NOTES);
        if (TERM_KEY != null) {
            termId = getIntent().getIntExtra(COURSE_TERM_ID, -1);
        }
        String startDateString = getIntent().getStringExtra(COURSE_START_DATE);
        String endDateString = getIntent().getStringExtra(COURSE_END_DATE);
        //if the start and end dates are not null, convert them to date objects
        if (startDateString != null && endDateString != null) {
            try {
                newCourseStartDate = Formatting.dateFormat.parse(startDateString);
                newCourseEndDate = Formatting.dateFormat.parse(endDateString);
            } catch (Exception e) {
                Log.v("Exception", Objects.requireNonNull(e.getLocalizedMessage()));
            }
        } else {
            newCourseStartDate = null;
            newCourseEndDate = null;
        }
        newCourseStartAlert = getIntent().getBooleanExtra(COURSE_START_ALARM, false);
        newCourseEndAlert = getIntent().getBooleanExtra(COURSE_END_ALARM, false);

        //if the COURSE_STATUS is null, then check the status of the course by course ID
        if (COURSE_KEY != null) {
            newCourseStatus = Converters.fromStringToCourseStatus(getIntent().getStringExtra(COURSE_STATUS));
        } else {
            courseId = getIntent().getIntExtra(COURSE_KEY, -1);
            if (courseId != -1) {
                newCourseStatus = viewModel.getCourseById(courseId).getCourseStatus();
            } else {
                newCourseStatus = Status.PLANNED;
            }
        }

        //calculate the end date for the course in case this is a new course
        currentDate = Formatting.dateFormat.format(Calendar.getInstance(Locale.getDefault()).getTime());
        //increment the current date by 1 month
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        endDate = Formatting.dateFormat.format(calendar.getTime());

        //if the new course start date is null, set it to the current date
        if (newCourseStartDate == null) {
            newCourseStartDate = Calendar.getInstance(Locale.getDefault()).getTime();
        }
        //if the new course end date is null, set it to the end date
        if (newCourseEndDate == null) {
            calendar.add(Calendar.MONTH, 1);
            newCourseEndDate = calendar.getTime();
        }

        //initiate the view model
        initiateViewModel();
        addStatusToSpinner(editCourseStatus);

        //set the existing information if there is any
        if (courseId != -1) {
            editCourseTitle.setText(newCourseTitle);
            editCourseDescription.setText(newCourseDescription);
            editCourseNote.setText(newCourseNote);
            editCourseStartDate.setText(Formatting.dateFormat.format(newCourseStartDate));
            editCourseEndDate.setText(Formatting.dateFormat.format(newCourseEndDate));
            editCourseStartAlert.setChecked(newCourseStartAlert);
            editCourseEndAlert.setChecked(newCourseEndAlert);
            editCourseStatus.setSelection(statusAdapter.getPosition(newCourseStatus));
        } else if (COURSE_KEY != null) {
            editCourseTitle.setText(newCourseTitle);
            editCourseDescription.setText(newCourseDescription);
            editCourseNote.setText(newCourseNote);
            editCourseStartDate.setText(Formatting.dateFormat.format(newCourseStartDate));
            editCourseEndDate.setText(Formatting.dateFormat.format(newCourseEndDate));
            editCourseStartAlert.setChecked(newCourseStartAlert);
            editCourseEndAlert.setChecked(newCourseEndAlert);
            editCourseStatus.setSelection(statusAdapter.getPosition(newCourseStatus));
        } else {
            editCourseTitle.setText("");
            editCourseDescription.setText("");
            editCourseNote.setText("");
            editCourseStartDate.setText(currentDate);
            editCourseEndDate.setText(endDate);
            editCourseStartAlert.setChecked(false);
            editCourseEndAlert.setChecked(false);
            editCourseStatus.setSelection(0);
        }

        //set the onclick listener for the start date button
        contentBinding.editTextStartDate.setOnClickListener(v -> {
            final Calendar calendar1 = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener date = (startView, year, month, day) -> {
                calendar1.set(Calendar.YEAR, year);
                calendar1.set(Calendar.MONTH, month);
                calendar1.set(Calendar.DAY_OF_MONTH, day);
                //update the button text with the selected date
                editCourseStartDate.setText(Formatting.dateFormat.format(calendar1.getTime()));
                //set the hint to the selected date
                editCourseStartDate.setHint(Formatting.dateFormat.format(calendar1.getTime()));
            };
            new DatePickerDialog(CourseEditActivity.this, date, calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH)).show();
        });

        //set the onclick listener for the end date button
        contentBinding.editTextEndDate.setOnClickListener(v -> {
            final Calendar calendar2 = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener date = (endView, year, month, day) -> {
                calendar2.set(Calendar.YEAR, year);
                calendar2.set(Calendar.MONTH, month);
                calendar2.set(Calendar.DAY_OF_MONTH, day);
                //update the button text with the selected date
                editCourseEndDate.setText(Formatting.dateFormat.format(calendar2.getTime()));
                //update the button hint
                editCourseEndDate.setHint(Formatting.dateFormat.format(calendar2.getTime()));
            };
            new DatePickerDialog(CourseEditActivity.this, date, calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DAY_OF_MONTH)).show();
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
                editCourseTitle.setText(newCourseTitle);
                editCourseDescription.setText(newCourseDescription);
                editCourseNote.setText(newCourseNote);
                editCourseStartDate.setText(Formatting.dateFormat.format(newCourseStartDate));
                editCourseEndDate.setText(Formatting.dateFormat.format(newCourseEndDate));
                editCourseStartAlert.setChecked(newCourseStartAlert);
                editCourseEndAlert.setChecked(newCourseEndAlert);
                editCourseStatus.setSelection(statusAdapter.getPosition(newCourseStatus));
            } else {
                newCourse = true;
                editCourseTitle.setText("");
                editCourseDescription.setText("");
                editCourseNote.setText("");
                editCourseStartDate.setText(Formatting.dateFormat.format(currentDate));
                editCourseEndDate.setText(Formatting.dateFormat.format(endDate));
                editCourseStartAlert.setChecked(false);
                editCourseEndAlert.setChecked(false);
                editCourseStatus.setSelection(0);
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
            String courseNameString = editCourseTitle.getText().toString().trim();
            String courseDescriptionString = editCourseDescription.getText().toString().trim();
            String courseNoteString = editCourseNote.getText().toString().trim();
            boolean bCourseStartAlert = editCourseStartAlert.isChecked();
            boolean bCourseEndAlert = editCourseEndAlert.isChecked();
            Status courseStatusValue = getStatusFromSpinner(editCourseStatus);
            //validate the course start and end dates are within the assigned term start and end dates if a term is assigned
            if (termId != -1) {
                //get the term object from the database
                Terms assignedTerm = viewModel.getTermById(termId);
                //get the start and end dates from the term object
                Date termStartDate = assignedTerm.getTermStartDate();
                Date termEndDate = assignedTerm.getTermEndDate();
                //get the start and end dates from the course object
                Date courseStartDateValue = editCourseStartDate.getText().toString().isEmpty() ? null : Formatting.dateFormat.parse(editCourseStartDate.getText().toString());
                Date courseEndDateValue = editCourseEndDate.getText().toString().isEmpty() ? null : Formatting.dateFormat.parse(editCourseEndDate.getText().toString());
                if (Objects.requireNonNull(courseStartDateValue).before(termStartDate) || Objects.requireNonNull(courseStartDateValue).after(termEndDate) || Objects.requireNonNull(courseEndDateValue).before(termStartDate) || Objects.requireNonNull(courseEndDateValue).after(termEndDate)) {
                    //build the alert and show it
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Warning");
                    builder.setMessage("The course start and/or end dates are not within the assigned term start and/or end dates.\nDo you want to continue?");
                    builder.setPositiveButton("Yes", (dialog, which) -> {
                        //if the user clicks yes, save the course and return to the previous activity
                        viewModel.saveCourse(courseNameString, courseDescriptionString, courseStartDateValue, courseEndDateValue, bCourseStartAlert, bCourseEndAlert, courseStatusValue, termId, courseNoteString);
                        //notify the user that the course was saved
                        Toast.makeText(CourseEditActivity.this, "Course Saved", Toast.LENGTH_SHORT).show();
                        //navigate back to the course list
                        Intent intent = new Intent(CourseEditActivity.this, CoursesListActivity.class);
                        startActivity(intent);
                        finish();
                    });
                    builder.setNegativeButton("No", (dialog, which) -> {
                        //if the user clicks no, close the alert so they can fix the dates
                    });
                } else {
                    //if the course start and end dates are within the term start and end dates, save the course and return to the previous activity
                    viewModel.saveCourse(courseNameString, courseDescriptionString, courseStartDateValue, courseEndDateValue, bCourseStartAlert, bCourseEndAlert, courseStatusValue, termId, courseNoteString);
                    //notify the user that the course was saved
                    Toast.makeText(CourseEditActivity.this, "Course Saved", Toast.LENGTH_SHORT).show();
                    //navigate back to the course list
                    Intent intent = new Intent(CourseEditActivity.this, CoursesListActivity.class);
                    startActivity(intent);
                    finish();
                }
            } else {
                //if no term is assigned, save the course and return to the previous activity
                Date courseStartDateValue = editCourseStartDate.getText().toString().isEmpty() ? null : Formatting.dateFormat.parse(editCourseStartDate.getText().toString());
                Date courseEndDateValue = editCourseEndDate.getText().toString().isEmpty() ? null : Formatting.dateFormat.parse(editCourseEndDate.getText().toString());
                viewModel.saveCourse(courseNameString, courseDescriptionString, courseStartDateValue, courseEndDateValue, bCourseStartAlert, bCourseEndAlert, courseStatusValue, termId, courseNoteString);
                //notify the user that the course was saved
                Toast.makeText(CourseEditActivity.this, "Course Saved", Toast.LENGTH_SHORT).show();
                //navigate back to the course list
                Intent intent = new Intent(CourseEditActivity.this, CoursesListActivity.class);
                startActivity(intent);
                finish();
            }
        } catch (Exception e) {
            Log.v("Exception", Objects.requireNonNull(e.getLocalizedMessage()));
            //notify the user that there was an error saving the course
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Error");
            builder.setMessage("There was an error saving the course.  Please try again.");
            builder.setPositiveButton("OK", (dialog, which) -> {
                //if the user clicks ok, close the alert
            });
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
