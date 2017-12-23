package com.tranxitpro.app.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.facebook.FacebookSdk;
import com.facebook.accountkit.AccountKit;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.tranxitpro.app.Fragments.Coupon;
import com.tranxitpro.app.Fragments.Help;
import com.tranxitpro.app.Fragments.HomeFragment;
import com.tranxitpro.app.Fragments.Payment;
import com.tranxitpro.app.Fragments.Wallet;
import com.tranxitpro.app.Fragments.YourTrips;
import com.tranxitpro.app.Helper.CustomDialog;
import com.tranxitpro.app.Helper.SharedHelper;
import com.tranxitpro.app.Helper.URLHelper;
import com.tranxitpro.app.R;
import com.tranxitpro.app.TranxitApplication;
import com.tranxitpro.app.Utils.CustomTypefaceSpan;
import com.tranxitpro.app.Utils.MyTextView;
import com.tranxitpro.app.Utils.ResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.tranxitpro.app.TranxitApplication.trimMessage;

public class MainActivity extends AppCompatActivity implements HomeFragment.HomeFragmentListener, ResponseListener {


    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_PAYMENT = "payments";
    private static final String TAG_YOURTRIPS = "yourtrips";
    private static final String TAG_COUPON = "coupon";
    private static final String TAG_WALLET = "wallet";
    private static final String TAG_HELP = "help";
    private static final String TAG_SHARE = "share";
    private static final String TAG_LOGOUT = "logout";
    public Context context = MainActivity.this;
    public Activity activity = MainActivity.this;
    // index to identify current nav menu item
    public int navItemIndex = 0;
    public String CURRENT_TAG = TAG_HOME;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgProfile;
    private TextView txtWebsite;
    private MyTextView txtName;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;
    CustomDialog customDialog;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    private String notificationMsg;



    private static final int REQUEST_LOCATION = 1450;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(SharedHelper.getKey(context,"login_by").equals("facebook"))
         FacebookSdk.sdkInitialize(getApplicationContext());
            setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null)
            notificationMsg = intent.getExtras().getString("Notification");
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        mHandler = new Handler();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);


        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (MyTextView) navHeader.findViewById(R.id.usernameTxt);
        txtWebsite = (TextView) navHeader.findViewById(R.id.status_txt);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        navHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity, EditProfile.class));
            }
        });

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);


//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();



        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
    }



    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {
        // name, website
        txtName.setText(SharedHelper.getKey(context, "first_name") + " " + SharedHelper.getKey(context, "last_name"));
        txtWebsite.setText("");


        // Loading profile image
        //Glide.with(activity).load(SharedHelper.getKey(context, "picture")).placeholder(R.drawable.loading).error(R.drawable.ic_dummy_user).into(imgProfile);
       if (!SharedHelper.getKey(context,"picture").equalsIgnoreCase("")
               && !SharedHelper.getKey(context,"picture").equalsIgnoreCase(null) && SharedHelper.getKey(context,"picture") != null) {
           Picasso.with(context).load(SharedHelper.getKey(context, "picture"))
                   .placeholder(R.drawable.ic_dummy_user)
                   .error(R.drawable.ic_dummy_user)
                   .into(imgProfile);
       }else {
           Picasso.with(context).load(R.drawable.ic_dummy_user)
                   .placeholder(R.drawable.ic_dummy_user)
                   .error(R.drawable.ic_dummy_user)
                   .into(imgProfile);
       }

        // showing dot next to notifications label
        //  navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {

        SharedHelper.putKey(context, "current_status", "");
        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            // show or hide the fab button
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                //fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }


        //Closing drawer on item click
        drawer.closeDrawers();
        // refresh toolbar menu
        invalidateOptionsMenu();

    }

    private Fragment getHomeFragment() {

        switch (navItemIndex) {
            case 0:
                // home
                HomeFragment homeFragment = HomeFragment.newInstance();
                Bundle bundle = new Bundle();
                bundle.putString("Notification",notificationMsg);
                homeFragment.setArguments(bundle);
                return homeFragment;
            case 1:
                // Payment fragment
                Payment paymentFragment = new Payment();
                return paymentFragment;
            case 2:
                // Your Trips
                YourTrips yourTripsFragment = new YourTrips();
                return yourTripsFragment;
            case 3:
                // Coupon
                Coupon couponFragment = new Coupon();
                return couponFragment;
            case 4:
                // wallet fragment
                Wallet walletFragment = new Wallet();
                return walletFragment;

            case 5:
                // Help fragment
                Help helpFragment = new Help();
                return helpFragment;

            default:
                return new HomeFragment();
        }

    }


    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    //case R.id.nav_home:
                    //navItemIndex = 0;
                    //CURRENT_TAG = TAG_HOME;
                    //break;
                    case R.id.nav_payment:
                        drawer.closeDrawers();
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_PAYMENT;
                        break;
                    case R.id.nav_yourtrips:
                        drawer.closeDrawers();
                      /*  navItemIndex = 2;
                        CURRENT_TAG = TAG_YOURTRIPS;*/
                        SharedHelper.putKey(context, "current_status", "");
                        Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                        intent.putExtra("tag","past");
                        startActivity(intent);
                        return true;
                    // break;
                    case R.id.nav_coupon:
                        drawer.closeDrawers();
                       /* navItemIndex = 3;
                        CURRENT_TAG = TAG_COUPON;
                        break;*/
                        SharedHelper.putKey(context, "current_status", "");
                        startActivity(new Intent(MainActivity.this, CouponActivity.class));
                        return true;
                    case R.id.nav_wallet:
                        drawer.closeDrawers();
                        /*navItemIndex = 4;
                        CURRENT_TAG = TAG_WALLET;*/
                        SharedHelper.putKey(context, "current_status", "");
                        startActivity(new Intent(MainActivity.this, ActivityWallet.class));
                        return true;
                    case R.id.nav_help:
                        drawer.closeDrawers();
                       /* navItemIndex = 5;
                        CURRENT_TAG = TAG_HELP;*/
                        SharedHelper.putKey(context, "current_status", "");
                        startActivity(new Intent(MainActivity.this, ActivityHelp.class));
                        break;
                    case R.id.nav_share:
                        // launch new intent instead of loading fragment
                        //startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                        navigateToShareScreen(URLHelper.APP_URL);
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_logout:
                        // launch new intent instead of loading fragment
                        //startActivity(new Intent(MainActivity.this, PrivacyPolicyActivity.class));
                        showLogoutDialog();
                        return true;

                    default:
                        navItemIndex = 0;
                }
                loadHomeFragment();

                return true;
            }
        });

        Menu m = navigationView.getMenu();

        for (int i = 0; i < m.size(); i++) {
            MenuItem menuItem = m.getItem(i);
            applyFontToMenuItem(menuItem);

        }

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.addDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    private void signOut() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //taken from google api console (Web api client id)
//                .requestIdToken("795253286119-p5b084skjnl7sll3s24ha310iotin5k4.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {

                FirebaseAuth.getInstance().signOut();
                if(mGoogleApiClient.isConnected()) {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            if (status.isSuccess()) {
                                Log.d("MainAct", "Google User Logged out");
                               /* Intent intent = new Intent(LogoutActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();*/
                            }
                        }
                    });
                }
            }

            @Override
            public void onConnectionSuspended(int i) {
                Log.d("MAin", "Google API Client Connection Suspended");
            }
        });
    }

    public void logout() {
        customDialog = new CustomDialog(context);
        customDialog.setCancelable(false);
        if (customDialog != null)
        customDialog.show();
        JSONObject object = new JSONObject();
        try {
            object.put("id",SharedHelper.getKey(this,"id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("MainActivity", "logout: " + object);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.LOGOUT, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if ((customDialog != null)&& (customDialog.isShowing()))
                customDialog.dismiss();
                drawer.closeDrawers();
                if (SharedHelper.getKey(context,"login_by").equals("facebook"))
                    LoginManager.getInstance().logOut();
                if (SharedHelper.getKey(context,"login_by").equals("google"))
                    signOut();
                if (!SharedHelper.getKey(MainActivity.this,"account_kit_token").equalsIgnoreCase("")){
                    Log.e("MainActivity", "Account kit logout: " + SharedHelper.getKey(MainActivity.this,"account_kit_token"));
                    AccountKit.logOut();
                    SharedHelper.putKey(MainActivity.this, "account_kit_token","");
                }
                SharedHelper.putKey(context, "current_status", "");
                SharedHelper.putKey(activity, "loggedIn", getString(R.string.False));
                SharedHelper.putKey(context,"email","");
                SharedHelper.putKey(context,"login_by","");
                Intent goToLogin = new Intent(activity, BeginScreen.class);
                goToLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(goToLogin);
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
                                displayMessage(errorObj.getString("message"));
                            } catch (Exception e) {
                                displayMessage(getString(R.string.something_went_wrong));
                            }
                        } else if (response.statusCode == 401) {
                            refreshAccessToken();
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
                        logout();
                    }
                }
            }
        }) {
            @Override
            public java.util.Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                Log.e("getHeaders: Token", SharedHelper.getKey(context, "access_token") + SharedHelper.getKey(context, "token_type"));
                headers.put("Authorization", "" + "Bearer" + " " + SharedHelper.getKey(context, "access_token"));
                return headers;
            }
        };
        TranxitApplication.getInstance().addToRequestQueue(jsonObjectRequest);
    }
    public void displayMessage(String toastString) {
        Log.e("displayMessage", "" + toastString);
        Snackbar.make(getCurrentFocus(), toastString, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

    private void refreshAccessToken() {


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
                logout();


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

    public void GoToBeginActivity() {
        Intent mainIntent = new Intent(activity, BeginScreen.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        activity.finish();
    }

    private void showLogoutDialog() {
        if (!isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            builder .setTitle(context.getString(R.string.app_name))
                    .setIcon(R.mipmap.ic_launcher)
                    .setMessage(getString(R.string.logout_alert));
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    logout();
                }
            });
            builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Reset to previous seletion menu in navigation
                    dialog.dismiss();
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.openDrawer(GravityCompat.START);
                }
            });
            builder.setCancelable(false);
            final AlertDialog dialog = builder.create();
            //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface arg) {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                }
            });
            dialog.show();
        }
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/ClanPro-NarrNews.otf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                SharedHelper.putKey(context, "current_status", "");
                loadHomeFragment();
                return;
            } else {
                SharedHelper.putKey(context, "current_status", "");
            }
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected
        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.main, menu);
        }

        // when fragment is notifications, load the menu created for notifications
        if (navItemIndex == 3) {
            getMenuInflater().inflate(R.menu.notification, menu);
        }
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
            Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_LONG).show();
            return true;
        }

        // user is in notifications fragment
        // and selected 'Mark all as Read'
        if (id == R.id.action_settings) {
            Toast.makeText(getApplicationContext(), "All notifications marked as read!", Toast.LENGTH_LONG).show();
        }

        // user is in notifications fragment
        // and selected 'Clear All'
        if (id == R.id.action_settings) {
            Toast.makeText(getApplicationContext(), "Clear all notifications!", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void navigateToShareScreen(String shareUrl) {
        try{
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareUrl + " -via " + getString(R.string.app_name));
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context, "Share applications not found!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void getJSONArrayResult(String strTag, JSONArray arrayResponse) {

    }
}