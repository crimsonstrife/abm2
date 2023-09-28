package com.pbarnhardt.abm2task1.Views;

import static com.pbarnhardt.abm2task1.Utils.Constants.ASSESSMENT_KEY;
import static com.pbarnhardt.abm2task1.Utils.Converters.fromAssessmentTypeToString;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.pbarnhardt.abm2task1.Entity.Courses;
import com.pbarnhardt.abm2task1.Models.EditorModel;
import com.pbarnhardt.abm2task1.R;
import com.pbarnhardt.abm2task1.Utils.Formatting;
import com.pbarnhardt.abm2task1.databinding.ActivityAssessmentDetailsBinding;
import com.pbarnhardt.abm2task1.databinding.ContentDetailsAssessmentsBinding;

public class AssessmentDetailsActivity extends AppCompatActivity {
    private int assessmentId;
    private Courses assignedCourse;
    private Toolbar toolbar;
    private EditorModel viewModel;
    private ActivityAssessmentDetailsBinding activityBinding;
    private ContentDetailsAssessmentsBinding contentBinding;
    private TextView assessmentTitleView;
    private TextView assessmentDescriptionView;
    private TextView assessmentTypeView;
    private TextView assessmentDateView;
    private TextView assessmentAssignedCourseView;

    /**
     * On create method
     *
     * @param savedInstanceState saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = ActivityAssessmentDetailsBinding.inflate(getLayoutInflater());
        View view = activityBinding.getRoot();
        setContentView(view);
        toolbar = activityBinding.toolbar;
        setSupportActionBar(toolbar);

        //initialize the binding
        contentBinding = activityBinding.contentInclude;
        assessmentTitleView = contentBinding.editTextAssessmentTitle;
        assessmentDescriptionView = contentBinding.courseDetailDescription;
        assessmentTypeView = contentBinding.textAssessmentType;
        assessmentDateView = contentBinding.termDetailStartDate;
        assessmentAssignedCourseView = contentBinding.assessmentCourseTitle;

        initiateViewModel();

        // On click listener for edit button
        activityBinding.floatingEditAssessmentButton.setOnClickListener(v -> {
            Intent intent = new Intent(AssessmentDetailsActivity.this, AssessmentEditActivity.class);
            intent.putExtra(ASSESSMENT_KEY, assessmentId);
            startActivity(intent);
        });
    }

    private void initiateViewModel() {
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(EditorModel.class);
        viewModel.liveAssessments.observe(this, assessments -> {
            if(assessments != null) {
                assessmentId = assessments.getAssessmentId();
                assessmentTitleView.setText(assessments.getAssessmentName());
                assessmentDescriptionView.setText(assessments.getAssessmentDescription());
                assessmentTypeView.setText(fromAssessmentTypeToString(assessments.getAssessmentType()));
                assessmentDateView.setText(Formatting.dateFormat.format(assessments.getAssessmentDueDate()));
                //get assigned course ID
                assignedCourse = viewModel.getCourseById(assessments.getAssessmentCourseId());
                //get assigned course title if it exists
                if(assignedCourse != null) {
                    assessmentAssignedCourseView.setText(assignedCourse.getCourseName());
                } else {
                    assessmentAssignedCourseView.setText(R.string.no_course_assigned);
                }
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