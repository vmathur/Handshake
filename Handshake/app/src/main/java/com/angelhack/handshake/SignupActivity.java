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

import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author curtiskroetsch
 */
public class SignupActivity extends Activity {

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

    private void showPackHask() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.angelhack.handshake",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                Log.d("BLUE", "packageName = " + info.packageName);
                Log.d("BLUE", "hashText = " + Base64.encodeToString(md.digest(), Base64.NO_WRAP));
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.d("SIGN", e1.getMessage(), e1);
        } catch (NoSuchAlgorithmException e2) {
            Log.d("SIGN", e2.getMessage(), e2);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
    }

    private void setUpdateState() {
        startActivity(new Intent(this, MainActivity.class));
    }

    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.W_SHARE);
    }
}
