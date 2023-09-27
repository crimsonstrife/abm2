package com.pbarnhardt.abm2task1.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pbarnhardt.abm2task1.Entity.Courses;
import com.pbarnhardt.abm2task1.Enums.RecyclerAdapter;
import com.pbarnhardt.abm2task1.R;
import com.pbarnhardt.abm2task1.Utils.Formatting;
import com.pbarnhardt.abm2task1.Views.CourseDetailsActivity;
import com.pbarnhardt.abm2task1.Views.CourseEditActivity;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder>{
    private final List<Courses> listedCourses;
    private final Context theContext;
    private final RecyclerAdapter recyclerContext;
    private CourseSelection courseSelected;

    public interface CourseSelection {
        void onCourseSelected(int position, Courses course);
    }

    public CourseAdapter(List<Courses> listedCourses, Context theContext, RecyclerAdapter recyclerContext, CourseSelection courseSelected) {
        this.listedCourses = listedCourses;
        this.theContext = theContext;
        this.recyclerContext = recyclerContext;
        this.courseSelected = courseSelected;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //bind views here
        TextView courseTitleView = itemView.findViewById(R.id.course_title);
        TextView courseDatesView = itemView.findViewById(R.id.course_dates);
        //bind button
        FloatingActionButton courseFloatingActionButton = itemView.findViewById(R.id.course_floatingEditButton);
        ImageButton courseDetailsButton = itemView.findViewById(R.id.courseDetails);
        CourseSelection courseSelected;
        public ViewHolder(@NonNull View itemView, CourseSelection courseSelected) {
            super(itemView);
            this.courseSelected = courseSelected;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            courseSelected.onCourseSelected(getAdapterPosition(), listedCourses.get(getAdapterPosition()));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.card_list_courses, parent, false);
        return new ViewHolder(view, courseSelected);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Courses course = listedCourses.get(position);
        holder.courseTitleView.setText(course.getCourseName());
        String startAndEnd = Formatting.dateFormat.format(course.getCourseStartDate()) + " - " + Formatting.dateFormat.format(course.getCourseEndDate());
        holder.courseDatesView.setText(startAndEnd);

        switch (recyclerContext) {
            case MAIN:
                holder.courseFloatingActionButton.setImageDrawable(ContextCompat.getDrawable(theContext, R.drawable.ic_action_edit));
                holder.courseDetailsButton.setOnClickListener(v -> {
                    Intent intent = new Intent(theContext, CourseDetailsActivity.class);
                    intent.putExtra("COURSE_ID", course.getCourseId());
                    theContext.startActivity(intent);
                });
                holder.courseFloatingActionButton.setOnClickListener(v -> {
                    Intent intent = new Intent(theContext, CourseEditActivity.class);
                    intent.putExtra("COURSE_ID", course.getCourseId());
                    theContext.startActivity(intent);
                });
                break;
            case CHILD:
                holder.courseFloatingActionButton.setImageDrawable(ContextCompat.getDrawable(theContext, R.drawable.ic_action_delete));
                holder.courseDetailsButton.setOnClickListener(v -> {
                    if (courseSelected != null) {
                        courseSelected.onCourseSelected(position, course);
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(theContext);
                    builder.setTitle("Are you sure you want to delete this course?");
                    builder.setMessage("This will not delete the course permanently, it will only unassign it from this term.");
                    builder.setIcon(android.R.drawable.ic_dialog_alert);
                    builder.setPositiveButton("Continue", (dialog, id) -> {
                        dialog.dismiss();
                        course.setTermId(-1);
                        listedCourses.remove(position);
                        notifyDataSetChanged();
                    });
                    builder.setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());
                    AlertDialog dialog = builder.create();
                    dialog.show();
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return listedCourses.size();
    }
}
