package at.mrtramoga.moviebuddy;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import at.mrtramoga.moviebuddy.themoviedb3.DiscoverMovieRequest;

/**
 * Contains all utils for basic network access with the volley framework.
 */
public class NetworkUtils {

    private static volatile NetworkUtils instance;

    private final RequestQueue requestQueue;

    private NetworkUtils(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static void initialize(Context context) {
        if (instance == null) {
            synchronized (NetworkUtils.class) {
                if (instance == null) {
                    instance = new NetworkUtils(context);
                }
            }
        }
    }

    public static NetworkUtils getInstance() {
        return instance;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request req) {
         getRequestQueue().add(req);
    }
}
