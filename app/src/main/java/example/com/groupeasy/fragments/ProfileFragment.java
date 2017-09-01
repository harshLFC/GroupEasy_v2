package example.com.groupeasy.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import example.com.groupeasy.R;
import example.com.groupeasy.activities.LoginActivity;


public class ProfileFragment extends Fragment {

    TextView user_name;
    TextView log_in_out;
    ImageView profile_pic;
    LinearLayout logOut, polls, lists, rosters, favourites;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private GoogleApiClient mGoogleApiClient;

    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile,null);

        initElementWIthIds(view);
        initElementWIthListeners();

        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

//check if user is Logged in and do the needful
        if (mUser == null){
            loggedOut();

        }
        else{
            loggedIn();

        }


        return  view;
    }

    private void loggedOut() {
        log_in_out.setText("Log in");
        user_name.setText("You are not logged in");
    }

    private void loggedIn() {
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

                //different outcomes for user log in status
                if(mUser == null){
                    Intent intent = new Intent(v.getContext(),LoginActivity.class);
                    startActivity(intent);
                }
                else    {
                    mAuth.signOut();
//                    Auth.GoogleSignInApi.signOut(mGoogleApiClient);

                    Intent intent = new Intent(v.getContext(),LoginActivity.class);
                    startActivity(intent);

                    Toast toast = Toast.makeText(v.getContext(),"You have been logged out", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

//                    Toast.makeText(v.getContext(), "You have been logged out",Toast.LENGTH_LONG).show();
                }
            }
        });

        polls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Will open up polls", Toast.LENGTH_SHORT).show();
            }
        });

        lists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Will open up lists", Toast.LENGTH_SHORT).show();
            }
        });

        rosters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Will open up rosters", Toast.LENGTH_SHORT).show();
            }
        });

        favourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Will open up Favourites", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initElementWIthIds(View view) {
        user_name = (TextView) view.findViewById(R.id.user_name);
        log_in_out = (TextView) view.findViewById(R.id.log_in_out);

        profile_pic = (ImageView) view.findViewById(R.id.displayPic);

        logOut = (LinearLayout) view.findViewById(R.id.help);
        polls = (LinearLayout) view.findViewById(R.id.polls);
        lists = (LinearLayout) view.findViewById(R.id.lists);
        rosters = (LinearLayout) view.findViewById(R.id.rosters);
        favourites = (LinearLayout) view.findViewById(R.id.fav_events);
    }
}
