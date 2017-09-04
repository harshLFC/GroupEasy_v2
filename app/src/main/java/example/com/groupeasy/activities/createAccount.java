package example.com.groupeasy.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import example.com.groupeasy.R;

/**
 * Created by Harsh on 31-08-2017.
 */

public class createAccount extends AppCompatActivity {

    private Toolbar mToolBar;
    private TextInputLayout userName,password,email;
    private Button mCreateBtn;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private ProgressDialog mRegProcess;

    public boolean onOptionsItemSelected(MenuItem item) {

        System.out.println("Inside opOptionsItemSelected");
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mAuth = FirebaseAuth.getInstance();

        mToolBar = (Toolbar) findViewById(R.id.create_acc_tool);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Create An Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initElementsWithIds();
        initElementsWithListeners();
    }

    private void initElementsWithListeners() {

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mUserName = userName.getEditText().getText().toString();
                String mUserEmail = email.getEditText().getText().toString();
                String mUserPass = password.getEditText().getText().toString();

                if( !TextUtils.isEmpty(mUserEmail) || !TextUtils.isEmpty(mUserName) || !TextUtils.isEmpty(mUserPass))
                {
                    mRegProcess = new ProgressDialog(v.getContext());
                    mRegProcess.setTitle("Registering User");
                    mRegProcess.setMessage("Please Wait");
                    mRegProcess.setCanceledOnTouchOutside(false);
                    mRegProcess.show();

                    register_user(mUserEmail,mUserName,mUserPass);
                }
            }
        });
    }

    private void register_user(final String mUserEmail, final String mUserName, String mUserPass) {

        mAuth.createUserWithEmailAndPassword(mUserEmail,mUserPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();

                    if(current_user != null)
                    {
                        String uid = current_user.getUid();

                        mDatabase = FirebaseDatabase.getInstance().getReference().child("Members").child(uid);

                        HashMap<String,String> userMap = new HashMap<String, String>();
                        userMap.put("member",mUserName);
                        userMap.put("status","Hi! Im on GroupEasy");
                        userMap.put("image","Default");
                        userMap.put("favs","0");
                        userMap.put("polls","0");
                        userMap.put("rosters","0");
                        userMap.put("lists","0");
                        userMap.put("thumb_image","Default");

                        mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful()){
                                    mRegProcess.dismiss();
                                    Intent i = new Intent(createAccount.this,DashboardActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                                else{
                                    Toast.makeText(createAccount.this,
                                            "Please check if you already have an account, or the entered details are right", Toast.LENGTH_LONG)
                                            .show();
                                }
                            }
                        });
                    }
                    else
                        {
                        mRegProcess.hide();

                        Toast.makeText(createAccount.this,
                                "Please check if you already have an account, or the entered details are right", Toast.LENGTH_LONG)
                                .show();
                    }
                }
                else
                    {
                    mRegProcess.hide();

                    Toast.makeText(createAccount.this,
                            "Please check if you already have an account, or the entered details are right", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    private void initElementsWithIds() {
        userName = (TextInputLayout) findViewById(R.id.text_input_user_name);
        email = (TextInputLayout) findViewById(R.id.text_input_email);
        password = (TextInputLayout) findViewById(R.id.text_input_password);

        mCreateBtn = (Button) findViewById(R.id.btn_create_acc);
    }
}