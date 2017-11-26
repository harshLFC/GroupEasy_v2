package example.com.groupeasy.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private ProgressDialog mRegProcess;

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

        mRegProcess = new ProgressDialog(CreateGroupActivity.this);
        mRegProcess.setTitle("Generating users list");
        mRegProcess.setMessage("Please Wait");
        mRegProcess.setCancelable(true);
        mRegProcess.setCanceledOnTouchOutside(false);

//        this.mRegProcess = ProgressDialog.show(this, "Fancy App",
//                "Loading...Please wait...", true, false);
//        // Start a new thread that will download all the data
//        new IAmABackgroundTask().execute();

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

                final String groupName = input.getText().toString().trim();

                //Input Validation
                if(groupName.isEmpty() || groupName == "" || !groupName.matches(".*\\w.*")){

                    Toast.makeText(context, "Please enter a Name for the Group",Toast.LENGTH_LONG).show();
                }

                else if(groupDP == null){
                    Toast.makeText(CreateGroupActivity.this, "group is null put code here",Toast.LENGTH_LONG).show();
                }

                // else the entered string will be pushed to the firebase database reference
                else {
                    mRegProcess.show();

                    final StorageReference filePath = mStorageRef.child("group_image").child(group_id+".jpg");
                    final StorageReference mThumbRef = mStorageRef.child("group_thumb").child(group_id+".jpg");

/**If image is selected
 * The image is already uploaded to server
 * this part sends the image to next activity via an intent
 *
 */
                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

//                            String download_url = taskSnapshot.getDownloadUrl().toString();
                            final Intent intent = new Intent(context,chooseUserActivity.class);

//                            new_groups newGroups = new new_groups(admin,uri.toString(),last_msg,groupName,group_id);
//                            groupRef.child(group_id).setValue(newGroups);
//                            msgRef.child(group_id).setValue(true);
//                            intent.putExtra("groupName",groupName);
//                            startActivity(intent);
//                            groupRef.push().setValue(newGroups);
//                            Intent intent = new Intent(context,chooseUserActivity.class);

                            intent.putExtra("imagePic",uri.toString());

                            mThumbRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri Thumburi) {
                                    intent.putExtra("groupName",groupName);
                                    intent.putExtra("imageThumb",Thumburi.toString());
                                    intent.putExtra("group_id",group_id);
                                    mRegProcess.dismiss();

                                    startActivity(intent);

                                }
                            });
                        }
                    });


/**If image is NOT selected
 * There is no image uploaded to server
 * this part sends the default image to next activity via an intent
 */
                    filePath.getDownloadUrl().addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            final String imagePic = getString(R.string.default_firebase_groups);
;

                            mThumbRef.getDownloadUrl().addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Intent intent = new Intent(context,chooseUserActivity.class);
                                    intent.putExtra("groupName",groupName);
                                    intent.putExtra("imagePic",imagePic);
                                    intent.putExtra("imageThumb",imagePic);
                                    intent.putExtra("group_id",group_id);
                                    mRegProcess.dismiss();

                                    startActivity(intent);
                                }
                            });
                        }
                    });
                }
            }
        });

        input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }

    private void hideKeyboard(View v) {

        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
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


  /*  class IAmABackgroundTask extends
            AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            // showDialog(AUTHORIZING_DIALOG);
        }

        @Override
        protected void onPostExecute(Void result) {

            // Pass the result data back to the main activity
//            CreateGroupActivity.this.data = result;

            if (CreateGroupActivity.this.mRegProcess != null) {
                CreateGroupActivity.this.mRegProcess.dismiss();
            }

            if (mRegProcess.isShowing()) {
                mRegProcess.dismiss();
            }

//            setContentView(R.layout.main);



        }

        @Override
        protected Void doInBackground(Void... params) {

            //Do all your slow tasks here but dont set anything on UI
            //ALL ui activities on the main thread

            return null;

        }

    }*/
}
