package example.com.groupeasy.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import example.com.groupeasy.R;
import example.com.groupeasy.activities.CreateGroupActivity;
import example.com.groupeasy.adapters.GroupAdapter;
import example.com.groupeasy.pojo.new_groups;

/**
 * This Fragment is a part of the Dashboard,
 * It displays a list of groups for the user to inteact with
 **/


public class GroupFragment extends Fragment {

    /**
     * Firebase db init
     */
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();
    final DatabaseReference userRef = myRef.child("members");
    final DatabaseReference groupRef = myRef.child("groups").child("");
    /** Ui elements init */
    //Declare list, view, and adapter
    private RecyclerView mGroupRecyclerView;
    private GroupAdapter mGroupAdapter;
    private List<new_groups> mLstGroups;
    private ImageView emptyView;
    //    private FloatingActionMenu floatingActionMenu;
    private View backgroundView;
    private FloatingActionButton fabCreateGroup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //we need this because we are working with a fragment
        final Context mcontext = getActivity();

        //inflate is used to populate the recycler view in this case
        /*Link to XML layout*/
        View rootView = inflater.inflate(R.layout.fragment_group, container, false);

        /* Calling methods for
        1. Initialising elements by IDs
        2. Generating a custom group list for each member
        3. Initialising elements with Listeners*/
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

//        floatingActionMenu = (FloatingActionMenu) view.findViewById(R.id.floating_action_button);
        backgroundView = view.findViewById(R.id.group_background);
        fabCreateGroup = (FloatingActionButton) view.findViewById(R.id.fab_create_group);
        emptyView = (ImageView) view.findViewById(R.id.empty_view);

    }

    /** handle your all on click events */
    private void initElementsWithListeners() {

        //The previous idea of having 3 types of events pop open from the main FAB was skipped, hence the code commented
/*        floatingActionMenu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
               *//* if(opened)
                {
                    backgroundView.setVisibility(View.VISIBLE);
                }
                else
                {
                    backgroundView.setVisibility(View.GONE);
                }*//*
            }
        });*/

       /*
        fabCreateList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateEventActivity.class);
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
        */

        //Create group on FAB click
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

        //Gather list of groups
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

                                new_groups newGroups = dataSnapshot.getValue(new_groups.class);
                                mLstGroups.add(0,newGroups);

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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
    }
}
