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

    /**
     * Adds a sample dataset to the database
     * @return - true if successful, false if not
     */
    public boolean addSampleDataset() {
        boolean success = repository.addSampleDataset();
        if (success) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Deletes all data from the database
     * @return - true if successful, false if not
     */
    public boolean deleteAllData() {
        boolean success = repository.deleteAllData();
        if (success) {
            return true;
        } else {
            return false;
        }
    }
}
