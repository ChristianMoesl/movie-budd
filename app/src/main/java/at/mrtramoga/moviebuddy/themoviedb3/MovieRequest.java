package at.mrtramoga.moviebuddy.themoviedb3;

import com.android.volley.Response;

public class MovieRequest extends MovieDbRequest<ExtendedMovieInformation> {

    private static final String MOVIE_PATH = "/movie";

    public MovieRequest(String apiKey, long movieId, Response.Listener<ExtendedMovieInformation> listener) {
        super(apiKey, MOVIE_PATH + '/' + Long.toString(movieId), ExtendedMovieInformation.class, listener);
    }
}
