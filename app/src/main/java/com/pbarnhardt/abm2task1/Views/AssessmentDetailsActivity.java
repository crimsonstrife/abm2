package com.pbarnhardt.abm2task1.Views;

import static com.pbarnhardt.abm2task1.Utils.Constants.ASSESSMENT_KEY;
import static com.pbarnhardt.abm2task1.Utils.Converters.fromAssessmentTypeToString;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pbarnhardt.abm2task1.Entity.Courses;
import com.pbarnhardt.abm2task1.Models.EditorModel;
import com.pbarnhardt.abm2task1.R;
import com.pbarnhardt.abm2task1.Utils.Formatting;

public class AssessmentDetailsActivity extends AppCompatActivity {
    private int assessmentId;
    private Courses assignedCourse;
    private Toolbar toolbar;
    private EditorModel viewModel;

    /**
     * On create method
     *
     * @param savedInstanceState saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_details);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initiateViewModel();

        // On click listener for edit button
        final FloatingActionButton editAssessmentButton = findViewById(R.id.floatingEditAssessmentButton);
        editAssessmentButton.setOnClickListener(view -> {
            Intent intent = new Intent(AssessmentDetailsActivity.this, AssessmentEditActivity.class);
            intent.putExtra(ASSESSMENT_KEY, assessmentId);
            startActivity(intent);
        });
    }

    private void initiateViewModel() {
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(EditorModel.class);
        final TextView assessmentTitleView = findViewById(R.id.editText_assessment_title);
        final TextView assessmentDescriptionView = findViewById(R.id.courseDetailDescription);
        final TextView assessmentTypeView = findViewById(R.id.text_assessment_type);
        final TextView assessmentDateView = findViewById(R.id.termDetailStartDate);
        final TextView assessmentAssignedCourseView = findViewById(R.id.assessment_course_title);
        viewModel.liveAssessments.observe(this, assessments -> {
            if(assessments != null) {
                assessmentId = assessments.getAssessmentId();
                assessmentTitleView.setText(assessments.getAssessmentName());
                assessmentDescriptionView.setText(assessments.getAssessmentDescription());
                assessmentTypeView.setText(fromAssessmentTypeToString(assessments.getAssessmentType()));
                assessmentDateView.setText(Formatting.dateFormat.format(assessments.getAssessmentDueDate()));
                //get assigned course ID
                assignedCourse = viewModel.getCourseById(assessments.getAssessmentCourseId());
                //get assigned course title
                assessmentAssignedCourseView.setText(assignedCourse.getCourseName());
                toolbar.setTitle(assessments.getAssessmentName());
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            assessmentId = extras.getInt(ASSESSMENT_KEY);
            viewModel.loadAssessment(assessmentId);
        } else {
            finish();
        }
    }
}