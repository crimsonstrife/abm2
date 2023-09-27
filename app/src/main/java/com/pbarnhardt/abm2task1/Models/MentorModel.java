package com.pbarnhardt.abm2task1.Models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pbarnhardt.abm2task1.Database.CourseRepository;
import com.pbarnhardt.abm2task1.Entity.Mentors;

import java.util.List;

public class MentorModel extends AndroidViewModel {
    public LiveData<List<Mentors>> mentors;
    private CourseRepository repository;

    public MentorModel(@NonNull Application application) {
        super(application);
        repository = CourseRepository.getInstance(application.getApplicationContext());
        mentors = repository.mAllMentors;
    }
}
