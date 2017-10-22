package example.com.groupeasy.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import example.com.groupeasy.R;

public class editProfileActivity extends AppCompatActivity {

    Toolbar mToolBar;
    TextInputLayout userName, userStatus;
    CircleImageView editImage,profileImage;
    ProgressDialog mDialog;
    Button saveChanges;

    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;
    private StorageReference mStorageRef;

    private ProgressDialog mProgressD;


    private static final int GALLERY_PICK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mToolBar = (Toolbar) findViewById(R.id.myToolBar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Edit your Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get username and status from previous intent
        String user_value_name = getIntent().getStringExtra("user_value_name");
        String user_value_status = getIntent().getStringExtra("user_value_status");

        initElementsWithIds();
        initElementsWithListeners();

        userName.getEditText().setText(user_value_name);
        userStatus.getEditText().setText(user_value_status);

       //code for getting userID
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = mCurrentUser.getUid();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("members").child(uid);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //code for displaying image
                    String image = dataSnapshot.child("image").getValue().toString();
                    String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();

                Picasso.with(editProfileActivity.this)
                        .load(image)
//                        .resize(100,100)
                        .into(profileImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initElementsWithListeners() {

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Progress
                    mDialog = new ProgressDialog(editProfileActivity.this);
                    mDialog.setTitle("Saving Changes");
                    mDialog.setMessage("Please Wait");
                    mDialog.show();

                    String Status = userStatus.getEditText().getText().toString();
                    String Name = userName.getEditText().getText().toString();

                //code for updating username and status
                //// TODO: 04-09-2017 use different ? loop or switch statement


                if((TextUtils.isEmpty(Status.trim()) || (TextUtils.isEmpty(Name.trim()))) || !(Name.length()>1) || !(Status.length()>1)){
                    mDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Username or Status should not be empty !",Toast.LENGTH_SHORT).show();
                }


                else {
                    mUserDatabase.child("status").setValue(Status.trim());
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//
//                            if (task.isSuccessful()) {
//
//                                mDialog.dismiss();
//                                Toast.makeText(getApplicationContext(), "Status Updated",Toast.LENGTH_SHORT).show();
//
//                            }
//                            else{
//                                mDialog.hide();
//                                Toast.makeText(getApplicationContext(), "Some error",Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });

                        mUserDatabase.child("name").setValue(Name.trim());
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//
//                                if (task.isSuccessful()) {
//                                    mDialog.dismiss();
//                                    Toast.makeText(getApplicationContext(), "UserName Updated",Toast.LENGTH_SHORT).show();
//
//                                }
//                                else{
//                                    mDialog.hide();
//                                    Toast.makeText(getApplicationContext(), "Some error",Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });

                    mDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Updated !",Toast.LENGTH_SHORT).show();

                }



//                Intent intent = new Intent(getApplicationContext(),DashboardActivity.class);
//                startActivity(intent);
//                finish();
            }
        });

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(editProfileActivity.this);

                //trying for button effect
                //// TODO: 04-09-2017 delete if not required
                buttonEffect(editImage);

                /*
                Intent gallery_intent = new Intent();
                gallery_intent.setType("image/*");
                gallery_intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(gallery_intent, "Select Image") ,GALLERY_PICK);
                */
            }
        });
    }

    private void initElementsWithIds() {
            userName = (TextInputLayout) findViewById(R.id.user_name);
            userStatus = (TextInputLayout) findViewById(R.id.user_status);

            saveChanges = (Button) findViewById(R.id.save_changes);

            editImage = (CircleImageView) findViewById(R.id.edit_dp);
            profileImage = (CircleImageView) findViewById(R.id.circleImageView);
    }

    public static void buttonEffect(View button){
        button.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                Uri resultUri = result.getUri();
                String image_uri = resultUri.toString();
//                Toast.makeText(this, image_uri,Toast.LENGTH_LONG).show();

                String uid = mCurrentUser.getUid();
                Uri file = Uri.fromFile(new File(image_uri));
                StorageReference filePath = mStorageRef.child("profile_images").child(uid+".jpg");

                filePath.putFile(resultUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content

                                @SuppressWarnings("VisibleForTests") String download_url = taskSnapshot.getDownloadUrl().toString();
                                Toast.makeText(editProfileActivity.this, "Success",Toast.LENGTH_LONG).show();

                                mUserDatabase.child("image").setValue(download_url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(editProfileActivity.this, "Success Uploading image in databse",Toast.LENGTH_LONG).show();
                                        }
                                        else{
                                            Toast.makeText(editProfileActivity.this, "There was some error",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                // ...
                                Toast.makeText(editProfileActivity.this, "Error",Toast.LENGTH_LONG).show();
                            }
                        });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }
    }
}
