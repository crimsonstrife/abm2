package com.pbarnhardt.abm2task1.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pbarnhardt.abm2task1.Entity.Courses;
import com.pbarnhardt.abm2task1.R;

import java.util.List;

public class CoursePopperAdapter  extends RecyclerView.Adapter<CoursePopperAdapter.ViewHolder> {
    private List<Courses> coursesList;
    private CourseListener courseListener;

    /**
     * Constructor
     * @param coursesList
     */
    public CoursePopperAdapter(List<Courses> coursesList) {
        super();
        this.coursesList = coursesList;
    }

    /**
     * Listener
     * @param courseListener
     */
    public void setCourseListener(CourseListener courseListener) {
        this.courseListener = courseListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_course, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Courses course = coursesList.get(position);
        holder.itemTitle.setText(course.getCourseName());
        holder.courseItemAddIcon.setOnClickListener(view -> {
            if(courseListener != null) {
                courseListener.onCourseSelected(position, course);
            }
        });
    }

    @Override
    public int getItemCount() {
        return coursesList.size();
    }

    public interface CourseListener {
        void onCourseSelected(int position, Courses course);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemTitle;
        ImageView courseItemAddIcon;

        public ViewHolder(View item) {
            super(item);
            itemTitle = item.findViewById(R.id.itemTitle);
            courseItemAddIcon = item.findViewById(R.id.courseItemAddIcon);
        }

    }
}
