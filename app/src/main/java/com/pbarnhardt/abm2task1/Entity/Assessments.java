package com.pbarnhardt.abm2task1.Entity;

import static com.pbarnhardt.abm2task1.Utils.Constants.ASSESSMENT_TABLE_NAME;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.pbarnhardt.abm2task1.Enums.Types;

import java.util.Date;

/**
 * The type Assessments for courses in student tracker.
 */
@Entity(tableName = ASSESSMENT_TABLE_NAME)
public class Assessments {
    @PrimaryKey(autoGenerate = true)
    private int assessmentId;

    private String assessmentName;
    private String assessmentDescription;
    private Types assessmentType;
    private Date assessmentStartDate;
    private Date assessmentDueDate;
    private boolean assessmentStartAlert;
    private boolean assessmentDueAlert;
    private int courseId;

    /**
     * Instantiates a new Assessments.
     *
     * @param assessmentName     the assessment name
     * @param assessmentDescription the assessment description
     * @param assessmentType     the assessment type
     * @param assessmentStartDate the assessment start date
     * @param assessmentDueDate  the assessment due date
     * @param assessmentStartAlert    the assessment start alert
     * @param assessmentDueAlert the assessment due alert
     * @param courseId           the course id
     */
    @Ignore
    public Assessments(int assessmentId, String assessmentName, Types assessmentType, String assessmentDescription, Date assessmentStartDate, Date assessmentDueDate, boolean assessmentStartAlert, boolean assessmentDueAlert, int courseId) {
        this.assessmentId = assessmentId;
        this.assessmentName = assessmentName;
        this.assessmentDescription = assessmentDescription;
        this.assessmentType = assessmentType;
        this.assessmentStartDate = assessmentStartDate;
        this.assessmentDueDate = assessmentDueDate;
        this.assessmentStartAlert = assessmentStartAlert;
        this.assessmentDueAlert = assessmentDueAlert;
        this.courseId = courseId;
    }
    public Assessments(String assessmentName, Types assessmentType, String assessmentDescription, Date assessmentStartDate, Date assessmentDueDate, boolean assessmentStartAlert, boolean assessmentDueAlert, int courseId) {
        this.assessmentName = assessmentName;
        this.assessmentDescription = assessmentDescription;
        this.assessmentType = assessmentType;
        this.assessmentStartDate = assessmentStartDate;
        this.assessmentDueDate = assessmentDueDate;
        this.assessmentStartAlert = assessmentStartAlert;
        this.assessmentDueAlert = assessmentDueAlert;
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
     * Gets assessment start date.
     *
     * @return the assessment start date
     */
    public Date getAssessmentStartDate() {
        return assessmentStartDate;
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
     * Gets assessment start alert.
     *
     * @return the assessment alert
     */
    public boolean getAssessmentStartAlert() {
        return assessmentStartAlert;
    }

    /**
     * Gets assessment due alert.
     *
     * @return the assessment alert
     */
    public boolean getAssessmentDueAlert() {
        return assessmentDueAlert;
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
     * Sets assessment start date.
     *
     * @param assessmentStartDate the assessment start date
     */
    public void setAssessmentStartDate(Date assessmentStartDate) {
        this.assessmentStartDate = assessmentStartDate;
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
    public void setAssessmentStartAlert(boolean assessmentAlert) {
        this.assessmentStartAlert = assessmentAlert;
    }

    /**
     * Sets assessment alert.
     *
     * @param assessmentAlert the assessment alert
     */
    public void setAssessmentDueAlert(boolean assessmentAlert) {
        this.assessmentDueAlert = assessmentAlert;
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
