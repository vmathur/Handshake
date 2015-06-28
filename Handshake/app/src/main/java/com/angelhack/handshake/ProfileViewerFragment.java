package com.angelhack.handshake;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by AkhilBatra on 6/27/15.
 */
public class ProfileViewerFragment extends Fragment {

    private static final String KEY_FIRST_NAME = "key_fname";
    private static final String KEY_LAST_NAME = "key_lname";
    private static final String KEY_TAG_LINE = "key_tline";
    private static final String KEY_PHONE_NUMBER = "key_pnum";
    private static final String KEY_EMAIL = "key_email";
    private static final String KEY_LOCATION = "key_loc";
    private static final String KEY_PICTURE_URL = "key_purl";
    private static final String KEY_LINKEDIN_ID = "key_linid";
    private static final String KEY_DATE_MET = "key_dmet";


    public static ProfileViewerFragment create(PersonProfile profile) {
        ProfileViewerFragment fragment = new ProfileViewerFragment();
        Bundle bundle = new Bundle();

        bundle.putString(KEY_FIRST_NAME, profile.getFirst_name());
        bundle.putString(KEY_LAST_NAME, profile.getLast_name());
//        bundle.putString(KEY_EMAIL, profile.getEmail());
//        bundle.putString(KEY_PHONE_NUMBER, profile.getPhoneNumber());
//        bundle.putString(KEY_LOCATION, profile.getLocation());
        bundle.putString(KEY_TAG_LINE, profile.getTag_line());
        bundle.putString(KEY_PICTURE_URL, profile.getPicture_url());
        bundle.putString(KEY_LINKEDIN_ID, profile.getLinkedin_id());

        fragment.setArguments(bundle);
        return fragment;
    }

    private PersonProfile profile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();

        profile = new PersonProfile(args.getString(KEY_FIRST_NAME),
                                    args.getString(KEY_LAST_NAME),
                                    args.getString(KEY_TAG_LINE),
                                    args.getString(KEY_LINKEDIN_ID),
                                    args.getString(KEY_PICTURE_URL));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.profile_page, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.profileLink).setVisible(false);
        menu.findItem(R.id.addListLink).setVisible(false);


        ((Toolbar)(getActivity().findViewById(R.id.toolbar))).setTitle("");
        ((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().findViewById(R.id.toolbar).setElevation(0);
        ((TextView)getActivity().findViewById(R.id.profileName)).setText(profile.getFullName());
        ((TextView)getActivity().findViewById(R.id.profileTagLine)).setText(profile.getTag_line());
//        ((TextView)getActivity().findViewById(R.id.phonenum)).setText(profile.getPhoneNumber());
//        ((TextView)getActivity().findViewById(R.id.email)).setText(profile.getEmail());
//        ((TextView)getActivity().findViewById(R.id.location)).setText(profile.location());
    }
}
