package example.com.groupeasy.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import example.com.groupeasy.R;
import example.com.groupeasy.activities.CreateGroupActivity;
import example.com.groupeasy.activities.CreateNewListActivity;
import example.com.groupeasy.activities.CreatePollActivity;
import example.com.groupeasy.activities.CreateRosterActivity;
import example.com.groupeasy.adapters.SampleAdapter;
import example.com.groupeasy.adapters.GroupAdapter;
import example.com.groupeasy.pojo.new_groups;


public class GroupFragment extends Fragment {

    /** Ui elements init */
    //Declare list, view, and adapter
    private RecyclerView mGroupRecyclerView;
    private GroupAdapter mGroupAdapter;
    private List<new_groups> mLstGroups;
    private TextView emptyView;
    private SampleAdapter sampleAdapter;

    private FloatingActionMenu floatingActionMenu;
    private View backgroundView;
    private FloatingActionButton fabCreatePoll;
    private FloatingActionButton fabCreateRoaster;
    private FloatingActionButton fabCreateList;
    private FloatingActionButton fabCreateGroup;

    /** Firebase db init*/
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();

    final DatabaseReference userRef = myRef.child("members");

    final DatabaseReference groupRef = myRef.child("groups").child("");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //we need this because we are working with a fragment
        final Context mcontext = getActivity();

        //inflate is used to populate the recycler view in this case
        View rootView = inflater.inflate(R.layout.fragment_group,container,false);

        initElementsWithIds(rootView);
        createListView();
        initElementsWithListeners();

        mLstGroups = new ArrayList<>();
        // initialize adapter to our List of <group>
        mGroupAdapter = new GroupAdapter(mLstGroups);

        //Initialize the Recycler view and set an adapter to populate the JSON data
        mGroupRecyclerView = (RecyclerView) rootView.findViewById(R.id.groupRecyclerView);
        mGroupRecyclerView.setHasFixedSize(true);

        // a RecyclerView needs a LayoutManager to manage the positioning of its items
        mGroupRecyclerView.setLayoutManager(new LinearLayoutManager(mcontext));

        //a RecyclerView needs an adapter to access its data
        mGroupRecyclerView.setAdapter(mGroupAdapter);
        mGroupRecyclerView.setItemAnimator(new DefaultItemAnimator());

        return rootView;
    }

    private void initElementsWithIds(View view) {

        floatingActionMenu = (FloatingActionMenu) view.findViewById(R.id.floating_action_button);
        backgroundView = view.findViewById(R.id.group_background);
        fabCreatePoll = (FloatingActionButton) view.findViewById(R.id.fab_create_poll);
        fabCreateRoaster = (FloatingActionButton) view.findViewById(R.id.fab_create_roaster);
        fabCreateList = (FloatingActionButton) view.findViewById(R.id.fab_create_list);
        fabCreateGroup = (FloatingActionButton) view.findViewById(R.id.fab_create_group);
        emptyView = (TextView) view.findViewById(R.id.empty_view);

    }

    /** handle your all on click events */
    private void initElementsWithListeners()
    {

        floatingActionMenu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if(opened)
                {
                    backgroundView.setVisibility(View.VISIBLE);
                }
                else
                {
                    backgroundView.setVisibility(View.GONE);
                }
            }
        });

        fabCreateList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateNewListActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        fabCreatePoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreatePollActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        fabCreateRoaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateRosterActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        fabCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateGroupActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

    }

    private void createListView()
    {
        //code to keep firebase database offline functionality synced
        groupRef.keepSynced(true);

        final FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = current_user.getUid();


        userRef.child(uid).child("groupsIn").child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final ArrayList myArray = new ArrayList();

                for( DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    /**I am getting correct list of groups in here, and adding them successfully to an array**/
                    String temp = snapshot.getKey();
                    myArray.add(temp);

                }

                for(int k=0; k<myArray.size();k++){

                    myRef.child("groups").child((String) myArray.get(k)).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

//                            mLstGroups.removeAll(mLstGroups);

//                            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                new_groups newGroups = dataSnapshot.getValue(new_groups.class);
                                mLstGroups.add(newGroups);
//                            }
//                    new_groups newGroups  = dataSnapshot.getValue(new_groups.class);
//                    mLstGroups.add(newGroups);

                            mGroupAdapter.notifyDataSetChanged();

                            //COde for displaying something when RecyclerView data is empty

                            if(mLstGroups.isEmpty()){
                                emptyView.setVisibility(View.VISIBLE);
                                mGroupRecyclerView.setVisibility(View.GONE);
                            }
                            else{

                                emptyView.setVisibility(View.GONE);
                                mGroupRecyclerView.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

//                        for(int i=0; i<myArray.size();i++) {
//                            Log.w((String) myArray.get(i), "xyzzyspoon");
//                        }

                /**After here i wish to iterate through the previous array list and check if the keys of groups match,
                 if they do, then print out the group details**/
//                        groupRef.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                                mLstGroups.removeAll(mLstGroups);
//
//                                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
//                                    String temp = snapshot.getKey();
//                                    myGroupArray.add(temp);
//                                }
////                    new_groups newGroups  = dataSnapshot.getValue(new_groups.class);
////                    mLstGroups.add(newGroups);
//
//                                mGroupAdapter.notifyDataSetChanged();
//
//                                //COde for displaying something when RecyclerView data is empty
//
//                                if(mLstGroups.isEmpty()){
//                                    emptyView.setVisibility(View.VISIBLE);
//                                    mGroupRecyclerView.setVisibility(View.GONE);
//                                }
//                                else{
//
//                                    emptyView.setVisibility(View.GONE);
//                                    mGroupRecyclerView.setVisibility(View.VISIBLE);
//
//                                }
//
//
//
//
////                                for (int i=0; i < myArray.size(); i++){
////
////                                    String temp = myArray.toString();
//////                                    Log.w(temp,"xyzzyspoonTEMP");
////
////                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
////
////                                        if(snapshot.toString().equals(myArray.toString())){
////
////                                            new_groups newGroups = snapshot.getValue(new_groups.class);
////                                            mLstGroups.add(newGroups);
////                                        }
////
////                                        mGroupAdapter.notifyDataSetChanged();
////
////                                        if(mLstGroups.isEmpty()){
////                                            emptyView.setVisibility(View.VISIBLE);
////                                            mGroupRecyclerView.setVisibility(View.GONE);
////                                        }
////                                        else{
////                                            emptyView.setVisibility(View.GONE);
////                                            mGroupRecyclerView.setVisibility(View.VISIBLE);
////                                        }
////                                    }
////                                }
//                            }
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
                {
//                            new_groups newGroups = snapshot.getValue(new_groups.class);
//                            mLstGroups.add(newGroups);
                }
//                    }

//                    new_groups newGroups  = dataSnapshot.getValue(new_groups.class);
//                    mLstGroups.add(newGroups);
                //COde for displaying something when RecyclerView data is empty
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

// code i was writing wrong
//            groupRef.addChildEventListener(new ChildEventListener() {
//                @Override
//                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                    Map<String, String> map =  (Map<String,String>) dataSnapshot.getValue();
//
//                    mLstGroups.add(new new_groups(map.get("name")));
////                    mLstGroup.add(new Group(map.get("icon")));
//                    mGroupAdapter.notifyItemInserted(mLstGroups.size()-1);
//                }
//                @Override
//                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                }
//                @Override
//                public void onChildRemoved(DataSnapshot dataSnapshot) {
//                }
//                @Override
//                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//                }
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                }});

//        /** fetch your list from the json and add this to adapter*/
//        mLstGroups = new ArrayList<>();
//        mLstGroups.add(null);
//        mLstGroups.add(null);
//        mLstGroups.add(null);
//        mLstGroups.add(null);
//        adapterForAllGroups = new AdapterForAllGroups(getActivity(), mLstGroups);
// mGroupRecyclerView.setAdapter(adapterForAllGroups);

    }
    @Override
    public void onStart() {
        super.onStart();
    }
}
