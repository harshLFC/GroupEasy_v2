package example.com.groupeasy.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import example.com.groupeasy.R;
import id.zelory.compressor.Compressor;

/**
 * This Class edits changes for
 * 1. room name
 * 2. room image
 * 3. NEW: compresses as thumb image and sends to server
 * **/

public class editGroupActivity extends AppCompatActivity {

    Toolbar mToolBar;
    TextInputLayout userName;
    CircleImageView editImage,profileImage;
    ProgressDialog mDialog;
    Button saveChanges;
    TextInputEditText groupName;

    private DatabaseReference mUserDatabase;
    private StorageReference mStorageRef;
    private Bitmap thumb_bitmp = null;

    private Context context = editGroupActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);

//      toolbar init
        mToolBar = (Toolbar) findViewById(R.id.edit_group_tool);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Edit Group Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //get username and status from previous intent
        String groupName = getIntent().getStringExtra("room_name");
        String groupKey = getIntent().getStringExtra("room_key");

        /* Calling methods for
        1. Initialising elements by IDs
        2. Initialising elements with Listeners*/
        initElementsWithIds();
        initElementsWithListeners();

        userName.getEditText().setText(groupName);

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("groups").child(groupKey);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        //code for displaying image
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String image = dataSnapshot.child("image").getValue().toString();
                Picasso.with(context)
                        .load(image)
                        .placeholder(R.drawable.multi_user_alternate_2)
                        .into(profileImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /*Handling all onclick events(listeners)*/
    private void initElementsWithListeners() {

        //Hide keyboard onTouchedOutside
        groupName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Progress D.
                    mDialog = new ProgressDialog(context);
                    mDialog.setTitle("Saving Changes");
                    mDialog.setMessage("Please Wait");
                    mDialog.show();

                String Name = userName.getEditText().getText().toString();

                //code for updating groupName
                if( (TextUtils.isEmpty(Name.trim())) || !(Name.length()>1) ){
                    mDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Group name should not be empty !",Toast.LENGTH_SHORT).show();
                }

                else {
                        mUserDatabase.child("name").setValue(Name.trim());
                        mDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Updated !",Toast.LENGTH_SHORT).show();

                        //bug in app, the name updates only when redirected to main screen
                        Intent intent = new Intent(getApplicationContext(),DashboardActivity.class);
                        startActivity(intent);
                        finish();
                }
                finish();
            }

        });

        //opens up the crop image library
        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(editGroupActivity.this);
            }
        });
    }

    private void hideKeyboard(View v) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /*Initialize all ui elements by linking to xml ids*/
    private void initElementsWithIds() {
            userName = (TextInputLayout) findViewById(R.id.user_name);
            saveChanges = (Button) findViewById(R.id.save_changes);
            editImage = (CircleImageView) findViewById(R.id.edit_dp);
            profileImage = (CircleImageView) findViewById(R.id.circleImageView);
            groupName = (TextInputEditText) findViewById(R.id.group_name);
    }

    //Handling ImageCropper library callback
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                //code to compress image
                final File thumb_file = new File(resultUri.getPath());
                try{
                    thumb_bitmp = new Compressor(this)
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .setQuality(75)
                            .compressToBitmap(thumb_file);
                }
                catch (IOException e)
                {e.printStackTrace();}

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                thumb_bitmp.compress(Bitmap.CompressFormat.JPEG,75,byteArrayOutputStream);

                final byte[] thumb_byte = byteArrayOutputStream.toByteArray();

                String image_uri = resultUri.toString();
                String groupKey = getIntent().getStringExtra("room_key");

                Uri file = Uri.fromFile(new File(image_uri));
                StorageReference filePath = mStorageRef.child("group_image").child(groupKey+".jpg");
                StorageReference mThumbRef = mStorageRef.child("group_thumb").child(groupKey+".jpg");

                //puts the image in storage
                filePath.putFile(resultUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content

                                @SuppressWarnings("VisibleForTests") String download_url = taskSnapshot.getDownloadUrl().toString();

                                mUserDatabase.child("image").setValue(download_url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(context, "Group Image updated",Toast.LENGTH_LONG).show();

                                        }
                                        else{
                                            Toast.makeText(context, getString(R.string.error),Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                Toast.makeText(context, getString(R.string.error),Toast.LENGTH_LONG).show();
                            }
                        });
                try{
                    mThumbRef.putBytes(thumb_byte).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot task) {
                            String download_url = task.getDownloadUrl().toString();

                            mUserDatabase.child("thumb_image").setValue(download_url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
//                                        Toast.makeText(editGroupActivity.this, "Success Uploading Thumbimage in databse",Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        Toast.makeText(context, getString(R.string.error),Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        }
                    });
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                finish();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }
    }
    //End cropLibrary callback
}
