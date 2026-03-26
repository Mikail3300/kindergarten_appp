package com.example.kindergarten.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "kindergarten.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createChildrenTable = "CREATE TABLE " + DatabaseContract.ChildEntry.TABLE_NAME + " ("
                + DatabaseContract.ChildEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DatabaseContract.ChildEntry.COLUMN_CHILD_NAME + " TEXT NOT NULL, "
                + DatabaseContract.ChildEntry.COLUMN_PARENT_NAME + " TEXT NOT NULL, "
                + DatabaseContract.ChildEntry.COLUMN_GROUP_NAME + " TEXT NOT NULL, "
                + DatabaseContract.ChildEntry.COLUMN_TEACHER_NAME + " TEXT NOT NULL, "
                + DatabaseContract.ChildEntry.COLUMN_BIRTH_DATE + " TEXT NOT NULL, "
                + DatabaseContract.ChildEntry.COLUMN_PICKUP_TIME + " TEXT NOT NULL, "
                + DatabaseContract.ChildEntry.COLUMN_GENDER + " TEXT NOT NULL, "
                + DatabaseContract.ChildEntry.COLUMN_ALLERGIES + " INTEGER NOT NULL DEFAULT 0, "
                + DatabaseContract.ChildEntry.COLUMN_NAP_ENABLED + " INTEGER NOT NULL DEFAULT 1, "
                + DatabaseContract.ChildEntry.COLUMN_ACTIVITY_LEVEL + " INTEGER NOT NULL DEFAULT 50, "
                + DatabaseContract.ChildEntry.COLUMN_NOTES + " TEXT"
                + ")";
        db.execSQL(createChildrenTable);

        db.execSQL("INSERT INTO " + DatabaseContract.ChildEntry.TABLE_NAME + " ("
                + DatabaseContract.ChildEntry.COLUMN_CHILD_NAME + ", "
                + DatabaseContract.ChildEntry.COLUMN_PARENT_NAME + ", "
                + DatabaseContract.ChildEntry.COLUMN_GROUP_NAME + ", "
                + DatabaseContract.ChildEntry.COLUMN_TEACHER_NAME + ", "
                + DatabaseContract.ChildEntry.COLUMN_BIRTH_DATE + ", "
                + DatabaseContract.ChildEntry.COLUMN_PICKUP_TIME + ", "
                + DatabaseContract.ChildEntry.COLUMN_GENDER + ", "
                + DatabaseContract.ChildEntry.COLUMN_ALLERGIES + ", "
                + DatabaseContract.ChildEntry.COLUMN_NAP_ENABLED + ", "
                + DatabaseContract.ChildEntry.COLUMN_ACTIVITY_LEVEL + ", "
                + DatabaseContract.ChildEntry.COLUMN_NOTES
                + ") VALUES "
                + "('Ион Попеску', 'Мария Попеску', 'Старшая группа', 'Елена Русу', '12.04.2020', '17:30', 'Мальчик', 0, 1, 75, 'Любит конструкторы'),"
                + "('Анна Иванова', 'Олег Иванов', 'Средняя группа', 'Татьяна Ботнарь', '03.09.2021', '16:45', 'Девочка', 1, 1, 55, 'Аллергия на цитрусовые');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.ChildEntry.TABLE_NAME);
        onCreate(db);
    }
}
