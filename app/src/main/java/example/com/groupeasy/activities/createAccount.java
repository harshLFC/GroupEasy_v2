package example.com.groupeasy.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import example.com.groupeasy.R;

/**
 * Created by Harsh on 31-08-2017.
 */

public class createAccount extends AppCompatActivity {

    private Toolbar mToolBar;
    private TextInputLayout userName,password,email;
    private Button mCreateBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

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
                    register_user(mUserEmail,mUserName,mUserPass);
                }
            }
        });
    }

    private void register_user(String mUserEmail, String mUserName, String mUserPass) {

        mAuth.createUserWithEmailAndPassword(mUserEmail,mUserPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                }

                else {

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