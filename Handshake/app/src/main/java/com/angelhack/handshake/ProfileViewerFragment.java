package com.angelhack.handshake;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by AkhilBatra on 6/27/15.
 */
public class ProfileViewerFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_page, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((Toolbar)getActivity().findViewById(R.id.toolbar)).setTitle("");
        getActivity().findViewById(R.id.toolbar).setElevation(0);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((TextView) getActivity().findViewById(R.id.profileName)).setText("Paula Barcante");
        ((TextView)getActivity().findViewById(R.id.profileTagLine)).setText("UX Designer at Amazon");
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
