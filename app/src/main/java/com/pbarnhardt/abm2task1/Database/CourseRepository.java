package com.pbarnhardt.abm2task1.Database;

import android.app.Application;

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
import com.pbarnhardt.abm2task1.Entity.Status;
import com.pbarnhardt.abm2task1.Entity.Terms;

import java.util.List;

/**
 * The type Course repository.
 */
public class CourseRepository {
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
    private NotesDAO notesDao;

    /**
     * The Mentors dao.
     */
    private MentorsDAO mentorsDao;

    /**
     * The Status dao.
     */
    private StatusDAO statusDao;

    /**
     * The M all terms.
     */
    private List<Terms> mAllTerms;

    /**
     * The M all courses.
     */
    private List<Courses> mAllCourses;

    /**
     * The M all assessments.
     */
    private List<Assessments> mAllAssessments;

    /**
     * The M all notes.
     */
    private List<Notes> mAllNotes;

    /**
     * The M all mentors.
     */
    private List<Mentors> mAllMentors;

    /**
     * The M all statuses.
     */
    private List<Status> mAllStatus;

    /**
     * Instantiates a new Course repository.
     *
     * @param application the application
     */
    public CourseRepository(Application application) {
        CourseDatabase db = CourseDatabase.getDatabase(application);
        termsDao = db.termDao();
        mAllTerms = getmAllTerms();
        courseDao = db.courseDao();
        mAllCourses = getmAllCourses();
        assessmentsDao = db.assessmentDao();
        mAllAssessments = getmAllAssessments();
        notesDao = db.notesDao();
        mAllNotes = getmAllNotes();
        mentorsDao = db.mentorsDao();
        mAllMentors = getmAllMentors();
        statusDao = db.statusDao();
        mAllStatus = statusDao.getmAllStatus();
        //Delay so the constructor has time to complete
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets all terms.
     *
     * @return the list
     */
    public List<Terms> getmAllTerms() {
        CourseDatabase.databaseWriteExecutor.execute(() -> {
            mAllTerms = termsDao.getTerms();
        });
        return mAllTerms;
    }

    /**
     * Gets all statuses.
     *
     * @return the list
     */
    public List<Status> getmAllStatus() {
        CourseDatabase.databaseWriteExecutor.execute(() -> {
            mAllStatus = statusDao.getmAllStatus();
        });
        return mAllStatus;
    }

    /**
     * Gets term by id.
     *
     * @param termId the term id
     * @return the term by id
     */
    public Terms getTermById(int termId) {
        CourseDatabase.databaseWriteExecutor.execute(() -> {
            mAllTerms = termsDao.getTerms();
        });
        for (Terms term : mAllTerms) {
            if (term.getTermId() == termId) {
                return term;
            }
        }
        return null;
    }

    /**
     * Gets term by title.
     *
     * @param termTitle the term title
     * @return the term by title
     */
    public Terms getTermByTitle(String termTitle) {
        CourseDatabase.databaseWriteExecutor.execute(() -> {
            mAllTerms = termsDao.getTerms();
        });
        for (Terms term : mAllTerms) {
            if (term.getTermName().equals(termTitle)) {
                return term;
            }
        }
        return null;
    }

    /**
     * Gets all courses.
     *
     * @return the list
     */
    public List<Courses> getmAllCourses() {
        CourseDatabase.databaseWriteExecutor.execute(() -> {
            mAllCourses = courseDao.getCourses();
        });
        return mAllCourses;
    }

    /**
     * Gets course by id.
     *
     * @param courseId the course id
     * @return the course by id
     */
    public Courses getCourseById(int courseId) {
        CourseDatabase.databaseWriteExecutor.execute(() -> {
            mAllCourses = courseDao.getCourses();
        });
        for (Courses course : mAllCourses) {
            if (course.getCourseId() == courseId) {
                return course;
            }
        }
        return null;
    }

    /**
     * Gets course by term id.
     *
     * @param termId the term id
     * @return the course
     */
    public List<Courses> getCourseByTermId(int termId) {
        CourseDatabase.databaseWriteExecutor.execute(() -> {
            mAllCourses = courseDao.getCourses();
        });
        for (Courses course : mAllCourses) {
            if (course.getCourseTermId() == termId) {
                return mAllCourses;
            }
        }
        return null;
    }

    /**
     * Gets course by mentor name
     *
     * @param mentorName the mentor name
     * @return the course
     *
     * Searches the Mentor table for the mentor name and returns the course associated with that mentor
     */
    public Courses getCourseByMentorName(String mentorName) {
        CourseDatabase.databaseWriteExecutor.execute(() -> {
            mAllMentors = mentorsDao.getMentors();
        });
        for (Mentors mentor : mAllMentors) {
            if (mentor.getMentorName().equals(mentorName)) {
                return getCourseById(mentor.getCourseId());
            }
        }
        return null;
    }

    /**
     * Gets all assessments.
     *
     * @return the list
     */
    public List<Assessments> getmAllAssessments() {
        CourseDatabase.databaseWriteExecutor.execute(() -> {
            mAllAssessments = assessmentsDao.getAssessments();
        });
        return mAllAssessments;
    }

    /**
     * Gets all notes.
     *
     * @return the list
     */
    public List<Notes> getmAllNotes() {
        CourseDatabase.databaseWriteExecutor.execute(() -> {
            mAllNotes = notesDao.getNotes();
        });
        return mAllNotes;
    }

    /**
     * Gets all mentors.
     *
     * @return the list
     */
    public List<Mentors> getmAllMentors() {
        CourseDatabase.databaseWriteExecutor.execute(() -> {
            mAllMentors = mentorsDao.getMentors();
        });
        return mAllMentors;
    }

    /**
     * Insert term.
     *
     * @param term the term
     */
    public void insertTerm(Terms term) {
        CourseDatabase.databaseWriteExecutor.execute(() -> {
            termsDao.insertTerms(term);
        });
    }

    /**
     * Insert course.
     *
     * @param course the course
     */
    public void insertCourse(Courses course) {
        CourseDatabase.databaseWriteExecutor.execute(() -> {
            courseDao.insertCourses(course);
        });
    }

    /**
     * Insert assessment.
     *
     * @param assessment the assessment
     */
    public void insertAssessment(Assessments assessment) {
        CourseDatabase.databaseWriteExecutor.execute(() -> {
            assessmentsDao.insertAssessments(assessment);
        });
    }

    /**
     * Insert note.
     *
     * @param note the note
     */
    public void insertNote (Notes note) {
        CourseDatabase.databaseWriteExecutor.execute(() -> {
            notesDao.insertNotes(note);
        });
    }

    /**
     * Insert mentor.
     *
     * @param mentor the mentor
     */
    public void insertMentor (Mentors mentor) {
        CourseDatabase.databaseWriteExecutor.execute(() -> {
            mentorsDao.insertMentors(mentor);
        });
    }

    /**
     * Delete term.
     *
     * @param term the term
     */
    public void deleteTerm(Terms term) {
        CourseDatabase.databaseWriteExecutor.execute(() -> {
            termsDao.deleteTerms(term);
        });
    }

    /**
     * Delete course.
     *
     * @param course the course
     */
    public void deleteCourse(Courses course) {
        CourseDatabase.databaseWriteExecutor.execute(() -> {
            courseDao.deleteCourses(course);
        });
    }

    /**
     * Delete assessment.
     *
     * @param assessment the assessment
     */
    public void deleteAssessment(Assessments assessment) {
        CourseDatabase.databaseWriteExecutor.execute(() -> {
            assessmentsDao.deleteAssessments(assessment);
        });
    }

    /**
     * Delete note.
     *
     * @param note the note
     */
    public void deleteNote (Notes note) {
        CourseDatabase.databaseWriteExecutor.execute(() -> {
            notesDao.deleteNotes(note);
        });
    }

    /**
     * Delete mentor.
     *
     * @param mentor the mentor
     */
    public void deleteMentor (Mentors mentor) {
        CourseDatabase.databaseWriteExecutor.execute(() -> {
            mentorsDao.deleteMentors(mentor);
        });
    }

    /**
     * Update term.
     *
     * @param term the term
     */
    public void updateTerm(Terms term) {
        CourseDatabase.databaseWriteExecutor.execute(() -> {
            termsDao.updateTerms(term);
        });
    }

    /**
     * Update course.
     *
     * @param course the course
     */
    public void updateCourse(Courses course) {
        CourseDatabase.databaseWriteExecutor.execute(() -> {
            courseDao.updateCourses(course);
        });
    }

    /**
     * Update assessment.
     *
     * @param assessment the assessment
     */
    public void updateAssessment(Assessments assessment) {
        CourseDatabase.databaseWriteExecutor.execute(() -> {
            assessmentsDao.updateAssessments(assessment);
        });
    }

    /**
     * Update note.
     *
     * @param note the note
     */
    public void updateNote (Notes note) {
        CourseDatabase.databaseWriteExecutor.execute(() -> {
            notesDao.updateNotes(note);
        });
    }

    /**
     * Update mentor.
     *
     * @param mentor the mentor
     */
    public void updateMentor (Mentors mentor) {
        CourseDatabase.databaseWriteExecutor.execute(() -> {
            mentorsDao.updateMentors(mentor);
        });
    }
}
