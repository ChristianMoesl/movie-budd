package at.mrtramoga.moviebuddy;

public class Movie {

    private final String mTitle;
    private final String mDescription;
    private final String mPosterUrl;
    private boolean mIsSeen = false;

    public Movie(String title, String description, String posterUrl) {
        mTitle = title;
        mDescription = description;
        mPosterUrl = posterUrl;
    }

    public String title() {
        return mTitle;
    }

    public String description() {
        return mDescription;
    }

    public String posterUrl() {
        return mPosterUrl;
    }

    public boolean isSeen() {
        return mIsSeen;
    }

    public void markSeen() {
        mIsSeen = true;
    }

}
