package com.pbarnhardt.abm2task1.Popups;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pbarnhardt.abm2task1.Adapters.CoursePopperAdapter;
import com.pbarnhardt.abm2task1.Entity.Courses;
import com.pbarnhardt.abm2task1.R;

import java.util.List;

public class DropdownCourses extends PopupWindow {
    /**
     * Variables
     */
    private Context theContext;
    private List<Courses> coursesList;
    private CoursePopperAdapter popperAdapter;

    /**
     * Constructor
     * @param theContext Context
     * @param coursesList List of courses
     */
    public DropdownCourses(Context theContext, List<Courses> coursesList) {
        super(theContext);
        this.theContext = theContext;
        this.coursesList = coursesList;
        setupView();
    }

    private void setupView() {
        View view = LayoutInflater.from(theContext).inflate(R.layout.menu_popup, null);
        RecyclerView popupRecyclerView = view.findViewById(R.id.popupRecyclerView);
        popupRecyclerView.setHasFixedSize(true);
        popupRecyclerView.setLayoutManager(new LinearLayoutManager(theContext, LinearLayoutManager.VERTICAL, false));
        popupRecyclerView.addItemDecoration(new DividerItemDecoration(theContext, LinearLayoutManager.VERTICAL));

        popperAdapter = new CoursePopperAdapter(coursesList);
        popupRecyclerView.setAdapter(popperAdapter);
        setContentView(view);
    }

    public void setSelectedCourseListener(CoursePopperAdapter.CourseListener setCourseListener) {
        popperAdapter.setCourseListener(setCourseListener);
    }
}
