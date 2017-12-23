package com.tranxitpro.app.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.tranxitpro.app.Activities.MainActivity;
import com.tranxitpro.app.Constants.PlacesAutoCompleteAdapter;
import com.tranxitpro.app.Helper.SharedHelper;
import com.tranxitpro.app.R;

import java.util.List;

public class SoruceAndDestination extends AppCompatActivity {

    Activity activity = SoruceAndDestination.this;
    Context context = SoruceAndDestination.this;
    String source_location = "", destination_location = "";
    Double latitude, longitude;
    AutoCompleteTextView source, destination;
    ImageView backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_soruce_and_destination);
        findViewByIdAndInitialize();
        if (Build.VERSION.SDK_INT > 15) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedHelper.putKey(context,"current_status","1");
                GoToMainActivity();
            }
        });

        source.setAdapter(new PlacesAutoCompleteAdapter(SoruceAndDestination.this, R.layout.autocomplete_list_item));



        source.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get data associated with the specified position
                // in the list (AdapterView)
                source_location = (String) parent.getItemAtPosition(position);
                SharedHelper.putKey(context,"source",""+source_location);
                getAddressLatLng("SOURCE");
            }
        });

        destination.setAdapter(new PlacesAutoCompleteAdapter(SoruceAndDestination.this, R.layout.autocomplete_list_item));
        destination.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get data associated with the specified position
                // in the list (AdapterView)
                source_location = (String) parent.getItemAtPosition(position);
                SharedHelper.putKey(context,"destination",""+source_location);
                SharedHelper.putKey(context,"current_status","2");
                getAddressLatLng("DESTINATION");
                GoToMainActivity();
            }
        });


    }

    public void findViewByIdAndInitialize(){
        source = (AutoCompleteTextView) findViewById(R.id.source);
        destination = (AutoCompleteTextView) findViewById(R.id.destination);
        backArrow = (ImageView) findViewById(R.id.backArrow);
        if(SharedHelper.getKey(context,"currentAddress") != "" || SharedHelper.getKey(context,"currentAddress") != null){
            source.setText(SharedHelper.getKey(context,"currentAddress"));
            source_location = SharedHelper.getKey(context,"currentAddress");
            SharedHelper.putKey(context,"source",""+source_location);
            getAddressLatLng("SOURCE");
        }

    }


    private void getAddressLatLng(String type) {
        String addressValue = source_location;
        Geocoder coder = new Geocoder(activity);
        List<Address> address;

        try {
            address = coder.getFromLocationName(addressValue, 5);
            if (address == null) {
                displayMessage(getString(R.string.something_went_wrong));
            }else{
                    Address location = address.get(0);
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                if(type.equals("SOURCE")){
                    SharedHelper.putKey(context,"source_latitude",""+latitude);
                    SharedHelper.putKey(context,"source_longitude",""+longitude);
                }else{
                    SharedHelper.putKey(context,"destination_latitude",""+latitude);
                    SharedHelper.putKey(context,"destination_longitude",""+longitude);
                }

            }

            Log.e("Address", "Value" + latitude + "," + longitude);

        } catch (Exception ex) {
            ex.printStackTrace();
            displayMessage(getString(R.string.something_went_wrong));
        }
    }


    public void GoToMainActivity(){
        Intent mainIntent = new Intent(activity, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        activity.finish();
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
    public void onBackPressed() {
        SharedHelper.putKey(context,"current_status","1");
        GoToMainActivity();
    }
}