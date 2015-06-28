package com.angelhack.handshake;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by AkhilBatra on 6/27/15.
 */
public class ProfileListFragment extends Fragment {

    private ListView lview;
    private ProfileArrayAdapter pAdapter;
    private PersonProfile[] people;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_list_page, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.lview = (ListView)(getActivity().findViewById(R.id.list));

        people = new PersonProfile[19];
        String f = "First";
        String l = "last";
        String tl = "tagline";
        for (int i = 0; i < 11; i++) {
            f += i;
            l += i;
            tl = i + tl;
            people[i] = new PersonProfile(f, l, tl, "id", "picurl");
        }

        pAdapter = new ProfileArrayAdapter(getActivity(), people);
        lview.setAdapter(pAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);
    }

}
