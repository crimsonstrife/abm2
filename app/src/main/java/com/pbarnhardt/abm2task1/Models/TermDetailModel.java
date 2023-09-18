package com.pbarnhardt.abm2task1.Models;

import androidx.lifecycle.ViewModel;

import com.pbarnhardt.abm2task1.Database.CourseRepository;
import com.pbarnhardt.abm2task1.Entity.Terms;

public class TermDetailModel extends ViewModel {

    private CourseRepository courseRepository;

    public TermDetailModel(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public void addTerm(Terms newTerm) {
        this.courseRepository.insertTerm(newTerm);
    }

    public void deleteTerm(Terms termToDelete) {
        this.courseRepository.deleteTerm(termToDelete);
    }

    public void LiveData<Terms> getTermById(int termId) {
        return this.courseRepository.getTermById(termId);
    }
}
