package at.mrtramoga.moviebuddy;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Response;

import java.util.ArrayList;
import java.util.List;

import at.mrtramoga.moviebuddy.themoviedb3.BasicMovieInformation;
import at.mrtramoga.moviebuddy.themoviedb3.Configuration;
import at.mrtramoga.moviebuddy.themoviedb3.ConfigurationRequest;
import at.mrtramoga.moviebuddy.themoviedb3.DiscoverMovieAnswer;
import at.mrtramoga.moviebuddy.themoviedb3.DiscoverMovieRequest;

public class MovieStore {
    private static volatile MovieStore instance;
    private List<Movie> movies;
    private Configuration configuration;
    private String key;

    public interface InitializedListener {
        void onInitialized(boolean newDataLoaded);
    }

    private MovieStore() { }

    public static MovieStore get() {
        if (instance == null) {
            synchronized (MovieStore.class) {
                if (instance == null) {
                    instance = new MovieStore();
                }
            }
        }

        return instance;
    }

    public void initialize(final Context context, String movieDbKey, @Nullable final InitializedListener listener) {
        final MovieStore self = this;

        if (movies == null) {
            NetworkUtils network = NetworkUtils.getInstance(context);

            network.addToRequestQueue(new ConfigurationRequest(movieDbKey, new Response.Listener<Configuration>() {
                @Override
                public void onResponse(Configuration response) {
                    configuration = response;
                }
            }).build());

            network.addToRequestQueue(new DiscoverMovieRequest(movieDbKey, new Response.Listener<DiscoverMovieAnswer>() {
                @Override
                public void onResponse(DiscoverMovieAnswer response) {
                    movies = new ArrayList<>(response.getMovieDescriptions().size());
                    for (BasicMovieInformation movie : response.getMovieDescriptions()) {
                        final String posterUrl = configuration.getBaseUrl() + "w500" + movie.getPosterPath();
                        movies.add(new Movie(movie.getTitle(), movie.getOverview(), posterUrl));
                    }

                    if (listener != null) {
                        listener.onInitialized(true);
                    }
                }
            }).build());

        } else if (listener != null) {
            listener.onInitialized(false);
        }
    }

    public List<Movie> getMovies() {
        return movies;
    }

}
