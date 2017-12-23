package com.tranxitpro.app.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by RAJKUMAR on 20-06-2017.
 */

public class Driver implements Parcelable {
    public String fname;
    public String lname;
    public String email;
    public String mobile;
    public String rating;
    public String img;

    public Driver(Parcel in) {
        fname = in.readString();
        lname = in.readString();
        email = in.readString();
        mobile = in.readString();
        rating = in.readString();
        img = in.readString();
    }

    public static final Creator<Driver> CREATOR = new Creator<Driver>() {
        @Override
        public Driver createFromParcel(Parcel in) {
            return new Driver(in);
        }

        @Override
        public Driver[] newArray(int size) {
            return new Driver[size];
        }
    };

    public Driver() {
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fname);
        dest.writeString(lname);
        dest.writeString(email);
        dest.writeString(mobile);
        dest.writeString(rating);
        dest.writeString(img);
    }

    @Override
    public String toString() {
        return "Driver{" +
                "fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", rating='" + rating + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}
