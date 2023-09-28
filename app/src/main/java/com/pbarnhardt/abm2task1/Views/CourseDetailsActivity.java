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
import com.pbarnhardt.abm2task1.Models.AssessmentModel;
import com.pbarnhardt.abm2task1.Models.EditorModel;
import com.pbarnhardt.abm2task1.Models.MentorModel;
import com.pbarnhardt.abm2task1.Popups.DropdownAssessments;
import com.pbarnhardt.abm2task1.Popups.DropdownMentors;
import com.pbarnhardt.abm2task1.R;
import com.pbarnhardt.abm2task1.Utils.Formatting;
import com.pbarnhardt.abm2task1.databinding.ActivityCourseDetailsBinding;
import com.pbarnhardt.abm2task1.databinding.ContentDetailsCoursesBinding;

import java.util.ArrayList;
import java.util.List;

public class CourseDetailsActivity extends AppCompatActivity implements MentorAdapter.MentorSelectedListener, AssessmentAdapter.AssessmentSelection {
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
    private ActivityCourseDetailsBinding activityBinding;
    private ContentDetailsCoursesBinding contentBinding;
    private RecyclerView courseAssessmentsRecyclerView;
    private RecyclerView courseMentorsRecyclerView;
    private TextView courseTitleView;
    private TextView courseDescriptionView;
    private TextView courseStatusView;
    private TextView courseStartDateView;
    private TextView courseEndDateView;
    private TextView courseNotesView;


    /**
     * On create method
     * @param savedInstanceState saved instance state
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = ActivityCourseDetailsBinding.inflate(getLayoutInflater());
        View view = activityBinding.getRoot();
        setContentView(view);
        toolbar = activityBinding.toolbar;
        setSupportActionBar(toolbar);

        initiateViewModel();

        //initialize the binding
        contentBinding = activityBinding.contentInclude;
        courseTitleView = contentBinding.editTextCourseTitle;
        courseDescriptionView = contentBinding.courseDetailDescription;
        courseStatusView = contentBinding.spinnerCourseStatus;
        courseStartDateView = contentBinding.termDetailStartDate;
        courseEndDateView = contentBinding.termDetailEndDate;
        courseNotesView = contentBinding.editTextCourseNote;
        courseAssessmentsRecyclerView = contentBinding.courseAssessmentsRecyclerView;
        courseMentorsRecyclerView = contentBinding.courseMentorsRecyclerView;


        initializeRecyclerView(courseAssessmentsRecyclerView);
        initializeRecyclerView(courseMentorsRecyclerView);

        //setup buttons
        activityBinding.floatingEditCourseButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, CourseEditActivity.class);
            intent.putExtra(COURSE_KEY, courseId);
            this.startActivity(intent);
            finish();
        });
        contentBinding.floatingAddMentorToCourseButton.setOnClickListener(v -> {
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
                    dropdownMentors.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
                    dropdownMentors.showAsDropDown(contentBinding.floatingAddMentorToCourseButton);
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
        });
        contentBinding.floatingAddAssessmentToCourseButton.setOnClickListener(v -> {
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
                    dropdownAssessments.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
                    dropdownAssessments.showAsDropDown(contentBinding.floatingAddAssessmentToCourseButton);
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
            contentBinding.shareButton.setOnClickListener(v1 -> {
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                String shareHeader = "Course: " + contentBinding.editTextCourseTitle.getText().toString() + "Notes";
                String shareMessage = "Notes: " + contentBinding.editTextCourseNote.getText().toString();
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, shareHeader);
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(sendIntent, "Share Course Notes with"));
            });
        });
    }

    private void initializeRecyclerView (RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void initiateViewModel() {
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(EditorModel.class);
        viewModel.liveCourses.observe(this, courses -> {
            if(courses != null) {
                courseId = courses.getCourseId();
                contentBinding.editTextCourseTitle.setText(courses.getCourseName());
                contentBinding.courseDetailDescription.setText(courses.getCourseDescription());
                contentBinding.spinnerCourseStatus.setText(fromCourseStatusToString(courses.getCourseStatus()));
                contentBinding.termDetailStartDate.setText(Formatting.dateFormat.format(courses.getCourseStartDate()));
                contentBinding.termDetailEndDate.setText(Formatting.dateFormat.format(courses.getCourseEndDate()));
                contentBinding.editTextCourseNote.setText(courses.getCourseNote());
                toolbar.setTitle(courses.getCourseName());
            }
        });

        //setup mentor data
        final Observer<List<Mentors>> mentorObserver = mentors -> {
            mentorsListData.clear();
            mentorsListData.addAll(mentors);
            if(mentorAdapter == null) {
                mentorAdapter = new MentorAdapter(mentorsListData, this, RecyclerAdapter.CHILD, this);
                courseMentorsRecyclerView.setAdapter(mentorAdapter);
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
                courseAssessmentsRecyclerView.setAdapter(assessmentAdapter);
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
}