package com.angelhack.handshake;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkedin.platform.DeepLinkHelper;
import com.linkedin.platform.errors.LIDeepLinkError;
import com.linkedin.platform.listeners.DeepLinkListener;

/**
 * Created by AkhilBatra on 6/27/15.
 */
public class ProfileArrayAdapter extends ArrayAdapter<PersonProfile> implements View.OnClickListener {

    public ProfileArrayAdapter(Context context, PersonProfile[] people) {
        super(context, 0, people);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.profile_list_section, null);
        }

        PersonProfile p = getItem(position);

        if (p != null) {
            ImageView img1 = (ImageView) v.findViewById(R.id.image1);
            TextView tt1 = (TextView) v.findViewById(R.id.text1);
            TextView tt2 = (TextView) v.findViewById(R.id.text2);
            TextView tt3 = (TextView) v.findViewById(R.id.text3);

            if (img1 != null) {
            }

            if (tt1 != null) {
                tt1.setText(p.getFullName());
            }

            if (tt2 != null) {
                tt2.setText(p.getTag_line());
            }

            if (tt3 != null) {
                tt3.setTag(p);
                tt3.setOnClickListener(this);
            }
        }

        return v;
    }

    @Override
    public void onClick(View v) {

        DeepLinkHelper deepLinkHelper = DeepLinkHelper.getInstance();

        deepLinkHelper.openOtherProfile((MainActivity) getContext(), ((PersonProfile)v.getTag()).getUser_id(), new DeepLinkListener() {
            @Override
            public void onDeepLinkSuccess() {
                Log.d("SUCCESS", "went to profile");
            }

            @Override
            public void onDeepLinkError(LIDeepLinkError error) {
                Log.d("FAILURE", "you are still here");
            }
        });
    }
}
