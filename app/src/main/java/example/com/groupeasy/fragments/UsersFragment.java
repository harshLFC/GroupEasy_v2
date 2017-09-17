package example.com.groupeasy.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import example.com.groupeasy.R;


public class UsersFragment extends Fragment {

    /** Ui elements init */
    private RecyclerView usersList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_search,null);
        initElementsWithIds(rootView);
        initElementsWithListeners();
        return rootView;
    }

    private void initElementsWithListeners()
    {


    }

    private void initElementsWithIds(View view) {

        usersList = (RecyclerView) view.findViewById(R.id.users_list);


    }
}
