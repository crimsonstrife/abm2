package com.pbarnhardt.abm2task1.Models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pbarnhardt.abm2task1.Database.CourseRepository;
import com.pbarnhardt.abm2task1.Entity.Courses;

import java.util.List;

public class CourseModel extends AndroidViewModel {
    public LiveData<List<Courses>> courses;

    public CourseModel(@NonNull Application application) {
        super(application);
        CourseRepository repository = CourseRepository.getInstance(application.getApplicationContext());
        courses = repository.mAllCourses;
    }
}
