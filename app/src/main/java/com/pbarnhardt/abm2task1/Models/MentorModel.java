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

    public MentorModel(@NonNull Application application) {
        super(application);
        CourseRepository repository = CourseRepository.getInstance(application.getApplicationContext());
        mentors = repository.mAllMentors;
    }
}
