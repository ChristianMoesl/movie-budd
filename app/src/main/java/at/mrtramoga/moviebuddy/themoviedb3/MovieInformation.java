package at.mrtramoga.moviebuddy.themoviedb3;

import android.support.annotation.Nullable;

/**
 * Created by chris_000 on 28.07.2016.
 */
public interface MovieInformation {

    @Nullable
    String getPosterPath();

    boolean isAdult();

    String getOverview();

    @Nullable
    String getReleaseDate();

    long getId();

    String getOriginalTitle();

    String getOriginalLanguage();

    String getTitle();

    String getBackdropPath();

    double getPopularity();

    long getVoteCount();

    boolean isVideo();

    double getVoteAverage();

    boolean hasPoster();
}
