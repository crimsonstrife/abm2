package com.pbarnhardt.abm2task1.Models;

import androidx.lifecycle.ViewModel;

import com.pbarnhardt.abm2task1.Database.CourseRepository;
import com.pbarnhardt.abm2task1.Entity.Courses;
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

    public void getTermById(int termId) {
        this.courseRepository.getTermById(termId);
    }

    public void getTermByTitle(String termTitle) {
        this.courseRepository.getTermByTitle(termTitle);
    }

    public void getCoursesByTerm(int termId) {
        this.courseRepository.getCourseByTermId(termId);
    }

    public void addCourseToTerm(Courses course) {
        this.courseRepository.insertCourse(course);
    }

    public void deleteCourseFromTerm(Courses course) {
        this.courseRepository.deleteCourse(course);
    }
}
