package com.tranxitpro.app.Fragments;

import android.Manifest;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LayerDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.skyfishjy.library.RippleBackground;
import com.squareup.picasso.Picasso;
import com.tranxitpro.app.Activities.AddCard;
import com.tranxitpro.app.Activities.BeginScreen;
import com.tranxitpro.app.Activities.CustomGooglePlacesSearch;
import com.tranxitpro.app.Activities.HistoryActivity;
import com.tranxitpro.app.Helper.ConnectionHelper;
import com.tranxitpro.app.Helper.CustomDialog;
import com.tranxitpro.app.Helper.DirectionsJSONParser;
import com.tranxitpro.app.Helper.SharedHelper;
import com.tranxitpro.app.Helper.URLHelper;
import com.tranxitpro.app.Models.CardInfo;
import com.tranxitpro.app.Models.PlacePredictions;
import com.tranxitpro.app.R;
import com.tranxitpro.app.TranxitApplication;
import com.tranxitpro.app.Utils.MyBoldTextView;
import com.tranxitpro.app.Utils.MyButton;
import com.tranxitpro.app.Utils.MyCheckbox;
import com.tranxitpro.app.Utils.MyTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.tranxitpro.app.TranxitApplication.trimMessage;


public class HomeFragmentOld extends Fragment implements OnMapReadyCallback, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();
    public static SupportMapFragment mapFragment = null;
    private final int ADD_CARD_CODE = 435;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE_SOURCE = 1845;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE_DEST = 18945;
    Place searchPlace;
    Activity activity;
    Context context;
    View rootView;
    public String CurrentStatus = "";
    public String PreviousStatus = "";
    Boolean isStatusChanged = false;
    int flowValue = 0;
    Float pro_rating;
    CustomDialog customDialog;
    //Side Drawer
    int NAV_DRAWER = 0;
    DrawerLayout drawer;
    int value;
    Marker marker;
    Double latitude, longitude;
    String currentAddress;
    //Resize Marker
    BitmapDrawable markerBitmapDraw;
    Bitmap markerBitmap;
    Bitmap resizedMarker;
    //Internet
    ConnectionHelper helper;
    Boolean isInternet;
    //RecylerView
    RecyclerView serviceListRecyclerView;
    ServiceListAdapter serviceListAdapter;
    int currentPostion = 0;
    //Parent LinearLayout
    LinearLayout HomeLayout;
    LinearLayout SourceAndDestination;
    LinearLayout CommonBottomLayout;
    LinearLayout ApproximateLayout;
    LinearLayout ConfirmYourPickUp;
    RelativeLayout WaitingForProvider;
    LinearLayout ProviderAcceptedLayout;
    LinearLayout InvoiceLayout;
    LinearLayout RateYourProviderLayout;
    LinearLayout ScheduleLayout;
    //HomeLayout Views
    ImageView HomeLayout_menu_icon;
    LinearLayout HomeLayout_WhereToGo, HomeLayout_sourceLayer1;
    MyBoldTextView txt_Location_Search, HomeLayout_source1;
    RelativeLayout HomeRelativeLayout;
    //SourceAndDestination
    MyBoldTextView source, destination;
    LinearLayout sourceLayer;
    LinearLayout destinationLayer;
    ImageView backArrow, shadow_sd;
    MyButton requestXuberBtn;
    MyBoldTextView changePaymentOption;
    ImageView paymentTypeImg;
    MyBoldTextView paymentType;
    //Approximate Rate Layout
    MyBoldTextView approximateAmount;
    MyButton requestXuberBtnApr;
    MyCheckbox use_wallet;
    MyBoldTextView eta_time, lblSurgePrice;
    View lineView;
    ImageView schedule;
    //Schedule Layout
    MyBoldTextView scheduleDate;
    MyBoldTextView scheduleTime;
    MyButton scheduleBtn;
    //Confirm Your Pickup
    MyBoldTextView destinationAddress;
    MyButton requestXuberBtnConfirm;
    //Waiting for provider
    MyButton cancelRideBtn;
    RippleBackground rippleBackground;
    //ProviderAcceptedLayout
    ImageView provider_image;
    MyBoldTextView provider_name;
    RatingBar provider_rating;
    ImageView serviceRequestedImg;
    MyBoldTextView serviceRequestedName;
    Button call_btn;
    Button cancel_trip;
    MyBoldTextView statusTxt;
    LinearLayout AfterAcceptButtonLayout;
    LinearLayout AfterAcceptStatusLayout, lnrProviderPopup;
    MyBoldTextView modelandNumber;
    //InvoiceLayout
    MyBoldTextView base_price, extra_price, tax_price, total_price, distance_price;
    ImageView paymentTypeImgInvoice;
    MyBoldTextView paymentTypeInvoice;
    MyBoldTextView changePaymentOptionInvoice;
    MyButton pay_now;
    //Rate Your Driver Layout
    MyBoldTextView rateProviderName;
    ImageView rateProviderImg;
    RatingBar rateProviderRating;
    EditText comments;
    MyButton submitReview;
    DatePickerDialog datePickerDialog;
    String scheduledDate = "";
    String scheduledTime = "";
    private ArrayList<CardInfo> cardInfoArrayList;
    private boolean mIsShowing;
    private boolean mIsHiding;
    private LatLng sourceLatLng;
    private LatLng destLatLng;
    private Marker sourceMarker;
    private Marker destinationMarker;
    private Marker providerMarker;
    private Marker availableProviders;
    //For Map Usage
    private GoogleMap mMap;
    ImageView imgDestination;
    MyButton btnDone;
    String strPickLocation = "", strTag = "", strPickType = "";
    CameraPosition cmPosition;

    boolean isPopped=false;
    double h;
    double w;

    // Service type popup

    MyButton btnDonePopup;
    MyBoldTextView lblServiceName, lblBasePrice, lblPriceKm, lblPriceMin;
    ImageView imgProviderPopup;
    LinearLayout lnrPriceBase, lnrPricekm, lnrPricemin, lnrHidePopup;
    boolean isMarkerRotating = false;
    boolean once = true;

    private static final int REQUEST_LOCATION = 1450;
    GoogleApiClient mGoogleApiClient;

    public HomeFragmentOld() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_home_old, container, false);
        findViewByIdAndInitialize();
        //permission to access location
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Android M Permission check
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            setUpMapIfNeeded();
            MapsInitializer.initialize(getActivity());
        }

        clearVisibility();
        StatusHandler();
        getCards();

        final Handler ha = new Handler();
        //check status every 3 sec
        ha.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.w("Handler", "Called");
                checkStatus();
                ha.postDelayed(this, 3000);
            }
        }, 3000);


        HomeLayout_WhereToGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CustomGooglePlacesSearch.class);
                intent.putExtra("cursor", "destination");
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE_DEST);
            }
        });

        HomeLayout_sourceLayer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CustomGooglePlacesSearch.class);
                intent.putExtra("cursor", "source");
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE_DEST);
            }
        });

        destinationLayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CustomGooglePlacesSearch.class);
                intent.putExtra("cursor", "destination");
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE_DEST);
            }
        });

        sourceLayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CustomGooglePlacesSearch.class);
                intent.putExtra("cursor", "source");
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE_DEST);
            }
        });


        HomeLayout_menu_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NAV_DRAWER == 0) {
                    drawer.openDrawer(Gravity.LEFT);
                } else {
                    NAV_DRAWER = 0;
                    drawer.closeDrawers();
                }
            }
        });

        requestXuberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flowValue = 3;
                StatusHandler();
            }
        });


        btnDonePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnrProviderPopup.setVisibility(View.GONE);
                CommonBottomLayout.setVisibility(View.VISIBLE);
            }
        });

        lnrHidePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnrProviderPopup.setVisibility(View.GONE);
                CommonBottomLayout.setVisibility(View.VISIBLE);
            }
        });

        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flowValue = 5;
                StatusHandler();
            }
        });

        scheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (scheduledDate != "" && scheduledTime != "") {
                    if (checktimings(scheduledTime)) {
//                        flowValue = 4;
//                        StatusHandler();
                        sendRequest();
                    } else {
                        Toast.makeText(activity, getString(R.string.different_time), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(activity, getString(R.string.choose_date_time), Toast.LENGTH_SHORT).show();
                }
            }
        });

        scheduleDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(activity,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                // set day of month , month and year value in the edit text
                                String choosedMonth = "";
                                String choosedDate = "";
                                String choosedDateFormat = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                scheduledDate = choosedDateFormat;
                                try {
                                    choosedMonth = getMonth(choosedDateFormat);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if (dayOfMonth < 10) {
                                    choosedDate = "0" + dayOfMonth;
                                } else {
                                    choosedDate = "" + dayOfMonth;
                                }
                                scheduleDate.setText(choosedDate + " " + choosedMonth + " " + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.getDatePicker().setMaxDate((System.currentTimeMillis() - 1000) + (1000 * 60 * 60 * 24 * 7));
                datePickerDialog.show();
            }
        });

        scheduleTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        String choosedHour = "";
                        String choosedMinute = "";
                        String choosedTimeZone = "";
                        String choosedTime = "";

                        scheduledTime = selectedHour + ":" + selectedMinute;

                        if (selectedHour > 12) {
                            choosedTimeZone = "PM";
                            selectedHour = selectedHour - 12;
                            if (selectedHour < 10) {
                                choosedHour = "0" + selectedHour;
                            } else {
                                choosedHour = "" + selectedHour;
                            }
                        } else {
                            choosedTimeZone = "AM";
                            if (selectedHour < 10) {
                                choosedHour = "0" + selectedHour;
                            } else {
                                choosedHour = "" + selectedHour;
                            }
                        }

                        if (selectedMinute < 10) {
                            choosedMinute = "0" + selectedMinute;
                        } else {
                            choosedMinute = "" + selectedMinute;
                        }
                        choosedTime = choosedHour + ":" + choosedMinute + " " + choosedTimeZone;
                        scheduleTime.setText(choosedTime);
                        //scheduleTime.setText(selectedHour + ":" + selectedMinute);
                        //scheduleTime.setText( strHrsToShow+":"+datetime.get(Calendar.MINUTE)+" "+am_pm );
                    }
                }, hour, minute + 30, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        changePaymentOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cardInfoArrayList.size() > 0)
                    showChooser();
                else gotoAddCard();
            }
        });

        requestXuberBtnApr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                flowValue = 4;
//                StatusHandler();
                sendRequest();
            }
        });

        requestXuberBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest();
            }
        });

        cancelRideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelRequest();
            }
        });

        call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 2);
                } else {*/
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + SharedHelper.getKey(context, "provider_mobile_no")));
                startActivity(intent);
                // }
            }
        });

        cancel_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelRequest();
            }
        });


        pay_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payNow();
            }
        });


        rateProviderRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean b) {

                LayerDrawable drawable = (LayerDrawable) ratingBar.getProgressDrawable();
                drawable.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
                drawable.getDrawable(1).setColorFilter(Color.parseColor("#FFAB00"), PorterDuff.Mode.SRC_ATOP);
                drawable.getDrawable(2).setColorFilter(Color.parseColor("#FFAB00"), PorterDuff.Mode.SRC_ATOP);
                pro_rating = rating;
            }
        });

        submitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitReviewCall();
            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int Current_Status = flowValue;
                if (Current_Status == 5) {
                    Current_Status = Current_Status - 2;
                } else {
                    Current_Status = Current_Status - 1;
                }

                flowValue = Current_Status;
                StatusHandler();
            }
        });

        statusCheck();

        return rootView;

    }


    public void statusCheck() {
        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            enableLoc();
        }
    }

    private void enableLoc() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {

                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        mGoogleApiClient.connect();
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {

                        Log.d("Location error","Location error " + connectionResult.getErrorCode());
                    }
                }).build();
        mGoogleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(getActivity(), REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                }
            }
        });
//	        }

    }

    private boolean checktimings(String time) {

        String pattern = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        try {
            String currentTime = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
            Date date1 = sdf.parse(time);
            Date date2 = sdf.parse(currentTime);

            if (date1.after(date2)) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }


    private String getMonth(String date) throws ParseException {
        Date d = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String monthName = new SimpleDateFormat("MMM").format(cal.getTime());
        return monthName;
    }


    private void hide(final View view) {
        mIsHiding = true;
        ViewPropertyAnimator animator = view.animate()
                .translationY(view.getHeight())
                .setInterpolator(INTERPOLATOR)
                .setDuration(200);

        animator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                // Prevent drawing the View after it is gone
                mIsHiding = false;
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                // Canceling a hide should show the view
                mIsHiding = false;
                if (!mIsShowing) {
                    show(view);
                }
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });

        animator.start();
    }

    /**
     * Show the quick return view.
     * <p/>
     * Animates showing the view, with the view sliding up from the bottom of the screen.
     * After the view has reappeared, its visibility will change to VISIBLE.
     *
     * @param view The quick return view
     */
    private void show(final View view) {
        mIsShowing = true;
        ViewPropertyAnimator animator = view.animate()
                .translationY(0)
                .setInterpolator(INTERPOLATOR)
                .setDuration(500);

        animator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mIsShowing = false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                // Canceling a show should hide the view
                mIsShowing = false;
                if (!mIsHiding) {
                    hide(view);
                }
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });

        animator.start();
    }

    public void findViewByIdAndInitialize() {
        if (SharedHelper.getKey(context, "current_status").equalsIgnoreCase("2")) {
            flowValue = 2;
        } else {
            flowValue = 1;
        }
        helper = new ConnectionHelper(context);
        isInternet = helper.isConnectingToInternet();

        drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);

        //HomeLayout
        HomeLayout = (LinearLayout) rootView.findViewById(R.id.homeLayout);

        //SourceAndDestinationLayout
        SourceAndDestination = (LinearLayout) rootView.findViewById(R.id.sourceAndDestinationLayout);

        //CommonBottomLayout
        CommonBottomLayout = (LinearLayout) rootView.findViewById(R.id.CommonBottomLayout);

        //Approximate Rate Layout
        ApproximateLayout = (LinearLayout) rootView.findViewById(R.id.ApproximateLayout);

        //Schedule Layout
        ScheduleLayout = (LinearLayout) rootView.findViewById(R.id.ScheduleLayout);

        //ConfirmYourPickUp
        ConfirmYourPickUp = (LinearLayout) rootView.findViewById(R.id.ConfirmYourPickupLayout);

        //Waiting For provider
        WaitingForProvider = (RelativeLayout) rootView.findViewById(R.id.WaitingForProviderLayout);

        //ProviderAcceptedLayout
        ProviderAcceptedLayout = (LinearLayout) rootView.findViewById(R.id.providerAcceptedLayout);

        //Invoice Layout
        InvoiceLayout = (LinearLayout) rootView.findViewById(R.id.InvoiceLayout);

        //RateYourProvider Layout
        RateYourProviderLayout = (LinearLayout) rootView.findViewById(R.id.rateYourProviderLayout);


        //HomeLayout View Initialization
        HomeLayout_menu_icon = (ImageView) rootView.findViewById(R.id.menu_icon);
        HomeLayout_WhereToGo = (LinearLayout) rootView.findViewById(R.id.wheretogo);
        txt_Location_Search = (MyBoldTextView) rootView.findViewById(R.id.txt_location_search);
        HomeLayout_sourceLayer1 = (LinearLayout) rootView.findViewById(R.id.sourceLayer1);
        HomeLayout_source1 = (MyBoldTextView) rootView.findViewById(R.id.source1);
        HomeRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.HomeRelativeLayout);


        //SourceAndDestination View Initialization
        source = (MyBoldTextView) rootView.findViewById(R.id.source);
        destination = (MyBoldTextView) rootView.findViewById(R.id.destination);
        backArrow = (ImageView) rootView.findViewById(R.id.backArrow);
        shadow_sd = (ImageView) rootView.findViewById(R.id.shadow_sd);
        requestXuberBtn = (MyButton) rootView.findViewById(R.id.requestXuberBtn);
        btnDonePopup = (MyButton) rootView.findViewById(R.id.btnDonePopup);
        sourceLayer = (LinearLayout) rootView.findViewById(R.id.sourceLayer);
        destinationLayer = (LinearLayout) rootView.findViewById(R.id.destinationLayer);
        changePaymentOption = (MyBoldTextView) rootView.findViewById(R.id.changePaymentOption);
        paymentTypeImg = (ImageView) rootView.findViewById(R.id.paymentTypeImg);
        paymentType = (MyBoldTextView) rootView.findViewById(R.id.paymentType);
        cardInfoArrayList = new ArrayList<>();


        //Approximate Rate View Initialization
        approximateAmount = (MyBoldTextView) rootView.findViewById(R.id.approximateAmount);
        requestXuberBtnApr = (MyButton) rootView.findViewById(R.id.requestXuberBtnApr);
        use_wallet = (MyCheckbox) rootView.findViewById(R.id.use_wallet);
        eta_time = (MyBoldTextView) rootView.findViewById(R.id.eta_time);
        lblSurgePrice = (MyBoldTextView) rootView.findViewById(R.id.lblSurgePrice);
        lineView = (View) rootView.findViewById(R.id.lineView);
        schedule = (ImageView) rootView.findViewById(R.id.schedule);

        //Schedule Layout
        scheduleDate = (MyBoldTextView) rootView.findViewById(R.id.scheduleDate);
        scheduleTime = (MyBoldTextView) rootView.findViewById(R.id.scheduleTime);
        scheduleBtn = (MyButton) rootView.findViewById(R.id.scheduleBtn);

        //ConfirmYourPickUp View Initialization
        requestXuberBtnConfirm = (MyButton) rootView.findViewById(R.id.requestXuberBtnConfirm);
        destinationAddress = (MyBoldTextView) rootView.findViewById(R.id.destinationAddress);


        //Waiting For provider View Initialization
        cancelRideBtn = (MyButton) rootView.findViewById(R.id.cancelRideBtn);
        rippleBackground = (RippleBackground) rootView.findViewById(R.id.content);


        //Driver Accepted Layout View Initialization
        provider_image = (ImageView) rootView.findViewById(R.id.provider_image);
        provider_name = (MyBoldTextView) rootView.findViewById(R.id.provider_name);
        provider_rating = (RatingBar) rootView.findViewById(R.id.provider_rating);
        LayerDrawable drawable = (LayerDrawable) provider_rating.getProgressDrawable();
        drawable.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        drawable.getDrawable(1).setColorFilter(Color.parseColor("#FFAB00"), PorterDuff.Mode.SRC_ATOP);
        drawable.getDrawable(2).setColorFilter(Color.parseColor("#FFAB00"), PorterDuff.Mode.SRC_ATOP);
        serviceRequestedImg = (ImageView) rootView.findViewById(R.id.serviceRequestedImg);
        imgDestination = (ImageView) rootView.findViewById(R.id.imgDestination);
        btnDone = (MyButton) rootView.findViewById(R.id.btnDone);
        serviceRequestedName = (MyBoldTextView) rootView.findViewById(R.id.serviceRequestedName);
        statusTxt = (MyBoldTextView) rootView.findViewById(R.id.statusTxt);
        AfterAcceptButtonLayout = (LinearLayout) rootView.findViewById(R.id.AfterAcceptButtonLayout);
        AfterAcceptStatusLayout = (LinearLayout) rootView.findViewById(R.id.AfterAcceptStatusLayout);
        lnrProviderPopup = (LinearLayout) rootView.findViewById(R.id.lnrProviderPopup);
        call_btn = (Button) rootView.findViewById(R.id.call_btn);
        cancel_trip = (Button) rootView.findViewById(R.id.cancel_trip);
        modelandNumber = (MyBoldTextView) rootView.findViewById(R.id.modelandNumber);

        //Invoice Layout View Initialization
        base_price = (MyBoldTextView) rootView.findViewById(R.id.base_price);
        extra_price = (MyBoldTextView) rootView.findViewById(R.id.extra_price);
        tax_price = (MyBoldTextView) rootView.findViewById(R.id.tax_price);
        total_price = (MyBoldTextView) rootView.findViewById(R.id.total_price);
        paymentTypeImgInvoice = (ImageView) rootView.findViewById(R.id.paymentTypeImgInvoice);
        paymentTypeInvoice = (MyBoldTextView) rootView.findViewById(R.id.paymentTypeInvoice);
        changePaymentOptionInvoice = (MyBoldTextView) rootView.findViewById(R.id.changePaymentOptionInvoice);
        pay_now = (MyButton) rootView.findViewById(R.id.pay_now);
        distance_price = (MyBoldTextView) rootView.findViewById(R.id.distance_price);

        //RateYourProvider View Initialization
        rateProviderName = (MyBoldTextView) rootView.findViewById(R.id.rateProviderName);
        rateProviderImg = (ImageView) rootView.findViewById(R.id.rateProviderImg);
        rateProviderRating = (RatingBar) rootView.findViewById(R.id.rateProviderRating);
        comments = (EditText) rootView.findViewById(R.id.comments);
        submitReview = (MyButton) rootView.findViewById(R.id.submitReview);


        //Resize Marker
        markerBitmapDraw = (BitmapDrawable) getResources().getDrawable(R.drawable.user_marker);
        markerBitmap = markerBitmapDraw.getBitmap();
        resizedMarker = Bitmap.createScaledBitmap(markerBitmap, 100, 130, false);

        //Service list recyclerView
        serviceListRecyclerView = (RecyclerView) rootView.findViewById(R.id.serviceList);

        // service popup
        lblBasePrice = (MyBoldTextView) rootView.findViewById(R.id.lblBasePrice);
        lblServiceName = (MyBoldTextView) rootView.findViewById(R.id.lblServiceName);
        lblPriceKm = (MyBoldTextView) rootView.findViewById(R.id.lblPriceKm);
        lblPriceMin = (MyBoldTextView) rootView.findViewById(R.id.lblPriceMin);
        lnrPriceBase = (LinearLayout) rootView.findViewById(R.id.lnrPriceBase);
        lnrPricemin = (LinearLayout) rootView.findViewById(R.id.lnrPricemin);
        lnrPricekm = (LinearLayout) rootView.findViewById(R.id.lnrPricekm);
        lnrHidePopup = (LinearLayout) rootView.findViewById(R.id.lnrHidePopup);
        imgProviderPopup = (ImageView) rootView.findViewById(R.id.imgProviderPopup);
    }


    private void gotoAddCard() {
        Intent mainIntent = new Intent(activity, AddCard.class);
        startActivityForResult(mainIntent, ADD_CARD_CODE);
    }

    private void getCards() {
        Ion.with(this)
                .load(URLHelper.CARD_PAYMENT_LIST)
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .addHeader("Authorization", SharedHelper.getKey(context, "token_type") + " " + SharedHelper.getKey(context, "access_token"))
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<com.koushikdutta.ion.Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, com.koushikdutta.ion.Response<String> response) {
                        // response contains both the headers and the string result
                        try {
                            if (response.getHeaders().code() == 200) {
                                try {
                                    JSONArray jsonArray = new JSONArray(response.getResult());
                                    if (jsonArray.length() > 0) {
                                        CardInfo cardInfo = new CardInfo();
                                        cardInfo.setCardId("CASH");
                                        cardInfo.setCardType("CASH");
                                        cardInfo.setLastFour("CASH");
                                        cardInfoArrayList.add(cardInfo);
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject cardObj = jsonArray.getJSONObject(i);
                                            cardInfo = new CardInfo();
                                            cardInfo.setCardId(cardObj.optString("card_id"));
                                            cardInfo.setCardType(cardObj.optString("brand"));
                                            cardInfo.setLastFour(cardObj.optString("last_four"));
                                            cardInfoArrayList.add(cardInfo);
                                        }
                                    }

                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        } catch (Exception e2) {
                            e2.printStackTrace();
                            CardInfo cardInfo = new CardInfo();
                            cardInfo.setCardId("CASH");
                            cardInfo.setCardType("CASH");
                            cardInfo.setLastFour("CASH");
                            cardInfoArrayList.add(cardInfo);
                        }
                    }
                });

    }


    private void showChooser() {

        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle(getString(R.string.choose_payment));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, R.layout.custom_tv);
        for (int j = 0; j < cardInfoArrayList.size(); j++) {
            String card;
            if (cardInfoArrayList.get(j).getLastFour().equals("CASH")) {
                card = "CASH";
            } else {
                card = "XXXX-XXXX-XXXX-" + cardInfoArrayList.get(j).getLastFour();
            }
            arrayAdapter.add(card);
        }
        builderSingle.setNegativeButton(
                "cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builderSingle.setAdapter(
                arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getCardDetailsForPayment(cardInfoArrayList.get(which));
                        dialog.dismiss();
                    }
                });
        builderSingle.show();
    }


    private void getCardDetailsForPayment(CardInfo cardInfo) {

        if (cardInfo.getLastFour().equals("CASH")) {
            SharedHelper.putKey(context, "payment_mode", "CASH");
            paymentTypeImg.setImageResource(R.drawable.money_icon);
            paymentType.setText("CASH");
        } else {
            SharedHelper.putKey(context, "card_id", cardInfo.getCardId());
            SharedHelper.putKey(context, "payment_mode", "CARD");
            paymentTypeImg.setImageResource(R.drawable.visa);
            paymentType.setText("XXXX-XXXX-XXXX-" + cardInfo.getLastFour());
        }
    }

    public void payNow() {

        customDialog = new CustomDialog(context);
        customDialog.setCancelable(false);
        customDialog.show();

        JSONObject object = new JSONObject();
        try {
            object.put("request_id", SharedHelper.getKey(context, "request_id"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.PAY_NOW_API, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v("PayNowRequestResponse", response.toString());
                customDialog.dismiss();
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
                            }

                        } else if (response.statusCode == 401) {
                            refreshAccessToken("SEND_REQUEST");
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


    public void submitReviewCall() {

        customDialog = new CustomDialog(context);
        customDialog.setCancelable(false);
        customDialog.show();

        JSONObject object = new JSONObject();
        try {
            object.put("request_id", SharedHelper.getKey(context, "request_id"));
            object.put("rating", Math.round(pro_rating));
            object.put("comment", "" + comments.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.RATE_PROVIDER_API, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v("SubmitRequestResponse", response.toString());
                customDialog.dismiss();
                flowValue = 1;
                destination.setText("");
                StatusHandler();
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
                            }

                        } else if (response.statusCode == 401) {
                            refreshAccessToken("SEND_REQUEST");
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

    public void sendRequest() {

        customDialog = new CustomDialog(context);
        customDialog.setCancelable(false);
        customDialog.show();

        JSONObject object = new JSONObject();
        try {
            object.put("s_latitude", SharedHelper.getKey(context, "source_latitude"));
            object.put("s_longitude", SharedHelper.getKey(context, "source_longitude"));
            object.put("d_latitude", SharedHelper.getKey(context, "destination_latitude"));
            object.put("d_longitude", SharedHelper.getKey(context, "destination_longitude"));
            object.put("service_type", SharedHelper.getKey(context, "service_type"));
            object.put("distance", SharedHelper.getKey(context, "distance"));

            object.put("schedule_date", scheduledDate);
            object.put("schedule_time", scheduledTime);

            if (use_wallet.isChecked()) {
                object.put("use_wallet", 1);
            } else {
                object.put("use_wallet", 0);
            }
            if (SharedHelper.getKey(context, "payment_mode").equals("CASH")) {
                object.put("payment_mode", SharedHelper.getKey(context, "payment_mode"));
            } else {
                object.put("payment_mode", SharedHelper.getKey(context, "payment_mode"));
                object.put("card_id", SharedHelper.getKey(context, "card_id"));
            }
            Log.e("SendRequestInput", "" + object.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        TranxitApplication.getInstance().cancelRequestInQueue("send_request");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.SEND_REQUEST_API, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v("SendRequestResponse", response.toString());
                customDialog.dismiss();
                if (response.optString("request_id", "").equals("")) {
                    displayMessage(response.optString("message"));
                    StatusHandler();
                } else {
                    flowValue = 6;
                    SharedHelper.putKey(context, "current_status", "");
                    SharedHelper.putKey(context, "request_id", "" + response.optString("request_id"));
                }
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
                                displayMessage(errorObj.optString("error"));
                            } catch (Exception e) {
                                displayMessage(getString(R.string.something_went_wrong));
                            }
                            StatusHandler();
                        } else if (response.statusCode == 401) {
                            refreshAccessToken("SEND_REQUEST");
                        } else if (response.statusCode == 422) {

                            json = trimMessage(new String(response.data));
                            if (json != "" && json != null) {
                                displayMessage(json);
                            } else {
                                displayMessage(getString(R.string.please_try_again));
                            }
                            StatusHandler();
                        } else if (response.statusCode == 503) {
                            displayMessage(getString(R.string.server_down));
                            StatusHandler();
                        } else {
                            displayMessage(getString(R.string.please_try_again));
                            StatusHandler();
                        }

                    } catch (Exception e) {
                        displayMessage(getString(R.string.something_went_wrong));
                        StatusHandler();
                    }

                } else {
                    displayMessage(getString(R.string.please_try_again));
                    StatusHandler();
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


    public void cancelRequest() {

        customDialog = new CustomDialog(context);
        customDialog.setCancelable(false);
        customDialog.show();
        JSONObject object = new JSONObject();
        try {
            object.put("request_id", SharedHelper.getKey(context, "request_id"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.CANCEL_REQUEST_API, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v("CancelRequestResponse", response.toString());
                customDialog.dismiss();
                SharedHelper.putKey(context, "request_id", "");
                flowValue = 1;
                PreviousStatus = "";
                StatusHandler();
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
                            }
                            StatusHandler();
                        } else if (response.statusCode == 401) {
                            refreshAccessToken("CANCEL_REQUEST");
                        } else if (response.statusCode == 422) {

                            json = trimMessage(new String(response.data));
                            if (json != "" && json != null) {
                                displayMessage(json);
                            } else {
                                displayMessage(getString(R.string.please_try_again));
                            }
                            StatusHandler();
                        } else if (response.statusCode == 503) {
                            displayMessage(getString(R.string.server_down));
                            StatusHandler();
                        } else {
                            displayMessage(getString(R.string.please_try_again));
                            StatusHandler();
                        }

                    } catch (Exception e) {
                        displayMessage(getString(R.string.something_went_wrong));
                        StatusHandler();
                    }

                } else {
                    displayMessage(getString(R.string.please_try_again));
                    StatusHandler();
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

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    //Toast.makeText(SignInActivity.this, "PERMISSION_GRANTED", Toast.LENGTH_SHORT).show();
                    setUpMapIfNeeded();
                    MapsInitializer.initialize(getActivity());
                } else {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
                break;
            case 2:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    //Toast.makeText(SignInActivity.this, "PERMISSION_GRANTED", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + SharedHelper.getKey(context, "provider_mobile_no")));
                    startActivity(intent);
                } else {
                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @SuppressWarnings("MissingPermission")
    private void setupMap() {

        mMap.setMyLocationEnabled(true);
        mMap.setBuildingsEnabled(true);

        //noinspection deprecation

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if (value == 0) {
                    LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(myLocation).zoom(16).build();
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    mMap.setPadding(0, 0, 0, 0);
                    mMap.getUiSettings().setZoomControlsEnabled(true);
                    mMap.getUiSettings().setMyLocationButtonEnabled(true);
                    mMap.getUiSettings().setMapToolbarEnabled(false);
                    mMap.getUiSettings().setCompassEnabled(false);
                    currentAddress = getCompleteAddressString(latitude, longitude);
//                    txt_Location_Search.setText(currentAddress);
                    SharedHelper.putKey(context, "currentAddress", "" + currentAddress);
                    SharedHelper.putKey(context, "source", "" + currentAddress);
                    SharedHelper.putKey(context, "source_latitude", "" + latitude);
                    SharedHelper.putKey(context, "source_longitude", "" + longitude);
                    HomeLayout_source1.setText(currentAddress);
                    value++;
                }
            }
        });

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                if(strPickLocation.equalsIgnoreCase("yes")) {
                    cmPosition = cameraPosition;
                }
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("centerLat",cmPosition.target.latitude+"");
                Log.i("centerLong",cmPosition.target.longitude+"");

                Geocoder geocoder = null;
                List<Address> addresses;
                geocoder = new Geocoder(getActivity(), Locale.getDefault());

                String city = "", state = "", address = "";

                try {
                    addresses = geocoder.getFromLocation(cmPosition.target.latitude, cmPosition.target.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    address = addresses.get(0).getAddressLine(0);
                    city = addresses.get(0).getLocality();
                    state = addresses.get(0).getAdminArea();; // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(strPickType.equalsIgnoreCase("source")){
                    SharedHelper.putKey(context, "destination_latitude", "" + cmPosition.target.latitude);
                    SharedHelper.putKey(context, "destination_longitude", "" + cmPosition.target.longitude);
                    SharedHelper.putKey(context, "source", "" + address+","+city+","+state);
                }else{
                    SharedHelper.putKey(context, "source_latitude", "" + cmPosition.target.latitude);
                    SharedHelper.putKey(context, "source_longitude", "" + cmPosition.target.longitude);
                    SharedHelper.putKey(context, "destination", "" + address+","+city+","+state);
                }

                setValuesForSourceAndDestination();
                imgDestination.setVisibility(View.GONE);
                btnDone.setVisibility(View.GONE);
                flowValue = 2;
                StatusHandler();
                strPickLocation = "";
                strPickType = "";

                CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(cmPosition.target.latitude,
                        cmPosition.target.longitude));
                CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
                mMap.moveCamera(center);
                mMap.moveCamera(zoom);
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return true;
            }
        });

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

            @Override
            public void onMarkerDragStart(Marker marker) {
                Log.e("Postion ===>", "" + marker.getPosition());
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                Log.e("Postion ===>", "" + marker.getPosition());
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                String title = "";
                double lat, lng;
                if (marker.getTitle() != null) {
                    title = marker.getTitle();
                    if (sourceMarker != null && title.equalsIgnoreCase("Source")) {
                        LatLng markerLocation = sourceMarker.getPosition();
                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(getActivity(), Locale.getDefault());

                        SharedHelper.putKey(context, "source_latitude", "" + markerLocation.latitude);
                        SharedHelper.putKey(context, "source_longitude", "" + markerLocation.longitude);

                        try {
                            addresses = geocoder.getFromLocation(markerLocation.latitude, markerLocation.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                            String city = addresses.get(0).getLocality();
                            String state = addresses.get(0).getAdminArea();; // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                            SharedHelper.putKey(context, "source", "" + address+","+city+","+state);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else if (destinationMarker != null && title.equalsIgnoreCase("Destination")) {
                        LatLng markerLocation = destinationMarker.getPosition();
                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(getActivity(), Locale.getDefault());
                        SharedHelper.putKey(context, "destination_latitude", "" + markerLocation.latitude);
                        SharedHelper.putKey(context, "destination_longitude", "" + markerLocation.longitude);

                        try {
                            addresses = geocoder.getFromLocation(markerLocation.latitude, markerLocation.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                            String city = addresses.get(0).getLocality();
                            String state = addresses.get(0).getAdminArea();; // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                            SharedHelper.putKey(context, "destination", "" + address+","+city+","+state);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    setValuesForSourceAndDestination();
                }
            }
        });


    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getActivity(), R.raw.style_json));

            if (!success) {
                Log.e("Map:Style", "Style parsing failed.");
            } else {
                Log.e("Map:Style", "Style Applied.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("Map:Style", "Can't find style. Error: ", e);
        }
        mMap = googleMap;
        // do other tasks here
        setupMap();
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            FragmentManager fm = getChildFragmentManager();
            mapFragment = ((SupportMapFragment) fm.findFragmentById(R.id.provider_map));
            mapFragment.getMapAsync(this);
        }

        if (mMap != null) {
            setupMap();
        }
    }


    public void getServiceList() {

        customDialog = new CustomDialog(context);
        customDialog.setCancelable(false);
        customDialog.show();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URLHelper.GET_SERVICE_LIST_API, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Log.v("GetServices", response.toString());
                customDialog.dismiss();
                if (response.length() > 0) {
                    serviceListAdapter = new ServiceListAdapter(response);
                    // serviceListRecyclerView.setHasFixedSize(true);
                    serviceListRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
                    serviceListRecyclerView.setAdapter(serviceListAdapter);
                    setValuesForSourceAndDestination();
                } else {
                    displayMessage(getString(R.string.no_service));
                }
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
                            }
                            flowValue = 1;
                            StatusHandler();
                        } else if (response.statusCode == 401) {
                            refreshAccessToken("SERVICE_LIST");
                        } else if (response.statusCode == 422) {

                            json = trimMessage(new String(response.data));
                            if (json != "" && json != null) {
                                displayMessage(json);
                            } else {
                                displayMessage(getString(R.string.please_try_again));
                            }
                            flowValue = 1;
                            StatusHandler();
                        } else if (response.statusCode == 503) {
                            displayMessage(getString(R.string.server_down));
                            flowValue = 1;
                            StatusHandler();
                        } else {
                            displayMessage(getString(R.string.please_try_again));
                            flowValue = 1;
                            StatusHandler();
                        }

                    } catch (Exception e) {
                        displayMessage(getString(R.string.something_went_wrong));
                        flowValue = 1;
                        StatusHandler();
                    }

                } else {
                    displayMessage(getString(R.string.please_try_again));
                    flowValue = 1;
                    StatusHandler();
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

    public void getApproximateFare() {
        customDialog = new CustomDialog(context);
        customDialog.setCancelable(false);
        customDialog.show();
        JSONObject object = new JSONObject();
        String constructedURL = URLHelper.ESTIMATED_FARE_DETAILS_API + "?s_latitude=" + SharedHelper.getKey(context, "source_latitude") + "&s_longitude=" + SharedHelper.getKey(context, "source_longitude") + "&d_latitude=" + SharedHelper.getKey(context, "destination_latitude") + "&d_longitude=" + SharedHelper.getKey(context, "destination_longitude") + "&service_type=" + SharedHelper.getKey(context, "service_type");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, constructedURL, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v("ApproximateResponse", response.toString());
                SharedHelper.putKey(context, "estimated_fare", response.optString("estimated_fare"));
                SharedHelper.putKey(context, "distance", response.optString("distance"));
                SharedHelper.putKey(context, "eta_time", response.optString("time"));
                setValuesForApproximateLayout();
                double wallet_balance = response.optDouble("wallet_balance");
                if (!Double.isNaN(wallet_balance) && wallet_balance > 0) {
                    lineView.setVisibility(View.VISIBLE);
                    use_wallet.setVisibility(View.VISIBLE);
                } else {
                    lineView.setVisibility(View.GONE);
                    use_wallet.setVisibility(View.GONE);
                }
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
                            }
                            flowValue = 2;
                            StatusHandler();
                        } else if (response.statusCode == 401) {
                            refreshAccessToken("APPROXIMATE_RATE");
                        } else if (response.statusCode == 422) {
                            json = trimMessage(new String(response.data));
                            if (json != "" && json != null) {
                                displayMessage(json);
                            } else {
                                displayMessage(getString(R.string.please_try_again));
                            }
                            flowValue = 2;
                            StatusHandler();
                        } else if (response.statusCode == 503) {
                            displayMessage(getString(R.string.server_down));
                            flowValue = 2;
                            StatusHandler();
                        } else {
                            displayMessage(getString(R.string.please_try_again));
                            flowValue = 2;
                            StatusHandler();
                        }

                    } catch (Exception e) {
                        displayMessage(getString(R.string.something_went_wrong));
                        flowValue = 2;
                        StatusHandler();
                    }

                } else {
                    displayMessage(getString(R.string.please_try_again));
                    flowValue = 2;
                    StatusHandler();
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
                if (tag.equalsIgnoreCase("SERVICE_LIST")) {
                    getServiceList();
                } else if (tag.equalsIgnoreCase("APPROXIMATE_RATE")) {
                    getApproximateFare();
                } else if (tag.equalsIgnoreCase("SEND_REQUEST")) {
                    sendRequest();
                } else if (tag.equalsIgnoreCase("CANCEL_REQUEST")) {
                    cancelRequest();
                } else if(tag.equalsIgnoreCase("PROVIDERS_LIST")){
                    getProvidersList();
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

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current", "" + strReturnedAddress.toString());
            } else {
                Log.w("My Current", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current", "Canont get Address!");
        }
        return strAdd;
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(getString(R.string.connect_to_network))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.connect_to_wifi), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton(getString(R.string.quit), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        activity.finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void noProvidersFound() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(getString(R.string.no_providers_found))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.retry), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sendRequest();
                        StatusHandler();
                    }
                })
                .setNegativeButton(getString(R.string.try_later), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        flowValue = 1;
                        StatusHandler();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void displayMessage(String toastString) {
        Snackbar.make(getView(), toastString, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

    public void GoToBeginActivity() {
        Intent mainIntent = new Intent(activity, BeginScreen.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        activity.finish();
    }

    public void setValuesForSourceAndDestination() {
        if (isInternet) {
            if(!SharedHelper.getKey(context, "source_latitude").equalsIgnoreCase("") &&
                    !SharedHelper.getKey(context, "destination_latitude").equalsIgnoreCase(""))
            {
                String s = SharedHelper.getKey(context, "source");
                String d = SharedHelper.getKey(context, "destination");
                source.setText(SharedHelper.getKey(context, "source"));
                destination.setText(SharedHelper.getKey(context, "destination"));
                sourceLatLng = new LatLng(Double.parseDouble(SharedHelper.getKey(context, "source_latitude")), Double.parseDouble(SharedHelper.getKey(context, "source_longitude")));
                destLatLng = new LatLng(Double.parseDouble(SharedHelper.getKey(context, "destination_latitude")), Double.parseDouble(SharedHelper.getKey(context, "destination_longitude")));
                Log.e("LatLng", "Source:" + sourceLatLng + " Destination: " + destLatLng);
//            CameraPosition cameraPosition = new CameraPosition.Builder().target(destLatLng).zoom(16).build();
//            MarkerOptions options = new MarkerOptions();
//            options.position(destLatLng).isDraggable();
//            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                String url = getDirectionsUrl(sourceLatLng, destLatLng);
                DownloadTask downloadTask = new DownloadTask();
                // Start downloading json data from Google Directions API
                downloadTask.execute(url);
            }
        }
    }

    public void reCreateMap() {
        if (mMap != null) {
//            mMap.clear();
            sourceLatLng = new LatLng(Double.parseDouble(SharedHelper.getKey(context, "source_latitude")), Double.parseDouble(SharedHelper.getKey(context, "source_longitude")));
            destLatLng = new LatLng(Double.parseDouble(SharedHelper.getKey(context, "destination_latitude")), Double.parseDouble(SharedHelper.getKey(context, "destination_longitude")));
            Log.e("LatLng", "Source:" + sourceLatLng + " Destination: " + destLatLng);
//            CameraPosition cameraPosition = new CameraPosition.Builder().target(destLatLng).zoom(16).build();
//            MarkerOptions options = new MarkerOptions();
//            options.position(destLatLng).isDraggable();
//            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            String url = getDirectionsUrl(sourceLatLng, destLatLng);
            DownloadTask downloadTask = new DownloadTask();
            // Start downloading json data from Google Directions API
            downloadTask.execute(url);
        }
    }

    public void setValuesForApproximateLayout() {
        if (isInternet) {
            approximateAmount.setText(SharedHelper.getKey(context, "currency") + "" + SharedHelper.getKey(context, "estimated_fare"));
            eta_time.setText(SharedHelper.getKey(context, "eta_time"));
            customDialog.dismiss();
        }
    }

    public void setVAluesForConfirmYourPickUp() {
        destinationAddress.setText(SharedHelper.getKey(context, "source"));
    }

//    public void setValueForSchedule(){
//        String formattedDate = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
//        String formattedTime = new SimpleDateFormat("HH:mm a").format(Calendar.getInstance().getTime());
//        scheduleDate.setText(formattedDate);
//        scheduleTime.setText(formattedTime);
//    }

    private void checkStatus() {
        Log.w("Handler", "Inside");

        if (isInternet) {

            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URLHelper.REQUEST_STATUS_CHECK_API, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Log.w("Response", "" + response.toString());

                    if (response.optJSONArray("data") != null && response.optJSONArray("data").length() > 0) {
                        Log.w("response", "not null");
                        try {
                            JSONArray requestStatusCheck = response.optJSONArray("data");
                            JSONObject requestStatusCheckObject = requestStatusCheck.getJSONObject(0);
                            String status = requestStatusCheckObject.optString("status");
                            SharedHelper.putKey(context, "source_latitude", "" + requestStatusCheckObject.optString("s_latitude"));
                            SharedHelper.putKey(context, "source_longitude", "" + requestStatusCheckObject.optString("s_longitude"));
                            SharedHelper.putKey(context, "destination_latitude", "" + requestStatusCheckObject.optString("d_latitude"));
                            SharedHelper.putKey(context, "destination_longitude", "" + requestStatusCheckObject.optString("d_longitude"));
                            // surge price
                            if(requestStatusCheckObject.optString("surge").equalsIgnoreCase("1")){
                                lblSurgePrice.setVisibility(View.VISIBLE);
                            }else{
                                lblSurgePrice.setVisibility(View.GONE);
                            }

                            Log.e("PreviousStatus", ""+PreviousStatus);

                            if (!PreviousStatus.equals(status)) {
                                mMap.clear();
                                PreviousStatus = status;
                                clearVisibility();
                                SharedHelper.putKey(context, "request_id", "" + requestStatusCheckObject.optString("id"));

                                reCreateMap();
                                Log.w("ResponseStatus", "SavedCurrentStatus: " + CurrentStatus + " Status: " + status);
                                switch (status) {
                                    case "SEARCHING":
                                        //WaitingForProvider.setVisibility(View.VISIBLE);
                                        show(WaitingForProvider);
                                        rippleBackground.startRippleAnimation();
                                        strTag = "search_completed";
                                        break;
                                    case "CANCELLED":
                                        strTag = "";
                                        break;
                                    case "ACCEPTED":
                                        strTag = "";
                                        try {
                                            JSONObject provider = requestStatusCheckObject.getJSONObject("provider");
                                            JSONObject service_type = requestStatusCheckObject.getJSONObject("service_type");
                                            JSONObject provider_service = requestStatusCheckObject.getJSONObject("provider_service");
                                            SharedHelper.putKey(context, "provider_mobile_no", "" + provider.optString("mobile"));
                                            provider_name.setText(provider.optString("first_name") + " " + provider.optString("last_name"));
                                            //Glide.with(activity).load(URLHelper.base+"storage/"+provider.optString("avatar")).placeholder(R.drawable.loading).error(R.drawable.loading).dontAnimate().into(provider_image);
                                            Picasso.with(context).load(URLHelper.base + "storage/" + provider.optString("avatar")).placeholder(R.drawable.loading).error(R.drawable.ic_dummy_user).into(provider_image);
                                            serviceRequestedName.setText(service_type.optString("name"));
                                            modelandNumber.setText(provider_service.optString("service_model") + "\n" + provider_service.optString("service_number"));
                                            //Glide.with(activity).load(service_type.optString("image")).placeholder(R.drawable.loading).error(R.drawable.loading).dontAnimate().into(serviceRequestedImg);
                                            Picasso.with(context).load(service_type.optString("image")).placeholder(R.drawable.loading).error(R.drawable.loading).into(serviceRequestedImg);
                                            provider_rating.setRating(Float.parseFloat(provider.optString("rating")));
                                            AfterAcceptStatusLayout.setVisibility(View.GONE);
                                            //ProviderAcceptedLayout.setVisibility(View.VISIBLE);
                                            show(ProviderAcceptedLayout);

                                            HomeLayout.setVisibility(View.VISIBLE);
                                            HomeRelativeLayout.setVisibility(View.GONE);
                                            HomeLayout_menu_icon.setVisibility(View.VISIBLE);
                                            ScheduleLayout.setVisibility(View.GONE);


                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    case "STARTED":
                                        strTag = "";
                                        try {
                                            JSONObject provider = requestStatusCheckObject.getJSONObject("provider");
                                            JSONObject service_type = requestStatusCheckObject.getJSONObject("service_type");
                                            JSONObject provider_service = requestStatusCheckObject.getJSONObject("provider_service");
                                            SharedHelper.putKey(context, "provider_mobile_no", "" + provider.optString("mobile"));
                                            provider_name.setText(provider.optString("first_name") + " " + provider.optString("last_name"));
                                            //Glide.with(activity).load(URLHelper.base+"storage/"+provider.optString("avatar")).placeholder(R.drawable.loading).error(R.drawable.loading).dontAnimate().into(provider_image);
                                            Picasso.with(context).load(URLHelper.base + "storage/" + provider.optString("avatar")).placeholder(R.drawable.loading).error(R.drawable.ic_dummy_user).into(provider_image);
                                            serviceRequestedName.setText(service_type.optString("name"));
                                            modelandNumber.setText(provider_service.optString("service_model") + "\n" + provider_service.optString("service_number"));
                                            //Glide.with(activity).load(service_type.optString("image")).placeholder(R.drawable.loading).error(R.drawable.loading).dontAnimate().into(serviceRequestedImg);
                                            Picasso.with(context).load(service_type.optString("image")).placeholder(R.drawable.loading).error(R.drawable.loading).into(serviceRequestedImg);
                                            provider_rating.setRating(Float.parseFloat(provider.optString("rating")));

                                            AfterAcceptStatusLayout.setVisibility(View.GONE);
                                            // ProviderAcceptedLayout.setVisibility(View.VISIBLE);
                                            show(ProviderAcceptedLayout);
                                            HomeLayout.setVisibility(View.VISIBLE);
                                            HomeRelativeLayout.setVisibility(View.GONE);
                                            HomeLayout_menu_icon.setVisibility(View.VISIBLE);

                                            HomeLayout.setVisibility(View.VISIBLE);
                                            SharedHelper.putKey(context, "current_status", "");
                                            Intent intent = new Intent(getActivity(), HistoryActivity.class);
                                            intent.putExtra("tag","upcoming");
                                            startActivity(intent);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    case "ARRIVED":
                                        strTag = "";
                                        Log.e("MyTest", "ARRIVED");
                                        try {
                                            Log.e("MyTest", "ARRIVED TRY");
                                            JSONObject provider = requestStatusCheckObject.getJSONObject("provider");
                                            JSONObject service_type = requestStatusCheckObject.getJSONObject("service_type");
                                            JSONObject provider_service = requestStatusCheckObject.getJSONObject("provider_service");
                                            provider_name.setText(provider.optString("first_name") + " " + provider.optString("last_name"));
                                            //Glide.with(activity).load(URLHelper.base+"storage/"+provider.optString("avatar")).placeholder(R.drawable.loading).error(R.drawable.loading).dontAnimate().into(provider_image);
                                            Picasso.with(context).load(URLHelper.base + "storage/" + provider.optString("avatar")).placeholder(R.drawable.loading).error(R.drawable.ic_dummy_user).into(provider_image);
                                            serviceRequestedName.setText(service_type.optString("name"));
                                            modelandNumber.setText(provider_service.optString("service_model") + "\n" + provider_service.optString("service_number"));
                                            //Glide.with(activity).load(service_type.optString("image")).placeholder(R.drawable.loading).error(R.drawable.loading).dontAnimate().into(serviceRequestedImg);
                                            Picasso.with(context).load(service_type.optString("image")).placeholder(R.drawable.loading).error(R.drawable.loading).into(serviceRequestedImg);
                                            statusTxt.setText(getString(R.string.arrived));
                                            provider_rating.setRating(Float.parseFloat(provider.optString("rating")));
                                            AfterAcceptButtonLayout.setVisibility(View.GONE);
                                            //  ProviderAcceptedLayout.setVisibility(View.VISIBLE);
                                            show(ProviderAcceptedLayout);
                                            AfterAcceptStatusLayout.setVisibility(View.VISIBLE);
                                            HomeLayout.setVisibility(View.VISIBLE);
                                            HomeRelativeLayout.setVisibility(View.GONE);
                                            HomeLayout_menu_icon.setVisibility(View.VISIBLE);
                                        } catch (Exception e) {
                                            Log.e("MyTest", "ARRIVED CATCH");
                                            e.printStackTrace();
                                        }
                                        break;
                                    case "PICKEDUP":
                                        strTag = "";
                                        try {
                                            JSONObject provider = requestStatusCheckObject.getJSONObject("provider");
                                            JSONObject service_type = requestStatusCheckObject.getJSONObject("service_type");
                                            JSONObject provider_service = requestStatusCheckObject.getJSONObject("provider_service");
                                            provider_name.setText(provider.optString("first_name") + " " + provider.optString("last_name"));
                                            //Glide.with(activity).load(URLHelper.base+"storage/"+provider.optString("avatar")).placeholder(R.drawable.loading).error(R.drawable.loading).dontAnimate().into(provider_image);
                                            Picasso.with(context).load(URLHelper.base + "storage/" + provider.optString("avatar")).placeholder(R.drawable.loading).error(R.drawable.ic_dummy_user).into(provider_image);
                                            serviceRequestedName.setText(service_type.optString("name"));
                                            modelandNumber.setText(provider_service.optString("service_model") + "\n" + provider_service.optString("service_number"));
                                            //Glide.with(activity).load(service_type.optString("image")).placeholder(R.drawable.loading).error(R.drawable.loading).dontAnimate().into(serviceRequestedImg);
                                            Picasso.with(context).load(service_type.optString("image")).placeholder(R.drawable.loading).error(R.drawable.loading).into(serviceRequestedImg);
                                            statusTxt.setText(getString(R.string.picked_up));
                                            provider_rating.setRating(Float.parseFloat(provider.optString("rating")));
                                            AfterAcceptButtonLayout.setVisibility(View.GONE);
                                            // ProviderAcceptedLayout.setVisibility(View.VISIBLE);
                                            show(ProviderAcceptedLayout);
                                            AfterAcceptStatusLayout.setVisibility(View.VISIBLE);
                                            HomeLayout.setVisibility(View.VISIBLE);
                                            HomeRelativeLayout.setVisibility(View.GONE);
                                            HomeLayout_menu_icon.setVisibility(View.VISIBLE);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    case "DROPPED":
                                        strTag = "";
                                        try {
                                            JSONObject payment = requestStatusCheckObject.optJSONObject("payment");
                                            JSONObject provider = requestStatusCheckObject.optJSONObject("provider");
                                            String isPaid = requestStatusCheckObject.optString("paid");
                                            String paymentMode = requestStatusCheckObject.optString("payment_mode");
                                            base_price.setText(SharedHelper.getKey(context, "currency") + "" + payment.optString("fixed"));
                                            tax_price.setText(SharedHelper.getKey(context, "currency") + "" + payment.optString("tax"));
                                            distance_price.setText(payment.optString("distance"));
                                            total_price.setText(SharedHelper.getKey(context, "currency") + "" + payment.optString("total"));
                                            if (isPaid.equalsIgnoreCase("0") && paymentMode.equalsIgnoreCase("CASH")) {
                                                //InvoiceLayout.setVisibility(View.VISIBLE);
                                                show(InvoiceLayout);
                                                pay_now.setVisibility(View.GONE);
                                            } else if (isPaid.equalsIgnoreCase("0") && paymentMode.equalsIgnoreCase("CARD")) {
                                                //  InvoiceLayout.setVisibility(View.VISIBLE);
                                                show(InvoiceLayout);
                                            } else if (isPaid.equalsIgnoreCase("1")) {
                                                rateProviderName.setText(getString(R.string.rate_provider) + " " + provider.optString("first_name") + " " + provider.optString("last_name"));
                                                //Glide.with(activity).load(provider.optString("avatar")).placeholder(R.drawable.loading).error(R.drawable.loading).dontAnimate().into(rateProviderImg);
                                                Picasso.with(context).load(URLHelper.base + "storage/" + provider.optString("avatar")).placeholder(R.drawable.loading).error(R.drawable.ic_dummy_user).into(rateProviderImg);
                                                // RateYourProviderLayout.setVisibility(View.VISIBLE);
                                                show(RateYourProviderLayout);
                                            }
                                            HomeLayout.setVisibility(View.VISIBLE);
                                            HomeRelativeLayout.setVisibility(View.GONE);
                                            HomeLayout_menu_icon.setVisibility(View.VISIBLE);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        break;
                                    case "COMPLETED":
                                        strTag = "";
                                        try {
                                            JSONObject payment = requestStatusCheckObject.optJSONObject("payment");
                                            JSONObject provider = requestStatusCheckObject.optJSONObject("provider");
                                            String isPaid = requestStatusCheckObject.optString("paid");
                                            String paymentMode = requestStatusCheckObject.optString("payment_mode");
                                            base_price.setText(payment.optString("fixed"));
                                            tax_price.setText(payment.optString("tax"));
                                            distance_price.setText(payment.optString("distance"));
                                            total_price.setText(payment.optString("total"));
                                            if (isPaid.equalsIgnoreCase("0") && paymentMode.equalsIgnoreCase("CASH")) {
                                                // InvoiceLayout.setVisibility(View.VISIBLE);
                                                show(InvoiceLayout);
                                                pay_now.setVisibility(View.GONE);
                                            } else if (isPaid.equalsIgnoreCase("0") && paymentMode.equalsIgnoreCase("CARD")) {
                                                //  InvoiceLayout.setVisibility(View.VISIBLE);
                                                show(InvoiceLayout);
                                            } else if (isPaid.equalsIgnoreCase("1")) {
                                                rateProviderName.setText(getString(R.string.rate_provider) + " " + provider.optString("first_name") + " " + provider.optString("last_name"));
                                                //Glide.with(activity).load(URLHelper.base+"storage/"+provider.optString("avatar")).placeholder(R.drawable.loading).error(R.drawable.loading).dontAnimate().into(rateProviderImg);
                                                Picasso.with(context).load(URLHelper.base + "storage/" + provider.optString("avatar")).placeholder(R.drawable.loading).error(R.drawable.ic_dummy_user).into(rateProviderImg);
                                                //RateYourProviderLayout.setVisibility(View.VISIBLE);
                                                show(RateYourProviderLayout);
                                            }
                                            HomeLayout.setVisibility(View.VISIBLE);
                                            HomeRelativeLayout.setVisibility(View.GONE);
                                            HomeLayout_menu_icon.setVisibility(View.VISIBLE);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                }
                            }

                            if ("ACCEPTED".equals(status) || "STARTED".equals(status) || "ARRIVED".equals(status) || "PICKEDUP".equals(status) || "DROPPED".equals(status)) {
                                Log.e("Livenavigation", "" + status);
                                Log.v("Destination Current Lat",""+requestStatusCheckObject.getJSONObject("provider").optString("latitude"));
                                Log.v("Destination Current Lng",""+requestStatusCheckObject.getJSONObject("provider").optString("longitude"));
                                livenavigation(requestStatusCheckObject.getJSONObject("provider").optString("latitude"), requestStatusCheckObject.getJSONObject("provider").optString("longitude"));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            displayMessage(getString(R.string.something_went_wrong));
                        }


                    } else {
//                    if(flowValue == 5){
//                        clearVisibility();
//                        noProvidersFound();
//                    }
                        if(!strTag.equalsIgnoreCase("")){
                            HomeLayout.setVisibility(View.VISIBLE);
                            SharedHelper.putKey(context, "current_status", "");
                            Intent intent = new Intent(getActivity(), HistoryActivity.class);
                            intent.putExtra("tag","upcoming");
                            startActivity(intent);
                            strTag = "";
                        }
                        WaitingForProvider.setVisibility(View.GONE);
                        Log.w("response", "null");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.v("Error", error.toString());

                }
            }) {
                @Override
                public java.util.Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("X-Requested-With", "XMLHttpRequest");
                    Log.e("Authorization", ""+SharedHelper.getKey(context, "token_type") + " " + SharedHelper.getKey(context, "access_token"));

                    headers.put("Authorization", "" + SharedHelper.getKey(context, "token_type") + " " + SharedHelper.getKey(context, "access_token"));
                    return headers;
                }
            };

            TranxitApplication.getInstance().addToRequestQueue(jsonObjectRequest);

        } else {
            displayMessage(getString(R.string.oops_connect_your_internet));
        }
    }


    public void livenavigation(String lat, String lng) {
        Log.e("Livenavigation", "ProLat" + lat + " ProLng" + lng);
        Double proLat = Double.parseDouble(lat);
        Double proLng = Double.parseDouble(lng);
            if(once){
                once=false;
                LatLng latLng = new LatLng(proLat, proLng);
                // Showing the current location in Google Map
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                // Zoom in the Google Map
                mMap.moveCamera(CameraUpdateFactory.zoomTo(18));
                LatLng providerPosition = new LatLng(proLat,proLng);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(providerPosition);
                providerMarker = mMap.addMarker(markerOptions);
                providerMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.provider_location_icon));
            }
//            animateMarker(providerMarker, providerMarker.getPosition(), false);
        providerMarker.setPosition(new LatLng(proLat,proLng));
    }

    public float getBearing(LatLng oldPosition, LatLng newPosition) {
        double deltaLongitude = newPosition.longitude - oldPosition.longitude;
        double deltaLatitude = newPosition.latitude - oldPosition.latitude;
        double angle = (Math.PI * .5f) - Math.atan(deltaLatitude / deltaLongitude);

        if (deltaLongitude > 0) {
            return (float) angle;
        } else if (deltaLongitude < 0) {
            return (float) (angle + Math.PI);
        } else if (deltaLatitude < 0) {
            return (float) Math.PI;
        }

        return 0.0f;
    }

    public void StatusHandler() {
        if (isInternet) {

            if (flowValue == 1) {
                clearVisibility();
                if (mMap != null) {
                    mMap.clear();
                    value = 0;
                    setupMap();
                }
                sourceLayer.setVisibility(View.GONE);
                destination.setText(getString(R.string.where_to_go));
                HomeLayout.setVisibility(View.VISIBLE);
                txt_Location_Search.setText("");
                HomeRelativeLayout.setVisibility(View.VISIBLE);
                ScheduleLayout.setVisibility(View.GONE);
                SharedHelper.putKey(context, "destination_latitude", "");
                SharedHelper.putKey(context, "destination_longitude", "");
                SharedHelper.putKey(context, "destination", "");
                SharedHelper.putKey(context, "source_latitude", "");
                SharedHelper.putKey(context, "source_longitude", "");
                SharedHelper.putKey(context, "source", "");
            } else if (flowValue == 2) {
                clearVisibility();
                getServiceList();
                SourceAndDestination.setVisibility(View.VISIBLE);
                sourceLayer.setVisibility(View.VISIBLE);
                destinationLayer.setVisibility(View.VISIBLE);
                shadow_sd.setVisibility(View.VISIBLE);
                CommonBottomLayout.setVisibility(View.VISIBLE);
                ScheduleLayout.setVisibility(View.GONE);
                if(sourceMarker != null && destinationMarker != null){
                    sourceMarker.setDraggable(true);
                    destinationMarker.setDraggable(true);
                }
                HomeLayout.setVisibility(View.GONE);
            } else if (flowValue == 3) {
                clearVisibility();
                SourceAndDestination.setVisibility(View.VISIBLE);
                sourceLayer.setVisibility(View.GONE);
                destinationLayer.setVisibility(View.GONE);
                shadow_sd.setVisibility(View.GONE);
                HomeLayout.setVisibility(View.GONE);
                getApproximateFare();
                ApproximateLayout.setVisibility(View.VISIBLE);
                if(sourceMarker != null && destinationMarker != null){
                    sourceMarker.setDraggable(false);
                    destinationMarker.setDraggable(false);
                }
                ScheduleLayout.setVisibility(View.GONE);
            } else if (flowValue == 4) {
                clearVisibility();
                SourceAndDestination.setVisibility(View.VISIBLE);
                setVAluesForConfirmYourPickUp();
                ConfirmYourPickUp.setVisibility(View.VISIBLE);
                ScheduleLayout.setVisibility(View.GONE);
            } else if (flowValue == 5) {
                clearVisibility();
                SourceAndDestination.setVisibility(View.VISIBLE);
                sourceLayer.setVisibility(View.GONE);
                destinationLayer.setVisibility(View.GONE);
                ScheduleLayout.setVisibility(View.VISIBLE);
            }

        } else {
            displayMessage(getString(R.string.oops_connect_your_internet));
        }
    }

    public void clearVisibility() {
        HomeLayout.setVisibility(View.GONE);
        SourceAndDestination.setVisibility(View.GONE);
        CommonBottomLayout.setVisibility(View.GONE);
        ApproximateLayout.setVisibility(View.GONE);
        ConfirmYourPickUp.setVisibility(View.GONE);
        if (rippleBackground.isRippleAnimationRunning()) {
            rippleBackground.stopRippleAnimation();
        }
        WaitingForProvider.setVisibility(View.GONE);

        ProviderAcceptedLayout.setVisibility(View.GONE);
        InvoiceLayout.setVisibility(View.GONE);
        RateYourProviderLayout.setVisibility(View.GONE);
    }

    private String getDirectionsUrl(LatLng sourceLatLng, LatLng destLatLng) {

        // Origin of routelng;
        String str_origin = "origin=" + SharedHelper.getKey(context, "source_latitude") + "," + SharedHelper.getKey(context, "source_longitude");
        String str_dest = "destination=" + SharedHelper.getKey(context, "destination_latitude") + "," + SharedHelper.getKey(context, "destination_longitude");
        // Sensor enabled
        String sensor = "sensor=false";
        // Waypoints
        String waypoints = "";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + waypoints;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        Log.e("url", url.toString());
        return url;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE_DEST) {
            if (resultCode == Activity.RESULT_OK) {
                PlacePredictions placePredictions;
                placePredictions = (PlacePredictions) data.getSerializableExtra("Location Address");
                strPickLocation = data.getExtras().getString("pick_location");
                strPickType = data.getExtras().getString("type");
                if(strPickLocation.equalsIgnoreCase("yes")){
                    imgDestination.setVisibility(View.VISIBLE);
                    HomeLayout.setVisibility(View.GONE);
                    HomeRelativeLayout.setVisibility(View.GONE);
                    SourceAndDestination.setVisibility(View.GONE);
                    CommonBottomLayout.setVisibility(View.GONE);
                    sourceLayer.setVisibility(View.GONE);
                    destinationLayer.setVisibility(View.GONE);
                    ApproximateLayout.setVisibility(View.GONE);
                    RateYourProviderLayout.setVisibility(View.GONE);
                    btnDone.setVisibility(View.VISIBLE);
                }else {
                    if (placePredictions != null) {
                        if (!placePredictions.strDestAddress.equalsIgnoreCase("")) {
                            txt_Location_Search.setText(placePredictions.strDestAddress);
                            SharedHelper.putKey(context, "destination_latitude", "" + placePredictions.strDestLatitude);
                            SharedHelper.putKey(context, "destination_longitude", "" + placePredictions.strDestLongitude);
                            SharedHelper.putKey(context, "destination", "" + placePredictions.strDestAddress);
                            SharedHelper.putKey(context, "current_status", "2");
                        }
                        if (!placePredictions.strSourceAddress.equalsIgnoreCase("")) {
                            HomeLayout_source1.setText(placePredictions.strSourceAddress);
                            SharedHelper.putKey(context, "source_latitude", "" + placePredictions.strSourceLatitude);
                            SharedHelper.putKey(context, "source_longitude", "" + placePredictions.strSourceLongitude);
                            SharedHelper.putKey(context, "source", "" + placePredictions.strSourceAddress);

                            double latitude = Double.parseDouble(placePredictions.strSourceLatitude);
                            double longitude = Double.parseDouble(placePredictions.strSourceLongitude);

                            LatLng location = new LatLng(latitude, longitude);

                            mMap.clear();
                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(location)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.user_marker));

                            marker = mMap.addMarker(markerOptions);
                            CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(16).build();
                            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                        }
                        flowValue = 2;
                        StatusHandler();
                        getProvidersList();
                    }
                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                // TODO: Handle the error.
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE_SOURCE) {
            if (resultCode == Activity.RESULT_OK) {
                PlacePredictions placePredictions;
                placePredictions = (PlacePredictions) data.getSerializableExtra("Location Address");
                HomeLayout_source1.setText(placePredictions.strSourceAddress);
                SharedHelper.putKey(context, "source_latitude", "" + placePredictions.strSourceLatitude);
                SharedHelper.putKey(context, "source_longitude", "" + placePredictions.strSourceLongitude);
                SharedHelper.putKey(context, "source", "" + placePredictions.strSourceAddress);

                double latitude = Double.parseDouble(placePredictions.strSourceLatitude);
                double longitude = Double.parseDouble(placePredictions.strSourceLongitude);

                LatLng location = new LatLng(latitude, longitude);

                mMap.clear();
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(location)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.user_marker));

                marker = mMap.addMarker(markerOptions);
                CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(16).build();
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                // TODO: Handle the error.
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
        if (requestCode == ADD_CARD_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                boolean result = data.getBooleanExtra("isAdded", false);
                if (result) {
                    getCards();
                }
            }
        }
         if (requestCode == REQUEST_LOCATION) {

        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {

    }

    private class ServiceListAdapter extends RecyclerView.Adapter<ServiceListAdapter.MyViewHolder> {
        JSONArray jsonArray;
        private SparseBooleanArray selectedItems;

        public ServiceListAdapter(JSONArray array) {
            this.jsonArray = array;
        }


        @Override
        public ServiceListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            @SuppressLint("InflateParams") View view = LayoutInflater.from(getActivity()).inflate(R.layout.service_type_list_item, null);
            return new ServiceListAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ServiceListAdapter.MyViewHolder holder, final int position) {
            Log.e("Title: ", "" + jsonArray.optJSONObject(position).optString("name") + " Image: " + jsonArray.optJSONObject(position).optString("image") + " Grey_Image:" + jsonArray.optJSONObject(position).optString("grey_image"));

            if (jsonArray.optJSONObject(position).optString("status").equalsIgnoreCase("1")) {
                holder.serviceTitle.setText(jsonArray.optJSONObject(position).optString("name"));
                if (position == currentPostion) {
                    SharedHelper.putKey(context, "service_type", "" + jsonArray.optJSONObject(position).optString("id"));
                    Glide.with(activity).load(jsonArray.optJSONObject(position).optString("image")).placeholder(R.drawable.loading).dontAnimate().error(R.drawable.loading).into(holder.serviceImg);
                    //Picasso.with(context).load(jsonArray.optJSONObject(position).optString("image")).placeholder(R.drawable.loading).error(R.drawable.loading).into(holder.serviceImg);
                    holder.selector_background.setBackgroundResource(R.drawable.full_rounded_button);
                    holder.serviceTitle.setTextColor(getResources().getColor(R.color.text_color_white));
                } else {
                    Glide.with(activity).load(jsonArray.optJSONObject(position).optString("image")).placeholder(R.drawable.loading).dontAnimate().error(R.drawable.loading).into(holder.serviceImg);
                    //Picasso.with(context).load(jsonArray.optJSONObject(position).optString("image")).placeholder(R.drawable.loading).error(R.drawable.loading).into(holder.serviceImg);
                    holder.selector_background.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    holder.serviceTitle.setTextColor(getResources().getColor(R.color.black_text_color));
                }



                holder.linearLayoutOfList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        currentPostion = position;
                        notifyDataSetChanged();
                        getProvidersList();
                        try {
                            showProviderPopup(jsonArray.getJSONObject(position));
                            lnrProviderPopup.setVisibility(View.VISIBLE);
                            CommonBottomLayout.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return jsonArray.length();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            MyTextView serviceTitle;
            ImageView serviceImg;
            LinearLayout linearLayoutOfList;
            FrameLayout selector_background;

            public MyViewHolder(View itemView) {
                super(itemView);
                serviceTitle = (MyTextView) itemView.findViewById(R.id.serviceItem);
                serviceImg = (ImageView) itemView.findViewById(R.id.serviceImg);
                linearLayoutOfList = (LinearLayout) itemView.findViewById(R.id.LinearLayoutOfList);
                selector_background = (FrameLayout) itemView.findViewById(R.id.selector_background);
                /*itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Glide.with(activity).load(jsonArray.optJSONObject(getAdapterPosition()).optString("grey_image")).placeholder(R.drawable.loading).error(R.drawable.loading).into(serviceImg);
                        Log.e("Intent", "" + jsonArray.optJSONObject(getAdapterPosition()).toString());
                    }
                });*/

                h = itemView.getHeight();
                w = itemView.getWidth();


            }
        }
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread


        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        private String downloadUrl(String strUrl) throws IOException {
            String data = "";
            InputStream iStream = null;
            HttpURLConnection urlConnection = null;
            try {
                Log.e("Entering dowload url", "entrng");
                URL url = new URL(strUrl);

                // Creating an http connection to communicate with url
                urlConnection = (HttpURLConnection) url.openConnection();

                // Connecting to url
                urlConnection.connect();

                // Reading data from url
                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                StringBuffer sb = new StringBuffer();

                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                data = sb.toString();

                br.close();

            } catch (Exception e) {

            } finally {
                iStream.close();
                urlConnection.disconnect();
            }
            return data;
        }

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service

            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.e("Entering dwnload task", "download task");
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("Resultmap", result);

            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, PolylineOptions> {
        //ProgressDialog progressDialog = new ProgressDialog(activity);
        // Parsing the data in non-ui thread

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected PolylineOptions doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);

                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("routes", routes.toString());


            } catch (Exception e) {
                e.printStackTrace();
            }
            ///////////////////////////////////
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            if (routes != null) {
                // Traversing through all the routes
                for (int i = 0; i < routes.size(); i++) {
                    points = new ArrayList<LatLng>();
                    lineOptions = new PolylineOptions();

                    // Fetching i-th route
                    List<HashMap<String, String>> path = routes.get(i);

                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);
                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);
                        points.add(position);
                        Log.d("abcde", points.toString());

                        if (j == 0) {
                            sourceLatLng = new LatLng(lat, lng);
                        }
                        if (j == path.size()) {
                            destLatLng = new LatLng(lat, lng);
                        }
                    }

                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(points);
                    lineOptions.width(5);
                    lineOptions.color(Color.BLACK);

                }
            }
            return lineOptions;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(PolylineOptions lineOptions) {


//            mMap.clear();

            MarkerOptions markerOptions = new MarkerOptions()
                    .position(sourceLatLng).title("Source").draggable(true)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.user_marker));

            sourceMarker = mMap.addMarker(markerOptions);
            sourceMarker.setDraggable(false);
            MarkerOptions markerOptions1 = new MarkerOptions()
                    .position(destLatLng).draggable(true).title("Destination")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.provider_marker));
            destinationMarker = mMap.addMarker(markerOptions1);
            destinationMarker.setDraggable(false);
//                    Display display =activity.getWindowManager().getDefaultDisplay();
//                    Point size = new Point();
//                    display.getSize(size);
//                    int width = size.x;
//                    int height = size.y;
//
//                    mMap.setPadding(0, 0, 0, height / 2);

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            LatLngBounds bounds;
            builder.include(sourceMarker.getPosition());
            builder.include(destinationMarker.getPosition());
            bounds = builder.build();
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 200, 200, 20);
            mMap.moveCamera(cu);
            mMap.getUiSettings().setMapToolbarEnabled(false);
//                    CameraPosition cameraPosition = new CameraPosition.Builder().target(bounds.getCenter()).zoom(14).build();
//                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions == null) {
                Toast.makeText(getActivity(), getString(R.string.no_route), Toast.LENGTH_SHORT).show();
                if (customDialog != null) {
                    customDialog.dismiss();
                }
                flowValue = 1;
                StatusHandler();

            } else {
                mMap.addPolyline(lineOptions);
                if (customDialog != null) {
                    customDialog.dismiss();
                }
            }

        }
    }

    void getProvidersList()
    {
        String providers_request = URLHelper.GET_PROVIDERS_LIST_API + "?" +
                "latitude=" + SharedHelper.getKey(context, "source_latitude") +
                "&longitude="+SharedHelper.getKey(context, "source_longitude") +
                "&service="+SharedHelper.getKey(context, "service_type");
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(providers_request, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.v("GetProvidersList", response.toString());

                mMap.clear();
                setValuesForSourceAndDestination();

                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                for(int i = 0; i < response.length(); i++){

                    try {
                        JSONObject jsonObj = response.getJSONObject(i);

                        Double proLat = Double.parseDouble(jsonObj.getString("latitude"));
                        Double proLng = Double.parseDouble(jsonObj.getString("longitude"));
                        Float rotation = 0.0f;

                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(new LatLng(proLat, proLng))
                                .rotation(rotation)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.provider_location_icon));

                        availableProviders = mMap.addMarker(markerOptions);

                        rotateMarker(availableProviders,  45.0f);

                        builder.include(new LatLng(proLat, proLng));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                CameraUpdate cu = null;
                LatLngBounds bounds = builder.build();

                cu = CameraUpdateFactory.newLatLngBounds(bounds, HomeLayout.getWidth(),  HomeLayout.getWidth(), context.getResources()
                        .getDimensionPixelSize(R.dimen._50sdp));
                mMap.moveCamera(cu);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customDialog.dismiss();
                String json = null;
                String Message;
                mMap.clear();
                setValuesForSourceAndDestination();
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
                    displayMessage(getString(R.string.no_drivers_found));

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

        TranxitApplication.getInstance().addToRequestQueue(jsonArrayRequest);

    }


    void showProviderPopup(JSONObject jsonObject){
        Glide.with(activity).load(jsonObject.optString("image")).placeholder(R.drawable.pickup_drop_icon).dontAnimate()
                .error(R.drawable.pickup_drop_icon).into(imgProviderPopup);

        lnrPriceBase.setVisibility(View.GONE);
        lnrPricemin.setVisibility(View.GONE);
        lnrPricekm.setVisibility(View.GONE);

        if(jsonObject.optString("calculator").equalsIgnoreCase("MIN")
                || jsonObject.optString("calculator").equalsIgnoreCase("HOUR")){
            lnrPriceBase.setVisibility(View.VISIBLE);
            lnrPricemin.setVisibility(View.VISIBLE);
        }else if(jsonObject.optString("calculator").equalsIgnoreCase("DISTANCE")){
            lnrPriceBase.setVisibility(View.VISIBLE);
            lnrPricekm.setVisibility(View.VISIBLE);
        }else if(jsonObject.optString("calculator").equalsIgnoreCase("DISTANCEMIN")
                || jsonObject.optString("calculator").equalsIgnoreCase("DISTANCEHOUR")){
            lnrPriceBase.setVisibility(View.VISIBLE);
            lnrPricemin.setVisibility(View.VISIBLE);
            lnrPricekm.setVisibility(View.VISIBLE);
        }

        lblServiceName.setText(""+jsonObject.optString("name"));
        lblBasePrice.setText(""+jsonObject.optString("fixed"));
        lblPriceKm.setText(""+jsonObject.optString("price"));
        lblPriceMin.setText(""+jsonObject.optString("minute"));
    }


    private void rotateMarker(final Marker marker, final float toRotation) {
        if(!isMarkerRotating) {
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final float startRotation = marker.getRotation();
            final long duration = 1000;

            final Interpolator interpolator = new LinearInterpolator();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    isMarkerRotating = true;

                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed / duration);

                    float rot = t * toRotation + (1 - t) * startRotation;

                    marker.setRotation(-rot > 180 ? rot / 2 : rot);
                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    } else {
                        isMarkerRotating = false;
                    }
                }
            });
        }
    }


    public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }
}