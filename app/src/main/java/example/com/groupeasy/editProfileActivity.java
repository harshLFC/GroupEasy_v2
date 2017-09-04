package example.com.groupeasy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;
import example.com.groupeasy.activities.DashboardActivity;

public class editProfileActivity extends AppCompatActivity {

    Toolbar mToolBar;
    TextInputLayout userName, userStatus;
    CircleImageView editImage;
    ProgressDialog mDialog;
    Button saveChanges;

    private DatabaseReference mStatusDatabase;
    private FirebaseUser mCurrentUser;

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

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = mCurrentUser.getUid();

        mStatusDatabase = FirebaseDatabase.getInstance().getReference().child("Members").child(uid);
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
                if(!TextUtils.isEmpty(Status)){
                    mStatusDatabase.child("status").setValue(Status).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {

                                mDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Updated",Toast.LENGTH_LONG).show();
                            }
                            else{
                                mDialog.hide();
                                Toast.makeText(getApplicationContext(), "Some error",Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    if (!TextUtils.isEmpty(Name)){

                        mStatusDatabase.child("member").setValue(Name).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    mDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Updated",Toast.LENGTH_LONG).show();

//                                    Intent intent = new Intent(getApplicationContext(),DashboardActivity.class);
//                                    startActivity(intent);
//                                    finish();
                                }
                                else{
                                    mDialog.hide();
                                    Toast.makeText(getApplicationContext(), "Some error",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }

                else if(!TextUtils.isEmpty(Name)){
                    mStatusDatabase.child("member").setValue(Name).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                mDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Username updated",Toast.LENGTH_LONG).show();
                            }
                            else{
                                mDialog.hide();
                                Toast.makeText(getApplicationContext(), "Some error",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
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
}
