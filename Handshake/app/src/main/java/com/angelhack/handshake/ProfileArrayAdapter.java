package com.angelhack.handshake;

import android.app.FragmentTransaction;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by AkhilBatra on 6/27/15.
 */
public class ProfileArrayAdapter extends ArrayAdapter<PersonProfile> implements View.OnClickListener{

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
            LinearLayout ll1 = (LinearLayout) v.findViewById(R.id.profileListSection);
            ImageView img1 = (ImageView) v.findViewById(R.id.image1);
            TextView tt1 = (TextView) v.findViewById(R.id.text1);
            TextView tt2 = (TextView) v.findViewById(R.id.text2);

            if(ll1 != null) {
                ll1.setOnClickListener(this);
            }

            if (img1 != null) {
            }

            if (tt1 != null) {
                tt1.setText(p.getFullName());
            }

            if (tt2 != null) {
                tt2.setText(p.getTagline());
            }
        }

        return v;
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction ftrans = ((MainActivity) getContext()).getFragmentManager().beginTransaction();
        ftrans.add(R.id.fragment_container, new ProfileViewerFragment());
        ftrans.commit();
    }
}
