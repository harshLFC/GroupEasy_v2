package example.com.groupeasy.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import example.com.groupeasy.R;
import id.zelory.compressor.Compressor;

public class editGroupActivity extends AppCompatActivity {

    Toolbar mToolBar;
    TextInputLayout userName;
    CircleImageView editImage,profileImage;
    ProgressDialog mDialog;
    Button saveChanges;

    private DatabaseReference mUserDatabase,mThumbRef;
    private FirebaseUser mCurrentUser;
    private StorageReference mStorageRef;
    private Bitmap thumb_bitmp = null;

    private ProgressDialog mProgressD;

    private static final int GALLERY_PICK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);

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

        initElementsWithIds();
        initElementsWithListeners();

        userName.getEditText().setText(groupName);
//        userStatus.getEditText().setText(user_value_status);


        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("groups").child(groupKey);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //code for displaying image
                    String image = dataSnapshot.child("image").getValue().toString();
                    String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();

                Picasso.with(editGroupActivity.this)
                        .load(image)
//                        .resize(100,100)
                        .placeholder(R.drawable.multi_user_alternate_2)
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
                    mDialog = new ProgressDialog(editGroupActivity.this);
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
                }
            }
        });


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

    private void initElementsWithIds() {
            userName = (TextInputLayout) findViewById(R.id.user_name);
//            userStatus = (TextInputLayout) findViewById(R.id.user_status);
            saveChanges = (Button) findViewById(R.id.save_changes);
            editImage = (CircleImageView) findViewById(R.id.edit_dp);
            profileImage = (CircleImageView) findViewById(R.id.circleImageView);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                Uri resultUri = result.getUri();
//                Toast.makeText(this, image_uri,Toast.LENGTH_LONG).show();


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


                filePath.putFile(resultUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content

                                @SuppressWarnings("VisibleForTests") String download_url = taskSnapshot.getDownloadUrl().toString();
                                Toast.makeText(editGroupActivity.this, "Success",Toast.LENGTH_LONG).show();

                                mUserDatabase.child("image").setValue(download_url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(editGroupActivity.this, "Success Uploading image in databse",Toast.LENGTH_LONG).show();
                                        }
                                        else{
                                            Toast.makeText(editGroupActivity.this, "There was some error",Toast.LENGTH_LONG).show();
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
                                Toast.makeText(editGroupActivity.this, "Error",Toast.LENGTH_LONG).show();
                            }
                        });
                try{
                    mThumbRef.putBytes(thumb_byte).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot task) {
                            String download_url = task.getDownloadUrl().toString();
                            Toast.makeText(editGroupActivity.this, "Thumb Successfully uploaded to Database",Toast.LENGTH_SHORT).show();

                            mUserDatabase.child("thumb_image").setValue(download_url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(editGroupActivity.this, "Success Uploading Thumbimage in databse",Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        Toast.makeText(editGroupActivity.this, "There was some error",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        }
                    });}
                catch (Exception e){
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }
    }
}
