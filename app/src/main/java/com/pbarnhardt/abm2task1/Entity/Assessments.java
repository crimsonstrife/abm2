package com.pbarnhardt.abm2task1.Entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.pbarnhardt.abm2task1.Enums.Types;

import java.util.Date;

/**
 * The type Assessments for courses in student tracker.
 */
@Entity(tableName = "Assessments", foreignKeys = @ForeignKey(entity = Courses.class,
        parentColumns = "courseId",
        childColumns = "courseId",
        onDelete = ForeignKey.RESTRICT))
public class Assessments {
    @PrimaryKey(autoGenerate = true)
    private int assessmentId;

    private String assessmentName;
    private String assessmentDescription;
    private Types assessmentType;
    private Date assessmentDueDate;
    private boolean assessmentAlert;
    private int courseId;

    /**
     * Instantiates a new Assessments.
     *
     * @param assessmentName     the assessment name
     * @param assessmentDescription the assessment description
     * @param assessmentType     the assessment type
     * @param assessmentDueDate  the assessment due date
     * @param assessmentAlert    the assessment alert
     * @param courseId           the course id
     */
    public Assessments(String assessmentName, Types assessmentType, String assessmentDescription, Date assessmentDueDate, boolean assessmentAlert, int courseId) {
        this.assessmentName = assessmentName;
        this.assessmentDescription = assessmentDescription;
        this.assessmentType = assessmentType;
        this.assessmentDueDate = assessmentDueDate;
        this.assessmentAlert = assessmentAlert;
        this.courseId = courseId;
    }

    /**
     * Gets assessment id.
     *
     * @return the assessment id
     */
    public int getAssessmentId() {
        return assessmentId;
    }

    /**
     * Gets assessment name.
     *
     * @return the assessment name
     */
    public String getAssessmentName() {
        return assessmentName;
    }

    /**
     * Gets assessment type.
     *
     * @return the assessment type
     */
    public Types getAssessmentType() {
        return assessmentType;
    }

    /**
     * Gets assessment description.
     *
     * @return the assessment description
     */
    public String getAssessmentDescription() {
        return assessmentDescription;
    }

    /**
     * Gets assessment due date.
     *
     * @return the assessment due date
     */
    public Date getAssessmentDueDate() {
        return assessmentDueDate;
    }

    /**
     * Gets assessment alert.
     *
     * @return the assessment alert
     */
    public boolean getAssessmentAlert() {
        return assessmentAlert;
    }

    /**
     * Gets course id.
     *
     * @return the course id
     */
    public int getAssessmentCourseId() {
        return courseId;
    }

    /**
     * Sets assessment name.
     *
     * @param assessmentName the assessment name
     */
    public void setAssessmentName(String assessmentName) {
        this.assessmentName = assessmentName;
    }

    /**
     * Sets assessment type.
     *
     * @param assessmentType the assessment type
     */
    public void setAssessmentType(Types assessmentType) {
        this.assessmentType = assessmentType;
    }

    /**
     * Sets assessment description.
     *
     * @param assessmentDescription the assessment description
     */
    public void setAssessmentDescription(String assessmentDescription) {
        this.assessmentDescription = assessmentDescription;
    }

    /**
     * Sets assessment due date.
     *
     * @param assessmentDueDate the assessment due date
     */
    public void setAssessmentDueDate(Date assessmentDueDate) {
        this.assessmentDueDate = assessmentDueDate;
    }

    /**
     * Sets course id.
     *
     * @param courseId the course id
     */
    public void setAssessmentCourseId(int courseId) {
        this.courseId = courseId;
    }

    /**
     * Sets assessment alert.
     *
     * @param assessmentAlert the assessment alert
     */
    public void setAssessmentAlert(boolean assessmentAlert) {
        this.assessmentAlert = assessmentAlert;
    }

    /**
     * Get all assessments for a course.
     *
     * @param courseId the course id
     * @return array of Assessments with name, type, description, and due date
     */
    public String[] getAssessmentsForCourse(int courseId) {
        String[] assessments = new String[5];
        assessments[0] = Integer.toString(this.assessmentId);
        assessments[1] = this.assessmentName;
        assessments[2] = this.assessmentType.toString();
        assessments[3] = this.assessmentDescription;
        assessments[4] = this.assessmentDueDate.toString();
        return assessments;
    }

    /**
     * Get courseId private int
     */
    public int getCourseId() {
        return courseId;
    }

    /**
     * Set assessmentId private int
     */
    public void setAssessmentId(int assessmentId) {
        this.assessmentId = assessmentId;
    }
}
