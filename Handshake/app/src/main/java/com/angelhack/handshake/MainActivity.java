package com.angelhack.handshake;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;


public class MainActivity extends ActionBarActivity {

    private Toolbar tbar;
    private ListView lview;
    private ProfileArrayAdapter pAdapter;
    private PersonProfile[] people;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.tbar = (Toolbar)findViewById(R.id.toolbar);
        this.lview = (ListView)findViewById(R.id.list);

        setSupportActionBar(tbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("All");

        people = new PersonProfile[19];
        String f = "First";
        String l = "last";
        String tl = "tagline";
        for (int i = 0; i < 11; i++) {
            f += i;
            l += i;
            tl = i + tl;
            people[i] = new PersonProfile(f, l, tl, "id", "picurl", 0);
        }

        pAdapter = new ProfileArrayAdapter(this, people);
        lview.setAdapter(pAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.addListLink:
                return true;
            case R.id.profileLink:
                FragmentTransaction ftrans = getFragmentManager().beginTransaction();
                ftrans.add(R.id.profile, new ProfileViewerFragment());
                ftrans.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

