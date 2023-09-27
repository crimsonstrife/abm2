package com.pbarnhardt.abm2task1.Models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pbarnhardt.abm2task1.Database.CourseRepository;
import com.pbarnhardt.abm2task1.Entity.Assessments;

import java.util.List;

public class AssessmentModel extends AndroidViewModel {
    public LiveData<List<Assessments>> assessments;
    private CourseRepository repository;

    public AssessmentModel(@NonNull Application application) {
        super(application);
        repository = CourseRepository.getInstance(application.getApplicationContext());
        assessments = repository.mAllAssessments;
    }
}
