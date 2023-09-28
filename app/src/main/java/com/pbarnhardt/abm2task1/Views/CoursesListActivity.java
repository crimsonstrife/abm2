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
import com.pbarnhardt.abm2task1.Adapters.CourseAdapter;
import com.pbarnhardt.abm2task1.Entity.Courses;
import com.pbarnhardt.abm2task1.Enums.RecyclerAdapter;
import com.pbarnhardt.abm2task1.Models.CourseModel;
import com.pbarnhardt.abm2task1.R;
import com.pbarnhardt.abm2task1.databinding.ActivityCourseListBinding;
import com.pbarnhardt.abm2task1.databinding.ContentListCoursesBinding;

import java.util.ArrayList;
import java.util.List;

public class CoursesListActivity extends AppCompatActivity implements CourseAdapter.CourseSelection {
    /**
     * Variables.
     */
    private CourseAdapter courseAdapter;
    private List<Courses> coursesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ActivityCourseListBinding activityBinding;
    private ContentListCoursesBinding contentBinding;

    /**
     * On create.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = ActivityCourseListBinding.inflate(getLayoutInflater());
        View view = activityBinding.getRoot();
        setContentView(view);
        Toolbar toolbar = activityBinding.toolbar;
        setSupportActionBar(toolbar);

        initializeViewModel();

        //initialize the binding
        contentBinding = activityBinding.contentInclude;
        recyclerView = contentBinding.courseListRecyclerView;
        initializeRecyclerView(recyclerView);

        //on click listener for the floating action button
        activityBinding.floatingAddCourseButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add Course");
            builder.setMessage("Are you sure you want to add a course?");
            builder.setIcon(R.drawable.ic_action_add);
            builder.setPositiveButton("Yes", (dialog, which) -> {
                Intent intent = new Intent(CoursesListActivity.this, CourseEditActivity.class);
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
        final Observer<List<Courses>> observer = courses -> {
            coursesList.clear();
            coursesList.addAll(courses);
            if (courseAdapter == null) {
                courseAdapter = new CourseAdapter(coursesList, this, RecyclerAdapter.MAIN, this);
                recyclerView.setAdapter(courseAdapter);
            } else {
                courseAdapter.notifyDataSetChanged();
            }
        };
        CourseModel courseModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(CourseModel.class);
        courseModel.courses.observe(this, observer);
    }

    private void initializeRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onCourseSelected(int position, Courses course) {

    }
}