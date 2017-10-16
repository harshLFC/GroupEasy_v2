package example.com.groupeasy.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import example.com.groupeasy.R;

/**
 *  Entry point of the app .
 *  Login using gmail or sign up for free
 * */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private Context context;
    private Button btnLogin,btnLoginEmail, register;
    private TextView googleSignIn;
    private TextInputLayout userEmail, userPassword;

    private ProgressDialog mRegProgress;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.context = LoginActivity.this; // initialize the context
        initElementsWithIds();
        initElementsWithListeners();

        mRegProgress = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

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

        btnLoginEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, createAccount.class);
                startActivity(intent);
            }
        });

        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,LoginGoogle.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = userEmail.getEditText().getText().toString();
                String password = userPassword.getEditText().getText().toString();

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

                    mRegProgress.setTitle("Checking your credentials");
                    mRegProgress.setCanceledOnTouchOutside(false);
                    mRegProgress.setMessage("Please wait");
                    mRegProgress.show();

                    loginUser(email,password);
                }
                else{
                    Toast.makeText(LoginActivity.this, "Please enter email and password",Toast.LENGTH_LONG).show();
                    //code for if empty
                }
            }
        });
    }

    // code for checking if device is connected to internet??

//    public static boolean hasActiveInternetConnection(Context context) {
//        if (isNetwokAvailable(context)) {
//            try {
//                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
//                urlc.setRequestProperty("User-Agent", "Test");
//                urlc.setRequestProperty("Connection", "close");
//                urlc.setConnectTimeout(1500);
//                urlc.connect();
//                return (urlc.getResponseCode() == 200);
//            } catch (IOException e) {
//                Log.e(LOG_TAG, "Error checking internet connection", e);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//            Log.d(LOG_TAG, "No network available!");
//        }
//        return false;
//    }
//
//    private static boolean isNetwokAvailable(Context context) {
//
//     return false;
//
//    }

    private void initElementsWithIds()
    {
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLoginEmail = (Button) findViewById(R.id.btn_login_email);
        googleSignIn = (TextView) findViewById(R.id.sign_in_with_google);
        register = (Button) findViewById(R.id.Register);

        userEmail = (TextInputLayout) findViewById(R.id.user_email);
        userPassword = (TextInputLayout) findViewById(R.id.user_password);
    }

    private void loginUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                     mRegProgress.dismiss();

                    Intent intent = new Intent(context,DashboardActivity.class);
                    startActivity(intent);
                    finish();
                }

                else{
                    mRegProgress.hide();

                    String error;

                    try {
                        throw task.getException();
                    }
                    catch (FirebaseAuthWeakPasswordException e){
                        error = "Password is weak";
                    } catch (FirebaseAuthInvalidUserException e) {
                        error = "Invalid Email!";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        error = "Please check if you do not have an account, or the entered details are right";
                    } catch (Exception e) {
                        error = "Unknown error!";
                        e.printStackTrace();
                    }

                   Toast.makeText(LoginActivity.this, R.string.check_details_or_resigistered, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
