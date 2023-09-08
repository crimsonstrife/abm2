package com.pbarnhardt.abm2task1.models;

import androidx.lifecycle.ViewModel;

import com.pbarnhardt.abm2task1.Database.CourseRepository;
import com.pbarnhardt.abm2task1.Entity.Terms;

import java.util.List;

public class TermListModel extends ViewModel {

    private CourseRepository courseRepository;

    public TermListModel(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Terms> getListOfAllTerms() {
        return this.courseRepository.getmAllTerms();
    }
}
