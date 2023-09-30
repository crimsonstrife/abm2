package com.pbarnhardt.abm2task1.Views;

import static com.pbarnhardt.abm2task1.Utils.Constants.CHANNEL_ID;
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
import static com.pbarnhardt.abm2task1.Utils.Constants.IMPORTANCE;
import static com.pbarnhardt.abm2task1.Utils.Constants.NOTIFICATION;
import static com.pbarnhardt.abm2task1.Utils.Constants.PENDING_INTENT;
import static com.pbarnhardt.abm2task1.Utils.Constants.SUBJECT;
import static com.pbarnhardt.abm2task1.Utils.Constants.TERM_KEY;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.pbarnhardt.abm2task1.Adapters.CourseAdapter;
import com.pbarnhardt.abm2task1.Entity.Assessments;
import com.pbarnhardt.abm2task1.Entity.Mentors;
import com.pbarnhardt.abm2task1.Entity.Terms;
import com.pbarnhardt.abm2task1.Enums.Status;
import com.pbarnhardt.abm2task1.Models.EditorModel;
import com.pbarnhardt.abm2task1.R;
import com.pbarnhardt.abm2task1.Utils.Alerts;
import com.pbarnhardt.abm2task1.Utils.Converters;
import com.pbarnhardt.abm2task1.Utils.Formatting;
import com.pbarnhardt.abm2task1.databinding.ActivityCourseEditBinding;
import com.pbarnhardt.abm2task1.databinding.ContentEditCoursesBinding;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
    private TextView courseStartDateView;
    private TextView courseEndDateView;
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
    private CourseAdapter adapter;

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

        //try to get the coursekey from the intent
        try {
            courseId = getIntent().getIntExtra(COURSE_KEY, -1);
        } catch (Exception e) {
            Log.v("Exception", Objects.requireNonNull(e.getLocalizedMessage()));
        }

        //initiate the binding
        contentBinding = activityBinding.contentInclude;
        editCourseTitle = contentBinding.editTextCourseTitle;
        editCourseDescription = contentBinding.courseDetailDescription;
        editCourseNote = contentBinding.editTextCourseNote;
        editCourseStartDate = contentBinding.editTextStartDate;
        editCourseEndDate = contentBinding.editTextEndDate;
        courseStartDateView = contentBinding.courseDetailStartDate;
        courseEndDateView = contentBinding.courseDetailEndDate;
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
            //set to the current date if the start and end dates are null
            newCourseStartDate = Calendar.getInstance(Locale.getDefault()).getTime();
            newCourseEndDate = Calendar.getInstance(Locale.getDefault()).getTime();
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
            editCourseStartDate.setText(Formatting.dateFormat.format(viewModel.getCourseById(courseId).getCourseStartDate()));
            courseStartDateView.setText(Formatting.dateFormat.format(viewModel.getCourseById(courseId).getCourseStartDate()));
            editCourseEndDate.setText(Formatting.dateFormat.format(viewModel.getCourseById(courseId).getCourseEndDate()));
            courseEndDateView.setText(Formatting.dateFormat.format(viewModel.getCourseById(courseId).getCourseEndDate()));
            editCourseStartAlert.setChecked(newCourseStartAlert);
            editCourseEndAlert.setChecked(newCourseEndAlert);
            editCourseStatus.setSelection(statusAdapter.getPosition(newCourseStatus));
        } else if (COURSE_KEY != null) {
            editCourseTitle.setText(newCourseTitle);
            editCourseDescription.setText(newCourseDescription);
            editCourseNote.setText(newCourseNote);
            editCourseStartDate.setText(getIntent().getStringExtra(COURSE_START_DATE));
            courseStartDateView.setText(getIntent().getStringExtra(COURSE_START_DATE));
            editCourseEndDate.setText(getIntent().getStringExtra(COURSE_START_DATE));
            courseEndDateView.setText(getIntent().getStringExtra(COURSE_START_DATE));
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
                courseStartDateView.setText(Formatting.dateFormat.format(calendar1.getTime()));
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
                courseEndDateView.setText(Formatting.dateFormat.format(calendar2.getTime()));
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
                editCourseStartDate.setText(Formatting.dateFormat.format(viewModel.getCourseById(courseId).getCourseStartDate()));
                courseStartDateView.setText(Formatting.dateFormat.format(viewModel.getCourseById(courseId).getCourseStartDate()));
                editCourseEndDate.setText(Formatting.dateFormat.format(viewModel.getCourseById(courseId).getCourseEndDate()));
                courseEndDateView.setText(Formatting.dateFormat.format(viewModel.getCourseById(courseId).getCourseEndDate()));
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
                courseStartDateView.setText(Formatting.dateFormat.format(currentDate));
                courseEndDateView.setText(Formatting.dateFormat.format(endDate));
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
                        finish();
                    });
                    builder.setNegativeButton("No", (dialog, which) -> {
                        //if the user clicks no, close the alert so they can fix the dates
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    //if the course start and end dates are within the term start and end dates, save the course and return to the previous activity
                    viewModel.saveCourse(courseNameString, courseDescriptionString, courseStartDateValue, courseEndDateValue, bCourseStartAlert, bCourseEndAlert, courseStatusValue, termId, courseNoteString);
                    //notify the user that the course was saved
                    Toast.makeText(CourseEditActivity.this, "Course Saved", Toast.LENGTH_SHORT).show();
                    //navigate back to the course list
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
            //ask the user if they want to save before exiting
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to save?");
            builder.setIcon(AppCompatResources.getDrawable(this, R.drawable.ic_question_mark));
            builder.setPositiveButton("Yes", (dialog, id) -> {
                dialog.dismiss();
                saveAndReturn();
                finish();
            });
            builder.setNegativeButton("No", (dialog, id) -> {
                dialog.dismiss();
                finish();
            });
            AlertDialog alert = builder.create();
            alert.show();
            return true;
        } else if (item.getItemId() == R.id.action_delete) {
            //ask the user if they want to delete the course
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to delete this course?");
            builder.setIcon(AppCompatResources.getDrawable(this, R.drawable.ic_question_mark));
            builder.setPositiveButton("Yes", (dialog, id) -> {
                dialog.dismiss();
                boolean hasAssessments;
                boolean hasMentors;
                //check if the course has any assessments assigned or mentors assigned to this course ID
                LiveData<List<Assessments>> assignedAssessments = viewModel.getAssessmentsByCourseId(courseId);
                LiveData<List<Mentors>> assignedMentors = viewModel.getMentorsByCourseId(courseId);
                //check that the lists are not null
                if (assignedAssessments.getValue() != null || assignedMentors.getValue() != null) {
                    //check each list to see if it has any items
                    if (assignedAssessments.getValue() != null) {
                        //if the list is bigger than 0, then there are assessments assigned to this course
                        hasAssessments = assignedAssessments.getValue().size() > 0;
                    } else {
                        hasAssessments = false;
                    }
                    if (assignedMentors.getValue() != null) {
                        //if the list is bigger than 0, then there are mentors assigned to this course
                        hasMentors = assignedMentors.getValue().size() > 0;
                    } else {
                        hasMentors = false;
                    }
                } else {
                    hasMentors = false;
                    hasAssessments = false;
                }
                //if the course has assessments or mentors assigned, ask the user if they want to delete the course
                if((hasMentors || hasAssessments)) {
                    //build the alert and show it
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                    builder2.setTitle("Delete Course?");
                    builder2.setMessage("Are you sure you want to delete this course?\nThis course has assessments and/or mentors assigned to it.  If you delete this course, the assessments and/or mentors will not be assigned to any course.\nDelete course anyway?");
                    builder2.setIcon(android.R.drawable.ic_dialog_alert);
                    builder2.setPositiveButton("Yes", (dialog2, which) -> {
                        //if the user clicks yes, delete the course and return to the previous activity
                        viewModel.deleteCourse();
                        //use the assigned assessments and mentors to remove the course ID from the assessment and mentor objects
                        if (hasAssessments) {
                            for (Assessments assessment : assignedAssessments.getValue()) {
                                viewModel.overwriteAssessment(assessment, -1);
                            }
                        }
                        if (hasMentors) {
                            for (Mentors mentor : assignedMentors.getValue()) {
                                viewModel.overwriteMentor(mentor, -1);
                            }
                        }
                        //notify the user that the course was deleted
                        Toast.makeText(CourseEditActivity.this, "Course Deleted", Toast.LENGTH_SHORT).show();
                        //navigate back to the course list
                        finish();
                    });
                    builder2.setNegativeButton("No", (dialog2, which) -> {
                        //if the user clicks no, close the alert so they can fix the dates
                    });
                    AlertDialog dialog2 = builder2.create();
                    dialog2.show();
                } else {
                    //if the course does not have assessments or mentors assigned, delete the course and return to the previous activity
                    viewModel.deleteCourse();
                    //notify the user that the course was deleted
                    Toast.makeText(CourseEditActivity.this, "Course Deleted", Toast.LENGTH_SHORT).show();
                    //navigate back to the course list
                    finish();
                }
            });
            builder.setNegativeButton("No", (dialog, id) -> dialog.dismiss());
            AlertDialog alert = builder.create();
            alert.show();
        } else if (item.getItemId() == R.id.action_save) {
            saveAndReturn();
            return true;
        } else if (item.getItemId() == R.id.action_help) {
            //TODO: add help dialog
        } else if (item.getItemId() == R.id.action_notify) {
            //check if the course is new or existing
            if (edit) {
                //if the course is existing, check if the course start and end dates have changed
                if (!editCourseStartDate.getText().toString().equals(courseStartDateView.getText().toString()) || !editCourseEndDate.getText().toString().equals(courseEndDateView.getText().toString())) {
                    //compare the start and end dates to the dates in the database to see if the course has been saved
                    Date courseStartDateValue;
                    Date courseEndDateValue;
                    try {
                        courseStartDateValue = Formatting.dateFormat.parse(courseStartDateView.getText().toString());
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        courseEndDateValue = Formatting.dateFormat.parse(courseEndDateView.getText().toString());
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    //if the course start and end dates have changed, check if the course has been saved
                    if (!(courseStartDateValue == viewModel.getCourseById(courseId).getCourseStartDate())) {
                        if (!(courseEndDateValue == viewModel.getCourseById(courseId).getCourseEndDate())) {
                            //ask the user if they want to set notifications for the start and end dates or both
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle("Set Notification");
                            builder.setMessage("Set a notification for the start date, end date, or both?");
                            builder.setIcon(AppCompatResources.getDrawable(this, R.drawable.ic_action_notify));
                            builder.setPositiveButton("Start Date", (dialog, which) -> {
                                //get the start date
                                Date targetDate = viewModel.getCourseById(courseId).getCourseStartDate();
                                Long trigger = targetDate.getTime();
                                //set notification for the start date
                                Intent intent = new Intent(this, Alerts.class);
                                intent.putExtra(CHANNEL_ID, "Course Start Notification");
                                intent.putExtra(SUBJECT, "Course " + viewModel.getCourseById(courseId).getCourseName());
                                intent.putExtra(NOTIFICATION, "Course " + viewModel.getCourseById(courseId).getCourseName() + " starts today.");
                                intent.putExtra(IMPORTANCE, NotificationManager.IMPORTANCE_HIGH);
                                //add this course activity to the intent
                                intent.putExtra(PENDING_INTENT, PendingIntent.getActivity(this, courseId, new Intent(this, CourseEditActivity.class), PendingIntent.FLAG_IMMUTABLE));
                                //set the alarm
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, courseId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger,pendingIntent);
                                //notify the user that the notification has been set
                                Toast.makeText(this, "Notification set for " + Formatting.dateFormat.format(targetDate), Toast.LENGTH_SHORT).show();
                                finish();
                            });
                            builder.setNegativeButton("End Date", (dialog, which) -> {
                                //get the end date
                                Date targetDate = viewModel.getCourseById(courseId).getCourseEndDate();
                                Long trigger = targetDate.getTime();
                                //set notification for the end date
                                Intent intent = new Intent(this, Alerts.class);
                                intent.putExtra(CHANNEL_ID, "Course End Notification");
                                intent.putExtra(SUBJECT, "Course " + viewModel.getCourseById(courseId).getCourseName());
                                intent.putExtra(NOTIFICATION, "Course " + viewModel.getCourseById(courseId).getCourseName() + " ends today.");
                                intent.putExtra(IMPORTANCE, NotificationManager.IMPORTANCE_HIGH);
                                //add this course activity to the intent
                                intent.putExtra(PENDING_INTENT, PendingIntent.getActivity(this, courseId, new Intent(this, CourseEditActivity.class), PendingIntent.FLAG_IMMUTABLE));
                                //set the alarm
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, courseId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger,pendingIntent);
                                //notify the user that the notification has been set
                                Toast.makeText(this, "Notification set for " + Formatting.dateFormat.format(targetDate), Toast.LENGTH_SHORT).show();
                                finish();
                            });
                            builder.setNeutralButton("Both", (dialog, which) -> {
                                //get the start date
                                Date targetDate = viewModel.getCourseById(courseId).getCourseStartDate();
                                Long trigger = targetDate.getTime();
                                //set notification for the start date
                                Intent intent = new Intent(this, Alerts.class);
                                intent.putExtra(CHANNEL_ID, "Course Start Notification");
                                intent.putExtra(SUBJECT, "Course " + viewModel.getCourseById(courseId).getCourseName());
                                intent.putExtra(NOTIFICATION, "Course " + viewModel.getCourseById(courseId).getCourseName() + " starts today.");
                                intent.putExtra(IMPORTANCE, NotificationManager.IMPORTANCE_HIGH);
                                //add this course activity to the intent
                                intent.putExtra(PENDING_INTENT, PendingIntent.getActivity(this, courseId, new Intent(this, CourseEditActivity.class), PendingIntent.FLAG_IMMUTABLE));
                                //set the alarm
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, courseId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger,pendingIntent);
                                //notify the user that the notification has been set
                                Toast.makeText(this, "Notification set for " + Formatting.dateFormat.format(targetDate), Toast.LENGTH_SHORT).show();
                                //get the end date
                                targetDate = viewModel.getCourseById(courseId).getCourseEndDate();
                                trigger = targetDate.getTime();
                                //set notification for the end date
                                intent = new Intent(this, Alerts.class);
                                intent.putExtra(CHANNEL_ID, "Course End Notification");
                                intent.putExtra(SUBJECT, "Course " + viewModel.getCourseById(courseId).getCourseName());
                                intent.putExtra(NOTIFICATION, "Course " + viewModel.getCourseById(courseId).getCourseName() + " ends today.");
                                intent.putExtra(IMPORTANCE, NotificationManager.IMPORTANCE_HIGH);
                                //add this course activity to the intent
                                intent.putExtra(PENDING_INTENT, PendingIntent.getActivity(this, courseId, new Intent(this, CourseEditActivity.class), PendingIntent.FLAG_IMMUTABLE));
                                //set the alarm
                                pendingIntent = PendingIntent.getBroadcast(this, courseId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger,pendingIntent);
                                //notify the user that the notification has been set
                                Toast.makeText(this, "Notification set for " + Formatting.dateFormat.format(targetDate), Toast.LENGTH_SHORT).show();
                                finish();
                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        } else {
                            //if the course end date has not changed, notify the user that the course has not been saved
                            Toast.makeText(this, "You must save the course before you can set notifications", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        //if the course start and end dates have not changed, notify the user that the course has not been saved
                        Toast.makeText(this, "You must save the course before you can set notifications", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
