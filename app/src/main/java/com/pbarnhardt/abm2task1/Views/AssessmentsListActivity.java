package com.pbarnhardt.abm2task1.Views;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.pbarnhardt.abm2task1.Adapters.AssessmentAdapter;
import com.pbarnhardt.abm2task1.Entity.Assessments;
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
    }

    private void initializeRecyclerView() {
    }

    @Override
    public void onAssessmentSelected(int position, Assessments assessment) {

    }
}