package com.pbarnhardt.abm2task1.Views;

import static com.pbarnhardt.abm2task1.Utils.Constants.ASSESSMENT_KEY;
import static com.pbarnhardt.abm2task1.Utils.Constants.COURSE_KEY;
import static com.pbarnhardt.abm2task1.Utils.Constants.EDIT_KEY;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

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

import com.pbarnhardt.abm2task1.Entity.Courses;
import com.pbarnhardt.abm2task1.Enums.Types;
import com.pbarnhardt.abm2task1.Models.CourseModel;
import com.pbarnhardt.abm2task1.Models.EditorModel;
import com.pbarnhardt.abm2task1.R;
import com.pbarnhardt.abm2task1.Utils.Formatting;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AssessmentEditActivity extends AppCompatActivity {
    /**
     * Bind views.
     */
    EditText assessmentTitle = findViewById(R.id.editText_assessment_title);
    EditText assessmentDescription = findViewById(R.id.assessmentDetailDescription);
    Spinner assessmentType = findViewById(R.id.spinner_assessment_type);
    Spinner assessmentAssignedCourse = findViewById(R.id.assessment_course);
    Button assessmentDueDate = findViewById(R.id.editText_assessmentEndDate);
    CheckBox assessmentDueAlert = findViewById(R.id.checkbox_remindMe);

    /**
     * Variables.
     */
    private boolean newAssessment;
    private boolean edit;
    private int courseId = -1;

    private List<Courses> courseList = new ArrayList<>();
    private Courses assignedCourse;
    private EditorModel viewModel;
    private ArrayAdapter<Types> typeAdapter;
    private ArrayAdapter<String> courseAdapter;
    private String[] courseNames;
    private int[] courseIds;

    /**
     * On create.
     *
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_edit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_action_check);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState != null) {
            edit = savedInstanceState.getBoolean(EDIT_KEY);
        }

        initiateViewModel();
        addTypesToSpinner();
        addCoursesToSpinner();
    }

    private Types getTypeSpinnerValue() {
        return (Types) assessmentType.getSelectedItem();
    }

    private String getCourseSpinnerValue() {
        return (String) assessmentAssignedCourse.getSelectedItem();
    }

    private void addCoursesToSpinner() {
        List<Courses> potentialCourses = collectCourseInfo();
        courseNames = new String[potentialCourses.size()];
        courseIds = new int[potentialCourses.size()];
        //Loop through the list of courses and store the name and id in the arrays
        for (int i = 0; i < potentialCourses.size(); i++) {
            courseNames[i] = potentialCourses.get(i).getCourseName();
            courseIds[i] = potentialCourses.get(i).getCourseId();
        }
        //set the course adapter
        courseAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, courseNames);
        assessmentAssignedCourse.setAdapter(courseAdapter);
    }

    /**
     * Used to gather the course information from the database for the spinner.
     * Should just need to store the course name and id.
     */
    private List<Courses> collectCourseInfo() {
        viewModel.coursesList.observe(this, courses -> {
            courseList.addAll(courses);
        });
        return courseList;
    }

    private void addTypesToSpinner() {
        typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Types.values());
        assessmentType.setAdapter(typeAdapter);
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
                assessmentAssignedCourse.setSelection(coursePosition);
            }
        });
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            setTitle("New Assessment");
            newAssessment = true;
        } else if (extras.containsKey(COURSE_KEY)) {
            courseId = extras.getInt(COURSE_KEY);
            setTitle("New Assessment");
        } else {
            setTitle("Edit Assessment");
            int assessmentId = extras.getInt(ASSESSMENT_KEY);
            viewModel.loadAssessment(assessmentId);
        }
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
            Types type = getTypeSpinnerValue();
            //match the spinner value to the course id
            for (int i = 0; i < courseNames.length; i++) {
                if (courseNames[i].equals(getCourseSpinnerValue())) {
                    courseId = courseIds[i];
                }
            }
            viewModel.saveAssessment(title, description, dueDate, type, courseId, alert);
            finish();
        } catch (Exception e) {
            Log.v("Exception: ", e.getLocalizedMessage());
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