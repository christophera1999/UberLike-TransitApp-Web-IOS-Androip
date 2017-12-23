package com.tranxitpro.provider.Activity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.splunk.mint.Mint;
import com.tranxitpro.provider.Helper.ConnectionHelper;
import com.tranxitpro.provider.Helper.CustomDialog;
import com.tranxitpro.provider.Helper.SharedHelper;
import com.tranxitpro.provider.Helper.URLHelper;
import com.tranxitpro.provider.Helper.XuberApplication;
import com.tranxitpro.provider.R;
import com.tranxitpro.provider.Utilities.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


public class BeginScreen extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int REQ_SIGN_IN_REQUIRED = 100;
    TextView enter_ur_mailID, connectSocial;

    /*----------Facebook Login---------------*/
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    String UserName, UserEmail, result, FBUserID, FBImageURLString;
    //Button fb_login, go_login;
    JSONObject json;
    ConnectionHelper helper;
    Boolean isInternet;
    CustomDialog customDialog;
    public Context context = BeginScreen.this;
    String TAG = "BEGINSCREEN";
    String device_token, device_UDID;
    Utilities utils = new Utilities();

    /*----------Google Login---------------*/
    GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        Mint.setApplicationEnvironment(Mint.appEnvironmentStaging);
        Mint.initAndStartSession(this.getApplication(), "2753c547");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_begin);
        helper = new ConnectionHelper(BeginScreen.this);
        isInternet = helper.isConnectingToInternet();

        GetToken();

        //fb_login = (Button) findViewById(R.id.fblogin);
        //go_login = (Button) findViewById(R.id.google_login);



                 /*----------Google Login---------------*/

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //taken from google api console (Web api client id)
//                .requestIdToken("795253286119-4b9tcj3helfg4k4gsl307dbrt4cfchsk.apps.googleusercontent.com")
                .requestEmail()
                .build();


        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
/*
        go_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
*/



                  /*----------Facebook Login---------------*/

        callbackManager = CallbackManager.Factory.create();
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        enter_ur_mailID = (TextView) findViewById(R.id.enter_ur_mailID);
        enter_ur_mailID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(BeginScreen.this, ActivityEmail.class);
                //   mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);
                // BeginScreen.this.finish();
            }
        });

        connectSocial = (TextView) findViewById(R.id.connect_social);
        connectSocial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(BeginScreen.this, AccActivity.class);
                startActivity(mainIntent);
            }
        });

        try {
            @SuppressLint("PackageManagerGetSignatures") PackageInfo info = getPackageManager().getPackageInfo(
                    "com.tranxitpro.provider",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException ignored) {

        }

//        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));

/*
        fb_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternet) {
                    LoginManager.getInstance().logInWithReadPermissions(
                            BeginScreen.this,
                            Arrays.asList("email"));


                    LoginManager.getInstance().registerCallback(callbackManager,
                            new FacebookCallback<LoginResult>() {


                                public void onSuccess(LoginResult loginResult) {
                                    if (AccessToken.getCurrentAccessToken() != null) {
                                        Log.i("loginresult", "" + loginResult.getAccessToken().getToken());
                                        SharedHelper.putKey(BeginScreen.this, "accessToken", loginResult.getAccessToken().getToken());
                                        login(loginResult.getAccessToken().getToken(), URLHelper.FACEBOOK_LOGIN, "facebook");
                                    }

                                }

                                @Override
                                public void onCancel() {
                                    // App code
                                }

                                @Override
                                public void onError(FacebookException exception) {
                                    // App code

                                    Log.d(TAG, "onError: " + exception.getMessage());
                                }
                            });
                } else {
                    //mProgressDialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(BeginScreen.this);
                    builder.setMessage("Check your Internet").setCancelable(false);
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent NetworkAction = new Intent(Settings.ACTION_SETTINGS);
                            startActivity(NetworkAction);

                        }
                    });
                    builder.show();
                }


            }
        });
*/

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            handleSignInResult(result);
        }

    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("Beginscreen", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.d("Google", "display_name:" + acct.getDisplayName());
            Log.d("Google", "mail:" + acct.getEmail());
            Log.d("Google", "photo:" + acct.getPhotoUrl());
            Log.d("Google", "Token:" + acct.getIdToken());
            new RetrieveTokenTask().execute(acct.getEmail());


        }
    }


    private class RetrieveTokenTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String accountName = params[0];
            String scopes = "oauth2:profile email";
            String token = null;
            try {
                token = GoogleAuthUtil.getToken(getApplicationContext(), accountName, scopes);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            } catch (UserRecoverableAuthException e) {
                startActivityForResult(e.getIntent(), REQ_SIGN_IN_REQUIRED);
            } catch (GoogleAuthException e) {
                Log.e(TAG, e.getMessage());
            }
            return token;
        }

        @Override
        protected void onPostExecute(String accessToken) {
            super.onPostExecute(accessToken);
            Log.e("Token", accessToken);

            login(accessToken, URLHelper.GOOGLE_LOGIN, "google");

        }
    }

    public void login(String accesstoken, String URL, final String Loginby) {

        customDialog = new CustomDialog(context);
        customDialog.setCancelable(false);
        customDialog.show();
        final JsonObject json = new JsonObject();
        json.addProperty("device_type", "android");
        json.addProperty("device_token", device_token);
        json.addProperty("accessToken", accesstoken);
        json.addProperty("device_id", device_UDID);
        json.addProperty("login_by", Loginby);

        Ion.with(BeginScreen.this)
                .load(URL)
                .addHeader("X-Requested-With", "XMLHttpRequest")
//                .addHeader("Authorization",""+SharedHelper.getKey(context, "token_type")+" "+SharedHelper.getKey(context, "access_token"))
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error

                        //Log.v(Loginby+"_Response",result.toString());
                        customDialog.dismiss();
                        if (result != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(result.toString());
                                String status = jsonObject.optString("status");
                                if (status.equalsIgnoreCase("true")) {
                                    SharedHelper.putKey(BeginScreen.this, "token_type", jsonObject.optString("token_type"));
                                    SharedHelper.putKey(BeginScreen.this, "access_token", jsonObject.optString("access_token"));
                                    SharedHelper.putKey(context, "currency", jsonObject.optString("currency"));
                                    getProfile();

                                } else {
                                    startActivity(new Intent(BeginScreen.this, MainActivity.class));
                                    finish();
                                }

                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }


                        // onBackPressed();
                    }
                });
    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void GetToken() {
        try {
            if (!SharedHelper.getKey(context, "device_token").equals("") && SharedHelper.getKey(context, "device_token") != null) {
                device_token = SharedHelper.getKey(context, "device_token");
                utils.print(TAG, "GCM Registration Token: " + device_token);
            } else {
                device_token = "COULD NOT GET FCM TOKEN";
                utils.print(TAG, "Failed to complete token refresh: " + device_token);
            }
        } catch (Exception e) {
            device_token = "COULD NOT GET FCM TOKEN";
            utils.print(TAG, "Failed to complete token refresh");
        }

        try {
            device_UDID = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            utils.print(TAG, "Device UDID:" + device_UDID);
        } catch (Exception e) {
            device_UDID = "COULD NOT GET UDID";
            e.printStackTrace();
            utils.print(TAG, "Failed to complete device UDID");
        }
    }


    public void GoToBeginActivity() {
        Intent mainIntent = new Intent(context, BeginScreen.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        finish();
    }

    public void displayMessage(String toastString) {
        Log.e("displayMessage", "" + toastString);
        Snackbar.make(getCurrentFocus(), toastString, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

    public void GoToMainActivity() {
        Intent mainIntent = new Intent(context, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
        finish();
    }


    public void getProfile() {

        if (isInternet) {

            customDialog = new CustomDialog(context);
            customDialog.setCancelable(false);
            customDialog.show();
            JSONObject object = new JSONObject();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URLHelper.USER_PROFILE_API, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    customDialog.dismiss();
                    utils.print("GetProfile", response.toString());
                    SharedHelper.putKey(context, "id", response.optString("id"));
                    SharedHelper.putKey(context, "first_name", response.optString("first_name"));
                    SharedHelper.putKey(context, "last_name", response.optString("last_name"));
                    SharedHelper.putKey(context, "email", response.optString("email"));
                    if (response.optString("avatar").startsWith("http"))
                        SharedHelper.putKey(context, "picture", response.optString("avatar"));
                    else
                        SharedHelper.putKey(context, "picture", URLHelper.base + "storage/" + response.optString("avatar"));
                    SharedHelper.putKey(context, "gender", "" + response.optString("gender"));
                    SharedHelper.putKey(context, "sos", response.optString("sos"));
                    SharedHelper.putKey(context, "mobile", response.optString("mobile"));
                    SharedHelper.putKey(context, "approval_status", response.optString("status"));
                    SharedHelper.putKey(context, "loggedIn", getString(R.string.True));
                    if (response.optJSONObject("service") != null) {
                        JSONObject service = response.optJSONObject("service");
                        JSONObject serviceType = service.optJSONObject("service_type");
                        SharedHelper.putKey(context, "service", serviceType.optString("name"));
                    }


                    GoToMainActivity();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    customDialog.dismiss();
                    String json = null;
                    String Message;
                    NetworkResponse response = error.networkResponse;
                    if (response != null && response.data != null) {
                        try {
                            JSONObject errorObj = new JSONObject(new String(response.data));

                            if (response.statusCode == 400 || response.statusCode == 405 || response.statusCode == 500) {
                                displayMessage(getString(R.string.something_went_wrong));
                            } else if (response.statusCode == 401) {
                                SharedHelper.putKey(context, "loggedIn", getString(R.string.False));
                                GoToBeginActivity();
                            } else if (response.statusCode == 422) {
                                json = XuberApplication.trimMessage(new String(response.data));
                                if (json != "" && json != null) {
                                    displayMessage(json);
                                } else {
                                    displayMessage(getString(R.string.please_try_again));
                                }

                            } else if (response.statusCode == 503) {
                                displayMessage(getString(R.string.server_down));
                            } else {
                                displayMessage(getString(R.string.please_try_again));
                            }

                        } catch (Exception e) {
                            displayMessage(getString(R.string.something_went_wrong));
                        }

                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("X-Requested-With", "XMLHttpRequest");
                    headers.put("Authorization", "Bearer " + SharedHelper.getKey(context, "access_token"));
                    return headers;
                }
            };

            XuberApplication.getInstance().addToRequestQueue(jsonObjectRequest);
        } else {
            displayMessage(getString(R.string.something_went_wrong_net));
        }

    }

    public void RequestData() {


        if (isInternet) {
            GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {

                    // if (isInternetPresent) {

                    Log.e("response", "" + response);
                    json = response.getJSONObject();
                    Log.e("FB JSON", "" + json);

                    try {
                        if (json != null) {
                            UserName = json.optString("name");
                            UserEmail = json.optString("email");
                            com.facebook.Profile profile = com.facebook.Profile.getCurrentProfile();
                            FBUserID = profile.getId();
                            Log.e("FBUserID", "" + FBUserID);
                            URL image_value = new URL("https://graph.facebook.com/" + FBUserID + "/picture?type=large");
                            FBImageURLString = image_value.toString();
                            Log.e("Connected FB", "" + UserName);
                            Log.e("Connected FB", "" + UserEmail);
                            Log.e("FBUserPhoto FB", FBImageURLString);

//                            fblogin()
                        } else {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,link,email,picture");
            request.setParameters(parameters);
            request.executeAsync();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(BeginScreen.this);
            builder.setMessage("Check your Internet").setCancelable(false);
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent NetworkAction = new Intent(Settings.ACTION_SETTINGS);
                    startActivity(NetworkAction);

                }
            });
            builder.show();
        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}