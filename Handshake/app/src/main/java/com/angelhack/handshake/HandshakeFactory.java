package com.angelhack.handshake;

import retrofit.RestAdapter;

/**
 * @author curtiskroetsch
 */
public class HandshakeFactory {

    private static HandshakeAPI api;

    public static HandshakeAPI get() {
        if (api == null) {
            RestAdapter adapter = new RestAdapter.Builder()
                    .setEndpoint("")
                    .build();
            api = adapter.create(HandshakeAPI.class);
        }
        return api;
    }
}
