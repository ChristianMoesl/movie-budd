package at.mrtramoga.moviebuddy.themoviedb3;

import java.util.List;

/**
 * Created by chris_000 on 25.07.2016.
 */
public class MovieReviewsAnswer {

    public static class Review {

        private String id;

        private String author;

        private String content;

        private String url;

        public String getId() {
            return id;
        }

        public String getAuthor() {
            return author;
        }

        public String getContent() {
            return content;
        }

        public String getUrl() {
            return url;
        }
    }

    private long id;

    private long page;

    private List<Review> results;

    private long totalPages;

    private long totalResults;

    public long getId() {
        return id;
    }

    public long getPage() {
        return page;
    }

    public List<Review> getReviews() {
        return results;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public long getTotalResults() {
        return totalResults;
    }

}
