package com.angelhack.handshake;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;


public class MainActivity extends ActionBarActivity {

    private Toolbar tbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.tbar = (Toolbar)findViewById(R.id.toolbar);

        setSupportActionBar(tbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("All");

        FragmentTransaction ftrans = getFragmentManager().beginTransaction();
        ftrans.replace(R.id.fragment_container, new ProfileListFragment()).addToBackStack(null);
        ftrans.commit();
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
                return true;
            case android.R.id.home:
                getSupportActionBar().setTitle("All");
                getFragmentManager().popBackStack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

