package com.pbarnhardt.abm2task1.Views;

import static com.pbarnhardt.abm2task1.Utils.Constants.COURSE_DESCRIPTION;
import static com.pbarnhardt.abm2task1.Utils.Constants.COURSE_END_ALARM;
import static com.pbarnhardt.abm2task1.Utils.Constants.COURSE_END_DATE;
import static com.pbarnhardt.abm2task1.Utils.Constants.COURSE_ID;
import static com.pbarnhardt.abm2task1.Utils.Constants.COURSE_KEY;
import static com.pbarnhardt.abm2task1.Utils.Constants.COURSE_NAME;
import static com.pbarnhardt.abm2task1.Utils.Constants.COURSE_NOTES;
import static com.pbarnhardt.abm2task1.Utils.Constants.COURSE_START_ALARM;
import static com.pbarnhardt.abm2task1.Utils.Constants.COURSE_START_DATE;
import static com.pbarnhardt.abm2task1.Utils.Constants.COURSE_STATUS;
import static com.pbarnhardt.abm2task1.Utils.Constants.COURSE_TERM_ID;
import static com.pbarnhardt.abm2task1.Utils.Converters.fromCourseStatusToString;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pbarnhardt.abm2task1.Adapters.AssessmentAdapter;
import com.pbarnhardt.abm2task1.Adapters.MentorAdapter;
import com.pbarnhardt.abm2task1.Entity.Assessments;
import com.pbarnhardt.abm2task1.Entity.Courses;
import com.pbarnhardt.abm2task1.Entity.Mentors;
import com.pbarnhardt.abm2task1.Enums.RecyclerAdapter;
import com.pbarnhardt.abm2task1.Models.EditorModel;
import com.pbarnhardt.abm2task1.Popups.DropdownAssessments;
import com.pbarnhardt.abm2task1.Popups.DropdownMentors;
import com.pbarnhardt.abm2task1.R;
import com.pbarnhardt.abm2task1.Utils.Alerts;
import com.pbarnhardt.abm2task1.Utils.Formatting;
import com.pbarnhardt.abm2task1.databinding.ActivityCourseDetailsBinding;
import com.pbarnhardt.abm2task1.databinding.ContentDetailsCoursesBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class CourseDetailsActivity extends AppCompatActivity implements MentorAdapter.MentorSelectedListener, AssessmentAdapter.AssessmentSelection {
    /**
     * Variables
     */
    private Toolbar toolbar;
    private EditorModel viewModel;
    private int courseId;
    private AssessmentAdapter assessmentAdapter;
    private MentorAdapter mentorAdapter;
    private final List<Assessments> assessmentsListData = new ArrayList<>();
    private final List<Mentors> mentorsListData = new ArrayList<>();
    private final List<Assessments> unassignedAssessmentsList = new ArrayList<>();
    private final List<Mentors> unassignedMentorsList = new ArrayList<>();
    private ActivityCourseDetailsBinding activityBinding;
    private ContentDetailsCoursesBinding contentBinding;
    private RecyclerView courseAssessmentsRecyclerView;
    private RecyclerView courseMentorsRecyclerView;
    private CheckBox editCourseStartAlert;
    private CheckBox editCourseEndAlert;
    private TextView courseTitleView;
    private TextView courseDescriptionView;
    private TextView courseStatusView;
    private TextView courseStartDateView;
    private TextView courseEndDateView;
    private TextView courseNotesView;
    private Courses course;


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
        //set toolbar color
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_action_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
            intent.putExtra(COURSE_ID, courseId);
            intent.putExtra(COURSE_NAME, courseTitleView.getText().toString());
            intent.putExtra(COURSE_START_DATE, courseStartDateView.getText().toString());
            intent.putExtra(COURSE_END_DATE, courseEndDateView.getText().toString());
            intent.putExtra(COURSE_STATUS, courseStatusView.getText().toString());
            intent.putExtra(COURSE_NOTES, courseNotesView.getText().toString());
            intent.putExtra(COURSE_TERM_ID, Objects.requireNonNull(viewModel.liveCourses.getValue()).getTermId());
            intent.putExtra(COURSE_DESCRIPTION, courseDescriptionView.getText().toString());
            intent.putExtra(COURSE_START_ALARM, viewModel.liveCourses.getValue().getCourseStartAlert());
            intent.putExtra(COURSE_END_ALARM, viewModel.liveCourses.getValue().getCourseEndAlert());
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
                courseMentorsRecyclerView.setAdapter(mentorAdapter);
            } else {
                mentorAdapter.notifyItemRangeChanged(0, mentorsListData.size());
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
                assessmentAdapter.notifyItemRangeChanged(0, assessmentsListData.size());
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
            mentorAdapter.notifyItemRangeChanged(0, mentorsListData.size());
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
            assessmentAdapter.notifyItemRangeChanged(0, assessmentsListData.size());
            dialog.dismiss();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
           MenuInflater inflater = getMenuInflater();
           inflater.inflate(R.menu.menu_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.action_delete) {
            //ask user to confirm delete action of course
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete Course");
            builder.setMessage("Are you sure you want to delete this course? \nThis will leave unassigned assessments and/or mentors.");
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.setPositiveButton("Yes", (dialog, which) -> {
                //reassign any assessments or mentors to -1
                int thisCourseId = viewModel.liveCourses.getValue().getCourseId();
                List<Assessments> assessments = viewModel.assessmentsList.getValue();
                List<Mentors> mentors = viewModel.mentorsList.getValue();
                //if there are assessments, for each assessment, if the course id matches, set it to -1
                if (assessments != null) {
                    for (Assessments assessment : assessments) {
                        if (assessment.getAssessmentCourseId() == thisCourseId) {
                            assessment.setAssessmentCourseId(-1);
                            viewModel.overwriteAssessment(assessment, -1);
                        }
                    }
                }
                //if there are mentors, for each mentor, if the course id matches, set it to -1
                if (mentors != null) {
                    for (Mentors mentor : mentors) {
                        if (mentor.getCourseId() == thisCourseId) {
                            mentor.setCourseId(-1);
                            viewModel.overwriteMentor(mentor, -1);
                        }
                    }
                }
                //delete course
                viewModel.deleteCourse();
                dialog.dismiss();
                finish();
            });
        } else if (item.getItemId() == R.id.action_help) {
            //TODO: add help dialog
        } else if (item.getItemId() == R.id.action_notify) {
            Intent intent = new Intent(this, Alerts.class);
            //current course id
            int currentId = viewModel.liveCourses.getValue().getCourseId();
            if (currentId > 0) {
                Date currentDate = viewModel.liveCourses.getValue().getCourseEndDate();
                Long trigger = currentDate.getTime();
                intent.putExtra("notification", courseTitleView.getText().toString() + " ends today!");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, currentId, intent, PendingIntent.FLAG_IMMUTABLE);
                //set the alarm
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, pendingIntent);
                Toast.makeText(this, "Notification set for " + Formatting.dateFormat.format(currentDate), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Unable to set notification", Toast.LENGTH_LONG).show();
            }
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}