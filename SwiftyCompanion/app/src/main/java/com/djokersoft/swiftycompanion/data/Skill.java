package com.djokersoft.swiftycompanion.data;

import android.os.Parcel;
import android.os.Parcelable;
public class Skill implements Parcelable {
    private int id;
    private String name;
    private double level;

    public Skill() {
    }

    protected Skill(Parcel in) {
        id = in.readInt();
        name = in.readString();
        level = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeDouble(level);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Skill> CREATOR = new Creator<Skill>() {
        @Override
        public Skill createFromParcel(Parcel in) {
            return new Skill(in);
        }

        @Override
        public Skill[] newArray(int size) {
            return new Skill[size];
        }
    };

    // Getters e setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLevel() {
        return level;
    }

    public void setLevel(double level) {
        this.level = level;
    }

    // Métodos auxiliares
    public int getLevelPercentage() {
        return (int) (level * 100);
    }

    public String getLevelDisplay() {
        return String.format("%.2f", level);
    }
}
