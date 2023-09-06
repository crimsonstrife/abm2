package com.pbarnhardt.abm2task1.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.pbarnhardt.abm2task1.Entity.Assessments;
import com.pbarnhardt.abm2task1.Entity.Courses;
import com.pbarnhardt.abm2task1.Entity.Notes;
import com.pbarnhardt.abm2task1.Entity.Terms;

/**
 * The type Course (StudentTracker) database.
 */
@Database(entities = {Assessments.class, Courses.class, Notes.class, Terms.class}, version = 1, exportSchema = false)
public abstract class CourseDatabase extends RoomDatabase {

}
