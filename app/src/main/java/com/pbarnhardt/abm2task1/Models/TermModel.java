package com.pbarnhardt.abm2task1.Models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pbarnhardt.abm2task1.Database.CourseRepository;
import com.pbarnhardt.abm2task1.Entity.Terms;

import java.util.List;

public class TermModel extends AndroidViewModel {

    private CourseRepository courseRepository;
    public LiveData<List<Terms>> terms;

    public TermModel(@NonNull Application application) {
        super(application);
        courseRepository = CourseRepository.getInstance(application.getApplicationContext());
        terms = courseRepository.mAllTerms;
    }
}
