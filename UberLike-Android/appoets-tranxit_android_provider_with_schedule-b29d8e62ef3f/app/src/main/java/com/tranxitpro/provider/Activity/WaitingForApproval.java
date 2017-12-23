package com.tranxitpro.provider.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tranxitpro.provider.Helper.SharedHelper;
import com.tranxitpro.provider.Helper.URLHelper;
import com.tranxitpro.provider.Helper.XuberApplication;
import com.tranxitpro.provider.R;

import org.json.JSONObject;

import java.util.HashMap;

public class WaitingForApproval extends AppCompatActivity {
    Button logoutBtn;
    public Handler ha;
    private String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_waiting_for_approval);
        token = SharedHelper.getKey(WaitingForApproval.this, "access_token");
        logoutBtn = (Button)findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedHelper.putKey(WaitingForApproval.this,"loggedIn",getString(R.string.False));
                Intent mainIntent = new Intent(WaitingForApproval.this, BeginScreen.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);
                WaitingForApproval.this.finish();
            }
        });

        ha = new Handler();
        //check status every 3 sec
        ha.postDelayed(new Runnable() {
            @Override
            public void run() {
                //call function
                checkStatus();
                ha.postDelayed(this,2000);
            }
        },2000);
    }



    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    public void onBackPressed() {

    }

    private void checkStatus() {
        String url = URLHelper.base + "api/provider/trip";

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.e("CheckStatus",""+response.toString());
                //SharedHelper.putKey(context, "currency", response.optString("currency"));

                if (response.optString("account_status").equals("approved")) {
                    ha.removeMessages(0);
                    startActivity(new Intent(WaitingForApproval.this, MainActivity.class));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("Error", error.toString());

                if (error instanceof NoConnectionError) {
                    displayMessage(getString(R.string.oops_connect_your_internet));
                } else if (error instanceof NetworkError) {
                    displayMessage(getString(R.string.oops_connect_your_internet));
                } else if (error instanceof TimeoutError) {
                    checkStatus();
                }


            }
        }){
            @Override
            public java.util.Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                headers.put("Authorization","Bearer "+token);
                return headers;
            }
        };
        XuberApplication.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public void displayMessage(String toastString) {
        Toast.makeText(WaitingForApproval.this, toastString, Toast.LENGTH_SHORT).show();
    }

}
