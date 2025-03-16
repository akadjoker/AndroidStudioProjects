package com.djokersoft.swiftycompanion.data;
import android.os.Parcel;
import android.os.Parcelable;


public class UserImage implements Parcelable {
    private String link;
    private Versions versions;

    public UserImage() {
    }

    protected UserImage(Parcel in) {
        link = in.readString();
        versions = in.readParcelable(Versions.class.getClassLoader());
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Versions getVersions() {
        return versions;
    }

    public void setVersions(Versions versions) {
        this.versions = versions;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(link);
        dest.writeParcelable(versions, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserImage> CREATOR = new Creator<UserImage>() {
        @Override
        public UserImage createFromParcel(Parcel in) {
            return new UserImage(in);
        }

        @Override
        public UserImage[] newArray(int size) {
            return new UserImage[size];
        }
    };

    public static class Versions implements Parcelable {
        private String large;
        private String medium;
        private String small;
        private String micro;

        public Versions() {
        }

        protected Versions(Parcel in) {
            large = in.readString();
            medium = in.readString();
            small = in.readString();
            micro = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(large);
            dest.writeString(medium);
            dest.writeString(small);
            dest.writeString(micro);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Versions> CREATOR = new Creator<Versions>() {
            @Override
            public Versions createFromParcel(Parcel in) {
                return new Versions(in);
            }

            @Override
            public Versions[] newArray(int size) {
                return new Versions[size];
            }
        };

        // Getters e setters
        public String getLarge() {
            return large;
        }

        public void setLarge(String large) {
            this.large = large;
        }

        public String getMedium() {
            return medium;
        }

        public void setMedium(String medium) {
            this.medium = medium;
        }

        public String getSmall() {
            return small;
        }

        public void setSmall(String small) {
            this.small = small;
        }

        public String getMicro() {
            return micro;
        }

        public void setMicro(String micro) {
            this.micro = micro;
        }
    }
}
