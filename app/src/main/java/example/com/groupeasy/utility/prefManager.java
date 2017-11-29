package example.com.groupeasy.utility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Harsh on 12-11-2017.
 */

/**
 * This util class was created as an attempt to make login page appear only once,
 * after user logs in he will alwasy be redirected to the dashboard activity,
 * This class is still buggy and under construction
 **/

public class prefManager {
    // Shared preferences file name
    private static final String PREF_NAME = "welcome";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    // shared pref mode
    int PRIVATE_MODE = 0;

    public prefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }
}
