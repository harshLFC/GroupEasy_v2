package example.com.groupeasy.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import example.com.groupeasy.R;
import example.com.groupeasy.activities.LoginActivity;
import example.com.groupeasy.activities.LoginGoogle;


public class ProfileFragment extends Fragment {

    TextView user_name;
    TextView log_in_out;
    ImageView profile_pic;
    LinearLayout logOut;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile,null);

        initElementWIthIds(view);
        initElementWIthListeners();

        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();


        if (mUser == null){
            log_in_out.setText("Log in");

            user_name.setText("You are not logged in");

        }
        else{
            checkUserLogin();
        }


        return  view;
    }

    private void checkUserLogin() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();
        user_name.setText(uid);

            Toast.makeText(getContext(), "User is "+user,Toast.LENGTH_LONG).show();
    }

    private void initElementWIthListeners() {




        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Will open up gallery", Toast.LENGTH_LONG).show();
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mUser == null){
                    Intent intent = new Intent(v.getContext(),LoginGoogle.class);
                    startActivity(intent);

                }
                else    {
                    mAuth.signOut();

                    Intent intent = new Intent(v.getContext(),LoginActivity.class);
                    startActivity(intent);

                    Toast.makeText(v.getContext(), "You have been logged out",Toast.LENGTH_LONG).show();
                }


            }
        });


    }

    private void initElementWIthIds(View view) {
        user_name = (TextView) view.findViewById(R.id.user_name);
        log_in_out = (TextView) view.findViewById(R.id.log_in_out);
        profile_pic = (ImageView) view.findViewById(R.id.displayPic);
        logOut = (LinearLayout) view.findViewById(R.id.log_out);



    }
}
