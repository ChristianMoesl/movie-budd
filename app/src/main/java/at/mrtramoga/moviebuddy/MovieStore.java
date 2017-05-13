package at.mrtramoga.moviebuddy;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import at.mrtramoga.moviebuddy.themoviedb3.BasicMovieInformation;
import at.mrtramoga.moviebuddy.themoviedb3.Configuration;
import at.mrtramoga.moviebuddy.themoviedb3.ConfigurationRequest;
import at.mrtramoga.moviebuddy.themoviedb3.DiscoverMovieAnswer;
import at.mrtramoga.moviebuddy.themoviedb3.DiscoverMovieRequest;
import at.mrtramoga.moviebuddy.themoviedb3.Genre;
import at.mrtramoga.moviebuddy.themoviedb3.GenreList;
import at.mrtramoga.moviebuddy.themoviedb3.GenreListRequest;

public class MovieStore {
    private static volatile MovieStore instance;

    private NetworkUtils network;
    private List<Movie> movies;
    private List<Genre> genres;
    private Genre genre;
    private Configuration configuration;
    private String key;

    public interface Listener {
        void onMoviesLoaded(boolean newDataLoaded);
    }

    private MovieStore(NetworkUtils network) {
        this.network = network;
    }

    public static void initialize(String movieDbKey, NetworkUtils network, @Nullable final Listener listener) {
        if (instance == null) {
            synchronized (MovieStore.class) {
                if (instance == null) {
                    instance = new MovieStore(network);
                }
            }
        }

        instance.initialize(movieDbKey, listener);
    }

    public static MovieStore getInstance() { return instance; }

    private void initialize(String movieDbKey, @Nullable final Listener listener) {
        final MovieStore self = this;
        key = movieDbKey;

        if (movies == null) {
            network.addToRequestQueue(new ConfigurationRequest(movieDbKey, new Response.Listener<Configuration>() {
                @Override
                public void onResponse(Configuration response) {
                    configuration = response;
                }
            }).build());

            network.addToRequestQueue(new GenreListRequest(movieDbKey, new Response.Listener<GenreList>() {
                @Override
                public void onResponse(GenreList response) {
                    genres = response.getList();
                }
            }).build());

            network.addToRequestQueue(new DiscoverMovieRequest(movieDbKey, new Response.Listener<DiscoverMovieAnswer>() {
                @Override
                public void onResponse(DiscoverMovieAnswer response) {
                    convertMovieInformation(response.getMovieDescriptions());

                    if (listener != null)
                        listener.onMoviesLoaded(true);
                }
            }).build());

        } else if (listener != null) {
            listener.onMoviesLoaded(false);
        }
    }

    private void convertMovieInformation(List<BasicMovieInformation> infos) {
        movies = new ArrayList<>(infos.size());

        for (BasicMovieInformation movie : infos) {
            final String posterUrl = configuration.getBaseUrl() + "w500" + movie.getPosterPath();
            movies.add(new Movie(movie.getTitle(), movie.getOverview(), posterUrl));
        }
    }

    public void loadMovies(Genre genre, @Nullable final Listener listener) {
        if (this.genre == genre) {
            if (listener != null)
                listener.onMoviesLoaded(false);
            return;
        }

        this.genre = genre;

        network.addToRequestQueue(new DiscoverMovieRequest(key, new Response.Listener<DiscoverMovieAnswer>() {
            @Override
            public void onResponse(DiscoverMovieAnswer response) {
                convertMovieInformation(response.getMovieDescriptions());

                if (listener != null)
                    listener.onMoviesLoaded(true);
            }
        }).setGenre(genre).build());
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public Genre getCurrentGenre() {
        return genre;
    }

    public List<Genre> getGenres() {
        return genres;
    }

}
