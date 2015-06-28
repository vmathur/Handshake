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
import android.widget.Toast;
import android.widget.TextView;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends ActionBarActivity {

    private static final int PEBBLE_KEY_TYPE = 1;
    private static final int PEBBLE_KEY_CONTACTS = 2;
    private static final int PEBBLE_KEY_HANDSHAKE = 3;
    private static final int PEBBLE_KEY_SELECTED = 4;


    private static final String PEBBLE_TYPE_HANDSHAKE = "handshake";
    private static final String PEBBLE_TYPE_SELECTED = "selected_";

    private static String sSelectedId;
    private static List<PersonProfile> sPeople = new ArrayList<>();


    private static final UUID PEBBLE_APP_UUID = UUID.fromString("0456b648-c89d-4f90-898d-8cd87e1d78be");
    public final static String TAG  = MainActivity.class.getName();
    private Toolbar tbar;
    private List<String> addresses = new ArrayList<String>();
    private PersonProfile youSelfi;

    private static PersonProfile findPerson(String id ) {
        for (PersonProfile p : sPeople) {
            if (p.user_id.equals(id)) {
                return p;
            }
        }
        return null;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        youSelfi = new PersonProfile("first", "last", "title", "inid", "pic", "macaddr" , "email", "phone", "location");

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
                String msg = data.getString(0);

                if (PEBBLE_TYPE_HANDSHAKE.equals(msg)) {
                    final PersonProfile profile = findPerson(sSelectedId);
                    if (profile != null) {
                        Toast.makeText(getApplicationContext(), "Handshook with " + profile.first_name, Toast.LENGTH_SHORT).show();
                        // HANDLE HANDSHAKE
                        HandshakeFactory.get().addition(SignupActivity.ME.user_id, sSelectedId, new Callback<Void>() {
                            @Override
                            public void success(Void aVoid, Response response) {
                                Toast.makeText(getApplicationContext(), "Connected with " + profile.first_name, Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Toast.makeText(getApplicationContext(), "Failed to connect with " + profile.first_name, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    sSelectedId = msg;

                }


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

                if (addresses.isEmpty()) {
                    BluetoothAdapter.getDefaultAdapter().startDiscovery();
                    return;
                }

                StringBuilder builder = new StringBuilder();

                if (addresses.size() > 1) {
                    for (int i = 0; i < addresses.size() - 1; i++) {
                        builder.append(addresses.get(i)).append(", ");
                    }
                }
                builder.append(addresses.get(addresses.size() - 1));
                String request = builder.toString();

                HandshakeFactory.get().register(SignupActivity.ME.user_id, request, new Callback<List<PersonProfile>>() {
                    @Override
                    public void success(List<PersonProfile> aVoid, Response response) {
                        Log.i(TAG, "Got profiles of devices");
                        for (PersonProfile p : aVoid) {
                            Log.d(TAG, "FOUND: " + p.getFullName());
                            Toast.makeText(getApplicationContext(), "FOUND: " + p.getFullName(), Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.i(TAG, "FAILED getting profiles of devices");
                        Toast.makeText(getApplicationContext(), "FAILED getting profiles of devices", Toast.LENGTH_SHORT).show();
                        //BluetoothAdapter.getDefaultAdapter().startDiscovery();
                    }
                });

                addresses.clear();
                BluetoothAdapter.getDefaultAdapter().startDiscovery();


                //delay
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

