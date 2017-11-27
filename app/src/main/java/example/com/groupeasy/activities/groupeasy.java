package example.com.groupeasy.activities;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Code for implementing Firebase Offline capabilities
 */

public class groupeasy extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
