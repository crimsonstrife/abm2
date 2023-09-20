package com.pbarnhardt.abm2task1.Entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

/**
 * The type Courses(Classes) for student tracker.
 */
@Entity(tableName = "Courses", foreignKeys = @ForeignKey(entity = Terms.class,
        parentColumns = "termId",
        childColumns = "termId",
        onDelete = ForeignKey.RESTRICT))
public class Courses {
    @PrimaryKey(autoGenerate = true)
    private int courseId;
    private String courseName;
    private String courseDescription;
    private String courseStartDate;
    private String courseEndDate;
    private String courseStatus;

    // Made the mentor into its own entity
    //private String courseMentorName;
    //private String courseMentorPhone;
    //private String courseMentorEmail;

    private int courseNotesCount;
    private int courseAssessmentsCount;
    private int termId;

    /**
     * Instantiates a new Course.
     *
     * @param courseName           the course name
     * @param courseDescription    the course description
     * @param courseStartDate      the course start date
     * @param courseEndDate        the course end date
     * @param courseStatus         the course status
     * @param termId               the term id
     * Course note and assessment counts are set to 0 by default.
     */
    public Courses(String courseName, String courseDescription, String courseStartDate, String courseEndDate, String courseStatus, int termId) {
        this.courseName = courseName;
        this.courseDescription = courseDescription;
        this.courseStartDate = courseStartDate;
        this.courseEndDate = courseEndDate;
        this.courseStatus = courseStatus;
        //this.courseMentorName = courseMentorName;
        //this.courseMentorPhone = courseMentorPhone;
        //this.courseMentorEmail = courseMentorEmail;
        this.courseNotesCount = 0;
        this.courseAssessmentsCount = 0;
        this.termId = termId;
    }

    /**
     * Gets course id.
     *
     * @return the course id
     */
    public int getCourseId() {
        return courseId;
    }

    /**
     * Gets course name.
     *
     * @return the course name
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * Gets course description.
     *
     * @return the course description
     */
    public String getCourseDescription() {
        return courseDescription;
    }

    /**
     * Gets course start date.
     *
     * @return the course start date
     */
    public String getCourseStartDate() {
        return courseStartDate;
    }

    /**
     * Gets course end date.
     *
     * @return the course end date
     */
    public String getCourseEndDate() {
        return courseEndDate;
    }

    /**
     * Gets course status.
     *
     * @return the course status
     */
    public String getCourseStatus() {
        return courseStatus;
    }

    /**
     * Gets course mentor name.
     *
     * @return the course mentor name
     */
    //public String getCourseMentorName() {
        //return courseMentorName;
    //}

    /**
     * Gets course mentor phone.
     *
     * @return the course mentor phone
     */
    //public String getCourseMentorPhone() {
        //return courseMentorPhone;
    //}

    /**
     * Gets course mentor email.
     *
     * @return the course mentor email
     */
    //public String getCourseMentorEmail() {
        //return courseMentorEmail;
    //}

    /**
     * Gets course notes count.
     *
     * @return the course notes count
     */
    public int getCourseNotesCount() {
        return courseNotesCount;
    }

    /**
     * Gets course assessments count.
     *
     * @return the course assessments count
     */
    public int getCourseAssessmentsCount() {
        return courseAssessmentsCount;
    }

    /**
     * Gets term id.
     *
     * @return the term id
     */
    public int getCourseTermId() {
        return termId;
    }

    /**
     * Sets course name.
     *
     * @param courseName the course name
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    /**
     * Sets course description.
     *
     * @param courseDescription the course description
     */
    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    /**
     * Sets course start date.
     *
     * @param courseStartDate the course start date
     */
    public void setCourseStartDate(String courseStartDate) {
        this.courseStartDate = courseStartDate;
    }

    /**
     * Sets course end date.
     *
     * @param courseEndDate the course end date
     */
    public void setCourseEndDate(String courseEndDate) {
        this.courseEndDate = courseEndDate;
    }

    /**
     * Sets course status.
     *
     * @param courseStatus the course status
     */
    public void setCourseStatus(String courseStatus) {
        this.courseStatus = courseStatus;
    }

    /**
     * Sets course mentor name.
     *
     * @param courseMentorName the course mentor name
     */
    //public void setCourseMentorName(String courseMentorName) {
        //this.courseMentorName = courseMentorName;
    //}

    /**
     * Sets course mentor phone.
     *
     * @param courseMentorPhone the course mentor phone
     */
    //public void setCourseMentorPhone(String courseMentorPhone) {
        //this.courseMentorPhone = courseMentorPhone;
    //}

    /**
     * Sets course mentor email.
     *
     * @param courseMentorEmail the course mentor email
     */
    //public void setCourseMentorEmail(String courseMentorEmail) {
        //this.courseMentorEmail = courseMentorEmail;
    //}

    /**
     * Increment course notes count.
     */
    public void incrementCourseNotesCount() {
        this.courseNotesCount++;
    }

    /**
     * Increment course assessments count.
     */
    public void incrementCourseAssessmentsCount() {
        this.courseAssessmentsCount++;
    }

    /**
     * Decrement course notes count.
     */
    public void decrementCourseNotesCount() {
        // only decrement if count is greater than 0
        if (this.courseNotesCount > 0) {
            this.courseNotesCount--;
        }
    }

    /**
     * Decrement course assessments count.
     */
    public void decrementCourseAssessmentsCount() {
        // only decrement if count is greater than 0
        if (this.courseAssessmentsCount > 0) {
            this.courseAssessmentsCount--;
        }
    }

    /**
     * Sets term id.
     *
     * @param termId the term id
     */
    public void setTermId(int termId) {
        this.termId = termId;
    }

    /**
     * Get a list of all Courses and their IDs.
     *
     * @return an array of courses
     */
    public String[] getCourseList() {
        String[] courseList = new String[2];
        courseList[0] = Integer.toString(this.courseId);
        courseList[1] = this.courseName;
        return courseList;
    }
}
