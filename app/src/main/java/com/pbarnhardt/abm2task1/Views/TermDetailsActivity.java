package com.pbarnhardt.abm2task1.Views;

import static com.pbarnhardt.abm2task1.Utils.Constants.TERM_KEY;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.pbarnhardt.abm2task1.Adapters.CourseAdapter;
import com.pbarnhardt.abm2task1.Entity.Courses;
import com.pbarnhardt.abm2task1.Entity.Terms;
import com.pbarnhardt.abm2task1.Enums.RecyclerAdapter;
import com.pbarnhardt.abm2task1.Models.EditorModel;
import com.pbarnhardt.abm2task1.Models.TermModel;
import com.pbarnhardt.abm2task1.Popups.DropdownCourses;
import com.pbarnhardt.abm2task1.R;
import com.pbarnhardt.abm2task1.TrackerApplication;
import com.pbarnhardt.abm2task1.Utils.Formatting;

import java.util.ArrayList;
import java.util.List;

public class TermDetailsActivity extends AppCompatActivity implements CourseAdapter.CourseSelection {
    private int termId;
    private List<Courses> coursesListData = new ArrayList<>();
    private List<Courses> unassignedCoursesList = new ArrayList<>();
    private LayoutInflater inflater;
    private CourseAdapter courseAdapter;
    private Toolbar toolbar;
    private EditorModel viewModel;

    /**
     * Assign views by id
     */
    TextView termTitleView = findViewById(R.id.termDetailTitle);
    TextView termStartDateView = findViewById(R.id.termDetailStartDate);
    TextView termEndDateView = findViewById(R.id.termDetailEndDate);
    RecyclerView termCourseRecyclerView = findViewById(R.id.termCoursesRecyclerView);
    FloatingActionButton termAddCourseButton = findViewById(R.id.floatingAddCourseToTermButton);

    /**
     * On create method
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initiateRecyclerView();
        initiateViewModel();
    }

    private void initiateViewModel() {
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(EditorModel.class);
        viewModel.liveTerms.observe(this, terms -> {
            if (terms != null) {
                termTitleView.setText(terms.getTermName());
                termStartDateView.setText(Formatting.dateFormat.format(terms.getTermStartDate()));
                termEndDateView.setText(Formatting.dateFormat.format(terms.getTermEndDate()));
                termId = terms.getTermId();
                toolbar.setTitle(terms.getTermName());
            }
        });
        //load related courses
        final Observer<List<Courses>> observer = courses -> {
            coursesListData.clear();
            coursesListData.addAll(courses);
            if (courseAdapter == null) {
                courseAdapter = new CourseAdapter(coursesListData, TermDetailsActivity.this, RecyclerAdapter.CHILD, this);
                termCourseRecyclerView.setAdapter(courseAdapter);
            } else {
                courseAdapter.notifyDataSetChanged();
            }
        };
        //load unassigned courses (courses not assigned to a term), so they can potentially be added to this term
        final Observer<List<Courses>> unassignedObserver = unassignedCourses -> {
            unassignedCoursesList.clear();
            unassignedCoursesList.addAll(unassignedCourses);
        };

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            termId = extras.getInt(TERM_KEY);
            viewModel.loadTerm(termId);
        } else {
            finish();
        }

        viewModel.getCoursesByTermId(termId).observe(this, observer);
        viewModel.getUnassignedCourses().observe(this, unassignedObserver);
    }

    private void initiateRecyclerView() {
        termCourseRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        termCourseRecyclerView.setLayoutManager(layoutManager);
    }

    /**
     * On term edit button click
     */
    public void termEditButtonClick(View view) {
        Intent intent = new Intent(this, TermEditActivity.class);
        intent.putExtra(TERM_KEY, termId);
        startActivity(intent);
        finish();
    }

    /**
     * On add course button click
     */
    public void addCourseButtonClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Course to Term");
        builder.setMessage("Would you like to add a new or an existing course to this term?");
        builder.setIcon(R.drawable.ic_action_add);
        builder.setPositiveButton("New Course", (dialog, id) -> {
            dialog.dismiss();
            Intent intent = new Intent(this, CourseEditActivity.class);
            intent.putExtra(TERM_KEY, termId);
            this.startActivity(intent);
        });
        builder.setNegativeButton("Existing Course", (dialog, id) -> {
            // Check that at least one unassigned course exists
            if (unassignedCoursesList.size() >= 1) {
                final DropdownCourses menu = new DropdownCourses(this, unassignedCoursesList);
                menu.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
                menu.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
                menu.setFocusable(true);
                menu.setOutsideTouchable(true);
                menu.showAsDropDown(termAddCourseButton);
                menu.setSelectedCourseListener((position, course) -> {
                    menu.dismiss();
                    course.setTermId(termId);
                    viewModel.overwriteCourse(course, termId);
                });
            } else {
                //if we don't have any unassigned courses, notify the user
                Toast.makeText(getApplicationContext(), "Could not find unassigned courses. Please create a new course.", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * On course selected
     * @param position
     * @param course
     */
    @Override
    public void onCourseSelected(int position, Courses course) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remove Course from Term");
        builder.setMessage("Would you like to remove this course from this term? \nThis will not delete the course, /\nonly remove it from this term and make it unassigned.");
        builder.setIcon(R.drawable.ic_action_delete);
        builder.setPositiveButton("Remove", (dialog, id) -> {
            dialog.dismiss();
            viewModel.overwriteCourse(course, -1);
            courseAdapter.notifyDataSetChanged();
        });
        builder.setNegativeButton("Cancel", (dialog, id) -> {
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}