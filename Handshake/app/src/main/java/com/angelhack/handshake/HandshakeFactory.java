package com.angelhack.handshake;

import android.util.Log;

import retrofit.RestAdapter;

/**
 * @author curtiskroetsch
 */
public class HandshakeFactory {

    private static HandshakeAPI api;

    public static HandshakeAPI get() {
        if (api == null) {
            RestAdapter adapter = new RestAdapter.Builder()
                    .setEndpoint("http://handshakehacks.herokuapp.com/")
                    .setLog(new RestAdapter.Log() {
                        @Override
                        public void log(String message) {
                            Log.d("RETRO", message);
                        }
                    })
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();
            api = adapter.create(HandshakeAPI.class);
        }
        return api;
    }
}
