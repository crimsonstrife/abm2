package com.pbarnhardt.abm2task1.Entity;

import static com.pbarnhardt.abm2task1.Utils.Constants.TERM_TABLE_NAME;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

/**
 * The type Terms(semesters) for student tracker.
 */
@Entity(tableName = TERM_TABLE_NAME)
public class Terms {
    @PrimaryKey(autoGenerate = true)
    private int termId;
    private String termName;
    private Date termStartDate;
    private Date termEndDate;
    private int termCourseCount;

    /**
     * Instantiates a new Terms.
     *
     * @param termName       the term name
     * @param termStartDate  the term start date
     * @param termEndDate    the term end date
     * Description: This is the constructor for the Terms class. It is used to create a new term. Course count is set to 0 by default.
     */
    @Ignore
    public Terms(int id, String termName, Date termStartDate, Date termEndDate) {
        this.termId = id;
        this.termName = termName;
        this.termStartDate = termStartDate;
        this.termEndDate = termEndDate;
        this.termCourseCount = 0;
    }

    public Terms(String termName, Date termStartDate, Date termEndDate) {
        this.termName = termName;
        this.termStartDate = termStartDate;
        this.termEndDate = termEndDate;
        this.termCourseCount = 0;
    }

    /**
     * Gets term id.
     *
     * @return the term id
     */
    public int getTermId() {
        return termId;
    }

    /**
     * Gets term name.
     *
     * @return the term name
     */
    public String getTermName() {
        return termName;
    }

    /**
     * Gets term start date.
     *
     * @return the term start date
     */
    public Date getTermStartDate() {
        return termStartDate;
    }

    /**
     * Gets term end date.
     *
     * @return the term end date
     */
    public Date getTermEndDate() {
        return termEndDate;
    }

    /**
     * Gets term course count.
     *
     * @return the term course count
     */
    public int getTermCourseCount() {
        return termCourseCount;
    }

    /**
     * Sets the term title.
     *
     * @param termName the term name
     */
    public void setTermName(String termName) {
        this.termName = termName;
    }

    /**
     * Sets term start date.
     *
     * @param termStartDate the term start date
     */
    public void setTermStartDate(Date termStartDate) {
        this.termStartDate = termStartDate;
    }

    /**
     * Sets term end date.
     *
     * @param termEndDate the term end date
     */
    public void setTermEndDate(Date termEndDate) {
        this.termEndDate = termEndDate;
    }

    /**
     * Set term id.
     *
     * @param termId the term id
     */
    public void setTermId(int termId) {
        this.termId = termId;
    }

    /**
     * Set term course count.
     *
     * @param termCourseCount the term course count
     */
    public void setTermCourseCount(int termCourseCount) {
        this.termCourseCount = termCourseCount;
    }
}
