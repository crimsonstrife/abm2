package com.pbarnhardt.abm2task1.Views;

import static com.pbarnhardt.abm2task1.Utils.Constants.COURSE_KEY;
import static com.pbarnhardt.abm2task1.Utils.Converters.fromCourseStatusToString;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pbarnhardt.abm2task1.Adapters.AssessmentAdapter;
import com.pbarnhardt.abm2task1.Adapters.MentorAdapter;
import com.pbarnhardt.abm2task1.Entity.Assessments;
import com.pbarnhardt.abm2task1.Entity.Mentors;
import com.pbarnhardt.abm2task1.Enums.RecyclerAdapter;
import com.pbarnhardt.abm2task1.Models.EditorModel;
import com.pbarnhardt.abm2task1.Popups.DropdownAssessments;
import com.pbarnhardt.abm2task1.Popups.DropdownMentors;
import com.pbarnhardt.abm2task1.R;
import com.pbarnhardt.abm2task1.Utils.Formatting;

import java.util.ArrayList;
import java.util.List;

public class CourseDetailsActivity extends AppCompatActivity implements MentorAdapter.MentorSelectedListener, AssessmentAdapter.AssessmentSelection {
    /**
     * Bind views
     */
    TextView courseTitleView = findViewById(R.id.editText_course_title);
    TextView courseDescriptionView = findViewById(R.id.courseDetailDescription);
    TextView courseStatusView = findViewById(R.id.spinner_course_status);
    TextView courseStartDateView = findViewById(R.id.termDetailStartDate);
    TextView courseEndDateView = findViewById(R.id.termDetailEndDate);
    TextView courseNotesView = findViewById(R.id.editText_course_note);
    RecyclerView courseAssessmentRecyclerView = findViewById(R.id.course_assessmentsRecyclerView);
    RecyclerView courseMentorRecyclerView = findViewById(R.id.course_mentorsRecyclerView);
    FloatingActionButton courseEditButton = findViewById(R.id.floatingEditCourseButton);
    FloatingActionButton courseAddMentorButton = findViewById(R.id.floatingAddMentorButton);
    FloatingActionButton courseAddAssessmentButton = findViewById(R.id.floatingAddAssessmentButton);

    /**
     * Variables
     */
    private Toolbar toolbar;
    private EditorModel viewModel;
    private int courseId;
    private AssessmentAdapter assessmentAdapter;
    private MentorAdapter mentorAdapter;
    private List<Assessments> assessmentsListData = new ArrayList<>();
    private List<Mentors> mentorsListData = new ArrayList<>();
    private List<Assessments> unassignedAssessmentsList = new ArrayList<>();
    private List<Mentors> unassignedMentorsList = new ArrayList<>();


    /**
     * On create method
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initiateViewModel();
        initiateMentorRecyclerView();
        initiateAssessmentRecyclerView();
    }

    private void initiateAssessmentRecyclerView() {
        courseAssessmentRecyclerView.setHasFixedSize(true);
        LinearLayoutManager assessmentLayoutManager = new LinearLayoutManager(this);
        courseAssessmentRecyclerView.setLayoutManager(assessmentLayoutManager);
    }

    private void initiateMentorRecyclerView() {
        courseMentorRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mentorLayoutManager = new LinearLayoutManager(this);
        courseMentorRecyclerView.setLayoutManager(mentorLayoutManager);
    }

    private void initiateViewModel() {
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(EditorModel.class);
        viewModel.liveCourses.observe(this, courses -> {
            if(courses != null) {
                courseId = courses.getCourseId();
                courseTitleView.setText(courses.getCourseName());
                courseDescriptionView.setText(courses.getCourseDescription());
                courseStatusView.setText(fromCourseStatusToString(courses.getCourseStatus()));
                courseStartDateView.setText(Formatting.dateFormat.format(courses.getCourseStartDate()));
                courseEndDateView.setText(Formatting.dateFormat.format(courses.getCourseEndDate()));
                courseNotesView.setText(courses.getCourseNote());
                toolbar.setTitle(courses.getCourseName());
            }
        });

        //setup mentor data
        final Observer<List<Mentors>> mentorObserver = mentors -> {
            mentorsListData.clear();
            mentorsListData.addAll(mentors);
            if(mentorAdapter == null) {
                mentorAdapter = new MentorAdapter(mentorsListData, this, RecyclerAdapter.CHILD, this);
                courseMentorRecyclerView.setAdapter(mentorAdapter);
            } else {
                mentorAdapter.notifyDataSetChanged();
            }
        };

        //load the unassigned mentors
        final Observer<List<Mentors>> unassignedMentorObserver = mentors -> {
            unassignedMentorsList.clear();
            unassignedMentorsList.addAll(mentors);
        };

        //setup assessment data
        final Observer<List<Assessments>> assessmentObserver = assessments -> {
            assessmentsListData.clear();
            assessmentsListData.addAll(assessments);
            if(assessmentAdapter == null) {
                assessmentAdapter = new AssessmentAdapter(assessmentsListData, this, RecyclerAdapter.CHILD, this);
                courseAssessmentRecyclerView.setAdapter(assessmentAdapter);
            } else {
                assessmentAdapter.notifyDataSetChanged();
            }
        };

        //load the unassigned assessments
        final Observer<List<Assessments>> unassignedAssessmentObserver = assessments -> {
            unassignedAssessmentsList.clear();
            unassignedAssessmentsList.addAll(assessments);
        };

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            courseId = extras.getInt(COURSE_KEY);
            viewModel.loadCourse(courseId);
        } else {
            finish();
        }

        viewModel.getAssessmentsByCourseId(courseId).observe(this, assessmentObserver);
        viewModel.getUnassignedAssessments().observe(this, unassignedAssessmentObserver);
        viewModel.getMentorsByCourseId(courseId).observe(this, mentorObserver);
        viewModel.getUnassignedMentors().observe(this, unassignedMentorObserver);
    }

    @Override
    public void onMentorSelected(int position, Mentors mentor) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Mentor");
        builder.setMessage("Are you sure you want to delete this mentor? \nThis will remove" + mentor.getMentorName() + " from this course \nand delete them.");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("Yes", (dialog, which) -> {
            //remove mentor from course
            viewModel.overwriteMentor(mentor, -1);
            //delete mentor
            viewModel.deleteMentor();
            mentorAdapter.notifyDataSetChanged();
            dialog.dismiss();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onAssessmentSelected(int position, Assessments assessment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Assessment");
        builder.setMessage("Are you sure you want to delete this assessment? \nThis will remove" + assessment.getAssessmentName() + " from this course \nand delete it.");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("Yes", (dialog, which) -> {
            //remove assessment from course
            viewModel.overwriteAssessment(assessment, -1);
            //delete assessment
            viewModel.deleteAssessment();
            assessmentAdapter.notifyDataSetChanged();
            dialog.dismiss();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void addAssessmentFloatingButtonClicked(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Assessment");
        builder.setMessage("Add a new or existing Assessment to this course?");
        builder.setIcon(R.drawable.ic_action_add);
        builder.setPositiveButton("New", (dialog, which) -> {
            Intent intent = new Intent(this, AssessmentEditActivity.class);
            intent.putExtra(COURSE_KEY, courseId);
            this.startActivity(intent);
        });
        builder.setNegativeButton("Existing", (dialog, which) -> {
            //verify there are unassigned assessments
            if (unassignedAssessmentsList.size() >= 1) {
                final DropdownAssessments dropdownAssessments = new DropdownAssessments(this, unassignedAssessmentsList);
                dropdownAssessments.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
                dropdownAssessments.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
                dropdownAssessments.showAsDropDown(courseAddAssessmentButton);
                dropdownAssessments.setOutsideTouchable(true);
                dropdownAssessments.setFocusable(true);
                dropdownAssessments.setSelectedAssessmentListener((position, assessment) -> {
                    assessment.setAssessmentCourseId(courseId);
                    viewModel.overwriteAssessment(assessment, courseId);
                    dropdownAssessments.dismiss();
                });
            } else {
                //no unassigned assessments, show toast
                Toast.makeText(this, "No unassigned assessments to add.", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void addMentorFloatingButtonClicked(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Mentor");
        builder.setMessage("Add a new or existing Mentor to this course?");
        builder.setIcon(R.drawable.ic_action_add);
        builder.setPositiveButton("New", (dialog, which) -> {
            Intent intent = new Intent(this, MentorEditActivity.class);
            intent.putExtra(COURSE_KEY, courseId);
            this.startActivity(intent);
        });
        builder.setNegativeButton("Existing", (dialog, which) -> {
            //verify there are unassigned mentors
            if (unassignedMentorsList.size() >= 1) {
                final DropdownMentors dropdownMentors = new DropdownMentors(this, unassignedMentorsList);
                dropdownMentors.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
                dropdownMentors.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
                dropdownMentors.showAsDropDown(courseAddMentorButton);
                dropdownMentors.setOutsideTouchable(true);
                dropdownMentors.setFocusable(true);
                dropdownMentors.setSelectedMentorListener((position, mentor) -> {
                    mentor.setCourseId(courseId);
                    viewModel.overwriteMentor(mentor, courseId);
                    dropdownMentors.dismiss();
                });
            } else {
                //no unassigned mentors, show toast
                Toast.makeText(this, "No unassigned mentors to add.", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void shareButtonClicked(View view) {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        String shareHeader = "Course: " + courseTitleView.getText().toString() + "Notes";
        String shareMessage = "Notes: " + courseNotesView.getText().toString();
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, shareHeader);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        startActivity(Intent.createChooser(sendIntent, "Share Course Notes with"));
    }
}