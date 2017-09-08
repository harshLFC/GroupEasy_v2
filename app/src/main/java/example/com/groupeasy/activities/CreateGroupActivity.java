package example.com.groupeasy.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import example.com.groupeasy.R;
import example.com.groupeasy.editProfileActivity;
import example.com.groupeasy.pojo.chatPOJO;
import example.com.groupeasy.pojo.new_groups;

public class CreateGroupActivity extends AppCompatActivity {

    private Context context;
    private ImageView ivClose;
    private CircleImageView groupDP;
    private TextView next;
    private EditText input;
    String tempChar = random();

    //initilize elements
    //database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_create_group);
        context = CreateGroupActivity.this;
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);



        Context mContext;

        initElementsWithIds();
        initElementsWithListeners();

//        Picasso.with(getApplicationContext())
//                .load(R.drawable.ic_default_groups)
//                .resize(100,100)
//                .centerCrop()
//                .into(groupDP);
    }

    private void initElementsWithListeners() {

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        groupDP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(context, "Will open gallery",Toast.LENGTH_LONG).show();

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(CreateGroupActivity.this);
            }
        });

        final DatabaseReference groupRef = myRef.child("groups").child("");

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String groupName = input.getText().toString();
                FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = current_user.getUid();
                String Groupuid = groupRef.getKey();

                String members = "";
                final String icon = "";
                final String admin = uid;
                final String last_msg = "";


                if(groupName.isEmpty() || groupName == "" || !groupName.matches(".*\\w.*")){

                    Toast.makeText(context, "Please enter a Name for the Group",Toast.LENGTH_LONG).show();
                }
// else the entered string will be pushed to the firebase database reference

                else if(groupDP == null){
                    Toast.makeText(CreateGroupActivity.this, "group is null put code here",Toast.LENGTH_LONG).show();
                }

                else {

                    mStorageRef = FirebaseStorage.getInstance().getReference();

                    //This code is not pushing image ??

                    final StorageReference filePath = mStorageRef.child("group_image").child(tempChar+".jpg");

//                    if(groupDP.equals(R.drawable.ic_default_groups))

                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {


//                            String download_url = taskSnapshot.getDownloadUrl().toString();

                            new_groups newGroups = new new_groups(admin,uri.toString(),last_msg,groupName);

                            groupRef.push().setValue(newGroups);

                            Intent intent = new Intent(context,DashboardActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    });

                    filePath.getDownloadUrl().addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            String defaultImage = getString(R.string.default_firebase_groups);

                            new_groups newGroups = new new_groups(admin,defaultImage,last_msg,groupName);

                            groupRef.push().setValue(newGroups);

                            Intent intent = new Intent(context,DashboardActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }
        });
    }

    private void initElementsWithIds() {
        ivClose = (ImageView) findViewById(R.id.iv_close);
        groupDP = (CircleImageView) findViewById(R.id.GroupDPImageView);
        next = (TextView) findViewById(R.id.next);
        input = (EditText) findViewById(R.id.input);


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(context,DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                Uri resultUri = result.getUri();
                String image_uri = resultUri.toString();
                Toast.makeText(this, image_uri,Toast.LENGTH_LONG).show();

                final DatabaseReference groupRef = myRef.child("groups").child("");
                String Groupuid = groupRef.getKey();
                String groupName = input.getText().toString();

                Picasso.with(CreateGroupActivity.this)
                        .load(image_uri)
                        .resize(100,100)
                        .into(groupDP);

                Uri file = Uri.fromFile(new File(image_uri));
                mStorageRef = FirebaseStorage.getInstance().getReference();

                //this code is pushing image
                StorageReference filePath = mStorageRef.child("group_image").child(tempChar+".jpg");

                filePath.putFile(resultUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content

                                @SuppressWarnings("VisibleForTests") String download_url = taskSnapshot.getDownloadUrl().toString();
                                Toast.makeText(CreateGroupActivity.this, "Success",Toast.LENGTH_LONG).show();

                                final DatabaseReference groupRef = myRef.child("groups").child("");
//
//                                groupRef.child("image").setValue(download_url).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if(task.isSuccessful()){
//                                            Toast.makeText(CreateGroupActivity.this, "Success Uploading image in databse",Toast.LENGTH_LONG).show();
//                                        }
//                                        else{
//                                            Toast.makeText(CreateGroupActivity.this, "There was some error",Toast.LENGTH_LONG).show();
//                                        }
//                                    }
//                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                // ...
                                Toast.makeText(CreateGroupActivity.this, "Error",Toast.LENGTH_LONG).show();
                            }
                        });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }
    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(10);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
}
