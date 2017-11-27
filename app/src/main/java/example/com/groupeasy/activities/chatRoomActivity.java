package example.com.groupeasy.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
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
import example.com.groupeasy.adapters.MessageAdapter;
import example.com.groupeasy.pojo.chatMessage;

/**
 * This Class sends chats to Firebase server
 * Retrieves chats and displays in a listview
 * **/

public class chatRoomActivity extends AppCompatActivity {

//    Initialize elements
    private Toolbar mToolBar;
    private LinearLayout chatRoomButton;
    private View chatBackground;
    private String groupKey;
    private TextView roomName;
    private TextView groupIdKey;
    private ListView listView;
    private ImageView sendButton;
    private TextInputEditText messageContent;
    private RelativeLayout myEventsFrame;
    private CircleImageView groupImageView;
    private FloatingActionMenu floatingActionMenu;
    private FloatingActionButton floatingActionButton;
    private ImageView noMessages;

    //Initialize Firebase elements
    private DatabaseReference mUserDatabase,mGroupDatabase;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();
    private FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

    String current_uid = mCurrentUser.getUid();

    MessageAdapter adapter;
    Context context = chatRoomActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Link to XML layout*/
        setContentView(R.layout.chatroom_main);

        /* Calling methods for
        1. Initialising elements by IDs
        2. Initialising elements with Listeners*/

        initElementWithIds();
        initElementsWithListeners();
        floatingActionMenu.close(true);

        //by default events frame is invisible, it will only be visible if event is created
        myEventsFrame.setVisibility(View.GONE);

        //snippet to stop keyboard from appearing onCreate
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // get room name from last intent and override the chatroom title
        groupKey = getIntent().getExtras().get("groupKey").toString();

        showAllOldMessages(groupKey);

        //toolbar method
        setSupportActionBar(mToolBar);
        //image in group toolbar
        loadImage(groupKey);
        //check event for events tab
        checkEvent(groupKey);

        roomName.setVisibility(View.VISIBLE);
        groupIdKey.setText(groupKey);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       //back button takes to dashboard
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(),DashboardActivity.class);
                startActivity(intent);
                finish();

            }
        });

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("members").child(current_uid);
        listView.setOnItemClickListener(null);
       }


    // show active events tab if snapshot is not null
    private void checkEvent(String groupKey) {
        final DatabaseReference groupRef = myRef.child("Events").child("lists").child(groupKey).child("");
        groupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue()!= null)
                    myEventsFrame.setVisibility(View.VISIBLE);

                else
                    myEventsFrame.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    //load image and group name in toolbar by obtaining the groupkey param
    private void loadImage(String groupKey) {

        mGroupDatabase = FirebaseDatabase.getInstance().getReference().child("groups").child(groupKey);

        mGroupDatabase.child("image").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String image = dataSnapshot.getValue().toString();

                if(image.isEmpty()){
                    groupImageView.setImageResource(R.drawable.ic_default_groups);
                }
                else    {
                    //default
                    Picasso.with(context)
                            .load(image)
                            .placeholder(R.drawable.multi_user)
                            .resize(100,100)
                            .into(groupImageView);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //attach listener for 'name' database node
        mGroupDatabase.child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                roomName.setText(dataSnapshot.getValue().toString());
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

        //Handling all onclick events(listeners)
        public void initElementsWithListeners() {

            //on tapped outside method handling
            messageContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        hideKeyboard(v);
                    }
                }
            });

            //hide fab
            messageContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    floatingActionMenu.close(true);
                }
            });

        // clicking on create event fab will take to that activity
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,CreateEventActivity.class);
                groupKey = getIntent().getExtras().get("groupKey").toString();
                intent.putExtra("groupKey",groupKey);
                String room_name = roomName.getText().toString();
                intent.putExtra("room_name",room_name);
                startActivity(intent);

            }
        });

        //clicking on chat room tab will take to chat room about page
        chatRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context,aboutChatRoom.class);
                String groupkey = getIntent().getExtras().get("groupKey").toString();
                i.putExtra("groupkey",groupkey);
                startActivity(i);
            }
        });

        //hide and show grey background
        floatingActionMenu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if(opened)
                    chatBackground.setVisibility(View.VISIBLE);
                else
                    chatBackground.setVisibility(View.GONE);
            }
        });

            //close fab
           chatBackground.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   floatingActionMenu.close(true);
               }
           });

        /* When user clicks send button,
         * 1. Validate data
         * 2. Send data to cloud database with relavent details
         * 3. update relevant nodes*/

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionMenu.close(true);

            // if entered string is null a toast will prompt
                if(messageContent.getText().toString().isEmpty() || messageContent.getText().toString().trim().length() == 0){

                    Toast.makeText(context, R.string.you_have_entered_nothing, Toast.LENGTH_SHORT).show();
                }

            // else the entered string will be pushed to the firebase database reference
                else {

                    mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String name = dataSnapshot.child("name").getValue().toString();
                            //send data to specific node
                            FirebaseDatabase.getInstance()
                                    .getReference()
                                    .child("messages")
                                    .child(groupKey)
                                    .child("groupMsgs").push()
                                    .setValue(new chatMessage(messageContent.getText().toString().trim(),
                                            name,
                                            current_uid,
                                            groupIdKey.getText().toString()));
                            //reset text field
                            messageContent.setText("");
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }

                //update last_msg field to show in group fragment
                FirebaseDatabase.getInstance()
                        .getReference()
                        .child("groups").child(groupKey).child("last_msg").setValue(messageContent.getText().toString());
            }
        });

        //handle clicking on active events tab
        myEventsFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(),activeEvents.class);
                groupKey = getIntent().getExtras().get("groupKey").toString();
                intent.putExtra("groupKey",groupKey);
                startActivity(intent);

            }
        });
    }

    //hides keyboard on click outside
    private void hideKeyboard(View v) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private void showAllOldMessages(String group_key) {

        final DatabaseReference messages = FirebaseDatabase.getInstance().getReference().child("messages").child(group_key);

        //send params to adapter
        adapter = new MessageAdapter(this, chatMessage.class, R.layout.item_in_message,messages.child("groupMsgs").child(""));
        listView.setAdapter(adapter);

        //show no messages image if the messages db returns null
        messages.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               if(!dataSnapshot.hasChild("groupMsgs"))
                   noMessages.setVisibility(View.VISIBLE);
               else
                   noMessages.setVisibility(View.GONE);
           }
           @Override
           public void onCancelled(DatabaseError databaseError) {
           }
       });

        /*For future use**/

        //list view click
        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            }
        });

        //list view long click
        listView.setLongClickable(true);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,int pos, long id) {
                Log.v("long clicked","pos: " + pos);

                 Give user ability to delete messages

                return true;
            }
        });*/
    }

    //end of showAllOldMessages

    private void initElementWithIds() {

        //Initialize all ui elements by linking to xml ids

        mToolBar = (Toolbar) findViewById(R.id.toolbar_chat);
        roomName = (TextView) findViewById(R.id.room_name);
        groupIdKey = (TextView) findViewById(R.id.group_id_key);
        listView = (ListView) findViewById(R.id.list);
        sendButton = (ImageView) findViewById(R.id.send_button);
        messageContent = (TextInputEditText) findViewById(R.id.message_content);
        groupImageView = (CircleImageView) findViewById(R.id.group_image_view);
        chatRoomButton = (LinearLayout) findViewById(R.id.chat_room_button);
        myEventsFrame = (RelativeLayout) findViewById(R.id.active_events);
        floatingActionMenu = (FloatingActionMenu) findViewById(R.id.floatingActionMenu);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_create_list);
        chatBackground = findViewById(R.id.chat_background);

        noMessages = (ImageView) findViewById(R.id.no_messages);

    }

    //handle back press
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(context,DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    //handle when activity is resumed
    @Override
    public void onResume(){
        super.onResume();
        floatingActionMenu.close(true);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}