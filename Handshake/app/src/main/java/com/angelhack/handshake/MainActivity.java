package com.angelhack.handshake;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.internal.view.menu.ActionMenuItemView;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Context;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {
    public final static String TAG  = "Log";
    private Toolbar tbar;
    private List<String> addresses = new ArrayList<String>();
    private PersonProfile youSelfi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        youSelfi = new PersonProfile("first", "last", "title", "inid", "pic", "email", "phone", "location");

        this.tbar = (Toolbar)findViewById(R.id.toolbar);

        setSupportActionBar(tbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("All");

        FragmentTransaction ftrans = getFragmentManager().beginTransaction();
        ftrans.replace(R.id.fragment_container, new ProfileListFragment()).addToBackStack(null);
        ftrans.commit();

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
        startActivity(discoverableIntent);
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String address = device.getAddress();
                String name = device.getName();
                int  rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);
                Log.d(TAG, "Address: " + address + " name = " + name + " RSSI: " + rssi);

                //if(rssi > -80){
                addresses.add(address);
                //}

            }
        }
    };

    private final BroadcastReceiver loopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Log.d(TAG, "Discovery finished");
                Log.d(TAG, "Devices: " + addresses.toString());
                addresses.clear();

                //delay
                BluetoothAdapter.getDefaultAdapter().startDiscovery();
            }

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter findFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        IntentFilter loopFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(receiver, findFilter);
        registerReceiver(loopReceiver, loopFilter);

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String address = bluetoothAdapter.getAddress();

        Log.d(TAG, "Bluetooth Address is: " + address);

        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }

        boolean started = bluetoothAdapter.startDiscovery();
        Log.d(TAG, started ? "STARTED" : "IDIOT");
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        unregisterReceiver(loopReceiver);
        super.onDestroy();
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
                ftrans.add(R.id.fragment_container, ProfileViewerFragment.create(youSelfi, true)).addToBackStack(null);
                ftrans.commit();
                return true;
            case R.id.editProfile:
                ((EditText)findViewById(R.id.phonenum)).setInputType(InputType.TYPE_CLASS_PHONE);
                ((EditText)findViewById(R.id.email)).setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                ((EditText)findViewById(R.id.location)).setInputType(InputType.TYPE_CLASS_TEXT);
                ((TextView)findViewById(R.id.add)).setVisibility(View.VISIBLE);
                ((TextView)findViewById(R.id.delete)).setVisibility(View.VISIBLE);
                ((ActionMenuItemView)findViewById(R.id.editProfile)).getItemData().setVisible(false);
                return true;
            case android.R.id.home:
                getSupportActionBar().setTitle("All");
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getFragmentManager().popBackStack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

