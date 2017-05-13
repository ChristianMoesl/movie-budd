package at.mrtramoga.moviebuddy.themoviedb3;

import com.android.volley.Response;

/**
 * Created by chris_000 on 13.05.2017.
 */
public class GenreListRequest extends MovieDbRequest<GenreList> {
    protected static final String GENRE_PATH = "/genre/movie/list";

    public GenreListRequest(String apiKey, Response.Listener<GenreList> listener) {
        super(apiKey, GENRE_PATH, GenreList.class, listener);
    }
}
