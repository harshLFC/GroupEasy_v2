package example.com.groupeasy.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import example.com.groupeasy.R;
import example.com.groupeasy.adapters.UserAdapter;
import example.com.groupeasy.pojo.users_list;


public class UsersFragment extends Fragment {

    /** Ui elements init */
    private RecyclerView mUserRecyclerView;
    private UserAdapter mUserAdapter;
    private List<users_list> mLstGroups;

    private TextView emptyView;

    /** Firebase db init*/
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();
    final DatabaseReference userRef = myRef.child("members").child("");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final Context mcontext = getActivity();

        View rootView = inflater.inflate(R.layout.fragment_users,null);

        initElementsWithIds(rootView);
        createListView();
        initElementsWithListeners();


        mLstGroups = new ArrayList<>();
        // initialize adapter to our List of <group>
        mUserAdapter = new UserAdapter(mLstGroups);

        mUserRecyclerView = (RecyclerView) rootView.findViewById(R.id.users_list);
        mUserRecyclerView.setHasFixedSize(true);

        mUserRecyclerView.setLayoutManager(new LinearLayoutManager(mcontext));

        //a RecyclerView needs an adapter to access its data
        mUserRecyclerView.setAdapter(mUserAdapter);
        mUserRecyclerView.setItemAnimator(new DefaultItemAnimator());

        return rootView;
    }



    private void initElementsWithListeners()
    {


    }

    private void initElementsWithIds(View view) {

    emptyView = (TextView) view.findViewById(R.id.empty_view_users);

    }

    private void createListView() {

        userRef.keepSynced(true);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mLstGroups.removeAll(mLstGroups);

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    users_list usersList = snapshot.getValue(users_list.class);
                    mLstGroups.add(usersList);
                }

                mUserAdapter.notifyDataSetChanged();

                if(mLstGroups.isEmpty()){
                    emptyView.setVisibility(View.VISIBLE);
                    mUserRecyclerView.setVisibility(View.GONE);
                }
                else{

                    emptyView.setVisibility(View.GONE);
                    mUserRecyclerView.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onStart(){
        super.onStart();
    }
}
