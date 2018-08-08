
package com.example.myapplication.models.yelp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Yelp implements Parcelable {

    @SerializedName("businesses")
    @Expose
    private List<Business> businesses = null;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("region")
    @Expose
    private Region region;

    public List<Business> getBusinesses() {
        return businesses;
    }

    public void setBusinesses(List<Business> businesses) {
        this.businesses = businesses;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.businesses);
        dest.writeValue(this.total);
        dest.writeParcelable(this.region, flags);
    }

    public Yelp() {
    }

    protected Yelp(Parcel in) {
        this.businesses = in.createTypedArrayList(Business.CREATOR);
        this.total = (Integer) in.readValue(Integer.class.getClassLoader());
        this.region = in.readParcelable(Region.class.getClassLoader());
    }

    public static final Parcelable.Creator<Yelp> CREATOR = new Parcelable.Creator<Yelp>() {
        @Override
        public Yelp createFromParcel(Parcel source) {
            return new Yelp(source);
        }

        @Override
        public Yelp[] newArray(int size) {
            return new Yelp[size];
        }
    };
}
