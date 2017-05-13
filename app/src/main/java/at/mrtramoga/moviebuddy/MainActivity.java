package at.mrtramoga.moviebuddy;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import at.mrtramoga.moviebuddy.themoviedb3.Genre;

public class MainActivity extends AppCompatActivity implements MovieStore.Listener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int SPEECH_REQUEST_CODE = 0;

    private Set<String> mSeenMovies = new HashSet<>();;
    private String mCurrentMovie;
    private List<Movie> mMovies;
    private ImageView mPoster;
    private TextView mTitle;
    private TextView mDescription;
    private FloatingActionButton mFab;
    private NestedScrollView mOuterView;
    private TextToSpeech mTextToSpeech;
    private ToastManager mToastManager;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private GestureDetectorCompat mDetector;
    private boolean mIsLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        NetworkUtils.initialize(this);
        MovieStore.initialize(getString(R.string.the_movie_db_api_key), NetworkUtils.getInstance(), this);

        // Grep all views
        mPoster = (ImageView) findViewById(R.id.poster);
        mTitle = (TextView) findViewById(R.id.title);
        mDescription = (TextView) findViewById(R.id.description);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mOuterView = (NestedScrollView) findViewById(R.id.outer_view);

        mToastManager = new ToastManager(this);
        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mDetector = new GestureDetectorCompat(this, new MyGestureListener());
        mOuterView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mDetector.onTouchEvent(motionEvent);
                return false;
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("mCurrentMovie", mCurrentMovie);
        outState.putStringArray("mSeenMovies", mSeenMovies.toArray(new String[mSeenMovies.size()]));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mCurrentMovie = savedInstanceState.getString("mCurrentMovie");

        final String[] seenMovies = savedInstanceState.getStringArray("mSeenMovies");
        if (seenMovies != null)
            mSeenMovies = new HashSet<>(Arrays.asList(seenMovies));

        loadCurrentMove();
    }

    private void load(Movie movie) {
        mTitle.setText(movie.title());
        mDescription.setText(movie.description());
        Picasso.with(this).load(movie.posterUrl()).into(mPoster, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                mTitle.animate()
                        .alpha(1f)
                        .setDuration(200)
                        .setListener(null);

                mDescription.animate()
                        .alpha(1f)
                        .setDuration(200)
                        .setListener(null);

                mPoster.animate()
                        .alpha(1f)
                        .setDuration(200)
                        .setListener(null);
            }

            @Override
            public void onError() {
                mToastManager.show(R.string.poster_load_error);
            }
        });

        mIsLoading = false;
    }

    private void loadCurrentMove() {
        Movie movie = null;

        for (Movie m : mMovies)
            if (m.title().equals(mCurrentMovie))
                movie = m;

        if (movie != null)
            load(movie);
    }

    private void fadeNewMovieIn() {
        ArrayList<Integer> indices = new ArrayList<>(mMovies.size());
        for (int i = 0; i < mMovies.size(); i++)
            indices.add(i);

        Movie movie;
        do {
            if (indices.isEmpty()) {
                mToastManager.show(R.string.no_more_movies, true);
                return;
            }

            int index = (int) (Math.random() * indices.size());
            int id = indices.get(index);
            indices.remove(index);

            movie = mMovies.get(id);
        } while (mSeenMovies.contains(movie.title()));

        mCurrentMovie = movie.title();
        mSeenMovies.add(mCurrentMovie);

        load(movie);
    }

    private void loadNewMovie() {
        if (mMovies == null || mIsLoading)
            return;

        mIsLoading = true;

        mTitle.animate()
                .alpha(0f)
                .setDuration(200)
                .setListener(null);

        mDescription.animate()
                .alpha(0f)
                .setDuration(200)
                .setListener(null);

        mPoster.animate()
                .alpha(0f)
                .setDuration(200)
                .setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                fadeNewMovieIn();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake(int count) {
                loadNewMovie();
            }
        });

        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displaySpeechRecognizer();
            }
        });
    }

    @Override
    public void onPause() {
        mSensorManager.unregisterListener(mShakeDetector);
        mShakeDetector.setOnShakeListener(null);
        mFab.setOnClickListener(null);
        if (mTextToSpeech != null) {
            mTextToSpeech.stop();
            mTextToSpeech.shutdown();
        }
        super.onPause();
    }

    @Override
    public void onMoviesLoaded(boolean newDataLoaded) {
        mMovies = MovieStore.getInstance().getMovies();

        if (newDataLoaded)
            loadNewMovie();
        else if (mMovies != null && mCurrentMovie != null)
            loadCurrentMove();
    }

    // Create an intent that can start the Speech Recognizer activity
    public void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    // This callback is invoked when the Speech Recognizer returns.
    // This is where you process the intent and extract the speech text from the intent.
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            processCommand(results);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void talk(String text) {
        final String t = text;
        if (mTextToSpeech != null) {
            mToastManager.show(text);
        } else {
            mTextToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int i) {
                    if (i == TextToSpeech.SUCCESS) {
                        mTextToSpeech.setLanguage(Locale.ENGLISH);
                        mTextToSpeech.speak(t, TextToSpeech.QUEUE_FLUSH, null);
                        mTextToSpeech = null;
                    }
                }
            });
        }
    }

    private void processCommand(List<String> commands) {
        boolean gotResult = false;

        for (String command : commands) {
            List<String> words = Arrays.asList(command.toLowerCase().split("\\s+"));

            Iterator<String> it = words.iterator();

            while (it.hasNext() && !gotResult) {
                if ("next".equals(it.next())) {
                    if (it.hasNext()) {
                        String second = it.next();
                        if (second.equals("movie")) {
                            fadeNewMovieIn();
                            gotResult = true;
                        } else {
                            List<Genre> genres = MovieStore.getInstance().getGenres();

                            String last = null;
                            if (it.hasNext())
                                last = it.next();

                            for (Genre genre : genres) {
                                if (second.equals(genre.getName().toLowerCase()) && "movie".equals(last)) {
                                    MovieStore.getInstance().loadMovies(genre, this);
                                    gotResult = true;
                                }
                            }
                        }
                    } else {
                        fadeNewMovieIn();
                        gotResult = true;
                    }
                }
            }
         }

        if (!gotResult)
            talk("Sorry i didn't get that");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        mDetector.onTouchEvent(event);
        // Be sure to call the superclass implementation
        return super.onTouchEvent(event);
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            loadNewMovie();
            return false;
        }
    }
}
