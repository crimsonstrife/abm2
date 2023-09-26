package com.pbarnhardt.abm2task1.Models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pbarnhardt.abm2task1.Database.CourseRepository;
import com.pbarnhardt.abm2task1.Entity.Assessments;
import com.pbarnhardt.abm2task1.Entity.Courses;
import com.pbarnhardt.abm2task1.Entity.Mentors;
import com.pbarnhardt.abm2task1.Entity.Terms;

import java.util.List;

public class HomeModel extends AndroidViewModel {
    public LiveData<List<Terms>> termsList;
    public LiveData<List<Courses>> coursesList;
    public LiveData<List<Assessments>> assessmentsList;
    public LiveData<List<Mentors>> mentorsList;

    private CourseRepository repository;

    public HomeModel(@NonNull Application application) {
        super(application);

        repository = CourseRepository.getInstance(application.getApplicationContext());
        termsList = repository.getAllTerms();
        coursesList = repository.getAllCourses();
        assessmentsList = repository.getAllAssessments();
        mentorsList = repository.getAllMentors();
    }

    public LiveData<List<Terms>> getAllTerms() {
        return repository.getAllTerms();
    }

    public void addSampleDataset() {
        repository.addSampleDataset();
    }

    public void deleteAllData() {
        repository.deleteAllData();
    }
}
