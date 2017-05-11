package at.mrtramoga.moviebuddy.themoviedb3;


import android.support.annotation.Nullable;

import java.util.List;

/**
 * Represents the whole description of a movie from the movie db webservice.
 */
public class BasicMovieInformation implements MovieInformation {

    @Nullable
    private String posterPath;

    private boolean adult;

    private String overview;

    @Nullable
    private String releaseDate;

    private List<Integer> genreIds;

    private long id;

    private String originalTitle;

    private String originalLanguage;

    private String title;

    private String backdropPath;

    private double popularity;

    private long voteCount;

    private boolean video;

    private double voteAverage;

    @Nullable
    public String getPosterPath() {
        return posterPath;
    }

    public boolean isAdult() {
        return adult;
    }

    public String getOverview() {
        return overview;
    }

    @Nullable
    public String getReleaseDate() {
        return releaseDate;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public long getId() {
        return id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getTitle() {
        return title;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public double getPopularity() {
        return popularity;
    }

    public long getVoteCount() {
        return voteCount;
    }

    public boolean isVideo() {
        return video;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public boolean hasPoster() {
        return posterPath != null;
    }
}