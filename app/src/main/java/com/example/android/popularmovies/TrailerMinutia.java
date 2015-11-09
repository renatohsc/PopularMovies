package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by RenatoHenrique on 09/11/2015.
 */
public class TrailerMinutia implements Parcelable {

    String sourceTrailer;


    public TrailerMinutia( String vsourceTrailer)
    {
        this.sourceTrailer = vsourceTrailer;

    }

    private TrailerMinutia(Parcel in) {
        sourceTrailer = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String toString() { return sourceTrailer; }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(sourceTrailer);

    }



    public final Parcelable.Creator<TrailerMinutia> CREATOR = new Parcelable.Creator<TrailerMinutia>() {
        @Override
        public TrailerMinutia createFromParcel(Parcel parcel) {
            return new TrailerMinutia(parcel);
        }

        @Override
        public TrailerMinutia[] newArray(int i) {
            return new TrailerMinutia[i];
        }

    };


}
