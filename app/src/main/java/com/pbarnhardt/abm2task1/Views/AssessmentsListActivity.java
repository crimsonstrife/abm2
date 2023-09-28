package com.pbarnhardt.abm2task1.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
import com.pbarnhardt.abm2task1.databinding.ActivityAssessmentListBinding;
import com.pbarnhardt.abm2task1.databinding.ContentListAssessmentsBinding;

import java.util.ArrayList;
import java.util.List;

public class AssessmentsListActivity extends AppCompatActivity implements AssessmentAdapter.AssessmentSelection {
    /** @noinspection FieldMayBeFinal*/
    private List<Assessments> assessmentsList = new ArrayList<>();
    private AssessmentAdapter assessmentAdapter;
    private ActivityAssessmentListBinding activityBinding;
    private ContentListAssessmentsBinding contentBinding;
    private RecyclerView recyclerView;

    /**
     * On create.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = ActivityAssessmentListBinding.inflate(getLayoutInflater());
        View view = activityBinding.getRoot();
        setContentView(view);
        Toolbar toolbar = activityBinding.toolbar;
        setSupportActionBar(toolbar);

        initializeViewModel();

        //initialize the binding
        contentBinding = activityBinding.contentInclude;
        recyclerView = contentBinding.assessmentListRecyclerView;
        initializeRecyclerView(recyclerView);

        // On click listener for add assessment button
        activityBinding.floatingAddAssessmentButton.setOnClickListener(v -> {
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

    private void initializeRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onAssessmentSelected(int position, Assessments assessment) {

    }
}