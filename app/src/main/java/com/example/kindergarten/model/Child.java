package com.example.kindergarten.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Child implements Parcelable {
    private long id;
    private String childName;
    private String parentName;
    private String groupName;
    private String teacherName;
    private String birthDate;
    private String pickupTime;
    private String gender;
    private boolean allergies;
    private boolean napEnabled;
    private int activityLevel;
    private String notes;

    public Child() {
    }

    public Child(long id, String childName, String parentName, String groupName, String teacherName,
                 String birthDate, String pickupTime, String gender, boolean allergies,
                 boolean napEnabled, int activityLevel, String notes) {
        this.id = id;
        this.childName = childName;
        this.parentName = parentName;
        this.groupName = groupName;
        this.teacherName = teacherName;
        this.birthDate = birthDate;
        this.pickupTime = pickupTime;
        this.gender = gender;
        this.allergies = allergies;
        this.napEnabled = napEnabled;
        this.activityLevel = activityLevel;
        this.notes = notes;
    }

    protected Child(Parcel in) {
        id = in.readLong();
        childName = in.readString();
        parentName = in.readString();
        groupName = in.readString();
        teacherName = in.readString();
        birthDate = in.readString();
        pickupTime = in.readString();
        gender = in.readString();
        allergies = in.readByte() != 0;
        napEnabled = in.readByte() != 0;
        activityLevel = in.readInt();
        notes = in.readString();
    }

    public static final Creator<Child> CREATOR = new Creator<Child>() {
        @Override
        public Child createFromParcel(Parcel in) {
            return new Child(in);
        }

        @Override
        public Child[] newArray(int size) {
            return new Child[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean hasAllergies() {
        return allergies;
    }

    public void setAllergies(boolean allergies) {
        this.allergies = allergies;
    }

    public boolean isNapEnabled() {
        return napEnabled;
    }

    public void setNapEnabled(boolean napEnabled) {
        this.napEnabled = napEnabled;
    }

    public int getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(int activityLevel) {
        this.activityLevel = activityLevel;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAllergiesText() {
        return allergies ? "Есть аллергия" : "Нет аллергии";
    }

    public String getNapText() {
        return napEnabled ? "Тихий час включен" : "Тихий час отключен";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(id);
        parcel.writeString(childName);
        parcel.writeString(parentName);
        parcel.writeString(groupName);
        parcel.writeString(teacherName);
        parcel.writeString(birthDate);
        parcel.writeString(pickupTime);
        parcel.writeString(gender);
        parcel.writeByte((byte) (allergies ? 1 : 0));
        parcel.writeByte((byte) (napEnabled ? 1 : 0));
        parcel.writeInt(activityLevel);
        parcel.writeString(notes);
    }
}
