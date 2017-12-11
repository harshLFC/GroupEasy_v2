package example.com.groupeasy.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import example.com.groupeasy.R;

/**
 * Created by Harsh on 31-08-2017.
 */

/**
 * This Class asks for user details, sends them to firebase auth server,
 * if approved, data is pushed to database and user is navigated to welcome page of app
 * This page when the usr presses on new user signup from the login page of the app
 * **/

public class createAccount extends AppCompatActivity {

    final DatabaseReference mDatabase1 = FirebaseDatabase.getInstance().getReference();
    final DatabaseReference mUserBD = mDatabase1.child("members");
    final DatabaseReference userRef = mDatabase1.child("members").child("");
    final String user_push_id = userRef.push().getKey();
    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
    StorageReference mStorageRef;
    //initilize varaibles
    private Toolbar mToolBar;
    private TextInputLayout userName,password,email;
    private TextInputEditText editText, Username,Password;
    private Button mCreateBtn;
    private CircleImageView userDp;
    private ProgressDialog mRegProcess;
    /** Firebase db init*/
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Link to XML layout*/
        setContentView(R.layout.activity_create_account);

        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        //Toolbar intialization
        mToolBar = (Toolbar) findViewById(R.id.create_acc_tool);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Create An Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /* Calling methods for
        1. Initialising elements by IDs
        2. Initialising elements with Listeners*/
        initElementsWithIds();
        initElementsWithListeners();
    }

    private void initElementsWithListeners() {

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        Password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        Username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                  }
                }
                });

        //this method validates user input, if all is valid, sends it to register_user method
        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mUserName = userName.getEditText().getText().toString();
                String mUserEmail = email.getEditText().getText().toString();
                String mUserPass = password.getEditText().getText().toString();

                if( !TextUtils.isEmpty(mUserEmail) && !TextUtils.isEmpty(mUserName) && !TextUtils.isEmpty(mUserPass))
                {
                    mRegProcess = new ProgressDialog(v.getContext());
                    mRegProcess.setTitle("Registering User");
                    mRegProcess.setMessage("Please Wait");
                    mRegProcess.setCanceledOnTouchOutside(false);
                    mRegProcess.show();

                    register_user(mUserEmail,mUserName,mUserPass);
                }
                else {
                    Toast.makeText(createAccount.this,
                            "All inputs are required, please check", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        //user can upload an image as an avatar
        userDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(createAccount.this);
            }
        });
    }

    private void hideKeyboard(View v) {

        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);

    }

    //after validation user is registered with GroupEasy
    private void register_user(final String mUserEmail, final String mUserName, String mUserPass) {

        mAuth.createUserWithEmailAndPassword(mUserEmail,mUserPass)

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("userIssue","Some NEW ERROR"+e.getMessage());
                    }
                })

                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    if(current_user != null)

                    /**Two codes for registering and sending user data 1. if image is uploaded, 2.if no image is uploaded***/
                    {
                        //we need a device token for notification service
                        final String device_token = FirebaseInstanceId.getInstance().getToken();

                        final String uid = current_user.getUid();

                        mDatabase = FirebaseDatabase.getInstance().getReference().child("members").child(uid);

                        StorageReference filePath = mStorageRef.child("profile_images").child(user_push_id+".jpg");

                        /**If image is uploaded by user**/
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                final HashMap<String,String> userMap = new HashMap<>();
                                userMap.put("name",mUserName);
                                userMap.put("status","Hi! Im on GroupEasy");
                                userMap.put("image",uri.toString());
                                userMap.put("thumb_image","Default");
                                userMap.put("last_seen","Default");
                                userMap.put("id",uid);

                                        mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if(task.isSuccessful()){

                                                    mUserBD.child(uid).child("device_token").setValue(device_token).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                            mRegProcess.dismiss();
                                                            Intent intent = new Intent(createAccount.this,WelcomeActivity.class);
                                                            startActivity(intent);
                                                            finish();

                                                        }
                                                    });

                                                }
                                                else{
                                                    mRegProcess.hide();
                                                    Toast.makeText(createAccount.this,
                                                            "Please check if you already have an account, or the entered details are right", Toast.LENGTH_LONG)
                                                            .show();
                                                }
                                            }
                                        });
                            }
                        });

                        /**If NO image is uploaded by user**/

                        filePath.getDownloadUrl().addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                HashMap<String,String> userMap = new HashMap<>();
                                userMap.put("name",mUserName);
                                userMap.put("status","Hi! Im on GroupEasy");
                                userMap.put("image","Default");
                                userMap.put("favs","0");
                                userMap.put("polls","0");
                                userMap.put("rosters","0");
                                userMap.put("lists","0");
                                userMap.put("thumb_image","Default");
                                userMap.put("last_seen","Default");
                                userMap.put("id",uid);

                                mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if(task.isSuccessful()){

                                            mUserBD.child(uid).child("device_token").setValue(device_token).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    mRegProcess.dismiss();
                                                    Intent i = new Intent(createAccount.this,WelcomeActivity.class);
                                                    startActivity(i);
                                                    finish();

                                                }
                                            });
                                        }
                                        else{
                                            mRegProcess.hide();
                                            Toast.makeText(createAccount.this,
                                                    "Please check if you already have an account, or the entered details are right", Toast.LENGTH_LONG)
                                                    .show();
                                        }
                                    }
                                });
                            }
                        });
                    }

                }
                else if (!task.isSuccessful()){
                    FirebaseAuthException e = (FirebaseAuthException )task.getException();
                    Toast.makeText(createAccount.this, "Failed Registration: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    mRegProcess.hide();
                }

                //the below code is not required at the moment as the above code handles all errors
            /*    else
                {
                    mRegProcess.hide();
                    String error;
                    try {
                        throw task.getException();
                    }
                    catch (FirebaseAuthWeakPasswordException e){
                        error = "Password is weak";
                    } catch (FirebaseAuthInvalidUserException e) {
                        error = "Invalid Email!";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        error = "Please check if you already have an account, or the entered details are right";
                    } catch (Exception e) {
                        error = "Unknown error!";
                        e.printStackTrace();
                    }

                    Toast.makeText(createAccount.this, error, Toast.LENGTH_LONG).show();

                    Toast.makeText(createAccount.this,
                            "Please check if you already have an account, or the entered details are right", Toast.LENGTH_LONG)
                            .show();
                }*/
            }
        });
    }


    //handles image uploads
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                String image_uri = resultUri.toString();

                //Loads image onto the UI
                Picasso.with(createAccount.this)
                        .load(image_uri)
                        .resize(300, 300)
                        .into(userDp);

                StorageReference filePath = mStorageRef.child("profile_images").child(user_push_id+".jpg");

                filePath.putFile(resultUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content

                                @SuppressWarnings("VisibleForTests") String download_url = taskSnapshot.getDownloadUrl().toString();
                                Toast.makeText(createAccount.this, "Image uploaded !",Toast.LENGTH_LONG).show();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                Toast.makeText(createAccount.this, "Error",Toast.LENGTH_LONG).show();
                            }
                        });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void initElementsWithIds() {
        userName = (TextInputLayout) findViewById(R.id.text_input_user_name);
        email = (TextInputLayout) findViewById(R.id.text_input_email);
        password = (TextInputLayout) findViewById(R.id.text_input_password);
        userDp = (CircleImageView) findViewById(R.id.userDP);
        mCreateBtn = (Button) findViewById(R.id.btn_create_acc);

        editText = (TextInputEditText) findViewById(R.id.editText);
        Password = (TextInputEditText) findViewById(R.id.email);
        Username = (TextInputEditText) findViewById(R.id.user_name);
    }
}