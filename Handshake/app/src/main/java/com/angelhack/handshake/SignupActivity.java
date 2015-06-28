package com.angelhack.handshake;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * @author curtiskroetsch
 */
public class SignupActivity extends Activity {

    private static final String TAG = SignupActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        Button button = (Button) findViewById(R.id.signin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showPackHask();
                onSignInClicked();
            }
        });
    }

    private void onSignInClicked() {
        LISessionManager.getInstance(getApplicationContext()).init(this, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {
                setUpdateState();
                Toast.makeText(getApplicationContext(), "success" + LISessionManager.getInstance(getApplicationContext()).getSession().getAccessToken().toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAuthError(LIAuthError error) {
                setUpdateState();
                Toast.makeText(getApplicationContext(), "failed " + error.toString(), Toast.LENGTH_LONG).show();
            }
        }, true);
    }

    private void onApiSuccess(JSONObject json) {
        Log.d(TAG, json.toString());
        PersonProfile me;
        try {
            String fName = json.getString("firstName");
            String lName = json.getString("lastName");
            String tagline = json.getString("headline");
            String id = json.getString("id");
            String picUrl = json.getString("pictureUrl");

            Log.d(TAG, "fName = " + fName + ", lName = " + lName + ", tagline = " + tagline + ", id = " + id + ", picUrl = " + picUrl);

             me = new PersonProfile(fName, lName, tagline, id, picUrl);

        } catch (JSONException e) {
            onApiError("Could not parse data");
            return;
        }

        HandshakeAPI api = HandshakeFactory.getHandshakeAPI();
        api.createUser(me, new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {
                Toast.makeText(getApplicationContext(), "SUCCESSFULLY CREATED PROFILE", Toast.LENGTH_SHORT);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "FAILED TO CREATE PROFILE", Toast.LENGTH_SHORT);
            }
        });
    }

    private void onApiError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
    }

    private void setUpdateState() {

        String url = "https://api.linkedin.com/v1/people/~:(id,firstName,lastName,headline,picture-url,email-address)?format=json";

        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(this, url, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse apiResponse) {
                SignupActivity.this.onApiSuccess(apiResponse.getResponseDataAsJson());
            }

            @Override
            public void onApiError(LIApiError LIApiError) {
                SignupActivity.this.onApiError("Could not grab user profile");
            }
        });

    }

    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }
}
