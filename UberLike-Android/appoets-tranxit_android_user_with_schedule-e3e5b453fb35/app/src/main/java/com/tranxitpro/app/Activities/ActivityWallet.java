package com.tranxitpro.app.Activities;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.tranxitpro.app.Helper.CustomDialog;
import com.tranxitpro.app.Helper.SharedHelper;
import com.tranxitpro.app.Helper.URLHelper;
import com.tranxitpro.app.Models.CardInfo;
import com.tranxitpro.app.R;
import com.tranxitpro.app.TranxitApplication;
import com.tranxitpro.app.Utils.MyBoldTextView;
import com.tranxitpro.app.Utils.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class ActivityWallet extends AppCompatActivity implements View.OnClickListener {

    private final int ADD_CARD_CODE = 435;

    private Button add_fund_button;
    private ProgressDialog loadingDialog;
    private CardView wallet_card, add_money_card;

    private Button add_money_button;
    private EditText money_et;
    private MyBoldTextView balance_tv;
    private String session_token;
    private Button one, two, three;
    private double update_amount = 0;
    private ArrayList<CardInfo> cardInfoArrayList;
    private String currency = "";
    private CustomDialog customDialog;
    private Context context;
    private TextView currencySymbol;

    Utilities utils = new Utilities();
    private CardInfo cardInfo;

    boolean loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        cardInfoArrayList = new ArrayList<>();
        add_fund_button = (Button) findViewById(R.id.add_fund_button);
        wallet_card = (CardView) findViewById(R.id.wallet_card);
        add_money_card = (CardView) findViewById(R.id.add_money_card);
        balance_tv = (MyBoldTextView) findViewById(R.id.balance_tv);
        currencySymbol = (TextView) findViewById(R.id.currencySymbol);
        context = this;
        customDialog = new CustomDialog(context);
        customDialog.setCancelable(false);

        currencySymbol.setText(SharedHelper.getKey(context, "currency"));
        money_et = (EditText) findViewById(R.id.money_et);
        one = (Button) findViewById(R.id.one);
        two = (Button) findViewById(R.id.two);
        three = (Button) findViewById(R.id.three);
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        one.setText(SharedHelper.getKey(context, "currency") + "199");
        two.setText(SharedHelper.getKey(context, "currency") + "599");
        three.setText(SharedHelper.getKey(context, "currency") + "1099");

        money_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.toString().length() == 0)
                    add_fund_button.setVisibility(View.GONE);
                else add_fund_button.setVisibility(View.VISIBLE);
                if (count == 1 || count == 0) {
                    one.setBackground(getResources().getDrawable(R.drawable.border_stroke));
                    two.setBackground(getResources().getDrawable(R.drawable.border_stroke));
                    three.setBackground(getResources().getDrawable(R.drawable.border_stroke));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        add_fund_button.setOnClickListener(this);
        loadingDialog = new ProgressDialog(this);
        loadingDialog.setIndeterminate(true);
        loadingDialog.setMessage("Please wait...");

        session_token = SharedHelper.getKey(this, "access_token");

        wallet_card.setVisibility(View.VISIBLE);
        add_money_card.setVisibility(View.VISIBLE);

        getBalance();
        getCards(false);
    }

    private void getBalance() {
        if ((customDialog != null))
            customDialog.show();
        Ion.with(this)
                .load(URLHelper.getUserProfileUrl)
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .addHeader("Authorization", SharedHelper.getKey(ActivityWallet.this, "token_type") + " " + session_token)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> response) {
                        // response contains both the headers and the string result
                        if ((customDialog != null) && customDialog.isShowing())
                            customDialog.dismiss();
                        if (e != null) {
                            if (e instanceof TimeoutException) {
                                displayMessage(getString(R.string.please_try_again));
                            }
                            if (e instanceof NetworkErrorException) {
                                getBalance();
                            }
                            return;
                        }
                        if (response != null) {
                            if (response.getHeaders().code() == 200) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response.getResult());
                                    currency = jsonObject.optString("currency");
                                    balance_tv.setText(jsonObject.optString("currency") + jsonObject.optString("wallet_balance"));
                                    SharedHelper.putKey(context, "wallet_balance", jsonObject.optString("wallet_balance"));
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                            } else {
                                if ((customDialog != null) && customDialog.isShowing())
                                    customDialog.dismiss();
                                if (response.getHeaders().code() == 401) {
                                    refreshAccessToken("GET_BALANCE");
                                }
                            }
                        } else {

                        }
                    }
                });
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

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.login, object, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                utils.print("SignUpResponse", response.toString());
                SharedHelper.putKey(context, "access_token", response.optString("access_token"));
                SharedHelper.putKey(context, "refresh_token", response.optString("refresh_token"));
                SharedHelper.putKey(context, "token_type", response.optString("token_type"));
                if (tag.equalsIgnoreCase("GET_BALANCE")) {
                    getBalance();
                } else if (tag.equalsIgnoreCase("GET_CARDS")) {
                    getCards(loading);
                } else {
                    addMoney(cardInfo);
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String json = "";
                NetworkResponse response = error.networkResponse;

                if (response != null && response.data != null) {
                    SharedHelper.putKey(context, "loggedIn", getString(R.string.False));
                    utils.GoToBeginActivity(ActivityWallet.this);
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void getCards(final boolean showLoading) {
        loading = showLoading;
        if (loading) {
            if (customDialog != null)
                customDialog.show();
        }
        Ion.with(this)
                .load(URLHelper.CARD_PAYMENT_LIST)
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .addHeader("Authorization", SharedHelper.getKey(ActivityWallet.this, "token_type") + " " + session_token)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> response) {
                        // response contains both the headers and the string result
                        if (response != null) {
                            if (showLoading) {
                                if ((customDialog != null) && (customDialog.isShowing()))
                                    customDialog.dismiss();
                            }
                            if (e != null) {
                                if (e instanceof TimeoutException) {
                                    displayMessage(getString(R.string.please_try_again));
                                }
                                if (e instanceof NetworkErrorException) {
                                    getCards(showLoading);
                                }
                                return;
                            }
                            if (response.getHeaders().code() == 200) {
                                try {
                                    JSONArray jsonArray = new JSONArray(response.getResult());
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject cardObj = jsonArray.getJSONObject(i);
                                        CardInfo cardInfo = new CardInfo();
                                        cardInfo.setCardId(cardObj.optString("card_id"));
                                        cardInfo.setCardType(cardObj.optString("brand"));
                                        cardInfo.setLastFour(cardObj.optString("last_four"));
                                        cardInfoArrayList.add(cardInfo);
                                    }

                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                            } else {
                                if (response.getHeaders().code() == 401) {
                                    refreshAccessToken("GET_CARDS");
                                }
                            }
                        }
                    }
                });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_fund_button:
                if (money_et.getText().toString().isEmpty()) {
                    update_amount = 0;
                    Toast.makeText(this, "Enter an amount greater than 0", Toast.LENGTH_SHORT).show();
                } else {
                    update_amount = Double.parseDouble(money_et.getText().toString());
                    //  payByPayPal(update_amount);
                    if (cardInfoArrayList.size() > 0) {
                        showChooser();
                    } else {
                        gotoAddCard();
                    }
                }
                break;

            case R.id.one:
                one.setBackground(getResources().getDrawable(R.drawable.border_stroke_black));
                two.setBackground(getResources().getDrawable(R.drawable.border_stroke));
                three.setBackground(getResources().getDrawable(R.drawable.border_stroke));
                money_et.setText("199");
                break;
            case R.id.two:
                one.setBackground(getResources().getDrawable(R.drawable.border_stroke));
                two.setBackground(getResources().getDrawable(R.drawable.border_stroke_black));
                three.setBackground(getResources().getDrawable(R.drawable.border_stroke));
                money_et.setText("599");
                break;
            case R.id.three:
                one.setBackground(getResources().getDrawable(R.drawable.border_stroke));
                two.setBackground(getResources().getDrawable(R.drawable.border_stroke));
                three.setBackground(getResources().getDrawable(R.drawable.border_stroke_black));
                money_et.setText("1099");
                break;
        }
    }

    private void gotoAddCard() {
        Intent mainIntent = new Intent(this, AddCard.class);
        startActivityForResult(mainIntent, ADD_CARD_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_CARD_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                boolean result = data.getBooleanExtra("isAdded", false);
                if (result) {
                    getCards(true);
                }
            }
        }
    }

    private void showChooser() {

        final String[] cardsList = new String[cardInfoArrayList.size()];

        for (int i = 0; i < cardInfoArrayList.size(); i++) {
            cardsList[i] = "XXXX-XXXX-XXXX-" + cardInfoArrayList.get(i).getLastFour();
        }

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setTitle("Add money using");
        builderSingle.setSingleChoiceItems(cardsList, 0, null);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                R.layout.custom_tv);

        for (int j = 0; j < cardInfoArrayList.size(); j++) {
            String card = "";
            card = "XXXX-XXXX-XXXX-" + cardInfoArrayList.get(j).getLastFour();
            arrayAdapter.add(card);
        }
        builderSingle.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                Log.e("Items clicked===>", "" + selectedPosition);
                cardInfo = cardInfoArrayList.get(selectedPosition);
                addMoney(cardInfoArrayList.get(selectedPosition));
            }
        });
        builderSingle.setNegativeButton(
                "cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
//        builderSingle.setAdapter(
//                arrayAdapter,
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        addMoney(cardInfoArrayList.get(which));
//                    }
//                });
        builderSingle.show();
    }

    private void addMoney(final CardInfo cardInfo) {
        if (customDialog != null)
            customDialog.show();

        JsonObject json = new JsonObject();
        json.addProperty("card_id", cardInfo.getCardId());
        json.addProperty("amount", money_et.getText().toString());

        Ion.with(this)
                .load(URLHelper.addCardUrl)
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .addHeader("Authorization", SharedHelper.getKey(ActivityWallet.this, "token_type") + " " + session_token)
                .setJsonObjectBody(json)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> response) {
                        // response contains both the headers and the string result

                        if ((customDialog != null) && (customDialog.isShowing()))
                            customDialog.dismiss();

                        if (e != null) {
                            if (e instanceof TimeoutException) {
                                displayMessage(getString(R.string.please_try_again));
                            }
                            if (e instanceof NetworkErrorException) {
                                addMoney(cardInfo);
                            }
                            return;
                        }

                        if (response.getHeaders().code() == 200) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.getResult());
                                Toast.makeText(ActivityWallet.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                                JSONObject userObj = jsonObject.getJSONObject("user");
                                balance_tv.setText(currency + userObj.optString("wallet_balance"));
                                SharedHelper.putKey(context, "wallet_balance", jsonObject.optString("wallet_balance"));
                                money_et.setText("");
                                if ((customDialog != null) && (customDialog.isShowing()))
                                    customDialog.dismiss();
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        } else {
                            if ((customDialog != null) && (customDialog.isShowing()))
                                customDialog.dismiss();
                            try {
                                if (response != null && response.getHeaders() != null) {
                                    if (response.getHeaders().code() == 401) {
                                        refreshAccessToken("ADD_MONEY");
                                    }
                                }
                            } catch (Exception exception) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    public void displayMessage(String toastString) {
        Log.e("displayMessage", "" + toastString);
        Snackbar.make(getCurrentFocus(), toastString, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

}
