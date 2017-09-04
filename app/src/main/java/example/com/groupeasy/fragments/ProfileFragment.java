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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import example.com.groupeasy.R;
import example.com.groupeasy.activities.LoginActivity;
import example.com.groupeasy.editProfileActivity;


public class ProfileFragment extends Fragment {

    private TextView user_name, user_status;
    TextView log_in_out;
    private CircleImageView profile_pic;
    LinearLayout logOut, polls, lists, rosters, favourites,userProfile;

    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile,null);

        initElementWIthIds(view);
        initElementWIthListeners();

//code for pulling data from server and displaying details on screen
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Members").child(current_uid);
        mUserDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Toast.makeText(getContext(),dataSnapshot.toString(),Toast.LENGTH_LONG).show();
                    String name = dataSnapshot.child("member").getValue().toString();
                    String image = dataSnapshot.child("image").getValue().toString();
                    String status = dataSnapshot.child("status").getValue().toString();
                    String thumbImage = dataSnapshot.child("thumb_image").getValue().toString();

                    user_name.setText(name);
                    user_status.setText(status);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //handle errors here
            }
        });

//check if user is Logged in and do the needful
        if (mCurrentUser == null){
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
//        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
//        final String uid = mCurrentUser.getUid();
//        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Members").child(uid);
//
//
//        mUserDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Toast.makeText(getContext(),dataSnapshot.toString(),Toast.LENGTH_LONG).show();
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//                //handle errors here
//
//            }
//        });
    }

    private void initElementWIthListeners() {

         logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //different outcomes for user log in status
                if(mCurrentUser == null){
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

        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(),edit);

                String user_value_name = user_name.getText().toString();
                String user_value_status = user_status.getText().toString();

                Intent intent = new Intent(v.getContext(), editProfileActivity.class);

                intent.putExtra("user_value_name",user_value_name);
                intent.putExtra("user_value_status",user_value_status);

                startActivity(intent);

            }
        });
    }

    private void initElementWIthIds(View view) {
        user_name = (TextView) view.findViewById(R.id.user_name);
        user_status = (TextView) view.findViewById(R.id.user_status);
        log_in_out = (TextView) view.findViewById(R.id.log_in_out);

        profile_pic = (CircleImageView) view.findViewById(R.id.displayPic);

        logOut = (LinearLayout) view.findViewById(R.id.help);
        polls = (LinearLayout) view.findViewById(R.id.polls);
        lists = (LinearLayout) view.findViewById(R.id.lists);
        rosters = (LinearLayout) view.findViewById(R.id.rosters);
        favourites = (LinearLayout) view.findViewById(R.id.fav_events);
        userProfile = (LinearLayout) view.findViewById(R.id.user_profile);
    }
}
