package com.djokersoft.swiftycompanion.data;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class CursusUser implements Parcelable {
    private int id;
    private double level;

    @SerializedName("cursus_id")
    private int cursusId;

    private List<Skill> skills;

    public CursusUser() {
    }

    protected CursusUser(Parcel in) {
        id = in.readInt();
        level = in.readDouble();
        cursusId = in.readInt();
        skills = new ArrayList<>();
        in.readList(skills, Skill.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeDouble(level);
        dest.writeInt(cursusId);
        dest.writeList(skills);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CursusUser> CREATOR = new Creator<CursusUser>() {
        @Override
        public CursusUser createFromParcel(Parcel in) {
            return new CursusUser(in);
        }

        @Override
        public CursusUser[] newArray(int size) {
            return new CursusUser[size];
        }
    };

    // Getters e setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLevel() {
        return level;
    }

    public void setLevel(double level) {
        this.level = level;
    }

    public int getCursusId() {
        return cursusId;
    }

    public void setCursusId(int cursusId) {
        this.cursusId = cursusId;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }
}
