package example.com.groupeasy.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import example.com.groupeasy.R;

/**
 *  Entry point of the app .
 *  Login using gmail or sign up for free
 * */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private Context context;
    private Button btnLogin,btnLoginGoogle,btnLoginEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.context = LoginActivity.this; // initialize the context
        initElementsWithIds();
        initElementsWithListeners();
    }
    //this snippet to disallow back key
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }


    private void initElementsWithListeners() {

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,LoginGoogle.class);
                startActivity(intent);
                finish();
            }
        });

        btnLoginEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LoginEmail.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void initElementsWithIds()
    {
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLoginGoogle = (Button) findViewById(R.id.btn_login_google);
        btnLoginEmail = (Button) findViewById(R.id.btn_login_email);
    }




}
