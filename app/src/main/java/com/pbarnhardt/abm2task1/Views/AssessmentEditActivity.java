package com.pbarnhardt.abm2task1.Views;

import static com.pbarnhardt.abm2task1.Utils.Constants.ASSESSMENT_KEY;
import static com.pbarnhardt.abm2task1.Utils.Constants.COURSE_KEY;
import static com.pbarnhardt.abm2task1.Utils.Constants.EDIT_KEY;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.pbarnhardt.abm2task1.Entity.Courses;
import com.pbarnhardt.abm2task1.Enums.Types;
import com.pbarnhardt.abm2task1.Models.EditorModel;
import com.pbarnhardt.abm2task1.R;
import com.pbarnhardt.abm2task1.Utils.Formatting;
import com.pbarnhardt.abm2task1.databinding.ActivityAssessmentEditBinding;
import com.pbarnhardt.abm2task1.databinding.ContentEditAssessmentsBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
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

    private List<Courses> courseList = new ArrayList<>();
    private EditorModel viewModel;
    private ArrayAdapter<Types> typeAdapter;
    private String[] courseNames;
    private int[] courseIds;
    private ActivityAssessmentEditBinding activityBinding;
    private ContentEditAssessmentsBinding contentBinding;
    private EditText assessmentTitle;
    private EditText assessmentDescription;
    private Button assessmentDueDate;
    private CheckBox assessmentDueAlert;
    private Spinner assessmentType;
    private Spinner assignedCourse;

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
        assessmentDueAlert = contentBinding.checkboxRemindMe;
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
            };
            new DatePickerDialog(AssessmentEditActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            //update the button text with the selected date
            assessmentDueDate.setText(Formatting.dateFormat.format(calendar.getTime()));
            assessmentDueDate.setHint(Formatting.dateFormat.format(calendar.getTime()));
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
        viewModel.coursesList.observe(this, courses -> courseList.addAll(courses));
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
                assessmentDueDate.setText(Formatting.dateFormat.format(assessments.getAssessmentDueDate()));
                assessmentDueAlert.setChecked(assessments.getAssessmentAlert());
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
        Log.v("courseList: ", String.valueOf(courseList.size()) + " courses");
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
            boolean alert = assessmentDueAlert.isChecked();
            Types type = getTypeSpinnerValue(assessmentType);
            //match the spinner value to the course id
            for (int i = 0; i < courseNames.length; i++) {
                if (courseNames[i].equals(getCourseSpinnerValue(assignedCourse))) {
                    courseId = courseIds[i];
                }
            }
            viewModel.saveAssessment(title, description, dueDate, type, courseId, alert);
            finish();
        } catch (Exception e) {
            Log.v("Exception: ", Objects.requireNonNull(e.getLocalizedMessage()));
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
            saveAndReturn();
            return true;
        } else if (item.getItemId() == R.id.action_delete) {
            viewModel.deleteAssessment();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}