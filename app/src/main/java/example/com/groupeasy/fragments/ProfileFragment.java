package example.com.groupeasy.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import example.com.groupeasy.R;
import example.com.groupeasy.activities.LoginActivity;
import example.com.groupeasy.activities.editProfileActivity;

/**
 * This Fragment is a part of the Dashboard,
 * It displays The user profile along with his/her details
 **/

public class ProfileFragment extends Fragment {

    TextView log_in_out;
    LinearLayout logOut, userEvents, lists, rosters, favourites;
    com.github.clans.fab.FloatingActionButton userProfile;
    TextView Favs;
    TextView EventCount;
    private TextView user_name, user_status;
    private CircleImageView profile_pic;
    private CollapsingToolbarLayout myCollapsingTool;
    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;
//    private GoogleApiClient mGoogleApiClient;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        /*Link to XML layout*/
        View view = inflater.inflate(R.layout.fragment_profile,null);

        /* Calling methods for
        1. Initialising elements by IDs
        2. Generating a Display
        3. Initialising elements with Listeners*/
        initElementWIthIds(view);
        initElementWIthListeners();
        renderDisplay();

        return  view;
    }

    private void renderDisplay() {
        //code for pulling data from server and displaying details on screen
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();

        mAuth = FirebaseAuth.getInstance();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("members").child(current_uid);
        mUserDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
//                String thumbImage = dataSnapshot.child("thumb_image").getValue().toString();

                Long FavCount = dataSnapshot.child("favs").getChildrenCount();
                Long eventCount = dataSnapshot.child("events_created").getChildrenCount();

                Favs.setText(FavCount.toString());
                EventCount.setText(eventCount.toString());

//                    user_name.setText(name);
                myCollapsingTool.setTitle(name);
                user_status.setText(status);
                Picasso.with(getContext())
                        .load(image)
                        .placeholder(R.drawable.single_user)
                        .resize(300,300)
                        .centerCrop()
                        .into(profile_pic);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //handle errors here
            }
        });
    }

    /*Handling all onclick events(listeners)*/
    private void initElementWIthListeners() {

        //handle user logout press
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                intent.putExtra("keyname", "value");
                    startActivity(intent);
                    getActivity().finish();

                    Toast toast = Toast.makeText(v.getContext(),"You have been logged out", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
            }
        });

        userEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "The number of Events created", Toast.LENGTH_SHORT).show();
            }
        });

   /*     rosters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Will open up rosters", Toast.LENGTH_SHORT).show();
            }
        });*/

        favourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "The number of events Favourited", Toast.LENGTH_SHORT).show();
            }
        });

        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(),edit);

                String user_value_name = myCollapsingTool.getTitle().toString();
                String user_value_status = user_status.getText().toString();

                Intent intent = new Intent(v.getContext(), editProfileActivity.class);

                intent.putExtra("user_value_name",user_value_name);
                intent.putExtra("user_value_status",user_value_status);

                startActivity(intent);
            }
        });

        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(),edit);

                String user_value_name = myCollapsingTool.getTitle().toString();
                String user_value_status = user_status.getText().toString();

                Intent intent = new Intent(v.getContext(), editProfileActivity.class);

                intent.putExtra("user_value_name",user_value_name);
                intent.putExtra("user_value_status",user_value_status);

                startActivity(intent);
            }
        });

    }

    private void initElementWIthIds(View view) {
        user_status = (TextView) view.findViewById(R.id.user_status);
        log_in_out = (TextView) view.findViewById(R.id.log_in_out);
        profile_pic = (CircleImageView) view.findViewById(R.id.displayPic);
        logOut = (LinearLayout) view.findViewById(R.id.help);
        userEvents = (LinearLayout) view.findViewById(R.id.user_events);
        favourites = (LinearLayout) view.findViewById(R.id.fav_events);
        userProfile = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.user_profile);

//        user_name = (TextView) view.findViewById(R.id.user_name);
//        lists = (LinearLayout) view.findViewById(R.id.lists);
//        rosters = (LinearLayout) view.findViewById(R.id.rosters);

        myCollapsingTool = (CollapsingToolbarLayout) view.findViewById(R.id.collapsingToolbar);

        Favs = (TextView) view.findViewById(R.id.favs_count);
        EventCount = (TextView) view.findViewById(R.id.event_count);
    }
}
