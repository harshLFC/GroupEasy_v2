package example.com.groupeasy.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import example.com.groupeasy.R;
import id.zelory.compressor.Compressor;

public class CreateGroupActivity extends AppCompatActivity {

    //initilize elements
    private Context context;
    private ImageView ivClose;
    private CircleImageView groupDP;
    private TextView next;
    private EditText input;
    private Bitmap thumb_bitmp = null;

    //database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();
    private StorageReference mStorageRef, mThumbRef;

    final DatabaseReference groupRef = myRef.child("groups").child("");

    final String group_id = groupRef.push().getKey();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_create_group);
        context = CreateGroupActivity.this;

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mThumbRef = FirebaseStorage.getInstance().getReference();

        initElementsWithIds();
        initElementsWithListeners();

    }

    private void initElementsWithListeners() {

        //close button
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StorageReference filePath = mStorageRef.child("group_image").child(group_id+".jpg");
                StorageReference mThumbRef = mStorageRef.child("group_thumb").child(group_id+".jpg");

                //If user changes his mind and cancels group creation, and if he had already uploaded image, This method deletes it
                if(!filePath.toString().isEmpty()){
                    filePath.delete();
            //add an optional onSuccesslistner

                }

                Intent intent = new Intent(context,DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //choose iage on cliking image button
        groupDP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(CreateGroupActivity.this);
            }
        });

        //Clicked on next button
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String groupName = input.getText().toString();

                FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
//                final String uid = current_user.getUid();

                //Input Validation
                if(groupName.isEmpty() || groupName == "" || !groupName.matches(".*\\w.*")){

                    Toast.makeText(context, "Please enter a Name for the Group",Toast.LENGTH_LONG).show();
                }

                else if(groupDP == null){
                    Toast.makeText(CreateGroupActivity.this, "group is null put code here",Toast.LENGTH_LONG).show();
                }

                // else the entered string will be pushed to the firebase database reference
                else {

                    //This code is not pushing image ??
                    final StorageReference filePath = mStorageRef.child("group_image").child(group_id+".jpg");
                    final StorageReference mThumbRef = mStorageRef.child("group_thumb").child(group_id+".jpg");
//                    if(groupDP.equals(R.drawable.ic_default_groups))
//                    final Intent intent = new Intent(CreateGroupActivity.this,chooseUserActivity.class);

/**If image is selected
 * The image is already uploaded to server
 * this part sends the image to next activity via an intent
 */
                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

//                            String download_url = taskSnapshot.getDownloadUrl().toString();
                            final Intent intent = new Intent(context,chooseUserActivity.class);

//                            new_groups newGroups = new new_groups(admin,uri.toString(),last_msg,groupName,group_id);
//                            groupRef.child(group_id).setValue(newGroups);
//                            msgRef.child(group_id).setValue(true);

//                            groupRef.push().setValue(newGroups);
//                            Intent intent = new Intent(context,chooseUserActivity.class);
                            intent.putExtra("imagePic",uri.toString());
//                            intent.putExtra("groupName",groupName);
//                            startActivity(intent);
                            mThumbRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    intent.putExtra("imagePic",uri.toString());
                                    intent.putExtra("groupName",groupName);
                                    intent.putExtra("imageThumb",uri.toString());
                                    intent.putExtra("group_id",group_id);
                                    startActivity(intent);


                                }
                            });


                        }
                    });



//                    mThumbRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            intent.putExtra("imageThumb",uri.toString());
//
//
//                        }
//                    });





/**If image is NOT selected
 * There is no image uploaded to server
 * this part sends the default image to next activity via an intent
 */
                    filePath.getDownloadUrl().addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            String imagePic = getString(R.string.default_firebase_groups);

//                            new_groups newGroups = new new_groups(admin,defaultImage,last_msg,groupName,group_id);
//                            groupRef.child(group_id).setValue(newGroups);
//                            msgRef.child(group_id).setValue(true);

                            Intent intent = new Intent(context,chooseUserActivity.class);
                            intent.putExtra("groupName",groupName);
                            intent.putExtra("imagePic",imagePic);
                            intent.putExtra("imageThumb",imagePic);

//                            startActivity(intent);
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

        StorageReference filePath = mStorageRef.child("group_image").child(group_id+".jpg");
        StorageReference mThumbRef = mStorageRef.child("group_thumb").child(group_id+".jpg");

        //If user changes his mind and cancels group creation, and if he had already uploaded image, This method deletes it
        if(!filePath.toString().isEmpty()){
            filePath.delete();
        }
        if(!mThumbRef.toString().isEmpty()){
            mThumbRef.delete();
        }

        Intent intent = new Intent(context,DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    //This method is called on CropImage activity result
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

                //Loads image onto the UI
                String image_uri = resultUri.toString();

                Picasso.with(CreateGroupActivity.this)
                        .load(image_uri)
                        .resize(100,100)
                        .onlyScaleDown()
                        .into(groupDP);

                //this code is pushing image
//                Uri file = Uri.fromFile(new File(image_uri));

                StorageReference filePath = mStorageRef.child("group_image").child(group_id+".jpg");
                final StorageReference mThumbRef = mStorageRef.child("group_thumb").child(group_id+".jpg");


                filePath.putFile(resultUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content

//                                @SuppressWarnings("VisibleForTests") String download_url = taskSnapshot.getDownloadUrl().toString();
                                Toast.makeText(CreateGroupActivity.this, "Image Successfully uploaded to Database",Toast.LENGTH_SHORT).show();

//                                UploadTask uploadTask = mThumbRef.putBytes(thumb_byte);
//                                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                    @Override
//                                    public void onSuccess(UploadTask.TaskSnapshot task) {
//                                        String download_url = task.getDownloadUrl().toString();
//                                        Toast.makeText(CreateGroupActivity.this, "Thumb Successfully uploaded to Database",Toast.LENGTH_LONG).show();
//
//
//                                    }
//                                });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                // ...
                                Toast.makeText(CreateGroupActivity.this, "Error"+exception,Toast.LENGTH_LONG).show();
                            }
                        });

                try{
                mThumbRef.putBytes(thumb_byte).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot task) {
                        String download_url = task.getDownloadUrl().toString();
                        Toast.makeText(CreateGroupActivity.this, "Thumb Successfully uploaded to Database",Toast.LENGTH_LONG).show();

                    }
                });}
                catch (Exception e){
                    e.printStackTrace();
                }



            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
                Toast.makeText(this, "You have some error"+error,Toast.LENGTH_SHORT).show();

            }


        }
    }
}
