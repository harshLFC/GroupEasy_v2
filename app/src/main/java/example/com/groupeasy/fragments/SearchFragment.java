package example.com.groupeasy.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import example.com.groupeasy.R;
import example.com.groupeasy.activities.NextBuildActivity;


public class SearchFragment extends Fragment {

    /** Ui elements init */
    private Button eventsGlobal, eventsLocal;

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
        eventsGlobal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**commenting out intent for NEXTBUILD
                Intent intent = new Intent(getActivity(), NextBuildActivity.class);
                startActivity(intent);
                getActivity().finish();*/

                Toast.makeText(getActivity(), "This part of the app is in construction",Toast.LENGTH_LONG).show();

            }
        });

        eventsLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**commenting out intent for NEXTBUILD
                 Intent intent = new Intent(getActivity(), NextBuildActivity.class);
                 startActivity(intent);
                 getActivity().finish();*/

                Toast.makeText(getActivity(), "This part of the app is in construction",Toast.LENGTH_LONG).show();

            }
        });
    }

    private void initElementsWithIds(View view) {
        eventsGlobal = (Button) view.findViewById(R.id.global_event);
        eventsLocal = (Button) view.findViewById(R.id.local_event);
    }
}
