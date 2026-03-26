package com.example.kindergarten.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.kindergarten.model.Child;

import java.util.ArrayList;
import java.util.List;

public class ChildRepository {
    private final DatabaseHelper databaseHelper;

    public ChildRepository(Context context) {
        this.databaseHelper = new DatabaseHelper(context);
    }

    public long addChild(Child child) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = mapChildToValues(child);
        long insertedId = db.insert(DatabaseContract.ChildEntry.TABLE_NAME, null, values);
        db.close();
        return insertedId;
    }

    public int updateChild(Child child) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = mapChildToValues(child);
        int updatedRows = db.update(
                DatabaseContract.ChildEntry.TABLE_NAME,
                values,
                DatabaseContract.ChildEntry._ID + "=?",
                new String[]{String.valueOf(child.getId())}
        );
        db.close();
        return updatedRows;
    }

    public int deleteChild(long childId) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int deletedRows = db.delete(
                DatabaseContract.ChildEntry.TABLE_NAME,
                DatabaseContract.ChildEntry._ID + "=?",
                new String[]{String.valueOf(childId)}
        );
        db.close();
        return deletedRows;
    }

    public List<Child> getAllChildren(String orderBy) {
        List<Child> children = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseContract.ChildEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                orderBy
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                children.add(readChild(cursor));
            }
            cursor.close();
        }
        db.close();
        return children;
    }

    public List<Child> searchChildren(String query, String orderBy) {
        if (query == null || query.trim().isEmpty()) {
            return getAllChildren(orderBy);
        }

        List<Child> children = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String selection = DatabaseContract.ChildEntry.COLUMN_CHILD_NAME + " LIKE ? OR "
                + DatabaseContract.ChildEntry.COLUMN_PARENT_NAME + " LIKE ? OR "
                + DatabaseContract.ChildEntry.COLUMN_GROUP_NAME + " LIKE ? OR "
                + DatabaseContract.ChildEntry.COLUMN_TEACHER_NAME + " LIKE ?";
        String wildcard = "%" + query.trim() + "%";
        String[] args = new String[]{wildcard, wildcard, wildcard, wildcard};

        Cursor cursor = db.query(
                DatabaseContract.ChildEntry.TABLE_NAME,
                null,
                selection,
                args,
                null,
                null,
                orderBy
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                children.add(readChild(cursor));
            }
            cursor.close();
        }
        db.close();
        return children;
    }

    public Child getChildById(long childId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseContract.ChildEntry.TABLE_NAME,
                null,
                DatabaseContract.ChildEntry._ID + "=?",
                new String[]{String.valueOf(childId)},
                null,
                null,
                null
        );

        Child child = null;
        if (cursor != null && cursor.moveToFirst()) {
            child = readChild(cursor);
            cursor.close();
        }
        db.close();
        return child;
    }

    private ContentValues mapChildToValues(Child child) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.ChildEntry.COLUMN_CHILD_NAME, child.getChildName());
        values.put(DatabaseContract.ChildEntry.COLUMN_PARENT_NAME, child.getParentName());
        values.put(DatabaseContract.ChildEntry.COLUMN_GROUP_NAME, child.getGroupName());
        values.put(DatabaseContract.ChildEntry.COLUMN_TEACHER_NAME, child.getTeacherName());
        values.put(DatabaseContract.ChildEntry.COLUMN_BIRTH_DATE, child.getBirthDate());
        values.put(DatabaseContract.ChildEntry.COLUMN_PICKUP_TIME, child.getPickupTime());
        values.put(DatabaseContract.ChildEntry.COLUMN_GENDER, child.getGender());
        values.put(DatabaseContract.ChildEntry.COLUMN_ALLERGIES, child.hasAllergies() ? 1 : 0);
        values.put(DatabaseContract.ChildEntry.COLUMN_NAP_ENABLED, child.isNapEnabled() ? 1 : 0);
        values.put(DatabaseContract.ChildEntry.COLUMN_ACTIVITY_LEVEL, child.getActivityLevel());
        values.put(DatabaseContract.ChildEntry.COLUMN_NOTES, child.getNotes());
        return values;
    }

    private Child readChild(Cursor cursor) {
        Child child = new Child();
        child.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.ChildEntry._ID)));
        child.setChildName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ChildEntry.COLUMN_CHILD_NAME)));
        child.setParentName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ChildEntry.COLUMN_PARENT_NAME)));
        child.setGroupName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ChildEntry.COLUMN_GROUP_NAME)));
        child.setTeacherName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ChildEntry.COLUMN_TEACHER_NAME)));
        child.setBirthDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ChildEntry.COLUMN_BIRTH_DATE)));
        child.setPickupTime(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ChildEntry.COLUMN_PICKUP_TIME)));
        child.setGender(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ChildEntry.COLUMN_GENDER)));
        child.setAllergies(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ChildEntry.COLUMN_ALLERGIES)) == 1);
        child.setNapEnabled(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ChildEntry.COLUMN_NAP_ENABLED)) == 1);
        child.setActivityLevel(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ChildEntry.COLUMN_ACTIVITY_LEVEL)));
        child.setNotes(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ChildEntry.COLUMN_NOTES)));
        return child;
    }
}
