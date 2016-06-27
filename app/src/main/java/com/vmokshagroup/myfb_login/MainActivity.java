package com.vmokshagroup.myfb_login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;



public class MainActivity extends AppCompatActivity {
    private LoginButton loginButton;
    //to manage the callbacks used in the app.
    private CallbackManager callbackManager;

    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    public static String age_range, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //intializing the SDK
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        //initialize the instance of CallbackManager
        callbackManager = CallbackManager.Factory.create();

        //call setContentView to set the layout defined above
        setContentView(R.layout.activity_main);

        //use findViewById to initialize the widgets.
        loginButton = (LoginButton) findViewById(R.id.login_button);

/*

        //to generate key hash, once the key is generated comment it
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
*/


        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                nextActivity(newProfile);
            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();

        //register callback object for facebook result
        callback = new FacebookCallback<LoginResult>() {

            //If the login attempt is successful, onSuccess is called.
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                // String accessToken ="EAACEdEose0cBAG68ZBv5cFNSS0g0YnpIIYcU2VjDv6PADcBr2Pvc7OUWP5oMtrZA2JIFcaHRZAHdpXgxzBJNemFsLPzy6ukShCG98oCxZC9vofDoN6RDG4wpAn9pU7mSmOC1yR43J1ZBDR6Hhd7TSW2OooD3J32wDFLD9YoSY5QZDZD";
                //accessToken.getPermissions();
                final Profile profile = Profile.getCurrentProfile();

                GraphRequest request = GraphRequest.newMeRequest(
                        accessToken,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                // Insert your code here
                                try {

                                    // String name = object.getString("name");
                                    // String userid = object.getString("userid");
                                    email = object.getString("email");
                                    age_range = object.getString("age_range");
                                    //String emailing = response.getJSONObject().getString("email");
                                    Log.d("all", "" + email + "" + age_range + "");

                                    nextActivity(profile);
                                    Toast.makeText(getApplicationContext(), "Logging in...", Toast.LENGTH_SHORT).show();
                                    //  Log.d("birth",userid+" "+birthday);



                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,birthday,age_range");
                request.setParameters(parameters);
                request.executeAsync();

            }
            //If the user cancels the login attempt, onCancel is called.
            @Override
            public void onCancel() {
            }
            //If an error occurs, onError is called.
            @Override
            public void onError(FacebookException e) {
            }
        };
        loginButton.setReadPermissions("user_friends");
        loginButton.setReadPermissions("user_birthday");
        loginButton.setReadPermissions("public_profile");
        loginButton.setReadPermissions("email");
        loginButton.setReadPermissions("user_birthday");
        loginButton.setReadPermissions("user_location");

        /*create a callback to handle the results of the login attempts and register it with the CallbackManager
        Custom callbacks should implement FacebookCallback.
        To register the custom callback, use the registerCallback method.*/
        loginButton.registerCallback(callbackManager, callback);

    }


    //Facebook login button
    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Profile profile = Profile.getCurrentProfile();
            nextActivity(profile);
        }

        @Override
        public void onCancel() {
        }

        @Override
        public void onError(FacebookException e) {
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Facebook login
        Profile profile = Profile.getCurrentProfile();
        nextActivity(profile);
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    protected void onStop() {
        super.onStop();
        //Facebook login
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }
/*Tapping the login button starts off a new Activity, which returns a result. To receive and handle the result,
    override the onActivityResult method of your Activity and pass its parameters to the onActivityResult
    method of CallbackManager. */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void nextActivity(Profile profile) {
        if (profile != null) {
            Intent main = new Intent(MainActivity.this, LoginActivity.class);
            main.putExtra("name", profile.getFirstName());
            main.putExtra("surname", profile.getLastName());
            main.putExtra("userid", profile.getId());
            // main.putExtra("linkuri",profile.getLinkUri());
            main.putExtra("imageUrl", profile.getProfilePictureUri(200, 200).toString());
            startActivity(main);
        }
    }
}
