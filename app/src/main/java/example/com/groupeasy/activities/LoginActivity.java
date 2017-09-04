package example.com.groupeasy.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

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

                if(!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)){

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

                    Toast.makeText(LoginActivity.this, "You have some error please try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
