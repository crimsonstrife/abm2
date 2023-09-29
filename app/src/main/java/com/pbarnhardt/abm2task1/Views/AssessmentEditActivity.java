package com.pbarnhardt.abm2task1.Views;

import static com.pbarnhardt.abm2task1.Utils.Constants.ASSESSMENT_KEY;
import static com.pbarnhardt.abm2task1.Utils.Constants.COURSE_KEY;
import static com.pbarnhardt.abm2task1.Utils.Constants.EDIT_KEY;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.pbarnhardt.abm2task1.Adapters.AssessmentAdapter;
import com.pbarnhardt.abm2task1.Entity.Courses;
import com.pbarnhardt.abm2task1.Enums.Status;
import com.pbarnhardt.abm2task1.Enums.Types;
import com.pbarnhardt.abm2task1.Models.EditorModel;
import com.pbarnhardt.abm2task1.R;
import com.pbarnhardt.abm2task1.Utils.Alerts;
import com.pbarnhardt.abm2task1.Utils.Formatting;
import com.pbarnhardt.abm2task1.databinding.ActivityAssessmentEditBinding;
import com.pbarnhardt.abm2task1.databinding.ContentEditAssessmentsBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AssessmentEditActivity extends AppCompatActivity {
    /**
     * Variables.
     */
    int assessmentId;
    private boolean newAssessment;
    private boolean edit;
    private int courseId = -1;

    private final List<Courses> courseList = new ArrayList<>();
    private EditorModel viewModel;
    private ArrayAdapter<Types> typeAdapter;
    private String[] courseNames;
    private int[] courseIds;
    private ActivityAssessmentEditBinding activityBinding;
    private ContentEditAssessmentsBinding contentBinding;
    private EditText assessmentTitle;
    private EditText assessmentDescription;
    private Button assessmentStartDate;
    private Button assessmentDueDate;
    private TextView assessmentStartDateText;
    private TextView assessmentDueDateText;
    private CheckBox assessmentStartAlert;
    private CheckBox assessmentDueAlert;
    private Spinner assessmentType;
    private Spinner assignedCourse;
    private String newCourseTitle;
    private String newCourseDescription;
    private String newCourseNote;
    private Date newCourseStartDate;
    private Date newCourseEndDate;
    private boolean newCourseStartAlert;
    private boolean newCourseEndAlert;
    private Status newCourseStatus;
    private AssessmentAdapter adapter;

    /**
     * On create.
     *
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = ActivityAssessmentEditBinding.inflate(getLayoutInflater());
        View view = activityBinding.getRoot();
        setContentView(view);
        Toolbar toolbar = activityBinding.toolbar;
        setSupportActionBar(toolbar);
        //set toolbar color
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_action_check);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState != null) {
            edit = savedInstanceState.getBoolean(EDIT_KEY);
        }

        //initiate the bindings
        contentBinding = activityBinding.contentInclude;
        assessmentTitle = contentBinding.editTextAssessmentTitle;
        assessmentDescription = contentBinding.assessmentDetailDescription;
        assessmentDueDate = contentBinding.editTextAssessmentEndDate;
        assessmentDueDateText = contentBinding.termDetailDueDate;
        assessmentStartDate = contentBinding.editTextAssessmentStartDate;
        assessmentStartDateText = contentBinding.termDetailStartDate;
        assessmentDueAlert = contentBinding.checkboxRemindMe;
        assessmentStartAlert = contentBinding.checkboxRemindMeStart;
        assessmentType = contentBinding.spinnerAssessmentType;
        assignedCourse = contentBinding.assessmentCourse;

        //initiate the view model
        initiateViewModel();
        addTypesToSpinner(assessmentType);
        addCoursesToSpinner(assignedCourse);

        //set the date button
        assessmentDueDate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener date = (startView, year, month, day) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                //update the button text with the selected date
                assessmentDueDate.setText(Formatting.dateFormat.format(calendar.getTime()));
                assessmentDueDate.setHint(Formatting.dateFormat.format(calendar.getTime()));
                assessmentDueDateText.setText(Formatting.dateFormat.format(calendar.getTime()));
            };
            new DatePickerDialog(AssessmentEditActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        //set the start date button
        assessmentStartDate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener date = (startView, year, month, day) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                //update the button text with the selected date
                assessmentStartDate.setText(Formatting.dateFormat.format(calendar.getTime()));
                assessmentStartDate.setHint(Formatting.dateFormat.format(calendar.getTime()));
                assessmentStartDateText.setText(Formatting.dateFormat.format(calendar.getTime()));
            };
            new DatePickerDialog(AssessmentEditActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private Types getTypeSpinnerValue(Spinner spinner) {
        return (Types) spinner.getSelectedItem();
    }

    private String getCourseSpinnerValue(Spinner spinner) {
        return (String) spinner.getSelectedItem();
    }

    private void addCoursesToSpinner(Spinner spinner) {
        List<Courses> potentialCourses = collectCourseInfo();
        courseNames = new String[potentialCourses.size()];
        courseIds = new int[potentialCourses.size()];
        //Loop through the list of courses and store the name and id in the arrays
        for (int i = 0; i < potentialCourses.size(); i++) {
            courseNames[i] = potentialCourses.get(i).getCourseName();
            courseIds[i] = potentialCourses.get(i).getCourseId();
        }
        //set the course adapter
        ArrayAdapter<String> courseAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, courseNames);
        spinner.setAdapter(courseAdapter);
    }

    /**
     * Used to gather the course information from the database for the spinner.
     * Should just need to store the course name and id.
     */
    private List<Courses> collectCourseInfo() {
        viewModel.coursesList.observe(this, courseList::addAll);
        //log courseList to see if it is empty
        Log.v("courseList: ", courseList.toString());
        return courseList;
    }

    private void addTypesToSpinner(Spinner spinner) {
        typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Types.values());
        spinner.setAdapter(typeAdapter);
    }

    private void initiateViewModel() {
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(EditorModel.class);
        viewModel.liveAssessments.observe(this, assessments -> {
            if (assessments != null && !edit) {
                assessmentTitle.setText(assessments.getAssessmentName());
                assessmentDescription.setText(assessments.getAssessmentDescription());
                assessmentStartDate.setText(Formatting.dateFormat.format(assessments.getAssessmentStartDate()));
                assessmentDueDate.setText(Formatting.dateFormat.format(assessments.getAssessmentDueDate()));
                assessmentStartAlert.setChecked(assessments.getAssessmentStartAlert());
                assessmentDueAlert.setChecked(assessments.getAssessmentDueAlert());
                int typePosition = getTypeSpinnerPosition(assessments.getAssessmentType());
                assessmentType.setSelection(typePosition);
                int coursePosition = getCourseSpinnerPosition(assessments.getAssessmentCourseId());
                assignedCourse.setSelection(coursePosition);
            }
        });
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            setTitle(getString(R.string.new_assessment));
            newAssessment = true;
        } else if (extras.containsKey(COURSE_KEY)) {
            courseId = extras.getInt(COURSE_KEY);
            setTitle(getString(R.string.new_assessment));
        } else {
            setTitle(getString(R.string.edit_assessment));
            assessmentId = extras.getInt(ASSESSMENT_KEY);
            viewModel.loadAssessment(assessmentId);
        }

        ArrayList<String> typeSpinnerArray = new ArrayList<>();
        for (Types type : Types.values()) {
            typeSpinnerArray.add(type.toString());
        }
        ArrayAdapter<String> typeArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, typeSpinnerArray);
        assessmentType.setAdapter(typeArrayAdapter);

        ArrayList<String> courseSpinnerArray = new ArrayList<>();
        viewModel.coursesList.observe(this, courses -> {
            if (courses != null) {
                courseList.addAll(courses);
                for (Courses course : courseList) {
                    courseSpinnerArray.add(course.getCourseName());
                }
                ArrayAdapter<String> courseArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, courseSpinnerArray);
                assignedCourse.setAdapter(courseArrayAdapter);
            } else {
                Log.v("courseList: ", "courseList is null");
            }
        });
        //log size of courseList
        Log.v("courseList: ", courseList.size() + " courses");
    }

    private int getCourseSpinnerPosition(int assessmentCourseId) {
        //search the courseIds array for a match to the course id passed in
        for (int i = 0; i < courseIds.length; i++) {
            if (courseIds[i] == assessmentCourseId) {
                //return the position of the match
                return i;
            }
        }
        //if no match is found, return 0
        return 0;
    }

    private int getTypeSpinnerPosition(Types assessmentType) {
        return typeAdapter.getPosition(assessmentType);
    }

    @Override
    public void onBackPressed() {
        saveAndReturn();
    }

    private void saveAndReturn() {
        try {
            String title = assessmentTitle.getText().toString().trim();
            String description = assessmentDescription.getText().toString().trim();
            Date dueDate = Formatting.dateFormat.parse(assessmentDueDate.getText().toString());
            Date startDate = Formatting.dateFormat.parse(assessmentStartDate.getText().toString());
            boolean startAlert = assessmentStartAlert.isChecked();
            boolean dueAlert = assessmentDueAlert.isChecked();
            Types type = getTypeSpinnerValue(assessmentType);
            //match the spinner value to the course id
            for (int i = 0; i < courseNames.length; i++) {
                if (courseNames[i].equals(getCourseSpinnerValue(assignedCourse))) {
                    courseId = courseIds[i];
                }
            }
            viewModel.saveAssessment(title, description, startDate, dueDate, type, courseId, startAlert, dueAlert);
            //notify the user that the assessment was saved
            Toast.makeText(AssessmentEditActivity.this, "Assessment saved", Toast.LENGTH_SHORT).show();
            //navigate back to the assessment list
            finish();
        } catch (Exception e) {
            Log.v("Exception: ", Objects.requireNonNull(e.getLocalizedMessage()));
            //notify the user that the assessment was not saved
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("The assessment was not saved. Please try again.");
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.setPositiveButton("OK", (dialog, id) -> dialog.dismiss());
        }
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(EDIT_KEY, true);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!newAssessment) {
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
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        } else if (item.getItemId() == R.id.action_delete) {
            //ask the user if they want to delete the assessment
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to delete this assessment?");
            builder.setIcon(AppCompatResources.getDrawable(this, R.drawable.ic_question_mark));
            builder.setPositiveButton("Yes", (dialog, id) -> {
                dialog.dismiss();
                viewModel.deleteAssessment();
                Toast.makeText(AssessmentEditActivity.this, "Assessment deleted", Toast.LENGTH_SHORT).show();
                finish();
            });
            builder.setNegativeButton("No", (dialog, id) -> dialog.dismiss());
        } else if (item.getItemId() == R.id.action_save) {
            saveAndReturn();
            return true;
        } else if (item.getItemId() == R.id.action_help) {
            //TODO: add help dialog
        } else if (item.getItemId() == R.id.action_notify) {
            //ask the user if they want to set an alert for the start or end date or both
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to set an alert for the start date, end date, or both?");
            builder.setIcon(AppCompatResources.getDrawable(this, R.drawable.ic_question_mark));
            builder.setPositiveButton("Start Date", (dialog, id) -> {
                dialog.dismiss();
                //set the start date alert
                Intent intent = new Intent(this, Alerts.class);
                //current assessment id
                int currentId = viewModel.getAssessmentById(assessmentId).getAssessmentId();
                if (currentId > 0) {
                    Date currentDate = viewModel.getAssessmentById(currentId).getAssessmentStartDate();
                    Long trigger = currentDate.getTime();
                    intent.putExtra("notification", assessmentTitle.getText().toString() + " is starting today!");
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, currentId, intent, PendingIntent.FLAG_IMMUTABLE);
                    //set the alarm
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, pendingIntent);
                    Toast.makeText(this, "Notification set for " + Formatting.dateFormat.format(currentDate), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Unable to set notification", Toast.LENGTH_LONG).show();
                }
                finish();
            });
            builder.setNegativeButton("End Date", (dialog, id) -> {
                dialog.dismiss();
                //set the end date alert
                Intent intent = new Intent(this, Alerts.class);
                //current assessment id
                int currentId = viewModel.getAssessmentById(assessmentId).getAssessmentId();
                if (currentId > 0) {
                    Date currentDate = viewModel.getAssessmentById(currentId).getAssessmentDueDate();
                    Long trigger = currentDate.getTime();
                    intent.putExtra("notification", assessmentTitle.getText().toString() + " is due today!");
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, currentId, intent, PendingIntent.FLAG_IMMUTABLE);
                    //set the alarm
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, pendingIntent);
                    Toast.makeText(this, "Notification set for " + Formatting.dateFormat.format(currentDate), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Unable to set notification", Toast.LENGTH_LONG).show();
                }
            });
            builder.setNeutralButton("Both", (dialog, id) -> {
                dialog.dismiss();
                //set both alerts
                Intent intent = new Intent(this, Alerts.class);
                //current assessment id
                int currentId = viewModel.getAssessmentById(assessmentId).getAssessmentId();
                if (currentId > 0) {
                    Date currentDate = viewModel.getAssessmentById(currentId).getAssessmentStartDate();
                    Long trigger = currentDate.getTime();
                    intent.putExtra("notification", assessmentTitle.getText().toString() + " is starting today!");
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, currentId, intent, PendingIntent.FLAG_IMMUTABLE);
                    //set the alarm
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, pendingIntent);
                    Toast.makeText(this, "Notification set for " + Formatting.dateFormat.format(currentDate), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Unable to set notification", Toast.LENGTH_LONG).show();
                }
                Intent intent2 = new Intent(this, Alerts.class);
                //current assessment id
                int currentId2 = viewModel.getAssessmentById(assessmentId).getAssessmentId();
                if (currentId2 > 0) {
                    Date currentDate2 = viewModel.getAssessmentById(currentId2).getAssessmentDueDate();
                    Long trigger2 = currentDate2.getTime();
                    intent2.putExtra("notification", assessmentTitle.getText().toString() + " is due today!");
                    PendingIntent pendingIntent2 = PendingIntent.getBroadcast(this, currentId2, intent2, PendingIntent.FLAG_IMMUTABLE);
                    //set the alarm
                    AlarmManager alarmManager2 = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManager2.set(AlarmManager.RTC_WAKEUP, trigger2, pendingIntent2);
                    Toast.makeText(this, "Notification set for " + Formatting.dateFormat.format(currentDate2), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Unable to set notification", Toast.LENGTH_LONG).show();
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }
}