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

    private static final int PEBBLE_KEY_TYPE = 1;
    private static final int PEBBLE_KEY_CONTACTS = 2;
    private static final String PEBBLE_TYPE_HANDSHAKE = "handshake";
    private static final String PEBBLE_TYPE_CONTACTS = "contacts";


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
        ftrans.replace(R.id.fragment_container, new ProfileListFragment()).addToBackStack(null);
        ftrans.commit();

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
        startActivity(discoverableIntent);

        PebbleKit.registerReceivedDataHandler(this, new PebbleKit.PebbleDataReceiver(PEBBLE_APP_UUID) {
            @Override
            public void receiveData(final Context context, final int transactionId, final PebbleDictionary data) {
                Log.i(getLocalClassName(), "Received value=" + data.getString(PEBBLE_KEY_TYPE) + " for key: 0");
                Toast.makeText(getApplicationContext(), "Pebble sent: " + data.getString(0), Toast.LENGTH_SHORT).show();
                PebbleKit.sendAckToPebble(getApplicationContext(), transactionId);
            }
        });

        List<PersonProfile> profiles = new ArrayList<>();
        PebbleDictionary dict = new PebbleDictionary();

        for (int i = 0; i < 5; i++) {
            profiles.add(new PersonProfile("Curtis", "Kroetsch", "Awesome guy", "curt" + i, "www.google.ca", BluetoothAdapter.getDefaultAdapter().getAddress()));
        }

        int index = 0;
        for (PersonProfile profile : profiles) {
            dict.addString(index, profile.getUser_id());
            dict.addString(index+1, profile.getFullName());
            dict.addString(index+2, profile.getTag_line());
            index += 3;
        }

        PebbleKit.registerReceivedAckHandler(getApplicationContext(), new PebbleKit.PebbleAckReceiver(PEBBLE_APP_UUID) {

            @Override
            public void receiveAck(Context context, int transactionId) {
                Log.i(getLocalClassName(), "Received ack for transaction " + transactionId);
            }

        });

        PebbleKit.registerReceivedNackHandler(getApplicationContext(), new PebbleKit.PebbleNackReceiver(PEBBLE_APP_UUID) {

            @Override
            public void receiveNack(Context context, int transactionId) {
                Log.i(getLocalClassName(), "Received nack for transaction " + transactionId);
            }

        });


        PebbleKit.sendDataToPebble(this, PEBBLE_APP_UUID, dict);
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

