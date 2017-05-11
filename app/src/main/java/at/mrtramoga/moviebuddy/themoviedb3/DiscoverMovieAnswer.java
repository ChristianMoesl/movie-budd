package at.mrtramoga.moviebuddy.themoviedb3;

import java.util.List;

/**
 * Answer on a /discover/move request.
 */
public class DiscoverMovieAnswer {
    private int page;
    private List<BasicMovieInformation> results;
    private int totalResults;
    private int totalPages;

    public int getPage() {
        return page;
    }

    public List<BasicMovieInformation> getMovieDescriptions() {
        return results;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
