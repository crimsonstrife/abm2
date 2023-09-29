package com.pbarnhardt.abm2task1.Models;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.pbarnhardt.abm2task1.Database.CourseRepository;
import com.pbarnhardt.abm2task1.Entity.Assessments;
import com.pbarnhardt.abm2task1.Entity.Courses;
import com.pbarnhardt.abm2task1.Entity.Mentors;
import com.pbarnhardt.abm2task1.Entity.Terms;
import com.pbarnhardt.abm2task1.Enums.Status;
import com.pbarnhardt.abm2task1.Enums.Types;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EditorModel extends AndroidViewModel {
    /**
     * Mutable
     */
    public MutableLiveData<Terms> liveTerms = new MutableLiveData<>();
    public MutableLiveData<Courses> liveCourses = new MutableLiveData<>();
    public MutableLiveData<Assessments> liveAssessments = new MutableLiveData<>();
    public MutableLiveData<Mentors> liveMentors = new MutableLiveData<>();

    /**
     * Live Lists
     */
    public LiveData<List<Terms>> termsList;
    public LiveData<List<Courses>> coursesList;
    public LiveData<List<Assessments>> assessmentsList;
    public LiveData<List<Mentors>> mentorsList;

    /**
     * Repository
     */
    private CourseRepository courseRepository;

    private Executor executor = Executors.newSingleThreadExecutor();

    /**
     * Constructor
     * @param application - application
     */
    public EditorModel(Application application) {
        super(application);
        courseRepository = CourseRepository.getInstance(application.getApplicationContext());
        termsList = courseRepository.getAllTerms();
        coursesList = courseRepository.getAllCourses();
        assessmentsList = courseRepository.getAllAssessments();
        mentorsList = courseRepository.getAllMentors();
    }

    /**
     * Load Term Data
     * @param termId - id of the term to load
     */
    public void loadTerm(final int termId) {
        executor.execute(() -> {
            Terms terms = courseRepository.getTermById(termId);
            liveTerms.postValue(terms);
        });
    }

    /**
     * Load Course Data
     * @param courseId - id of the course to load
     */
    public void loadCourse(final int courseId) {
        executor.execute(() -> {
            Courses courses = courseRepository.getCourseById(courseId);
            liveCourses.postValue(courses);
        });
    }

    /**
     * Load Assessment Data
     * @param assessmentId  - id of the assessment to load
     */
    public void loadAssessment(final int assessmentId) {
        executor.execute(() -> {
            Assessments assessments = courseRepository.getAssessmentById(assessmentId);
            liveAssessments.postValue(assessments);
        });
    }

    /**
     * Load Mentor Data
     * @param mentorId - id of the mentor to load
     */
    public void loadMentor(final int mentorId) {
        executor.execute(() -> {
            Mentors mentors = courseRepository.getMentorById(mentorId);
            liveMentors.postValue(mentors);
        });
    }

    /**
     * Save Term
     * @param termTitle - title of the term
     * @param startDate - start date of the term
     * @param endDate - end date of the term
     */
    public void saveTerm(String termTitle, Date startDate, Date endDate) {
        Terms terms = liveTerms.getValue();
        if (terms == null) {
            if (termTitle.trim().isEmpty()) {
                return;
            }
            terms = new Terms(termTitle.trim(), startDate, endDate);
        } else {
            terms.setTermName(termTitle.trim());
            terms.setTermStartDate(startDate);
            terms.setTermEndDate(endDate);
        }
        courseRepository.insertTerm(terms);
    }

    /**
     * Save Course
     * @param courseTitle   - title of the course
     * @param courseDescription - description of the course
     * @param startDate - start date of the course
     * @param endDate  - end date of the course
     * @param startDateAlert - start date alert boolean
     * @param endDateAlert - end date alert boolean
     * @param courseStatus - status of the course
     * @param termId - term id of the course
     * @param note - notes of the course
     */
    public void saveCourse(String courseTitle, String courseDescription, Date startDate, Date endDate, boolean startDateAlert, boolean endDateAlert, Status courseStatus, int termId, String note) {
        Courses courses = liveCourses.getValue();
        if (courses == null) {
            if (courseTitle.trim().isEmpty()) {
                return;
            } else if (courseDescription.trim().isEmpty()) {
                return;
            }
            courses = new Courses(courseTitle.trim(), courseDescription.trim(), startDateAlert, startDate, endDateAlert, endDate, courseStatus, note, termId);
        } else {
            courses.setCourseName(courseTitle.trim());
            courses.setCourseDescription(courseDescription.trim());
            courses.setCourseStartDate(startDate);
            courses.setCourseEndDate(endDate);
            courses.setCourseStartAlert(startDateAlert);
            courses.setCourseEndAlert(endDateAlert);
            courses.setCourseStatus(courseStatus);
            courses.setCourseNote(note);
            courses.setTermId(termId);
        }
        courseRepository.insertCourse(courses);
    }

    /**
     * Save Assessment
     * @param assessmentTitle   - title of the assessment
     * @param assessmentDescription - description of the assessment
     * @param startDate - start date of the assessment
     * @param dueDate - due date of the assessment
     * @param assessmentType - type of the assessment
     * @param courseId - course id of the assessment
     * @param startAlert - alert boolean of the assessment
     * @param dueAlert - alert boolean of the assessment
     */
    public void saveAssessment(String assessmentTitle, String assessmentDescription, Date startDate, Date dueDate, Types assessmentType, int courseId, boolean startAlert , boolean dueAlert) {
        Assessments assessments = liveAssessments.getValue();
        if (assessments == null) {
            if (assessmentTitle.trim().isEmpty()) {
                return;
            } else if (assessmentDescription.trim().isEmpty()) {
                return;
            }
            assessments = new Assessments(assessmentTitle.trim(), assessmentType, assessmentDescription.trim(), startDate, dueDate, startAlert, dueAlert, courseId);
        } else {
            assessments.setAssessmentName(assessmentTitle.trim());
            assessments.setAssessmentDescription(assessmentDescription.trim());
            assessments.setAssessmentStartDate(startDate);
            assessments.setAssessmentDueDate(dueDate);
            assessments.setAssessmentType(assessmentType);
            assessments.setAssessmentStartAlert(startAlert);
            assessments.setAssessmentDueAlert(dueAlert);
            assessments.setAssessmentCourseId(courseId);
        }
        courseRepository.insertAssessment(assessments);
    }

    /**
     * Save Mentor
     * @param mentorName - name of the mentor
     * @param mentorPhone - phone number of the mentor
     * @param mentorEmail - email of the mentor
     * @param courseId - course id of the mentor
     */
    public void saveMentor(String mentorName, String mentorPhone, String mentorEmail, int courseId) {
        Mentors mentors = liveMentors.getValue();
        if (mentors == null) {
            if (mentorName.trim().isEmpty()) {
                return;
            } else if (mentorPhone.trim().isEmpty()) {
                return;
            } else if (!android.util.Patterns.PHONE.matcher(mentorPhone.trim()).matches()) {
                //check for valid phone format if not empty
                return;
            } else if (mentorEmail.trim().isEmpty()) {
                return;
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mentorEmail.trim()).matches()) {
                //check for valid email format if not empty
                return;
            }
            mentors = new Mentors(mentorName.trim(), mentorPhone.trim(), mentorEmail.trim(), courseId);
        } else {
            mentors.setCourseMentorName(mentorName.trim());
            mentors.setCourseMentorPhone(mentorPhone.trim());
            mentors.setCourseMentorEmail(mentorEmail.trim());
            mentors.setCourseId(courseId);
        }
        courseRepository.insertMentor(mentors);
    }

    /**
     * Overwrite Course
     * @param courses - course to overwrite
     * @param termId - term id of the course
     */
    public void overwriteCourse(Courses courses, int termId) {
        courses.setTermId(termId);
        courseRepository.insertCourse(courses);
    }

    /**
     * Overwrite Assessment
     * @param assessments - assessment to overwrite
     * @param courseId - course id of the assessment
     */
    public void overwriteAssessment(Assessments assessments, int courseId) {
        assessments.setAssessmentCourseId(courseId);
        courseRepository.insertAssessment(assessments);
    }

    /**
     * Overwrite Mentor
     * @param mentors - mentor to overwrite
     * @param courseId - course id of the mentor
     */
    public void overwriteMentor(Mentors mentors, int courseId) {
        mentors.setCourseId(courseId);
        courseRepository.insertMentor(mentors);
    }

    /**
     * Delete Term
     */
    public void deleteTerm() {
        courseRepository.deleteTerm(liveTerms.getValue());
    }

    /**
     * Delete Course
     */
    public void deleteCourse() {
        courseRepository.deleteCourse(liveCourses.getValue());
    }

    /**
     * Delete Assessment
     */
    public void deleteAssessment() {
        courseRepository.deleteAssessment(liveAssessments.getValue());
    }

    /**
     * Delete Mentor
     */
    public void deleteMentor() {
        courseRepository.deleteMentor(liveMentors.getValue());
    }

    /**
     * Additional Gets
     * for more specific data
     */
    // Get all courses in a specific term by termId
    public LiveData<List<Courses>> getCoursesByTermId(final int termId) {
        return courseRepository.getCoursesByTermId(termId);
    }
    // Get all assessments in a specific course by courseId
    public LiveData<List<Assessments>> getAssessmentsByCourseId(final int courseId) {
        return courseRepository.getAssessmentsByCourseId(courseId);
    }
    // Get all mentors in a specific course by courseId
    public LiveData<List<Mentors>> getMentorsByCourseId(final int courseId) {
        return courseRepository.getMentorsByCourseId(courseId);
    }
    // Get courses not assigned to a term
    public LiveData<List<Courses>> getUnassignedCourses() {
        return courseRepository.getCoursesByTermId(-1);
    }
    // Get assessments not assigned to a course
    public LiveData<List<Assessments>> getUnassignedAssessments() {
        return courseRepository.getAssessmentsByCourseId(-1);
    }
    // Get mentors not assigned to a course
    public LiveData<List<Mentors>> getUnassignedMentors() {
        return courseRepository.getMentorsByCourseId(-1);
    }
    // Get a specific term by termId
    public Terms getTermById(int termId) {
        return courseRepository.getTermById(termId);
    }
    // Get a specific course by courseId
    public Courses getCourseById(int courseId) {
        return courseRepository.getCourseById(courseId);
    }
    // Get a specific assessment by assessmentId
    public Assessments getAssessmentById(int assessmentId) {
        return courseRepository.getAssessmentById(assessmentId);
    }
    // Get a specific mentor by mentorId
    public Mentors getMentorById(int mentorId) {
        return courseRepository.getMentorById(mentorId);
    }
}
