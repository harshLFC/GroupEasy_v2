package example.com.groupeasy.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import static example.com.groupeasy.R.id.room_name;

public class aboutChatRoom extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_chat_room);

        String roomKey = getIntent().getExtras().get("groupkey").toString();
        setName(roomKey);

        initElementsWithIds();
//        groupName.setText(room_name);
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

    private void initElementsWithListeners(final String roomKey) {

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

        addMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(aboutChatRoom.this, "Will give the users option to add or remove members",Toast.LENGTH_SHORT).show();

//                Intent intent = new Intent(aboutChatRoom.this,chooseUserActivity.class);
//                startActivity(intent);
            }
        });
    }

    private void createListView() {
        final String group_key = getIntent().getExtras().get("groupkey").toString();

        //code to populate recyclerview with users in group
        groupRef.child(group_key).child("members").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList tempList = new ArrayList();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String temp = snapshot.getKey();
                    Log.w("leyz",temp);
                    tempList.add(temp);

                }

                for(int i = 0; i<tempList.size(); i++){

                        myRef.child("members").child((String) tempList.get(i)).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot ds) {
                            users_list usersList = ds.getValue(users_list.class);
                            mLstGroups.add(usersList);

                            mUserAdapter.notifyDataSetChanged();
                            Log.w("leyz",ds.toString());
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


                //COde to display group details like name
                groupRef.child(group_key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String image = dataSnapshot.child("image").getValue().toString();
                        Picasso.with(getApplicationContext())
//// mContext where Context mContext;
                        .load(image)
                        .resize(500,500)
                        .placeholder(R.drawable.ic_default_groups)
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

        groupDP = (ImageView) findViewById(R.id.group_image);
//        groupName = (TextView) findViewById(R.id.group_name);
        adminName = (TextView) findViewById(R.id.admin_name);
        addMembers = (FloatingActionButton) findViewById(R.id.add_members);
        editGroupDetails = (ImageButton) findViewById(R.id.edit_group);
        mainImage = (ImageView) findViewById(R.id.group_image);

    }
}