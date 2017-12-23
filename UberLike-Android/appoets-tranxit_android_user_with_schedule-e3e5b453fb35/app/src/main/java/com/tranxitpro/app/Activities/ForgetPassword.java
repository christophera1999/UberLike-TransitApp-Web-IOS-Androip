package com.tranxitpro.app.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tranxitpro.app.Helper.ConnectionHelper;
import com.tranxitpro.app.Helper.CustomDialog;
import com.tranxitpro.app.Helper.SharedHelper;
import com.tranxitpro.app.Helper.URLHelper;
import com.tranxitpro.app.R;
import com.tranxitpro.app.TranxitApplication;
import com.tranxitpro.app.Utils.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.tranxitpro.app.TranxitApplication.trimMessage;

/**
 * Created by jayakumar on 31/01/17.
 */

public class ForgetPassword extends AppCompatActivity {
    public Context context = ForgetPassword.this;
    ImageView nextICON, backArrow;
    TextView titleText;
    TextInputLayout newPasswordLayout,confirmPasswordLayout,OtpLay;
    EditText newPassowrd,confirmPassword,OTP;
    EditText email;
    CustomDialog customDialog;
    String validation="",str_newPassword,str_confirmPassword,id,str_email="",str_otp,server_opt;
    ConnectionHelper helper;
    Boolean isInternet;
    TextView note_txt;
    Boolean fromActivity = false;

    Utilities utils = new Utilities();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        try{
            Intent intent = getIntent();
            if(intent != null){
                if(getIntent().getExtras().getBoolean("isFromMailActivity")){
                    fromActivity = true;
                }else if(!getIntent().getExtras().getBoolean("isFromMailActivity")){
                    fromActivity = false;
                }else {
                    fromActivity = false;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            fromActivity = false;
        }
        findViewById();

        if (Build.VERSION.SDK_INT > 15) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        nextICON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_email=email.getText().toString();
                if(validation.equalsIgnoreCase("")){
                    if (email.getText().toString().equals("")) {
                        displayMessage(getString(R.string.email_validation));
                    } else if (!Utilities.isValidEmail(email.getText().toString())) {
                        displayMessage(getString(R.string.not_valid_email));
                    }else{
                        if (isInternet) {
                            forgetPassword();
                        } else {
                            displayMessage(getString(R.string.something_went_wrong_net));
                        }

                    }
                }else {
                    str_newPassword=newPassowrd.getText().toString();
                    str_confirmPassword=confirmPassword.getText().toString();
                    str_otp=OTP.getText().toString();
                    if (str_otp.equals("") ) {
                        displayMessage(getString(R.string.otp_validation));
                    }else  if (!str_otp.equalsIgnoreCase(server_opt)){
                        displayMessage(getString(R.string.inncorrect_otp));
                    } else if (str_newPassword.equals("") || str_newPassword.equalsIgnoreCase(getString(R.string.new_password))) {
                        displayMessage(getString(R.string.password_validation));
                    }else if (str_newPassword.length()<=6) {
                        displayMessage(getString(R.string.password_size));
                    }else if(str_confirmPassword.equals("") || str_confirmPassword.equalsIgnoreCase(getString(R.string.confirm_password))||!str_newPassword.equalsIgnoreCase(str_confirmPassword)){
                        displayMessage(getString(R.string.confirm_password_validation));
                    }else if (str_confirmPassword.length()<=6) {
                        displayMessage(getString(R.string.password_size));
                    }else {
                        if (isInternet) {
                            resetpassword();
                        } else {
                            displayMessage(getString(R.string.something_went_wrong_net));
                        }
                    }
                }

            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent mainIntent = new Intent(ForgetPassword.this, ActivityPassword.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);
                ForgetPassword.this.finish();*/
               onBackPressed();
            }
        });

    }

    private void forgetPassword() {
        customDialog = new CustomDialog(ForgetPassword.this);
        customDialog.setCancelable(false);
        if (customDialog != null)
        customDialog.show();
        JSONObject object = new JSONObject();
        try {

            object.put("email", str_email);
            Log.e("ForgetPassword",""+object);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.FORGET_PASSWORD, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if ((customDialog != null)&& (customDialog.isShowing()))
                customDialog.dismiss();
                Log.v("ForgetPasswordResponse",response.toString());
                validation="reset";
                JSONObject userObject=response.optJSONObject("user");
                id= String.valueOf(userObject.optInt("id"));
                server_opt = userObject.optString("otp");
                email.setFocusable(false);
                email.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
                email.setClickable(false);
                titleText.setText(R.string.reset_password);
                newPasswordLayout.setVisibility(View.VISIBLE);
                confirmPasswordLayout.setVisibility(View.VISIBLE);
                OtpLay.setVisibility(View.VISIBLE);
                note_txt.setVisibility(View.VISIBLE);
                OTP.performClick();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if ((customDialog != null)&& (customDialog.isShowing()))
                customDialog.dismiss();
                String json = null;
                String Message;
                NetworkResponse response = error.networkResponse;
                Log.e("MyTest",""+error);
                Log.e("MyTestError",""+error.networkResponse);
                Log.e("MyTestError1",""+response.statusCode);
                if(response != null && response.data != null){
                    try {
                        JSONObject errorObj = new JSONObject(new String(response.data));

                        if(response.statusCode == 400 || response.statusCode == 405 || response.statusCode == 500){
                            try{
                                displayMessage(errorObj.optString("message"));
                            }catch (Exception e){
                                displayMessage("Something went wrong.");
                            }
                        }else if(response.statusCode == 401){
                            try{
                                if(errorObj.optString("message").equalsIgnoreCase("invalid_token")){
                                    refreshAccessToken("FORGOT_PASSWORD");
                                }else{
                                    displayMessage(errorObj.optString("message"));
                                }
                            }catch (Exception e){
                                displayMessage("Something went wrong.");
                            }

                        }else if(response.statusCode == 422){

                            json = trimMessage(new String(response.data));
                            if(json !="" && json != null) {
                                displayMessage(json);
                            }else{
                                displayMessage("Please try again.");
                            }

                        }else{
                            displayMessage("Please try again.");
                        }

                    }catch (Exception e){
                        displayMessage("Something went wrong.");
                    }

                } else {
                    if (error instanceof NoConnectionError) {
                        displayMessage(getString(R.string.oops_connect_your_internet));
                    } else if (error instanceof NetworkError) {
                        displayMessage(getString(R.string.oops_connect_your_internet));
                    } else if (error instanceof TimeoutError) {
                        forgetPassword();
                    }
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                return headers;
            }
        };

        TranxitApplication.getInstance().addToRequestQueue(jsonObjectRequest);

    }


    private void resetpassword() {
        customDialog = new CustomDialog(ForgetPassword.this);
        customDialog.setCancelable(false);
        if (customDialog != null)
        customDialog.show();
        JSONObject object = new JSONObject();
        try {
            object.put("id", id);
            object.put("password", str_newPassword);
            object.put("password_confirmation", str_confirmPassword);
            Log.e("ResetPassword",""+object);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.RESET_PASSWORD, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if ((customDialog != null)&& (customDialog.isShowing()))
                customDialog.dismiss();
                Log.v("ResetPasswordResponse",response.toString());
                try {
                    JSONObject object1 = new JSONObject(response.toString());
                    Toast.makeText(context,object1.optString("message"),Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ForgetPassword.this,ActivityEmail.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if ((customDialog != null)&& (customDialog.isShowing()))
                customDialog.dismiss();
                String json = null;
                String Message;
                NetworkResponse response = error.networkResponse;
                Log.e("MyTest",""+error);
                Log.e("MyTestError",""+error.networkResponse);
                Log.e("MyTestError1",""+response.statusCode);
                if(response != null && response.data != null){
                    try {
                        JSONObject errorObj = new JSONObject(new String(response.data));

                        if(response.statusCode == 400 || response.statusCode == 405 || response.statusCode == 500){
                            try{
                                displayMessage(errorObj.optString("message"));
                            }catch (Exception e){
                                displayMessage("Something went wrong.");
                            }
                        }else if(response.statusCode == 401){
                            try{
                                if(errorObj.optString("message").equalsIgnoreCase("invalid_token")){
                                    refreshAccessToken("RESET_PASSWORD");
                                }else{
                                    displayMessage(errorObj.optString("message"));
                                }
                            }catch (Exception e){
                                displayMessage("Something went wrong.");
                            }

                        }else if(response.statusCode == 422){

                            json = trimMessage(new String(response.data));
                            if(json !="" && json != null) {
                                displayMessage(json);
                            }else{
                                displayMessage("Please try again.");
                            }

                        }else{
                            displayMessage("Please try again.");
                        }

                    }catch (Exception e){
                        displayMessage("Something went wrong.");
                    }


                } else {
                    if (error instanceof NoConnectionError) {
                        displayMessage(getString(R.string.oops_connect_your_internet));
                    } else if (error instanceof NetworkError) {
                        displayMessage(getString(R.string.oops_connect_your_internet));
                    } else if (error instanceof TimeoutError) {
                        resetpassword();
                    }
                }

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                return headers;
            }
        };

        TranxitApplication.getInstance().addToRequestQueue(jsonObjectRequest);

    }

    public void findViewById(){
        email = (EditText)findViewById(R.id.email);
        nextICON = (ImageView) findViewById(R.id.nextIcon);
        backArrow = (ImageView) findViewById(R.id.backArrow);
        titleText = (TextView) findViewById(R.id.title_txt);
        note_txt = (TextView) findViewById(R.id.note);
        newPassowrd = (EditText) findViewById(R.id.new_password);
        OTP = (EditText) findViewById(R.id.otp);
        confirmPassword = (EditText) findViewById(R.id.confirm_password);
        confirmPasswordLayout = (TextInputLayout) findViewById(R.id.confirm_password_lay);
        OtpLay = (TextInputLayout) findViewById(R.id.otp_lay);
        newPasswordLayout = (TextInputLayout) findViewById(R.id.new_password_lay);
        helper = new ConnectionHelper(context);
        isInternet = helper.isConnectingToInternet();
        str_email= SharedHelper.getKey(ForgetPassword.this,"email");
        email.setText(str_email);

    }

    public void displayMessage(String toastString){
        Log.e("displayMessage",""+toastString);
        Snackbar.make(getCurrentFocus(),toastString, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if(fromActivity){
            Intent mainIntent = new Intent(ForgetPassword.this, ActivityEmail.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);
            ForgetPassword.this.finish();
        }else {
            Intent mainIntent = new Intent(ForgetPassword.this, ActivityPassword.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);
            ForgetPassword.this.finish();
        }
    }

    private void refreshAccessToken(final String tag) {

        JSONObject object = new JSONObject();
        try {

            object.put("grant_type", "refresh_token");
            object.put("client_id", URLHelper.client_id);
            object.put("client_secret", URLHelper.client_secret);
            object.put("refresh_token", SharedHelper.getKey(context, "refresh_token"));
            object.put("scope", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.login, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                utils.print("SignUpResponse", response.toString());
                SharedHelper.putKey(context, "access_token", response.optString("access_token"));
                SharedHelper.putKey(context, "refresh_token", response.optString("refresh_token"));
                SharedHelper.putKey(context, "token_type", response.optString("token_type"));
                if (tag.equalsIgnoreCase("FORGOT_PASSWORD")) {
                    forgetPassword();
                } else {
                    resetpassword();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String json = "";
                NetworkResponse response = error.networkResponse;

                if (response != null && response.data != null) {
                    SharedHelper.putKey(context, "loggedIn", getString(R.string.False));
                    utils.GoToBeginActivity(ForgetPassword.this);
                } else {
                    if (error instanceof NoConnectionError) {
                        displayMessage(getString(R.string.oops_connect_your_internet));
                    } else if (error instanceof NetworkError) {
                        displayMessage(getString(R.string.oops_connect_your_internet));
                    } else if (error instanceof TimeoutError) {
                        refreshAccessToken(tag);
                    }
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                return headers;
            }
        };

        TranxitApplication.getInstance().addToRequestQueue(jsonObjectRequest);
    }

}
