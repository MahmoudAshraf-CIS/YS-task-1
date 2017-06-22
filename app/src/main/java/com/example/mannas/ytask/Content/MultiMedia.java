package com.example.mannas.ytask.Content;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mannas on 6/19/2017.
 */

public class MultiMedia implements Parcelable {
    String url;
    String format;
    int height;
    int width;
    String type;
    String subtype;
    String caption;
    String copyright;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    protected MultiMedia(Parcel in) {
        url = in.readString();
        format = in.readString();
        height = in.readInt();
        width = in.readInt();
        type = in.readString();
        subtype = in.readString();
        caption = in.readString();
        copyright = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(format);
        dest.writeInt(height);
        dest.writeInt(width);
        dest.writeString(type);
        dest.writeString(subtype);
        dest.writeString(caption);
        dest.writeString(copyright);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MultiMedia> CREATOR = new Parcelable.Creator<MultiMedia>() {
        @Override
        public MultiMedia createFromParcel(Parcel in) {
            return new MultiMedia(in);
        }

        @Override
        public MultiMedia[] newArray(int size) {
            return new MultiMedia[size];
        }
    };
}