package com.example.kindergarten.data;

import android.provider.BaseColumns;

public final class DatabaseContract {
    private DatabaseContract() {
    }

    public static class ChildEntry implements BaseColumns {
        public static final String TABLE_NAME = "children";
        public static final String COLUMN_CHILD_NAME = "child_name";
        public static final String COLUMN_PARENT_NAME = "parent_name";
        public static final String COLUMN_GROUP_NAME = "group_name";
        public static final String COLUMN_TEACHER_NAME = "teacher_name";
        public static final String COLUMN_BIRTH_DATE = "birth_date";
        public static final String COLUMN_PICKUP_TIME = "pickup_time";
        public static final String COLUMN_GENDER = "gender";
        public static final String COLUMN_ALLERGIES = "allergies";
        public static final String COLUMN_NAP_ENABLED = "nap_enabled";
        public static final String COLUMN_ACTIVITY_LEVEL = "activity_level";
        public static final String COLUMN_NOTES = "notes";
    }
}
