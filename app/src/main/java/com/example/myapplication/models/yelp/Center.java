
package com.example.myapplication.models.yelp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Center implements Parcelable {

    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("latitude")
    @Expose
    private Double latitude;

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.longitude);
        dest.writeValue(this.latitude);
    }

    public Center() {
    }

    protected Center(Parcel in) {
        this.longitude = (Double) in.readValue(Double.class.getClassLoader());
        this.latitude = (Double) in.readValue(Double.class.getClassLoader());
    }

    public static final Parcelable.Creator<Center> CREATOR = new Parcelable.Creator<Center>() {
        @Override
        public Center createFromParcel(Parcel source) {
            return new Center(source);
        }

        @Override
        public Center[] newArray(int size) {
            return new Center[size];
        }
    };
}
