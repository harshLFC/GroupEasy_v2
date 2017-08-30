package example.com.groupeasy.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import example.com.groupeasy.R;


public class ProfileFragment extends Fragment {

    TextView user_name;

    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile,null);

        initElementWIthIds(view);
        initElementWIthListeners();

        return  view;
    }

    private void initElementWIthListeners() {

        String uid = current_user.getUid();


        user_name.setText(uid);

    }

    private void initElementWIthIds(View view) {
        user_name = (TextView) view.findViewById(R.id.user_name);
    }
}
