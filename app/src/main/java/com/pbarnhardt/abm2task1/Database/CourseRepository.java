package com.pbarnhardt.abm2task1.Database;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;

import com.pbarnhardt.abm2task1.DAO.AssessmentsDAO;
import com.pbarnhardt.abm2task1.DAO.CourseDAO;
import com.pbarnhardt.abm2task1.DAO.MentorsDAO;
import com.pbarnhardt.abm2task1.DAO.NotesDAO;
import com.pbarnhardt.abm2task1.DAO.StatusDAO;
import com.pbarnhardt.abm2task1.DAO.TermsDAO;
import com.pbarnhardt.abm2task1.Entity.Assessments;
import com.pbarnhardt.abm2task1.Entity.Courses;
import com.pbarnhardt.abm2task1.Entity.Mentors;
import com.pbarnhardt.abm2task1.Entity.Notes;
import com.pbarnhardt.abm2task1.Enums.Status;
import com.pbarnhardt.abm2task1.Entity.Terms;
import com.pbarnhardt.abm2task1.Utils.SampleDataSet;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * The type Course repository.
 */
public class CourseRepository {
    private static CourseRepository instance;
    /**
     * The Db.
     */
    private CourseDatabase mdatabase;
    /**
     * The Terms dao.
     */
    private TermsDAO termsDao;

    /**
     * The Course dao.
     */
    private CourseDAO courseDao;

    /**
     * The Assessments dao.
     */
    private AssessmentsDAO assessmentsDao;

    /**
     * The Notes dao.
     */
    //private NotesDAO notesDao;

    /**
     * The Mentors dao.
     */
    private MentorsDAO mentorsDao;

    /**
     * The Status dao.
     */
    //private StatusDAO statusDao;

    /**
     * The M all terms.
     */
    public LiveData<List<Terms>> mAllTerms;

    /**
     * The M all courses.
     */
    public LiveData<List<Courses>> mAllCourses;

    /**
     * The M all assessments.
     */
    public LiveData<List<Assessments>> mAllAssessments;

    /**
     * The M all notes.
     */
    private List<Notes> mAllNotes;

    /**
     * The M all mentors.
     */
    public LiveData<List<Mentors>> mAllMentors;

    /**
     * The M all statuses.
     */
    private List<Status> mAllStatus;

    /**
     * Handle database operations on a background thread
     */
    private static final int NUMBER_OF_THREADS = 4;
    /**
     * The executor.
     */
    private Executor executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /**
     * Instantiates a new Course repository.
     *
     * @param context the application
     */
    private CourseRepository(Context context) {
        mdatabase = CourseDatabase.getInstance(context);
        termsDao = mdatabase.termDao();
        mAllTerms = getAllTerms();
        courseDao = mdatabase.courseDao();
        mAllCourses = getAllCourses();
        assessmentsDao = mdatabase.assessmentDao();
        mAllAssessments = getAllAssessments();
        //notesDao = mdatabase.notesDao();
        //mAllNotes = getAllNotes();
        mentorsDao = mdatabase.mentorsDao();
        mAllMentors = getAllMentors();
        //statusDao = mdatabase.statusDao();
        //mAllStatus = getAllStatus();
        //Delay so the constructor has time to complete
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static CourseRepository getInstance(Context context) {
        if(instance == null) {
            instance = new CourseRepository(context);
        }
        return instance;
    }

    /**
     * Delete all data to start fresh
     */
    public void deleteAllData() {
        executor.execute(() -> mdatabase.termDao().deleteAll());
        executor.execute(() -> mdatabase.courseDao().deleteAll());
        executor.execute(() -> mdatabase.assessmentDao().deleteAll());
        executor.execute(() -> mdatabase.mentorsDao().deleteAll());
    }

    /**
     * Add sample data to the database
     */
    public void addSampleDataset() {
        executor.execute(() -> mdatabase.termDao().insertAll(SampleDataSet.getTerms()));
        executor.execute(() -> mdatabase.courseDao().insertAll(SampleDataSet.getCourses()));
        executor.execute(() -> mdatabase.assessmentDao().insertAll(SampleDataSet.getAssessments()));
        executor.execute(() -> mdatabase.mentorsDao().insertAll(SampleDataSet.getMentors()));
    }

    /**
     * Gets all terms.
     *
     * @return the all terms
     */
    public LiveData<List<Terms>> getAllTerms() {
        return mdatabase.termDao().getAll();
    }

    /**
     * Gets term by id.
     * @param termId the term id
     * @return the term
     */
    public Terms getTermById(int termId) {
        return mdatabase.termDao().getTermById(termId);
    }

    /**
     * Insert term.
     * (will overwrite if term already exists)
     * @param term the term
     */
    public void insertTerm(final Terms term) {
        executor.execute(() -> mdatabase.termDao().insertTerms(term));
    }

    /**
     * Delete term.
     * @param term the term
     */
    public void deleteTerm(final Terms term) {
        executor.execute(() -> mdatabase.termDao().deleteTerms(term));
    }

    /**
     * Gets all courses.
     * @return the all courses
     */
    public LiveData<List<Courses>> getAllCourses() {
        return mdatabase.courseDao().getAllCourses();
    }

    /**
     * Gets course by id.
     * @param courseId the course id
     * @return the course by id
     */
    public Courses getCourseById(int courseId) {
        return mdatabase.courseDao().getCourseById(courseId);
    }

    /**
     * Gets courses by term.
     * @param termId the term id
     * @return the courses by term
     */
    public LiveData<List<Courses>> getCoursesByTermId(final int termId) {
        return mdatabase.courseDao().getCoursesByTerm(termId);
    }

    /**
     * Insert course.
     * @param course the course
     */
    public void insertCourse(final Courses course) {
        executor.execute(() -> mdatabase.courseDao().insertCourses(course));
    }

    /**
     * Delete course.
     * @param course the course
     */
    public void deleteCourse(final Courses course) {
        executor.execute(() -> mdatabase.courseDao().deleteCourses(course));
    }

    /**
     * Gets all assessments.
     * @return the all assessments
     */
    public LiveData<List<Assessments>> getAllAssessments() {
        return mdatabase.assessmentDao().getAllAssessments();
    }

    /**
     * Gets assessment by id.
     * @param assessmentId the assessment id
     * @return the assessment by id
     */
    public Assessments getAssessmentById(int assessmentId) {
        return mdatabase.assessmentDao().getAssessmentsById(assessmentId);
    }

    /**
     * Gets assessments by course.
     * @param courseId the course id
     * @return the assessments by course
     */
    public LiveData<List<Assessments>> getAssessmentsByCourseId(final int courseId) {
        return mdatabase.assessmentDao().getAssessmentsByCourse(courseId);
    }

    /**
     * Insert assessment.
     * @param assessment the assessment
     */
    public void insertAssessment(final Assessments assessment) {
        executor.execute(() -> mdatabase.assessmentDao().insertAssessments(assessment));
    }

    /**
     * Delete assessment.
     * @param assessment the assessment
     */
    public void deleteAssessment(final Assessments assessment) {
        executor.execute(() -> mdatabase.assessmentDao().deleteAssessments(assessment));
    }

    /**
     * Gets all Mentors.
     * @return the all Mentors
     */
    public LiveData<List<Mentors>> getAllMentors() {
        return mdatabase.mentorsDao().getAllMentors();
    }

    /**
     * Gets Mentors by id.
     * @param mentorId the mentor id
     * @return the Mentors by id
     */
    public Mentors getMentorById(int mentorId) {
        return mdatabase.mentorsDao().getMentorsById(mentorId);
    }

    /**
     * Gets Mentors by course.
     * @param courseId the course id
     * @return the Mentors by course
     */
    public LiveData<List<Mentors>> getMentorsByCourseId(final int courseId) {
        return mdatabase.mentorsDao().getMentorsByCourse(courseId);
    }

    /**
     * Insert Mentors.
     * @param mentor the mentor
     */
    public void insertMentor(final Mentors mentor) {
        executor.execute(() -> mdatabase.mentorsDao().insertMentors(mentor));
    }

    /**
     * Delete Mentors.
     * @param mentor the mentor
     */
    public void deleteMentor(final Mentors mentor) {
        executor.execute(() -> mdatabase.mentorsDao().deleteMentors(mentor));
    }
}
