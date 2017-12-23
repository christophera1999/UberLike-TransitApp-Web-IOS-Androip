package com.tranxitpro.provider.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.daasuu.cat.CountAnimationTextView;
import com.tranxitpro.provider.Activity.BeginScreen;
import com.tranxitpro.provider.Helper.CustomDialog;
import com.tranxitpro.provider.Helper.SharedHelper;
import com.tranxitpro.provider.Helper.URLHelper;
import com.tranxitpro.provider.Helper.XuberApplication;
import com.tranxitpro.provider.R;

import org.json.JSONObject;

import java.util.HashMap;

import static com.tranxitpro.provider.Helper.XuberApplication.trimMessage;

/**
 * A simple {@link Fragment} subclass.
 */
public class SummaryFragment extends Fragment implements View.OnClickListener {

    ImageView imgBack;
    LinearLayout cardLayout;

    CountAnimationTextView noOfRideTxt;
    CountAnimationTextView scheduleTxt;
    CountAnimationTextView revenueTxt;
    CountAnimationTextView cancelTxt;

    CardView ridesCard;
    CardView cancelCard;
    CardView scheduleCard;
    CardView revenueCard;

    TextView currencyTxt;

    int rides, revenue, schedule, cancel;
    Double doubleRevenue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_summary, container, false);
        findViewsById(view);
        setClickListeners();

        getProviderSummary();
        return view;

    }

    private void setClickListeners() {
        imgBack.setOnClickListener(this);
        revenueCard.setOnClickListener(this);
        ridesCard.setOnClickListener(this);
        revenueCard.setOnClickListener(this);
        scheduleCard.setOnClickListener(this);
    }

    private void findViewsById(View view) {
        imgBack = (ImageView) view.findViewById(R.id.backArrow);
        cardLayout = (LinearLayout) view.findViewById(R.id.card_layout);
        noOfRideTxt = (CountAnimationTextView) view.findViewById(R.id.no_of_rides_txt);
        scheduleTxt = (CountAnimationTextView) view.findViewById(R.id.schedule_txt);
        cancelTxt = (CountAnimationTextView) view.findViewById(R.id.cancel_txt);
        revenueTxt = (CountAnimationTextView) view.findViewById(R.id.revenue_txt);
        currencyTxt = (TextView) view.findViewById(R.id.currency_txt);
        revenueCard = (CardView) view.findViewById(R.id.revenue_card);
        scheduleCard = (CardView) view.findViewById(R.id.schedule_card);
        ridesCard = (CardView) view.findViewById(R.id.rides_card);
        cancelCard = (CardView) view.findViewById(R.id.cancel_card);
    }

    @Override
    public void onClick(View v) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment = new Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("toolbar", "true");
        switch (v.getId()) {
            case R.id.backArrow:
                getFragmentManager().popBackStackImmediate();
                break;
            case R.id.rides_card:
                fragment = new PastTrips();
                fragment.setArguments(bundle);
                transaction.add(R.id.content, fragment);
                transaction.hide(this);
                transaction.commit();
                break;
            case R.id.schedule_card:
                fragment = new OnGoingTrips();
                transaction.add(R.id.content, fragment);
                fragment.setArguments(bundle);
                transaction.hide(this);
                transaction.commit();
                break;
            case R.id.revenue_card:
                fragment = new EarningsFragment();
                transaction.add(R.id.content, fragment);
                transaction.hide(this);
                transaction.commit();
                break;
            case R.id.cancel_card:
                fragment = new PastTrips();
                transaction.add(R.id.content, fragment);
                fragment.setArguments(bundle);
                transaction.hide(this);
                transaction.commit();
                break;
        }
    }

    private void setDetails() {
        Animation txtAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.txt_size);
        if (schedule > 0) {
            scheduleTxt.setAnimationDuration(500)
                    .countAnimation(0, schedule);
        } else {
            scheduleTxt.setText("0");
        }
        if (revenue > 0) {
            revenueTxt.setAnimationDuration(500)
                    .countAnimation(0, revenue);
        } else {
            revenueTxt.setText("0");
        }
        if (rides > 0) {
            noOfRideTxt.setAnimationDuration(500)
                    .countAnimation(0, rides);

        } else {
            noOfRideTxt.setText("0");
        }
        if (cancel > 0) {
            cancelTxt.setAnimationDuration(500)
                    .countAnimation(0, cancel);
        } else {
            cancelTxt.setText("0");
        }
        scheduleTxt.startAnimation(txtAnim);
        revenueTxt.startAnimation(txtAnim);
        noOfRideTxt.startAnimation(txtAnim);
        cancelTxt.startAnimation(txtAnim);
        currencyTxt.setText(SharedHelper.getKey(getContext(), "currency"));
    }

    public void getProviderSummary() {
        {
            final CustomDialog customDialog = new CustomDialog(getActivity());
            customDialog.setCancelable(false);
            customDialog.show();
            JSONObject object = new JSONObject();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.SUMMARY, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    customDialog.dismiss();
                    cardLayout.setVisibility(View.VISIBLE);
                    Animation slideUp = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up);
                    cardLayout.startAnimation(slideUp);
                    rides = Integer.parseInt(response.optString("rides"));
                    schedule = Integer.parseInt(response.optString("scheduled_rides"));
                    cancel = Integer.parseInt(response.optString("cancel_rides"));
                    doubleRevenue = Double.parseDouble(response.optString("revenue"));
                    revenue= doubleRevenue.intValue();
                    //revenue = Integer.parseInt(response.optString("revenue"));

                    slideUp.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            setDetails();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });


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
                                try {
                                    displayMessage(errorObj.optString("message"));
                                } catch (Exception e) {
                                    displayMessage(getString(R.string.something_went_wrong));
                                    e.printStackTrace();
                                }
                            } else if (response.statusCode == 401) {
                                GoToBeginActivity();
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
                            e.printStackTrace();
                        }

                    } else {
                        if (error instanceof NoConnectionError) {
                            displayMessage(getString(R.string.oops_connect_your_internet));
                        } else if (error instanceof NetworkError) {
                            displayMessage(getString(R.string.oops_connect_your_internet));
                        } else if (error instanceof TimeoutError) {
                            getProviderSummary();
                        }
                    }
                }
            }) {
                @Override
                public java.util.Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("X-Requested-With", "XMLHttpRequest");
                    headers.put("Authorization", "Bearer " + SharedHelper.getKey(getContext(), "access_token"));
                    Log.e("", "Access_Token" + SharedHelper.getKey(getContext(), "access_token"));
                    return headers;
                }
            };

            XuberApplication.getInstance().addToRequestQueue(jsonObjectRequest);
        }
    }

    public void displayMessage(String toastString) {
        Snackbar.make(getView(), toastString, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

    public void GoToBeginActivity() {
        SharedHelper.putKey(getContext(), "loggedIn", getString(R.string.False));
        Intent mainIntent = new Intent(getContext(), BeginScreen.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        getActivity().finish();
    }

}
