package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by RenatoHenrique on 09/11/2015.
 */
public class ReviewMinutia implements Parcelable {

    String authorReview;
    String contentReview;


    public ReviewMinutia( String vauthorReview, String vcontentReview)
    {
        this.authorReview = vauthorReview;
        this.contentReview = vcontentReview;

    }

    private ReviewMinutia(Parcel in) {
        authorReview = in.readString();
        contentReview = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String toString() { return contentReview; }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(authorReview);
        parcel.writeString(contentReview);

    }



    public final Parcelable.Creator<ReviewMinutia> CREATOR = new Parcelable.Creator<ReviewMinutia>() {
        @Override
        public ReviewMinutia createFromParcel(Parcel parcel) {
            return new ReviewMinutia(parcel);
        }

        @Override
        public ReviewMinutia[] newArray(int i) {
            return new ReviewMinutia[i];
        }

    };

}
