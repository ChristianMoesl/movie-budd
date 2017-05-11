package at.mrtramoga.moviebuddy;

import android.app.Service;
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
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements MovieStore.InitializedListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String MOVIE_ID_KEY = "MOVIE_ID_KEY";
    private static final String NUMBER_OF_SEEN_MOVIES_KEY = "NUMBER_OF_SEEN_MOVIES_KEY";
    private static final int SPEECH_REQUEST_CODE = 0;
    private static final String DEBUG_TAG = "Gestures";

    private int mMovieId;
    private int mNumberOfSeenMovies;
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

        outState.putInt(MOVIE_ID_KEY, mMovieId);
        outState.putInt(NUMBER_OF_SEEN_MOVIES_KEY, mNumberOfSeenMovies);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mMovieId = savedInstanceState.getInt(MOVIE_ID_KEY, 0);
        mNumberOfSeenMovies = savedInstanceState.getInt(NUMBER_OF_SEEN_MOVIES_KEY, 0);
    }

    private void loadCurrentMovie() {
        final Movie movie = mMovies.get(mMovieId);

        mTitle.setText(movie.title());
        mDescription.setText(movie.description());
        Picasso.with(this).load(movie.posterUrl()).into(mPoster);

        mIsLoading = false;
    }

    private void fadeNewMovieIn() {
        Movie movie;
        do {
            mMovieId = (int) (Math.random() * mMovies.size());

            movie = mMovies.get(mMovieId);
        } while (movie.isSeen());

        movie.markSeen();
        mNumberOfSeenMovies++;

        loadCurrentMovie();
    }

    private void loadNewMovie() {
        if (mMovies == null || mIsLoading)
            return;

        if (mNumberOfSeenMovies == mMovies.size()) {
            mToastManager.show(R.string.no_more_movies, true);
            return;
        }

        mIsLoading = true;

        TranslateAnimation animation = new TranslateAnimation(0, -1000, 0, -200);
        animation.setDuration(500);
        animation.setFillAfter(false);
        animation.setInterpolator(new AccelerateInterpolator((float)1.5));

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                fadeNewMovieIn();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mPoster.startAnimation(animation);
        mTitle.startAnimation(animation);
        mDescription.startAnimation(animation);
    }

    @Override
    public void onResume() {
        super.onResume();

        MovieStore.get().initialize(this, getString(R.string.the_movie_db_api_key), this);

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
    public void onInitialized(boolean newDataLoaded) {
        mMovies = MovieStore.get().getMovies();

        if (newDataLoaded) {
            loadNewMovie();
        } else if (mMovies != null) {
            loadCurrentMovie();
        }
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
        int i = 0;
        for (String command : commands) {
            switch (command.toLowerCase()) {
                case "next":
                case "next film":
                    fadeNewMovieIn();
                    break;
                default:
                    i++;
                    break;
            }
        }

        if (i == commands.size()) {
            talk("Sorry i didn't get that");
        }
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
