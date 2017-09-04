package example.com.groupeasy.activities;

               import android.app.AlertDialog;
               import android.app.ProgressDialog;
               import android.content.DialogInterface;
               import android.content.Intent;
               import android.net.Uri;
               import android.os.Bundle;
               import android.support.annotation.NonNull;
               import android.support.v7.app.ActionBar;
               import android.support.v7.app.AppCompatActivity;
               import android.support.v7.widget.Toolbar;
               import android.util.Log;
               import android.view.View;
               import android.view.Window;
               import android.view.WindowManager;
               import android.widget.EditText;
               import android.widget.ImageView;
               import android.widget.TextView;
               import android.widget.Toast;

               import com.google.android.gms.auth.api.Auth;
               import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
               import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
               import com.google.android.gms.auth.api.signin.GoogleSignInResult;
               import com.google.android.gms.common.ConnectionResult;
               import com.google.android.gms.common.api.GoogleApiClient;
               import com.google.android.gms.common.api.ResultCallback;
               import com.google.android.gms.common.api.Status;
               import com.google.android.gms.tasks.OnCompleteListener;
               import com.google.android.gms.tasks.Task;
               import com.google.firebase.auth.AuthCredential;
               import com.google.firebase.auth.AuthResult;
               import com.google.firebase.auth.FirebaseAuth;
               import com.google.firebase.auth.FirebaseUser;
               import com.google.firebase.auth.GoogleAuthProvider;
               import com.google.firebase.auth.UserInfo;

               import example.com.groupeasy.R;


/**
 * Demonstrate Firebase Authentication using a Google ID Token.
 */
public class LoginGoogle extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    //Creating sign in flow using firebase Open source Ui
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private TextView userName;
    private TextView eMail;
    private ImageView userPhoto;
    private String name;
    private Toolbar mToolBar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Views
        mStatusTextView = (TextView) findViewById(R.id.status);
        mDetailTextView = (TextView) findViewById(R.id.detail);
        userName = (TextView) findViewById(R.id.userName);
        eMail = (TextView) findViewById(R.id.eMail);
        userPhoto = (ImageView) findViewById(R.id.userPhoto);

        mToolBar = (Toolbar) findViewById(R.id.create_acc_tool);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Create An Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);
//        findViewById(R.id.go_to_chat).setOnClickListener(this);
//        findViewById(R.id.disconnect_button).setOnClickListener(this);

        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestId()
                .requestProfile()
                .build();
        // [END config_signin]

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

//        Trying to make page full screen
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    // [START on_start_check_user]
    @Override
    public void onStart()
    {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

// Get user Provider profile information
        if (currentUser != null) {
            for (UserInfo profile : currentUser.getProviderData()) {
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();

                // UID specific to the provider
                String uid = profile.getUid();

                // Name, email address, and profile photo Url
//                String name = profile.getDisplayName();
                String email = profile.getEmail();
                Uri photoUrl = profile.getPhotoUrl();

// attempt to send username data to fragment
//                Bundle bundle = new Bundle();
//                bundle.putString(uid,"From Activity");
//                Fragmentclass frag = new Fragmentclass;
//                frag.setArguments(bundle);

                Toast.makeText(this,"You are now logged in "+mAuth.getCurrentUser().getDisplayName(),Toast.LENGTH_SHORT).show();
                Intent i = new Intent(LoginGoogle.this, DashboardActivity.class);
                startActivity(i);
                finish();

            }
        }

        updateUI(currentUser);
    }
    // [END on_start_check_user]

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
//                Intent i = new Intent(login.this, MainActivity.class);
//                startActivity(i);
            }
            else
            {
                // Google Sign In failed, update UI appropriately
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct)
    {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        final ProgressDialog pDialog = ProgressDialog.show(LoginGoogle.this, "Fetching Information", "Please wait...");
//        showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                        } else
                        {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginGoogle.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        pDialog.hide();
//                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    // [END auth_with_google]

    // [START signin]
    private void signIn()
    {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
//        Intent i = new Intent(getApplicationContext(), MainActivity.class);
//
//        startActivity(i);
    }
    // [END signin]

    private void request_user_name() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter User Name");

        final EditText input_field = new EditText(this);

        builder.setView(input_field);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                name = input_field.getText().toString();

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void revokeAccess()
    {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback
                (
                        new ResultCallback<Status>()
                        {
                            @Override
                            public void onResult(@NonNull Status status)
                            {
                                updateUI(null);
                            }
                        });
    }

    private void updateUI(FirebaseUser user)
    {
//        pDialog.hide();
//        hideProgressDialog();
        if (user != null)
        {
//            mStatusTextView.setText(getString(R.string.google_status_fmt, user.getEmail()));
//            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()) );
//            userName.setText(getString(R.string.google_status_fmt, user.getDisplayName()));

            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
            findViewById(R.id.userName).setVisibility(View.VISIBLE);

            Intent i = new Intent(LoginGoogle.this, DashboardActivity.class);
            startActivity(i);

        }
        else
        {
//            mStatusTextView.setText(R.string.Not_logged_in);
            mDetailTextView.setText(null);

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
            userName.setVisibility(View.GONE);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.sign_in_button) {
            signIn();
//        }
//        else if (i == R.id.go_to_chat)
//        {
//        }
//        else if (i == R.id.disconnect_button)
//        {
//            revokeAccess();
//        }
        }
    }

    public void GoToApp(View view)
    {
        Intent i = new Intent(LoginGoogle.this, DashboardActivity.class);
        startActivity(i);
    }
}
