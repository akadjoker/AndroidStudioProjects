package com.djokersoft.swiftycompanion.data;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class User implements Parcelable {
    private int id;
    private String login;
    private String email;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    private String location;
    private int wallet;

    @SerializedName("image")
    private UserImage image;

    @SerializedName("cursus_users")
    private List<CursusUser> cursusUsers;

    // Campos processados que não estão no JSON
    private transient List<Skill> skills = new ArrayList<>();
    private transient List<Project> projects = new ArrayList<>();



    private transient double level;
    private transient int completedProjects;
    private transient int failedProjects;

    // Construtor vazio para Gson
    public User() {
    }

    // Getter e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getWallet() {
        return wallet;
    }

    public void setWallet(int wallet) {
        this.wallet = wallet;
    }

    public UserImage getImage() {
        return image;
    }

    public void setImage(UserImage image) {
        this.image = image;
    }

    public List<CursusUser> getCursusUsers() {
        return cursusUsers;
    }

    public void setCursusUsers(List<CursusUser> cursusUsers) {
        this.cursusUsers = cursusUsers;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public double getLevel() {
        return level;
    }

    public void setLevel(double level) {
        this.level = level;
    }

    public int getCompletedProjects() {
        return completedProjects;
    }

    public void setCompletedProjects(int completedProjects) {
        this.completedProjects = completedProjects;
    }

    public int getFailedProjects() {
        return failedProjects;
    }

    public void setFailedProjects(int failedProjects) {
        this.failedProjects = failedProjects;
    }

    // Métodos auxiliares
    public String getImageUrl() {
        if (image != null && image.getLink() != null) {
            return image.getLink();
        }
        return "https://cdn.intra.42.fr/users/default.png";
    }

    public String getFullName() {
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        }
        return login;
    }

    public int getLevelInteger() {
        return (int) Math.floor(level);
    }

    public int getLevelPercentage() {
        double fractionalPart = level - Math.floor(level);
        return (int) (fractionalPart * 100);
    }

    // Implementação Parcelable
    protected User(Parcel in) {
        id = in.readInt();
        login = in.readString();
        email = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        location = in.readString();
        wallet = in.readInt();
        image = in.readParcelable(UserImage.class.getClassLoader());
        level = in.readDouble();
        completedProjects = in.readInt();
        failedProjects = in.readInt();
        skills = new ArrayList<>();
        in.readList(skills, Skill.class.getClassLoader());
        projects = new ArrayList<>();
        in.readList(projects, Project.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(login);
        dest.writeString(email);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(location);
        dest.writeInt(wallet);
        dest.writeParcelable(image, flags);
        dest.writeDouble(level);
        dest.writeInt(completedProjects);
        dest.writeInt(failedProjects);
        dest.writeList(skills);
        dest.writeList(projects);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}