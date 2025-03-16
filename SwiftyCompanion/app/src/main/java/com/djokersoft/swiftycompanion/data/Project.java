package com.djokersoft.swiftycompanion.data;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

public class Project implements Parcelable {
    private int id;
    private String name;
    private String slug;
    private String status;
    private String statusDisplay;
    private double finalMark;
    private String validated;

    public Project() {
    }

    protected Project(Parcel in) {
        id = in.readInt();
        name = in.readString();
        slug = in.readString();
        status = in.readString();
        statusDisplay = in.readString();
        finalMark = in.readDouble();
        validated = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(slug);
        dest.writeString(status);
        dest.writeString(statusDisplay);
        dest.writeDouble(finalMark);
        dest.writeString(validated);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Project> CREATOR = new Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDisplay() {
        return statusDisplay;
    }

    public void setStatusDisplay(String statusDisplay) {
        this.statusDisplay = statusDisplay;
    }

    public double getFinalMark() {
        return finalMark;
    }

    public void setFinalMark(double finalMark) {
        this.finalMark = finalMark;
    }

    public String getValidated() {
        return validated;
    }

    public void setValidated(String validated) {
        this.validated = validated;
    }

    // Métodos auxiliares
    public boolean isSuccessful() {
        return "finished".equals(status) && ("true".equals(validated) || finalMark >= 50);
    }

    public boolean isFinished() {
        return "finished".equals(status);
    }

    public boolean isInProgress() {
        return "in_progress".equals(status);
    }

    public int getStatusColor() {
        if (!isFinished()) {
            return Color.BLUE; // Em progresso
        } else if (isSuccessful()) {
            return Color.GREEN; // Sucesso
        } else {
            return Color.RED; // Falha
        }
    }

    public String getMarkDisplay() {
        if (finalMark <= 0 && !isFinished()) {
            return "N/A";
        }
        return String.format("%.0f", finalMark);
    }
}