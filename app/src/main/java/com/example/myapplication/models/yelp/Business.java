
package com.example.myapplication.models.yelp;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Business implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("is_closed")
    @Expose
    private Boolean isClosed;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("review_count")
    @Expose
    private Integer reviewCount;
    @SerializedName("categories")
    @Expose
    private List<Category> categories = null;
    @SerializedName("rating")
    @Expose
    private Double rating;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("coordinates")
    @Expose
    private Coordinates coordinates;
    @SerializedName("transactions")
    @Expose
    private List<Object> transactions = null;
    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("display_phone")
    @Expose
    private String displayPhone;
    @SerializedName("distance")
    @Expose
    private Double distance;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(Boolean isClosed) {
        this.isClosed = isClosed;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public List<Object> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Object> transactions) {
        this.transactions = transactions;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDisplayPhone() {
        return displayPhone;
    }

    public void setDisplayPhone(String displayPhone) {
        this.displayPhone = displayPhone;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.imageUrl);
        dest.writeValue(this.isClosed);
        dest.writeString(this.url);
        dest.writeValue(this.reviewCount);
        dest.writeTypedList(this.categories);
        dest.writeValue(this.rating);
        dest.writeParcelable(this.coordinates, flags);
        dest.writeList(this.transactions);
        dest.writeParcelable(this.location, flags);
        dest.writeString(this.phone);
        dest.writeString(this.displayPhone);
        dest.writeValue(this.distance);
        dest.writeString(this.price);
    }

    public Business() {
    }

    protected Business(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.imageUrl = in.readString();
        this.isClosed = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.url = in.readString();
        this.reviewCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.categories = in.createTypedArrayList(Category.CREATOR);
        this.rating = (Double) in.readValue(Double.class.getClassLoader());
        this.coordinates = in.readParcelable(Coordinates.class.getClassLoader());
        this.transactions = new ArrayList<Object>();
        in.readList(this.transactions, Object.class.getClassLoader());
        this.location = in.readParcelable(Location.class.getClassLoader());
        this.phone = in.readString();
        this.displayPhone = in.readString();
        this.distance = (Double) in.readValue(Double.class.getClassLoader());
        this.price = (String) in.readString();
    }

    public static final Parcelable.Creator<Business> CREATOR = new Parcelable.Creator<Business>() {
        @Override
        public Business createFromParcel(Parcel source) {
            return new Business(source);
        }

        @Override
        public Business[] newArray(int size) {
            return new Business[size];
        }
    };
}
