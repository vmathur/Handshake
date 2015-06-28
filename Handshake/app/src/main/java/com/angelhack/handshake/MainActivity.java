package com.angelhack.handshake;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Context;
import android.widget.Toast;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends ActionBarActivity {

    private static final UUID PEBBLE_APP_UUID = UUID.fromString("0456b648-c89d-4f90-898d-8cd87e1d78be");
    public final static String TAG  = MainActivity.class.getName();
    private Toolbar tbar;
    private List<String> addresses = new ArrayList<String>();

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
        ftrans.add(R.id.fragment_container, new ProfileListFragment());
        ftrans.commit();

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
        startActivity(discoverableIntent);

        PebbleKit.registerReceivedDataHandler(this, new PebbleKit.PebbleDataReceiver(PEBBLE_APP_UUID) {
            @Override
            public void receiveData(final Context context, final int transactionId, final PebbleDictionary data) {
                Log.i(getLocalClassName(), "Received value=" + data.getString(0) + " for key: 0");
                Toast.makeText(getApplicationContext(), "Pebble sent: " + data.getString(0), Toast.LENGTH_SHORT).show();
                PebbleKit.sendAckToPebble(getApplicationContext(), transactionId);
            }
        });

        
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
                return true;
            case R.id.profileListSection:
                FragmentTransaction ftrans = getFragmentManager().beginTransaction();
                ftrans.add(R.id.fragment_container, new ProfileViewerFragment());
                ftrans.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

