package com.djokersoft.swiftycompanion.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ProjectUser implements Parcelable {
    private int id;

    @SerializedName("final_mark")
    private Integer finalMark;

    private String status;

    @SerializedName("validated?")
    private Boolean validated;

    private Project project;


    public ProjectUser() {
    }

    // Getters e setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getFinalMark() {
        return finalMark;
    }

    public void setFinalMark(Integer finalMark) {
        this.finalMark = finalMark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getValidated() {
        return validated;
    }

    public void setValidated(Boolean validated) {
        this.validated = validated;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    // Implementação Parcelable
    protected ProjectUser(Parcel in) {
        id = in.readInt();
        if (in.readByte() == 0) {
            finalMark = null;
        } else {
            finalMark = in.readInt();
        }
        status = in.readString();
        byte tmpValidated = in.readByte();
        validated = tmpValidated == 0 ? null : tmpValidated == 1;
        project = in.readParcelable(Project.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        if (finalMark == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(finalMark);
        }
        dest.writeString(status);
        dest.writeByte((byte) (validated == null ? 0 : validated ? 1 : 2));
        dest.writeParcelable(project, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<ProjectUser> CREATOR = new Creator<ProjectUser>() {
        @Override
        public ProjectUser createFromParcel(Parcel in) {
            return new ProjectUser(in);
        }

        @Override
        public ProjectUser[] newArray(int size) {
            return new ProjectUser[size];
        }
    };
}