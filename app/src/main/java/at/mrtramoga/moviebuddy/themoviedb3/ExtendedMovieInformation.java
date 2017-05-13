package at.mrtramoga.moviebuddy.themoviedb3;

import java.util.List;

public class ExtendedMovieInformation implements MovieInformation {

    public static class ProductionCompany {

        private long id;

        private String name;

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    public static class ProductionCountry {

        private String iso31661;

        private String name;

        public String getIso31661() {
            return iso31661;
        }

        public String getName() {
            return name;
        }
    }

    public static class SpokenLanguage {

        private String iso6391;

        private String name;

        public String getIso6391() {
            return iso6391;
        }

        public String getName() {
            return name;
        }
    }

    private boolean adult;

    private String backdropPath;

    private Object belongsToCollection;

    private long budget;

    private List<Genre> genres;

    private String homepage;

    private long id;

    private String imdbId;

    private String originalLanguage;

    private String originalTitle;

    private String overview;

    private double popularity;

    private String posterPath;

    private List<ProductionCompany> productionCompanies;

    private List<ProductionCountry> productionCountries;

    private String releaseDate;

    private long revenue;

    private long runtime;

    private List<SpokenLanguage> spokenLanguages;

    private String status;

    private String tagline;

    private String title;

    private boolean video;

    private double voteAverage;

    private long voteCount;

    public boolean isAdult() {
        return adult;
    }

    @Override
    public String getBackdropPath() {
        return backdropPath;
    }

    public Object getBelongsToCollection() {
        return belongsToCollection;
    }

    public long getBudget() {
        return budget;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public String getHomepage() {
        return homepage;
    }

    @Override
    public long getId() {
        return id;
    }

    public String getImdbId() {
        return imdbId;
    }

    @Override
    public String getOriginalLanguage() {
        return originalLanguage;
    }

    @Override
    public String getOriginalTitle() {
        return originalTitle;
    }

    @Override
    public String getOverview() {
        return overview;
    }

    @Override
    public double getPopularity() {
        return popularity;
    }

    @Override
    public String getPosterPath() {
        return posterPath;
    }

    public List<ProductionCompany> getProductionCompanies() {
        return productionCompanies;
    }

    public List<ProductionCountry> getProductionCountries() {
        return productionCountries;
    }

    @Override
    public String getReleaseDate() {
        return releaseDate;
    }

    public long getRevenue() {
        return revenue;
    }

    public long getRuntime() {
        return runtime;
    }

    public List<SpokenLanguage> getSpokenLanguages() {
        return spokenLanguages;
    }

    public String getStatus() {
        return status;
    }

    public String getTagline() {
        return tagline;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public boolean isVideo() {
        return video;
    }

    @Override
    public double getVoteAverage() {
        return voteAverage;
    }

    public long getVoteCount() {
        return voteCount;
    }

    @Override
    public boolean hasPoster() {
        return posterPath != null;
    }
}
