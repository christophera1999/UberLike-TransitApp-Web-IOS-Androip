package com.tranxitpro.app.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.tranxitpro.app.Helper.ConnectionHelper;
import com.tranxitpro.app.Helper.CustomDialog;
import com.tranxitpro.app.Helper.SharedHelper;
import com.tranxitpro.app.Helper.URLHelper;
import com.tranxitpro.app.Models.Driver;
import com.tranxitpro.app.R;
import com.tranxitpro.app.TranxitApplication;
import com.tranxitpro.app.Utils.MyBoldTextView;
import com.tranxitpro.app.Utils.MyButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.tranxitpro.app.TranxitApplication.trimMessage;

public class HistoryDetails extends AppCompatActivity {

    public JSONObject jsonObject;
    Activity activity;
    Context context;
    Boolean isInternet;
    ConnectionHelper helper;
    CustomDialog customDialog;
    MyBoldTextView tripAmount;
    MyBoldTextView tripDate;
    MyBoldTextView paymentType;
    MyBoldTextView booking_id;
    MyBoldTextView tripComments;
    MyBoldTextView tripProviderName;
    MyBoldTextView tripSource;
    MyBoldTextView lblTotalPrice;
    MyBoldTextView lblBookingID;
    MyBoldTextView tripDestination;
    MyBoldTextView lblTitle;
    MyBoldTextView lblBasePrice;
    MyBoldTextView lblDistancePrice;
    MyBoldTextView lblTaxPrice;
    ImageView tripImg, tripProviderImg, paymentTypeImg;
    RatingBar tripProviderRating;
    LinearLayout sourceAndDestinationLayout, lnrComments, lnrUpcomingLayout;
    View viewLayout;
    ImageView backArrow;
    LinearLayout parentLayout;
    LinearLayout profileLayout;
    LinearLayout lnrInvoice, lnrInvoiceSub;
    String tag = "";
    MyButton btnCancelRide;
    Driver driver;
    String reason = "";

    Button btnViewInvoice, btnCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_details);
        findViewByIdAndInitialize();
        try {
            Intent intent = getIntent();
            String post_details = intent.getStringExtra("post_value");
            tag = intent.getStringExtra("tag");
            jsonObject = new JSONObject(post_details);
        } catch (Exception e) {
            jsonObject = null;
        }

        if (jsonObject != null) {

            if (tag.equalsIgnoreCase("past_trips")) {
                btnCancelRide.setVisibility(View.GONE);
                lnrComments.setVisibility(View.VISIBLE);
                lnrUpcomingLayout.setVisibility(View.GONE);
                getRequestDetails();
                lblTitle.setText("Past Trips");
            } else {
                lnrUpcomingLayout.setVisibility(View.VISIBLE);
                btnViewInvoice.setVisibility(View.GONE);
                btnCancelRide.setVisibility(View.VISIBLE);
                lnrComments.setVisibility(View.GONE);
                getUpcomingDetails();
                lblTitle.setText("Upcoming Trips");
            }
        }
        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryDetails.this, ShowProfile.class);
                intent.putExtra("driver", driver);
                startActivity(intent);
            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void findViewByIdAndInitialize() {
        activity = HistoryDetails.this;
        context = HistoryDetails.this;
        helper = new ConnectionHelper(activity);
        isInternet = helper.isConnectingToInternet();
        parentLayout = (LinearLayout) findViewById(R.id.parentLayout);
        profileLayout = (LinearLayout) findViewById(R.id.profile_detail_layout);
        lnrInvoice = (LinearLayout) findViewById(R.id.lnrInvoice);
        lnrInvoiceSub = (LinearLayout) findViewById(R.id.lnrInvoiceSub);
        parentLayout.setVisibility(View.GONE);
        backArrow = (ImageView) findViewById(R.id.backArrow);
        tripAmount = (MyBoldTextView) findViewById(R.id.tripAmount);
        tripDate = (MyBoldTextView) findViewById(R.id.tripDate);
        paymentType = (MyBoldTextView) findViewById(R.id.paymentType);
        booking_id = (MyBoldTextView) findViewById(R.id.booking_id);
        paymentTypeImg = (ImageView) findViewById(R.id.paymentTypeImg);
        tripProviderImg = (ImageView) findViewById(R.id.tripProviderImg);
        tripImg = (ImageView) findViewById(R.id.tripImg);
        tripComments = (MyBoldTextView) findViewById(R.id.tripComments);
        tripProviderName = (MyBoldTextView) findViewById(R.id.tripProviderName);
        tripProviderRating = (RatingBar) findViewById(R.id.tripProviderRating);
        tripSource = (MyBoldTextView) findViewById(R.id.tripSource);
        tripDestination = (MyBoldTextView) findViewById(R.id.tripDestination);
        lblBookingID = (MyBoldTextView) findViewById(R.id.lblBookingID);
        lblBasePrice = (MyBoldTextView) findViewById(R.id.lblBasePrice);
        lblTaxPrice = (MyBoldTextView) findViewById(R.id.lblTaxPrice);
        lblDistancePrice = (MyBoldTextView) findViewById(R.id.lblDistancePrice);
        lblTotalPrice = (MyBoldTextView) findViewById(R.id.lblTotalPrice);
        lblTitle = (MyBoldTextView) findViewById(R.id.lblTitle);
        btnCancelRide = (MyButton) findViewById(R.id.btnCancelRide);
        sourceAndDestinationLayout = (LinearLayout) findViewById(R.id.sourceAndDestinationLayout);
        lnrComments = (LinearLayout) findViewById(R.id.lnrComments);
        viewLayout = (View) findViewById(R.id.ViewLayout);

        lnrUpcomingLayout = (LinearLayout) findViewById(R.id.lnrUpcomingLayout);
        btnViewInvoice = (Button) findViewById(R.id.btnViewInvoice);
        btnCall = (Button) findViewById(R.id.btnCall);

        btnCancelRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.mipmap.ic_launcher)
                        .setTitle(R.string.app_name)
                        .setMessage(getString(R.string.cencel_request))
                        .setCancelable(false)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                showreasonDialog();
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        btnViewInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnrInvoice.setVisibility(View.VISIBLE);
            }
        });

        lnrInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnrInvoice.setVisibility(View.GONE);
            }
        });

        lnrInvoiceSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (driver.getMobile() != null && !driver.getMobile().equalsIgnoreCase("null") && driver.getMobile().length() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 2);
                    } else {
                        Intent intentCall = new Intent(Intent.ACTION_CALL);
                        intentCall.setData(Uri.parse("tel:" + driver.getMobile()));
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        startActivity(intentCall);
                    }
                } else {
                    displayMessage(getString(R.string.user_no_mobile));
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission Granted
            //Toast.makeText(SignInActivity.this, "PERMISSION_GRANTED", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + driver.getMobile()));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivity(intent);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1);
            }
        }
    }

    private void showreasonDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.cancel_dialog,null);
        final EditText reasonEtxt = (EditText) view.findViewById(R.id.reason_etxt);
        Button submitBtn = (Button) view.findViewById(R.id.submit_btn);
        builder.setIcon(R.mipmap.ic_launcher)
                .setTitle(R.string.app_name)
                .setView(view)
                .setCancelable(true);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reason = reasonEtxt.getText().toString();
                cancelRequest();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void getRequestDetails() {

        customDialog = new CustomDialog(context);
        customDialog.setCancelable(false);
        if (customDialog != null)
        customDialog.show();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URLHelper.GET_HISTORY_DETAILS_API + "?request_id=" + jsonObject.optString("id"), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Log.v("GetPaymentList", response.toString());
                if (response != null && response.length() > 0) {
                    if (response.optJSONObject(0) != null) {
                        Glide.with(activity).load(response.optJSONObject(0).optString("static_map")).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(tripImg);
                        Log.e("History Details", "onResponse: Currency" + SharedHelper.getKey(context, "currency"));
                        JSONObject providerObj = response.optJSONObject(0).optJSONObject("provider");
                        if (providerObj != null) {
                            driver = new Driver();
                            driver.setFname(providerObj.optString("first_name"));
                            driver.setLname(providerObj.optString("last_name"));
                            driver.setMobile(providerObj.optString("mobile"));
                            driver.setEmail(providerObj.optString("email"));
                            driver.setImg(providerObj.optString("avatar"));
                            driver.setRating(providerObj.optString("rating"));
                        }
                        if (response.optJSONObject(0).optString("booking_id") != null &&
                                !response.optJSONObject(0).optString("booking_id").equalsIgnoreCase("")) {
                            booking_id.setText(response.optJSONObject(0).optString("booking_id"));
                            lblBookingID.setText(response.optJSONObject(0).optString("booking_id"));
                        }
                        String form;
                        if (tag.equalsIgnoreCase("past_trips")) {
                            form = response.optJSONObject(0).optString("assigned_at");
                        } else {
                            form = response.optJSONObject(0).optString("schedule_at");
                        }
                        if (response.optJSONObject(0).optJSONObject("payment") != null && response.optJSONObject(0).optJSONObject("payment").optString("total") != null &&
                                !response.optJSONObject(0).optJSONObject("payment").optString("total").equalsIgnoreCase("")) {
                            tripAmount.setText(SharedHelper.getKey(context, "currency") + "" + response.optJSONObject(0).optJSONObject("payment").optString("total"));
                            response.optJSONObject(0).optJSONObject("payment");
                            lblBasePrice.setText((SharedHelper.getKey(context, "currency") + ""
                                    + response.optJSONObject(0).optJSONObject("payment").optString("fixed")));
                            lblDistancePrice.setText((SharedHelper.getKey(context, "currency") + ""
                                    + response.optJSONObject(0).optJSONObject("payment").optString("distance")));
                            lblTaxPrice.setText((SharedHelper.getKey(context, "currency") + ""
                                    + response.optJSONObject(0).optJSONObject("payment").optString("tax")));
                            lblTotalPrice.setText((SharedHelper.getKey(context, "currency") + ""
                                    + response.optJSONObject(0).optJSONObject("payment").optString("total" +
                                    "")));
                        } else {
                            tripAmount.setVisibility(View.GONE);
                        }
                        try {
                            tripDate.setText(getDate(form) + "th " + getMonth(form) + " " + getYear(form) + "\n" + getTime(form));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        paymentType.setText(response.optJSONObject(0).optString("payment_mode"));
                        if (response.optJSONObject(0).optString("payment_mode").equalsIgnoreCase("CASH")) {
                            paymentTypeImg.setImageResource(R.drawable.money_icon);
                        } else {
                            paymentTypeImg.setImageResource(R.drawable.visa);
                        }
                        Glide.with(activity).load(URLHelper.base + "storage/" + response.optJSONObject(0).optJSONObject("provider").optString("avatar"))
                                .placeholder(R.drawable.car_select).error(R.drawable.car_select).dontAnimate().into(tripProviderImg);
                        if (response.optJSONObject(0).optJSONObject("rating") != null &&
                                !response.optJSONObject(0).optJSONObject("rating").optString("provider_comment").equalsIgnoreCase("")) {
                            tripComments.setText(response.optJSONObject(0).optJSONObject("rating").optString("provider_comment", ""));
                        } else {
                            tripComments.setText(getString(R.string.no_comments));
                        }
                        if (response.optJSONObject(0).optJSONObject("provider").optString("rating") != null
                                && !response.optJSONObject(0).optJSONObject("provider").optString("rating").equalsIgnoreCase("")) {
                            tripProviderRating.setRating(Float.parseFloat(response.optJSONObject(0).optJSONObject("provider").optString("rating")));
                        } else {
                            tripProviderRating.setRating(0);
                        }
                        tripProviderName.setText(response.optJSONObject(0).optJSONObject("provider").optString("first_name") + " " + response.optJSONObject(0).optJSONObject("provider").optString("last_name"));
                        if (response.optJSONObject(0).optString("s_address") == null || response.optJSONObject(0).optString("d_address") == null || response.optJSONObject(0).optString("d_address").equals("") || response.optJSONObject(0).optString("s_address").equals("")) {
                            sourceAndDestinationLayout.setVisibility(View.GONE);
                            viewLayout.setVisibility(View.GONE);
                        } else {
                            tripSource.setText(response.optJSONObject(0).optString("s_address"));
                            tripDestination.setText(response.optJSONObject(0).optString("d_address"));
                        }

                    }
                }
                if ((customDialog != null)&& (customDialog.isShowing()))
                customDialog.dismiss();
                parentLayout.setVisibility(View.VISIBLE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if ((customDialog != null)&& (customDialog.isShowing()))
                customDialog.dismiss();
                String json = null;
                String Message;
                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {

                    try {
                        JSONObject errorObj = new JSONObject(new String(response.data));

                        if (response.statusCode == 400 || response.statusCode == 405 || response.statusCode == 500) {
                            try {
                                displayMessage(errorObj.optString("message"));
                            } catch (Exception e) {
                                displayMessage(getString(R.string.something_went_wrong));
                            }

                        } else if (response.statusCode == 401) {
                            refreshAccessToken("PAST_TRIPS");
                        } else if (response.statusCode == 422) {

                            json = trimMessage(new String(response.data));
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

                } else {
                    displayMessage(getString(R.string.please_try_again));

                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                headers.put("Authorization", "" + SharedHelper.getKey(context, "token_type") + " " + SharedHelper.getKey(context, "access_token"));
                return headers;
            }
        };

        TranxitApplication.getInstance().addToRequestQueue(jsonArrayRequest);
    }

    public void getUpcomingDetails() {

        customDialog = new CustomDialog(context);
        customDialog.setCancelable(false);
        if (customDialog != null)
        customDialog.show();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URLHelper.UPCOMING_TRIP_DETAILS + "?request_id=" + jsonObject.optString("id"), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Log.v("GetPaymentList", response.toString());
                if (response != null && response.length() > 0) {
                    if (response.optJSONObject(0) != null) {
                        Glide.with(activity).load(response.optJSONObject(0).optString("static_map")).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(tripImg);
//                    tripDate.setText(response.optJSONObject(0).optString("assigned_at"));
                        paymentType.setText(response.optJSONObject(0).optString("payment_mode"));
                        String form = response.optJSONObject(0).optString("schedule_at");
                        JSONObject providerObj = response.optJSONObject(0).optJSONObject("provider");
                        if (response.optJSONObject(0).optString("booking_id") != null &&
                                !response.optJSONObject(0).optString("booking_id").equalsIgnoreCase("")) {
                            booking_id.setText(response.optJSONObject(0).optString("booking_id"));
                        }
                        if (providerObj != null) {
                            driver = new Driver();
                            driver.setFname(providerObj.optString("first_name"));
                            driver.setLname(providerObj.optString("last_name"));
                            driver.setMobile(providerObj.optString("mobile"));
                            driver.setEmail(providerObj.optString("email"));
                            driver.setImg(providerObj.optString("avatar"));
                            driver.setRating(providerObj.optString("rating"));
                        }
                        try {
                            tripDate.setText(getDate(form) + "th " + getMonth(form) + " " + getYear(form) + "\n" + getTime(form));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (response.optJSONObject(0).optString("payment_mode").equalsIgnoreCase("CASH")) {
                            paymentTypeImg.setImageResource(R.drawable.money_icon);
                        } else {
                            paymentTypeImg.setImageResource(R.drawable.visa);
                        }

                        if (response.optJSONObject(0).optJSONObject("provider").optString("avatar") != null)
                            Glide.with(activity).load(URLHelper.base + "storage/" + response.optJSONObject(0).optJSONObject("provider").optString("avatar"))
                                    .placeholder(R.drawable.car_select).error(R.drawable.car_select).dontAnimate().into(tripProviderImg);

                        tripProviderRating.setRating(Float.parseFloat(response.optJSONObject(0).optJSONObject("provider").optString("rating")));
                        tripProviderName.setText(response.optJSONObject(0).optJSONObject("provider").optString("first_name") + " " + response.optJSONObject(0).optJSONObject("provider").optString("last_name"));
                        if (response.optJSONObject(0).optString("s_address") == null || response.optJSONObject(0).optString("d_address") == null || response.optJSONObject(0).optString("d_address").equals("") || response.optJSONObject(0).optString("s_address").equals("")) {
                            sourceAndDestinationLayout.setVisibility(View.GONE);
                            viewLayout.setVisibility(View.GONE);
                        } else {
                            tripSource.setText(response.optJSONObject(0).optString("s_address"));
                            tripDestination.setText(response.optJSONObject(0).optString("d_address"));
                        }

                        try {
                            JSONObject serviceObj = response.optJSONObject(0).optJSONObject("service_type");
                            if (serviceObj != null) {
//                            holder.car_name.setText(serviceObj.optString("name"));
                                if (tag.equalsIgnoreCase("past_trips")) {
                                    tripAmount.setText(SharedHelper.getKey(context, "currency") + serviceObj.optString("price"));
                                } else {
                                    tripAmount.setVisibility(View.GONE);
                                }
                                Glide.with(activity).load(serviceObj.optString("image"))
                                        .placeholder(R.drawable.loading).error(R.drawable.loading)
                                        .dontAnimate().into(tripProviderImg);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                    if ((customDialog != null) && (customDialog.isShowing()))
                        customDialog.dismiss();
                    parentLayout.setVisibility(View.VISIBLE);

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
                if (response != null && response.data != null) {

                    try {
                        JSONObject errorObj = new JSONObject(new String(response.data));

                        if (response.statusCode == 400 || response.statusCode == 405 || response.statusCode == 500) {
                            try {
                                displayMessage(errorObj.optString("message"));
                            } catch (Exception e) {
                                displayMessage(getString(R.string.something_went_wrong));
                            }

                        } else if (response.statusCode == 401) {
                            refreshAccessToken("UPCOMING_TRIPS");
                        } else if (response.statusCode == 422) {

                            json = trimMessage(new String(response.data));
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

                } else {
                    displayMessage(getString(R.string.please_try_again));

                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                headers.put("Authorization", "" + SharedHelper.getKey(context, "token_type") + " " + SharedHelper.getKey(context, "access_token"));
                return headers;
            }
        };

        TranxitApplication.getInstance().addToRequestQueue(jsonArrayRequest);
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

                Log.v("SignUpResponse", response.toString());
                SharedHelper.putKey(context, "access_token", response.optString("access_token"));
                SharedHelper.putKey(context, "refresh_token", response.optString("refresh_token"));
                SharedHelper.putKey(context, "token_type", response.optString("token_type"));
                if(tag.equalsIgnoreCase("PAST_TRIPS")){
                    getRequestDetails();
                }else if(tag.equalsIgnoreCase("UPCOMING_TRIPS")){
                    getUpcomingDetails();
                }else if(tag.equalsIgnoreCase("CANCEL_REQUEST")){
                    cancelRequest();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String json = null;
                String Message;
                NetworkResponse response = error.networkResponse;

                if (response != null && response.data != null) {
                    SharedHelper.putKey(context, "loggedIn", getString(R.string.False));
                    GoToBeginActivity();
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


    public void displayMessage(String toastString) {
        Snackbar.make(getCurrentFocus(), toastString, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

    public void GoToBeginActivity() {
        Intent mainIntent = new Intent(activity, BeginScreen.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        activity.finish();
    }

    @Override
    public void onBackPressed() {
        if (lnrInvoice.getVisibility() == View.VISIBLE){
            lnrInvoice.setVisibility(View.GONE);
        }else{
            super.onBackPressed();
        }
    }

    public void cancelRequest() {

        customDialog = new CustomDialog(context);
        customDialog.setCancelable(false);
        if (customDialog != null)
        customDialog.show();
        JSONObject object = new JSONObject();
        try {
            object.put("request_id", jsonObject.optString("id"));
            object.put("cancel_reason",reason);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.CANCEL_REQUEST_API, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v("CancelRequestResponse", response.toString());
                if ((customDialog != null)&& (customDialog.isShowing()))
                customDialog.dismiss();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if ((customDialog != null)&& (customDialog.isShowing()))
                customDialog.dismiss();
                String json = null;
                String Message;
                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {

                    try {
                        JSONObject errorObj = new JSONObject(new String(response.data));

                        if (response.statusCode == 400 || response.statusCode == 405 || response.statusCode == 500) {
                            try {
                                displayMessage(errorObj.optString("message"));
                            } catch (Exception e) {
                                displayMessage(getString(R.string.something_went_wrong));
                            }
                        } else if (response.statusCode == 401) {
                            refreshAccessToken("CANCEL_REQUEST");
                        } else if (response.statusCode == 422) {

                            json = trimMessage(new String(response.data));
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

                } else {
                    displayMessage(getString(R.string.please_try_again));
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                headers.put("Authorization", "" + SharedHelper.getKey(context, "token_type") + " " + SharedHelper.getKey(context, "access_token"));
                return headers;
            }
        };

        TranxitApplication.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    private String getMonth(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String monthName = new SimpleDateFormat("MMM").format(cal.getTime());
        return monthName;
    }
    private String getDate(String date) throws ParseException{
        Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String dateName = new SimpleDateFormat("dd").format(cal.getTime());
        return dateName;
    }
    private String getYear(String date) throws ParseException{
        Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String yearName = new SimpleDateFormat("yyyy").format(cal.getTime());
        return yearName;
    }

    private String getTime(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String timeName = new SimpleDateFormat("hh:mm a").format(cal.getTime());
        return timeName;
    }
}
