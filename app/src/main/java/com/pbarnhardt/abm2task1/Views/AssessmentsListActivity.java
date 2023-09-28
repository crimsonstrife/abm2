package com.pbarnhardt.abm2task1.Views;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pbarnhardt.abm2task1.Adapters.AssessmentAdapter;
import com.pbarnhardt.abm2task1.Entity.Assessments;
import com.pbarnhardt.abm2task1.Enums.RecyclerAdapter;
import com.pbarnhardt.abm2task1.Models.AssessmentModel;
import com.pbarnhardt.abm2task1.R;

import java.util.ArrayList;
import java.util.List;

public class AssessmentsListActivity extends AppCompatActivity implements AssessmentAdapter.AssessmentSelection {
    /** @noinspection FieldMayBeFinal*/
    private List<Assessments> assessmentsList = new ArrayList<>();
    private AssessmentAdapter assessmentAdapter;

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

        // On click listener for add assessment button
        final FloatingActionButton addAssessmentButton = findViewById(R.id.floatingAddAssessmentButton);
        addAssessmentButton.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add Assessment");
            builder.setMessage("Are you sure you want to add an assessment?");
            builder.setIcon(R.drawable.ic_action_add);
            builder.setPositiveButton("Yes", (dialog, which) -> {
                Intent intent = new Intent(AssessmentsListActivity.this, AssessmentEditActivity.class);
                startActivity(intent);
            });
            builder.setNegativeButton("No", (dialog, which) -> {
                // Do nothing
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private void initializeViewModel() {
        RecyclerView recyclerView = findViewById(R.id.assessmentListRecyclerView);
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
        AssessmentModel assessmentModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(AssessmentModel.class);
        assessmentModel.assessments.observe(this, observer);
    }

    private void initializeRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.assessmentListRecyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onAssessmentSelected(int position, Assessments assessment) {

    }
}