package com.pbarnhardt.abm2task1.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.pbarnhardt.abm2task1.Adapters.AssessmentAdapter;
import com.pbarnhardt.abm2task1.Entity.Assessments;
import com.pbarnhardt.abm2task1.Enums.RecyclerAdapter;
import com.pbarnhardt.abm2task1.Models.AssessmentModel;
import com.pbarnhardt.abm2task1.R;

import java.util.ArrayList;
import java.util.List;

public class AssessmentsListActivity extends AppCompatActivity implements AssessmentAdapter.AssessmentSelection {
    private List<Assessments> assessmentsList = new ArrayList<>();
    RecyclerView recyclerView = findViewById(R.id.assessmentListRecyclerView);
    private AssessmentAdapter assessmentAdapter;
    private AssessmentModel assessmentModel;

    /**
     * On create.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initializeRecyclerView();
        initializeViewModel();
    }

    private void initializeViewModel() {
        final Observer<List<Assessments>> observer = assessments -> {
            assessmentsList.clear();
            assessmentsList.addAll(assessments);
            if (assessmentAdapter == null) {
                assessmentAdapter = new AssessmentAdapter(assessmentsList, AssessmentsListActivity.this, RecyclerAdapter.MAIN, this);
                recyclerView.setAdapter(assessmentAdapter);
            } else {
                assessmentAdapter.notifyDataSetChanged();
            }
        };
        assessmentModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(AssessmentModel.class);
        assessmentModel.assessments.observe(this, observer);
    }

    private void initializeRecyclerView() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onAssessmentSelected(int position, Assessments assessment) {

    }

    public void addAssessmentFloatingButtonClicked(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Assessment");
        builder.setMessage("Are you sure you want to add an assessment?");
        builder.setIcon(R.drawable.ic_action_add);
        builder.setPositiveButton("Yes", (dialog, which) -> {
            Intent intent = new Intent(this, AssessmentEditActivity.class);
            startActivity(intent);
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            // Do nothing
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}