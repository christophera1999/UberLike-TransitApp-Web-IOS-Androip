package com.tranxitpro.provider.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.android.gms.internal.a;
import com.squareup.picasso.Picasso;
import com.tranxitpro.provider.Helper.AppHelper;
import com.tranxitpro.provider.Helper.ConnectionHelper;
import com.tranxitpro.provider.Helper.CustomDialog;
import com.tranxitpro.provider.Helper.SharedHelper;
import com.tranxitpro.provider.Helper.URLHelper;
import com.tranxitpro.provider.Helper.VolleyMultipartRequest;
import com.tranxitpro.provider.Helper.XuberApplication;
import com.tranxitpro.provider.R;
import com.tranxitpro.provider.Utilities.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tranxitpro.provider.Helper.XuberApplication.trimMessage;

public class EditProfile extends AppCompatActivity {

    private static final String TAG = "EditProfile";
    public Context context = EditProfile.this;
    public Activity activity = EditProfile.this;
    private static final int SELECT_PHOTO = 100;
    CustomDialog customDialog;
    ConnectionHelper helper;
    Boolean isInternet;
    Button saveBTN;
    ImageView backArrow;
    TextView changePasswordTxt;
    EditText email, first_name, last_name, mobile_no, service;
    ImageView profile_Image;
    Boolean isImageChanged = false;
    Utilities utils = new Utilities();
    public static int deviceHeight;
    public static int deviceWidth;
    Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        findViewByIdandInitialization();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        deviceHeight = displayMetrics.heightPixels;
        deviceWidth = displayMetrics.widthPixels;

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                GoToMainActivity();
                onBackPressed();
            }
        });

        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Pattern ps = Pattern.compile(".*[0-9].*");
                Matcher firstName = ps.matcher(first_name.getText().toString());
                Matcher lastName = ps.matcher(last_name.getText().toString());


                if (email.getText().toString().equals("") || email.getText().toString().length() == 0) {
                    displayMessage(getString(R.string.email_validation));
                } else if (mobile_no.getText().toString().equals("") || mobile_no.getText().toString().length() == 0) {
                    displayMessage(getString(R.string.mobile_number_empty));
                } else if (mobile_no.getText().toString().length() < 10 || mobile_no.getText().toString().length() > 20) {
                    displayMessage(getString(R.string.mobile_number_validation));
                } else if (first_name.getText().toString().equals("") || first_name.getText().toString().length() == 0) {
                    displayMessage(getString(R.string.first_name_empty));
                } else if (last_name.getText().toString().equals("") || last_name.getText().toString().length() == 0) {
                    displayMessage(getString(R.string.last_name_empty));
                } else if (firstName.matches()) {
                    displayMessage(getString(R.string.first_name_no_number));
                } else if (lastName.matches()) {
                    displayMessage(getString(R.string.last_name_no_number));
                } else {
                    if (isInternet) {
                        updateProfile();
                    } else {
                        displayMessage(getString(R.string.something_went_wrong_net));
                    }
                }


            }
        });

        getProfile();

        changePasswordTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity, ChangePassword.class));
            }
        });


        profile_Image.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {

                if (checkStoragePermission())
                    requestPermissions(new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                else
                    goToImageIntent();

            }
        });

    }

    public void getProfile() {

        if (isInternet) {
//
//            customDialog = new CustomDialog(context);
//            customDialog.setCancelable(false);
//            customDialog.show();
            JSONObject object = new JSONObject();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URLHelper.USER_PROFILE_API, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.v("GetProfile", response.toString());
                    SharedHelper.putKey(context, "id", response.optString("id"));
                    SharedHelper.putKey(context, "first_name", response.optString("first_name"));
                    SharedHelper.putKey(context, "last_name", response.optString("last_name"));
                    SharedHelper.putKey(context, "email", response.optString("email"));
                    SharedHelper.putKey(context, "picture", URLHelper.base + "storage/" + response.optString("picture"));
                    SharedHelper.putKey(context, "gender", response.optString("gender"));
                    SharedHelper.putKey(context, "sos", response.optString("sos"));
                    SharedHelper.putKey(context, "mobile", response.optString("mobile"));
                    SharedHelper.putKey(context, "refer_code", response.optString("refer_code"));
                    SharedHelper.putKey(context, "wallet_balance", response.optString("wallet_balance"));
                    SharedHelper.putKey(context, "payment_mode", response.optString("payment_mode"));
                    SharedHelper.putKey(context, "currency", response.optString("currency"));
                    SharedHelper.putKey(context, "loggedIn", getString(R.string.True));
                    if (response.optString("avatar").startsWith("http"))
                        SharedHelper.putKey(context, "picture", response.optString("avatar"));
                    else
                        SharedHelper.putKey(context, "picture", URLHelper.base + "storage/" + response.optString("avatar"));

                    if (response.optJSONObject("service") != null) {
                        try {
                            JSONObject service = response.optJSONObject("service");
                            if (service.optJSONObject("service_type") != null) {
                                JSONObject serviceType = service.optJSONObject("service_type");
                                SharedHelper.putKey(context, "service", serviceType.optString("name"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    setProviderDetails();


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String json = null;
                    String Message;
                    NetworkResponse response = error.networkResponse;
                    if (response != null && response.data != null) {
                        try {
                            JSONObject errorObj = new JSONObject(new String(response.data));

                            if (response.statusCode == 400 || response.statusCode == 405 || response.statusCode == 500) {
                                try {
                                    displayMessage(errorObj.getString("message"));
                                } catch (Exception e) {
                                    displayMessage(getString(R.string.something_went_wrong));
                                }
                            } else if (response.statusCode == 401) {
                                /*refreshAccessToken();*/
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
                        if (error instanceof NoConnectionError) {
                            displayMessage(getString(R.string.oops_connect_your_internet));
                        } else if (error instanceof NetworkError) {
                            displayMessage(getString(R.string.oops_connect_your_internet));
                        } else if (error instanceof TimeoutError) {
                            getProfile();
                        }
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("X-Requested-With", "XMLHttpRequest");
                    Log.e(TAG, "getHeaders: Token" + SharedHelper.getKey(context, "access_token") + SharedHelper.getKey(context, "token_type"));
                    headers.put("Authorization", "" + "Bearer" + " " + SharedHelper.getKey(context, "access_token"));
                    return headers;
                }
            };

            XuberApplication.getInstance().addToRequestQueue(jsonObjectRequest);
        } else {
            displayMessage(getString(R.string.something_went_wrong_net));
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100)
            for (int grantResult : grantResults)
                if (grantResult == PackageManager.PERMISSION_GRANTED)
                    goToImageIntent();
    }

    public void goToImageIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PHOTO && resultCode == activity.RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();
            try {
                isImageChanged = true;
                //bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                Bitmap resizeImg = getBitmapFromUri(this, uri);
                if (resizeImg != null) {
                    Bitmap reRotateImg = AppHelper.modifyOrientation(resizeImg, AppHelper.getPath(this, uri));
                    profile_Image.setImageBitmap(reRotateImg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static Bitmap getBitmapFromUri(@NonNull Context context, @NonNull Uri uri) throws IOException {
        Log.e(TAG, "getBitmapFromUri: Resize uri" + uri);
        ParcelFileDescriptor parcelFileDescriptor =
                context.getContentResolver().openFileDescriptor(uri, "r");
        assert parcelFileDescriptor != null;
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        Log.e(TAG, "getBitmapFromUri: Height" + deviceHeight);
        Log.e(TAG, "getBitmapFromUri: width" + deviceWidth);
        int maxSize = Math.min(deviceHeight, deviceWidth);
        if (image != null) {
            Log.e(TAG, "getBitmapFromUri: Width" + image.getWidth());
            Log.e(TAG, "getBitmapFromUri: Height" + image.getHeight());
            int inWidth = image.getWidth();
            int inHeight = image.getHeight();
            int outWidth;
            int outHeight;
            if (inWidth > inHeight) {
                outWidth = maxSize;
                outHeight = (inHeight * maxSize) / inWidth;
            } else {
                outHeight = maxSize;
                outWidth = (inWidth * maxSize) / inHeight;
            }
            return Bitmap.createScaledBitmap(image, outWidth, outHeight, false);
        } else {
            Toast.makeText(context, context.getString(R.string.valid_image), Toast.LENGTH_SHORT).show();
            return null;
        }

    }

    public void updateProfile() {
        if (isImageChanged) {
            updateProfileWithImage();
        } else {
            updateProfileWithoutImage();
        }
    }

    private void updateProfileWithoutImage() {
        customDialog = new CustomDialog(context);
        customDialog.setCancelable(false);
        customDialog.show();
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URLHelper.USER_PROFILE_API, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                customDialog.dismiss();
                String res = new String(response.data);
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    SharedHelper.putKey(context, "id", jsonObject.optString("id"));
                    SharedHelper.putKey(context, "first_name", jsonObject.optString("first_name"));
                    SharedHelper.putKey(context, "last_name", jsonObject.optString("last_name"));
                    SharedHelper.putKey(context, "email", jsonObject.optString("email"));
                    if (jsonObject.optString("avatar").equals("") || jsonObject.optString("avatar") == null) {
                        SharedHelper.putKey(context, "picture", "");
                    } else {
                        if (jsonObject.optString("avatar").startsWith("http"))
                            SharedHelper.putKey(context, "picture", jsonObject.optString("avatar"));
                        else
                            SharedHelper.putKey(context, "picture", URLHelper.base + "storage/" + jsonObject.optString("avatar"));
                    }
                    SharedHelper.putKey(context, "sos", jsonObject.optString("sos"));
                    SharedHelper.putKey(context, "gender", jsonObject.optString("gender"));
                    SharedHelper.putKey(context, "mobile", jsonObject.optString("mobile"));
//                        SharedHelper.putKey(context, "wallet_balance", jsonObject.optString("wallet_balance"));
//                        SharedHelper.putKey(context, "payment_mode", jsonObject.optString("payment_mode"));
                    GoToMainActivity();
                    Toast.makeText(EditProfile.this, getString(R.string.update_success), Toast.LENGTH_SHORT).show();
                    //displayMessage(getString(R.string.update_success));

                } catch (JSONException e) {
                    e.printStackTrace();
                    displayMessage(getString(R.string.something_went_wrong));
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if ((customDialog != null) && customDialog.isShowing())
                    customDialog.dismiss();
                String json = null;
                String Message;
                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    try {
                        JSONObject errorObj = new JSONObject(new String(response.data));
                        if (response.getClass().equals(TimeoutError.class)) {
                            updateProfileWithoutImage();
                            return;
                        }
                        if (response.statusCode == 400 || response.statusCode == 405 || response.statusCode == 500) {
                            try {
                                displayMessage(errorObj.optString("message"));
                            } catch (Exception e) {
                                displayMessage(getString(R.string.something_went_wrong));
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
                    }

                } else {
                    if (error instanceof NoConnectionError) {
                        displayMessage(getString(R.string.oops_connect_your_internet));
                    } else if (error instanceof NetworkError) {
                        displayMessage(getString(R.string.oops_connect_your_internet));
                    } else if (error instanceof TimeoutError) {
                        updateProfileWithoutImage();
                    }
                }
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("first_name", first_name.getText().toString());
                params.put("last_name", last_name.getText().toString());
                params.put("email", email.getText().toString());
                params.put("mobile", mobile_no.getText().toString());
                params.put("avatar", "");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                headers.put("Authorization", "Bearer " + SharedHelper.getKey(context, "access_token"));
                return headers;
            }
        };
        XuberApplication.getInstance().addToRequestQueue(volleyMultipartRequest);
    }

    private void updateProfileWithImage() {

        customDialog = new CustomDialog(context);
        customDialog.setCancelable(false);
        customDialog.show();
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URLHelper.USER_PROFILE_API, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                customDialog.dismiss();

                String res = new String(response.data);
                utils.print("ProfileUpdateRes", "" + res);
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    SharedHelper.putKey(context, "id", jsonObject.optString("id"));
                    SharedHelper.putKey(context, "first_name", jsonObject.optString("first_name"));
                    SharedHelper.putKey(context, "last_name", jsonObject.optString("last_name"));
                    SharedHelper.putKey(context, "sos", jsonObject.optString("sos"));
                    SharedHelper.putKey(context, "email", jsonObject.optString("email"));
                    if (jsonObject.optString("avatar").equals("") || jsonObject.optString("avatar") == null) {
                        SharedHelper.putKey(context, "picture", "");
                    } else {
                        if (jsonObject.optString("avatar").startsWith("http"))
                            SharedHelper.putKey(context, "picture", jsonObject.optString("avatar"));
                        else
                            SharedHelper.putKey(context, "picture", URLHelper.base + "storage/" + jsonObject.optString("avatar"));
                    }

                    SharedHelper.putKey(context, "gender", jsonObject.optString("gender"));
                    SharedHelper.putKey(context, "mobile", jsonObject.optString("mobile"));
//                        SharedHelper.putKey(context, "wallet_balance", jsonObject.optString("wallet_balance"));
//                        SharedHelper.putKey(context, "payment_mode", jsonObject.optString("payment_mode"));
                    GoToMainActivity();


                    Toast.makeText(EditProfile.this, getString(R.string.update_success), Toast.LENGTH_SHORT).show();
                    //displayMessage(getString(R.string.update_success));

                } catch (JSONException e) {
                    e.printStackTrace();
                    displayMessage(getString(R.string.something_went_wrong));
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if ((customDialog != null) && customDialog.isShowing())
                    customDialog.dismiss();
                String json = null;
                String Message;
                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    try {
                        JSONObject errorObj = new JSONObject(new String(response.data));
                        if (response.getClass().equals(TimeoutError.class)) {
                            updateProfileWithoutImage();
                            return;
                        }
                        if (response.statusCode == 400 || response.statusCode == 405 || response.statusCode == 500) {
                            try {
                                displayMessage(errorObj.optString("message"));
                            } catch (Exception e) {
                                displayMessage(getString(R.string.something_went_wrong));
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
                    }

                } else {
                    if (error instanceof NoConnectionError) {
                        displayMessage(getString(R.string.oops_connect_your_internet));
                    } else if (error instanceof NetworkError) {
                        displayMessage(getString(R.string.oops_connect_your_internet));
                    } else if (error instanceof TimeoutError) {
                        updateProfileWithoutImage();
                    }
                }
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("first_name", first_name.getText().toString());
                params.put("last_name", last_name.getText().toString());
                params.put("email", email.getText().toString());
                params.put("mobile", mobile_no.getText().toString());

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                headers.put("Authorization", "Bearer " + SharedHelper.getKey(context, "access_token"));
                return headers;
            }

            @Override
            protected Map<String, VolleyMultipartRequest.DataPart> getByteData() throws AuthFailureError {
                Map<String, VolleyMultipartRequest.DataPart> params = new HashMap<>();
                params.put("avatar", new VolleyMultipartRequest.DataPart("userImage.jpg", AppHelper.getFileDataFromDrawable(profile_Image.getDrawable()), "image/jpeg"));
                return params;
            }
        };
        XuberApplication.getInstance().addToRequestQueue(volleyMultipartRequest);

    }

    public void findViewByIdandInitialization() {

        Utilities.hideKeyboard(activity);
        email = (EditText) findViewById(R.id.email);
        service = (EditText) findViewById(R.id.service);
        first_name = (EditText) findViewById(R.id.first_name);
        last_name = (EditText) findViewById(R.id.last_name);
        mobile_no = (EditText) findViewById(R.id.mobile_no);
        saveBTN = (Button) findViewById(R.id.saveBTN);
        changePasswordTxt = (TextView) findViewById(R.id.changePasswordTxt);
        backArrow = (ImageView) findViewById(R.id.backArrow);
        profile_Image = (ImageView) findViewById(R.id.img_profile);
        helper = new ConnectionHelper(context);
        isInternet = helper.isConnectingToInternet();
        setProviderDetails();
        //Assign current profile values to the edittext
        //Glide.with(activity).load(SharedHelper.getKey(context,"picture")).placeholder(R.drawable.ic_dummy_user).error(R.drawable.ic_dummy_user).into(profile_Image);
    }

    private void setProviderDetails() {
        if (!SharedHelper.getKey(context, "picture").equalsIgnoreCase("")
                && SharedHelper.getKey(context, "picture") != null
                && !SharedHelper.getKey(context, "picture").equalsIgnoreCase("null")) {
            Picasso.with(context)
                    .load(SharedHelper.getKey(context, "picture"))
                    .placeholder(R.drawable.ic_dummy_user)
                    .error(R.drawable.ic_dummy_user)
                    .into(profile_Image);
        } else {
            Picasso.with(context)
                    .load(R.drawable.ic_dummy_user)
                    .placeholder(R.drawable.ic_dummy_user)
                    .error(R.drawable.ic_dummy_user)
                    .into(profile_Image);
        }

        email.setText(SharedHelper.getKey(context, "email"));
        first_name.setText(SharedHelper.getKey(context, "first_name"));
        last_name.setText(SharedHelper.getKey(context, "last_name"));
        String mobile = SharedHelper.getKey(context, "mobile");
        if (mobile != null && !mobile.equalsIgnoreCase("null") && mobile.length() > 0)
            mobile_no.setText(mobile);
        else
            mobile_no.setText("");
        if (SharedHelper.getKey(context, "service") != null
                && !SharedHelper.getKey(context, "service").equalsIgnoreCase("null")
                && SharedHelper.getKey(context, "service").length() > 0)
            service.setText(SharedHelper.getKey(context, "service"));
        else
            service.setText(getString(R.string.no_services));

    }

    public void GoToMainActivity() {
        Intent mainIntent = new Intent(activity, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        activity.finish();
    }

    public void displayMessage(String toastString) {
        utils.print("displayMessage", "" + toastString);
        Snackbar.make(getCurrentFocus(), toastString, Snackbar.LENGTH_SHORT)
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
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void GoToBeginActivity() {
        SharedHelper.putKey(activity, "loggedIn", getString(R.string.False));
        Intent mainIntent = new Intent(activity, BeginScreen.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        activity.finish();
    }

}
