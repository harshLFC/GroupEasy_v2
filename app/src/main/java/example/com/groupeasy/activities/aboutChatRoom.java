package example.com.groupeasy.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import example.com.groupeasy.R;
import example.com.groupeasy.adapters.UserAdapter;
import example.com.groupeasy.pojo.users_list;


/**
 * This Class shows group name & list of members in a recyclerview
 * **/

public class aboutChatRoom extends AppCompatActivity {

    //initilize varaibles
    /**import RecyclerView, Custom Adapter, and List */
    private RecyclerView mUserRecyclerView;
    private UserAdapter mUserAdapter;
    private List<users_list> mLstGroups;
    private ImageView groupDP,mainImage;
    private ImageButton editGroupDetails;
    private TextView groupName;
    private TextView adminName;
    private FloatingActionButton addMembers;

    /** Firebase db init*/
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();
    final DatabaseReference groupRef = myRef.child("groups");

    private Context context = aboutChatRoom.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_chat_room);

        String roomKey = getIntent().getExtras().get("groupkey").toString();
        setName(roomKey);

        /* Calling methods for
        1. Initialising elements by IDs
        2. Generating the listview using custom adapter
        2. Initialising elements with Listeners using roomKey as Parameter*/
        initElementsWithIds();
        createListView();
        initElementsWithListeners(roomKey);

        mLstGroups = new ArrayList<>();

        mUserAdapter = new UserAdapter(mLstGroups);
        mUserRecyclerView = (RecyclerView) findViewById(R.id.group_users_reycler_view);
        mUserRecyclerView.setHasFixedSize(true);
        mUserRecyclerView.setLayoutManager(new LinearLayoutManager(aboutChatRoom.this));
        mUserRecyclerView.setAdapter(mUserAdapter);
        mUserRecyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private void setName(String roomKey) {

        groupRef.child(roomKey).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //set toolbar with group name
                final Drawable upArrow = getResources().getDrawable(R.drawable.ic_back_arrow_left);

                Toolbar mToolbar = (Toolbar) findViewById(R.id.users_group_toolbar);
                setSupportActionBar(mToolbar);
                getSupportActionBar().setTitle(dataSnapshot.getValue().toString());
                getSupportActionBar().setShowHideAnimationEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(upArrow);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    //Handling all onclick events(listeners)
    private void initElementsWithListeners(final String roomKey) {

        //Will navigate user to editGroupActivity
        editGroupDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                groupRef.child(roomKey).child("name").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Intent intent = new Intent(aboutChatRoom.this,editGroupActivity.class);

                        intent.putExtra("room_name",dataSnapshot.getValue().toString());
                        String roomKey = getIntent().getExtras().get("groupkey").toString();
                        intent.putExtra("room_key",roomKey);

                        startActivity(intent);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });

        /*For future use*/
        addMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, getString(R.string.add_members), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Generats a list of members added in group
    private void createListView() {
        final String group_key = getIntent().getExtras().get("groupkey").toString();

        //code to populate recyclerview with users in group
        groupRef.child(group_key).child("members").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList tempList = new ArrayList();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String temp = snapshot.getKey();
                    tempList.add(temp);
                }

                for(int i = 0; i<tempList.size(); i++){
                        myRef.child("members").child((String) tempList.get(i)).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot ds) {
                            users_list usersList = ds.getValue(users_list.class);
                            mLstGroups.add(usersList);

                            mUserAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
                mUserAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

                //Code to display group details
                groupRef.child(group_key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String image = dataSnapshot.child("image").getValue().toString();

                        Picasso.with(getApplicationContext())
                        .load(image)
                        .resize(500,500)
                        .placeholder(R.drawable.multi_user_main)
                        .centerCrop()
                        .into(groupDP, new com.squareup.picasso.Callback(){
                            @Override
                            public void onSuccess(){
                                groupDP.setPadding(5,5,5,5);
                                groupDP.setBackgroundColor(Color.BLACK);
                            }
                            @Override
                            public void onError() {
                            }
                        });

                        String admin = dataSnapshot.child("admin").getValue().toString();

                        myRef.child("members").child(admin).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String name = dataSnapshot.child("name").getValue().toString();
                                adminName.setText(name);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }

    private void initElementsWithIds() {
        /*Initialize all ui elements by linking to xml ids*/

        groupDP = (ImageView) findViewById(R.id.group_image);
        adminName = (TextView) findViewById(R.id.admin_name);
        addMembers = (FloatingActionButton) findViewById(R.id.add_members);
        editGroupDetails = (ImageButton) findViewById(R.id.edit_group);
    }
}