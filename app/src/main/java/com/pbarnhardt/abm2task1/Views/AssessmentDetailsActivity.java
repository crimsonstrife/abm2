package com.pbarnhardt.abm2task1.Views;

import static com.pbarnhardt.abm2task1.Utils.Constants.ASSESSMENT_KEY;
import static com.pbarnhardt.abm2task1.Utils.Converters.fromAssessmentTypeToString;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.pbarnhardt.abm2task1.Entity.Courses;
import com.pbarnhardt.abm2task1.Models.EditorModel;
import com.pbarnhardt.abm2task1.R;
import com.pbarnhardt.abm2task1.Utils.Alerts;
import com.pbarnhardt.abm2task1.Utils.Formatting;
import com.pbarnhardt.abm2task1.databinding.ActivityAssessmentDetailsBinding;
import com.pbarnhardt.abm2task1.databinding.ContentDetailsAssessmentsBinding;

import java.util.Date;
import java.util.Objects;

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
        //set toolbar color
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_action_check);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); //close the activity
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
                    intent.putExtra("notification", assessmentTitleView.getText().toString() + " is starting today!");
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
                    intent.putExtra("notification", assessmentTitleView.getText().toString() + " is due today!");
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
                    intent.putExtra("notification", assessmentTitleView.getText().toString() + " is starting today!");
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
                    intent2.putExtra("notification", assessmentTitleView.getText().toString() + " is due today!");
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