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

import com.pbarnhardt.abm2task1.Adapters.CourseAdapter;
import com.pbarnhardt.abm2task1.Entity.Courses;
import com.pbarnhardt.abm2task1.Enums.RecyclerAdapter;
import com.pbarnhardt.abm2task1.Models.CourseModel;
//import com.pbarnhardt.abm2task1.Models.EditorModel;
import com.pbarnhardt.abm2task1.R;

import java.util.ArrayList;
import java.util.List;

public class CoursesListActivity extends AppCompatActivity implements CourseAdapter.CourseSelection {
    /**
     * Bind views.
     */
    RecyclerView recyclerView = findViewById(R.id.courseListRecyclerView);

    /**
     * Variables.
     */
    private CourseAdapter courseAdapter;
    private CourseModel courseModel;
    private List<Courses> coursesList = new ArrayList<>();

    /**
     * On create.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initializeRecyclerView();
        initializeViewModel();
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
        courseModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(CourseModel.class);
        courseModel.courses.observe(this, observer);
    }

    private void initializeRecyclerView() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onCourseSelected(int position, Courses course) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Edit or Delete Course");
//        builder.setMessage("What would you like to do?");
//        builder.setPositiveButton("Edit", (dialog, which) -> {
//            Intent intent = new Intent(CoursesListActivity.this, CourseEditActivity.class);
//            intent.putExtra("courseId", course.getCourseId());
//            startActivity(intent);
//        });
//        builder.setNegativeButton("Delete", (dialog, which) -> {
//            //setup confirmation dialog
//            AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(this);
//            deleteBuilder.setTitle("Delete Course");
//            deleteBuilder.setMessage("Are you sure you want to delete this course?");
//            deleteBuilder.setPositiveButton("Yes", (deleteDialog, deleteWhich) -> {
//                EditorModel editorModel = new EditorModel(getApplication());
//                editorModel.deleteCourse();
//            });
//            deleteBuilder.setNegativeButton("No", (deleteDialog, deleteWhich) -> {
//                // Do nothing
//            });
//        });
//        builder.setNeutralButton("Cancel", (dialog, which) -> {
//            // Do nothing
//        });
    }

    public void addCourseButtonClick(View view) {
        Intent intent = new Intent(this, CourseEditActivity.class);
        startActivity(intent);
    }
}