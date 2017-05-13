package at.mrtramoga.moviebuddy.themoviedb3;

import java.util.List;

/**
 * Created by chris_000 on 13.05.2017.
 */
public class GenreList {
    private List<Genre> genres;

    public GenreList(List<Genre> genres) {
        this.genres = genres;
    }

    public List<Genre> getList() {
        return genres;
    }
}
