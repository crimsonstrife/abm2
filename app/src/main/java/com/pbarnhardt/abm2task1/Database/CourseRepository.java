package com.pbarnhardt.abm2task1.Database;

import static com.pbarnhardt.abm2task1.Utils.Constants.NUMBER_OF_THREADS;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.pbarnhardt.abm2task1.DAO.AssessmentsDAO;
import com.pbarnhardt.abm2task1.DAO.CourseDAO;
import com.pbarnhardt.abm2task1.DAO.MentorsDAO;
import com.pbarnhardt.abm2task1.DAO.TermsDAO;
import com.pbarnhardt.abm2task1.Entity.Assessments;
import com.pbarnhardt.abm2task1.Entity.Courses;
import com.pbarnhardt.abm2task1.Entity.Mentors;
import com.pbarnhardt.abm2task1.Entity.Notes;
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
    private CourseDatabase database;

    /*
      The Notes dao.
     */
    //private NotesDAO notesDao;

    /*
      The Status dao.
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

    /*
      Handle database operations on a background thread
     */
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
        database = CourseDatabase.getInstance(context);
        /**
         * The Terms dao.
         */
        TermsDAO termsDao = database.termDao();
        mAllTerms = getAllTerms();
        /**
         * The Course dao.
         */
        CourseDAO courseDao = database.courseDao();
        mAllCourses = getAllCourses();
        /**
         * The Assessments dao.
         */
        AssessmentsDAO assessmentsDao = database.assessmentDao();
        mAllAssessments = getAllAssessments();
        //notesDao = mdatabase.notesDao();
        //mAllNotes = getAllNotes();
        /**
         * The Mentors dao.
         */
        MentorsDAO mentorsDao = database.mentorsDao();
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
        executor.execute(() -> database.termDao().deleteAll());
        executor.execute(() -> database.courseDao().deleteAll());
        executor.execute(() -> database.assessmentDao().deleteAll());
        executor.execute(() -> database.mentorsDao().deleteAll());
    }

    /**
     * Add sample data to the database
     */
    public void addSampleDataset() {
        executor.execute(() -> database.termDao().insertAll(SampleDataSet.getTerms()));
        executor.execute(() -> database.courseDao().insertAll(SampleDataSet.getCourses()));
        executor.execute(() -> database.assessmentDao().insertAll(SampleDataSet.getAssessments()));
        executor.execute(() -> database.mentorsDao().insertAll(SampleDataSet.getMentors()));
    }

    /**
     * Gets all terms.
     *
     * @return the all terms
     */
    public LiveData<List<Terms>> getAllTerms() {
        return database.termDao().getAll();
    }

    /**
     * Gets term by id.
     * @param termId the term id
     * @return the term
     */
    public Terms getTermById(int termId) {
        return database.termDao().getTermById(termId);
    }

    /**
     * Insert term.
     * (will overwrite if term already exists)
     * @param term the term
     */
    public void insertTerm(final Terms term) {
        executor.execute(() -> database.termDao().insertTerms(term));
    }

    /**
     * Delete term.
     * @param term the term
     */
    public void deleteTerm(final Terms term) {
        executor.execute(() -> database.termDao().deleteTerms(term));
    }

    /**
     * Gets all courses.
     * @return the all courses
     */
    public LiveData<List<Courses>> getAllCourses() {
        return database.courseDao().getAllCourses();
    }

    /**
     * Gets course by id.
     * @param courseId the course id
     * @return the course by id
     */
    public Courses getCourseById(int courseId) {
        return database.courseDao().getCourseById(courseId);
    }

    /**
     * Gets courses by term.
     * @param termId the term id
     * @return the courses by term
     */
    public LiveData<List<Courses>> getCoursesByTermId(final int termId) {
        return database.courseDao().getCoursesByTerm(termId);
    }

    /**
     * Insert course.
     * @param course the course
     */
    public void insertCourse(final Courses course) {
        executor.execute(() -> database.courseDao().insertCourses(course));
    }

    /**
     * Delete course.
     * @param course the course
     */
    public void deleteCourse(final Courses course) {
        executor.execute(() -> database.courseDao().deleteCourses(course));
    }

    /**
     * Gets all assessments.
     * @return the all assessments
     */
    public LiveData<List<Assessments>> getAllAssessments() {
        return database.assessmentDao().getAllAssessments();
    }

    /**
     * Gets assessment by id.
     * @param assessmentId the assessment id
     * @return the assessment by id
     */
    public Assessments getAssessmentById(int assessmentId) {
        return database.assessmentDao().getAssessmentsById(assessmentId);
    }

    /**
     * Gets assessments by course.
     * @param courseId the course id
     * @return the assessments by course
     */
    public LiveData<List<Assessments>> getAssessmentsByCourseId(final int courseId) {
        return database.assessmentDao().getAssessmentsByCourse(courseId);
    }

    /**
     * Insert assessment.
     * @param assessment the assessment
     */
    public void insertAssessment(final Assessments assessment) {
        executor.execute(() -> database.assessmentDao().insertAssessments(assessment));
    }

    /**
     * Delete assessment.
     * @param assessment the assessment
     */
    public void deleteAssessment(final Assessments assessment) {
        executor.execute(() -> database.assessmentDao().deleteAssessments(assessment));
    }

    /**
     * Gets all Mentors.
     * @return the all Mentors
     */
    public LiveData<List<Mentors>> getAllMentors() {
        return database.mentorsDao().getAllMentors();
    }

    /**
     * Gets Mentors by id.
     * @param mentorId the mentor id
     * @return the Mentors by id
     */
    public Mentors getMentorById(int mentorId) {
        return database.mentorsDao().getMentorsById(mentorId);
    }

    /**
     * Gets Mentors by course.
     * @param courseId the course id
     * @return the Mentors by course
     */
    public LiveData<List<Mentors>> getMentorsByCourseId(final int courseId) {
        return database.mentorsDao().getMentorsByCourse(courseId);
    }

    /**
     * Insert Mentors.
     * @param mentor the mentor
     */
    public void insertMentor(final Mentors mentor) {
        executor.execute(() -> database.mentorsDao().insertMentors(mentor));
    }

    /**
     * Delete Mentors.
     * @param mentor the mentor
     */
    public void deleteMentor(final Mentors mentor) {
        executor.execute(() -> database.mentorsDao().deleteMentors(mentor));
    }
}
