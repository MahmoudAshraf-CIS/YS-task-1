package com.example.mannas.ytask.Content;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mannas on 6/19/2017.
 */

public class Feed implements Parcelable {
    String section;
    String subsection;

    String title;
    @SerializedName("abstract")
    String abstrac;
    String url;
    String updated_date;
    MultiMedia[] multimedia;

    public Feed() {
        url = "https://www.nytimes.com/";
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getSubsection() {
        return subsection;
    }

    public void setSubsection(String subsection) {
        this.subsection = subsection;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbstrac() {
        return abstrac;
    }

    public void setAbstrac(String abstrac) {
        this.abstrac = abstrac;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }

    public MultiMedia[] getMultimedia() {
        return multimedia;
    }


    public void setMultimedia(MultiMedia[] multimedia) {
        this.multimedia = multimedia;
    }

    protected Feed(Parcel in) {
        section = in.readString();
        subsection = in.readString();
        title = in.readString();
        abstrac = in.readString();
        url = in.readString();
        updated_date = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(section);
        dest.writeString(subsection);
        dest.writeString(title);
        dest.writeString(abstrac);
        dest.writeString(url);
        dest.writeString(updated_date);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Feed> CREATOR = new Parcelable.Creator<Feed>() {
        @Override
        public Feed createFromParcel(Parcel in) {
            return new Feed(in);
        }

        @Override
        public Feed[] newArray(int size) {
            return new Feed[size];
        }
    };
}