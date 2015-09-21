package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by RenatoHenrique on 15/09/2015.
 */
public class MovieMinutia implements Parcelable {

    String titleMovie;
    String posterMovie;
    String releaseDate;
    String ratingMovie;
    String plotMovie;


    public MovieMinutia(String vtitleMovie, String vposterMovie, String vreleaseDate, String vratingMovie, String vplotMovie)
    {
        this.titleMovie = vtitleMovie;
        this.posterMovie = vposterMovie;
        this.releaseDate = vreleaseDate;
        this.ratingMovie = vratingMovie;
        this.plotMovie = vplotMovie;
    }

    private MovieMinutia(Parcel in) {
        titleMovie = in.readString();
        posterMovie = in.readString();
        releaseDate = in.readString();
        ratingMovie = in.readString();
        plotMovie = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String toString() { return titleMovie + "--" + posterMovie + "--" + releaseDate  + "--" + ratingMovie + "--" + plotMovie; }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(titleMovie);
        parcel.writeString(posterMovie);
        parcel.writeString(releaseDate);
        parcel.writeString(ratingMovie);
        parcel.writeString(plotMovie);
    }



    public final Parcelable.Creator<MovieMinutia> CREATOR = new Parcelable.Creator<MovieMinutia>() {
        @Override
        public MovieMinutia createFromParcel(Parcel parcel) {
            return new MovieMinutia(parcel);
        }

        @Override
        public MovieMinutia[] newArray(int i) {
            return new MovieMinutia[i];
        }

    };














}
