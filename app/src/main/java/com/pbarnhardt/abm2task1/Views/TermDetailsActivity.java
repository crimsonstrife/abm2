package com.pbarnhardt.abm2task1.Views;

import static com.pbarnhardt.abm2task1.Utils.Constants.TERM_KEY;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import com.pbarnhardt.abm2task1.Adapters.CourseAdapter;
import com.pbarnhardt.abm2task1.Entity.Courses;
import com.pbarnhardt.abm2task1.Enums.RecyclerAdapter;
import com.pbarnhardt.abm2task1.Models.EditorModel;
import com.pbarnhardt.abm2task1.Popups.DropdownCourses;
import com.pbarnhardt.abm2task1.R;
import com.pbarnhardt.abm2task1.Utils.Formatting;
import com.pbarnhardt.abm2task1.databinding.ActivityTermDetailsBinding;
import com.pbarnhardt.abm2task1.databinding.ContentDetailsTermsBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TermDetailsActivity extends AppCompatActivity implements CourseAdapter.CourseSelection {
    private int termId;
    private final List<Courses> coursesListData = new ArrayList<>();
    private final List<Courses> unassignedCoursesList = new ArrayList<>();
    private CourseAdapter courseAdapter;
    private Toolbar toolbar;
    private EditorModel viewModel;
    private ActivityTermDetailsBinding activityBinding;
    private ContentDetailsTermsBinding contentBinding;
    private TextView termTitleView;
    private TextView termStartDateView;
    private TextView termEndDateView;
    private RecyclerView recyclerView;

    /**
     * On create method
     * @param savedInstanceState saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = ActivityTermDetailsBinding.inflate(getLayoutInflater());
        View view = activityBinding.getRoot();
        setContentView(view);
        toolbar = activityBinding.toolbar;
        setSupportActionBar(toolbar);
        //set toolbar color
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_action_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //initialize the binding
        contentBinding = activityBinding.contentInclude;
        termTitleView = contentBinding.termDetailTitle;
        termStartDateView = contentBinding.termDetailDueDate;
        termEndDateView = contentBinding.termDetailEndDate;
        recyclerView = contentBinding.termCoursesRecyclerView;

        initiateRecyclerView(recyclerView);
        initiateViewModel();

        //floating action button
        contentBinding.floatingAddCourseToTermButton.setOnClickListener(v -> {
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
                    menu.showAsDropDown(contentBinding.floatingAddCourseToTermButton);
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
        });
        activityBinding.floatingEditTermButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, TermEditActivity.class);
            intent.putExtra(TERM_KEY, termId);
            startActivity(intent);
            finish();
        });
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
                recyclerView.setAdapter(courseAdapter);
            } else {
                courseAdapter.notifyItemRangeChanged(0, coursesListData.size());
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

    private void initiateRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    /**
     * On course selected
     * @param position position of course
     * @param course course
     */
    @Override
    public void onCourseSelected(int position, Courses course) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remove Course from Term");
        builder.setMessage("Would you like to remove this course from this term? \nThis will not delete the course, \nonly remove it from this term and make it unassigned.");
        builder.setIcon(R.drawable.ic_action_delete);
        builder.setPositiveButton("Remove", (dialog, id) -> {
            dialog.dismiss();
            viewModel.overwriteCourse(course, -1);
            courseAdapter.notifyItemRangeChanged(0, coursesListData.size());
            courseAdapter.notifyItemChanged(position);
        });
        builder.setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
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
        } else if(item.getItemId() == R.id.action_delete) {
                doDelete();
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Do delete of the term
     */
    private void doDelete() {
        if(viewModel.liveTerms.getValue() != null) {
            String title = viewModel.liveTerms.getValue().getTermName();
            if(coursesListData != null && coursesListData.size() > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Problem deleting " + title);
                builder.setMessage("You cannot delete a term that has courses assigned to it. \nYou must first delete the courses assigned to this term.  \nThis deletion will be cancelled.");
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setPositiveButton("OK", (dialog, id) -> {
                    dialog.dismiss();
                    finish();
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Delete " + title + "?");
                builder.setMessage("Are you sure you want to delete term '" + title + "'?");
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setPositiveButton("Delete", (dialog, id) -> {
                    dialog.dismiss();
                    viewModel.deleteTerm();
                    finish();
                });
                builder.setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }
}