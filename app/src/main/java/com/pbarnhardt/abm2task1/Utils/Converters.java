package com.pbarnhardt.abm2task1.Utils;

import android.text.TextUtils;

import androidx.room.TypeConverter;

import com.pbarnhardt.abm2task1.Enums.Status;
import com.pbarnhardt.abm2task1.Enums.Types;

import java.util.Date;

public class Converters {
    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static String fromAssessmentTypeToString(Types assessmentType) {
        if(assessmentType == null) {
            return null;
        }
        return assessmentType.name();
    }

    @TypeConverter
    public static Types fromStringToAssessmentType(String assessmentType) {
        if(TextUtils.isEmpty(assessmentType)) {
            return Types.OBJECTIVE;
        }
        return Types.valueOf(assessmentType);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static String fromCourseStatusToString(Status courseStatus) {
        if(courseStatus == null) {
            return null;
        }
        return courseStatus.name();
    }

    @TypeConverter
    public static Status fromStringToCourseStatus(String courseStatus) {
        if(TextUtils.isEmpty(courseStatus)) {
            return Status.PROGRESS;
        }
        return Status.valueOf(courseStatus);
    }
}
