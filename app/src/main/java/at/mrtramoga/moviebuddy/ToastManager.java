package at.mrtramoga.moviebuddy;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

/**
 * Created by chris_000 on 11.05.2017.
 */
public class ToastManager {

    private Context mContext;
    private Toast mToast;

    public ToastManager(Context context) {
        mContext = context;
    }

    public void show(int stringId) {
        show(stringId, true);
    }

    public void show(int stringId, boolean override) {
        show(mContext.getString(stringId), override);
    }

    public void show(String text) {
        show(text, true);
    }

    public void show(String text, boolean override) {
        if (mToast != null) {
            View toastView = mToast.getView();
            if (toastView != null && toastView.isShown() && !override) {
                return;
            }
            mToast.cancel();
        }

        if (mContext != null) {
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
            mToast.show();
        }
    }
}
