package com.tranxitpro.provider.Utilities;

/**
 * Created by Ramya on 4/26/2017.
 */
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;


public class LocationTracking extends Service
{
    private static final String TAG = "Location_Tracking";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;
   public static double distance=0.0;

    //Distance calculation
    public double old_lat=0,old_lng=0,new_lat,new_lng;
    Utilities utils = new Utilities();

    private class LocationListener implements android.location.LocationListener
    {
        Location mLastLocation;

        public LocationListener(String provider)
        {
            utils.print(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(final Location location)
        {
            utils.print(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);
            new_lat = location.getLatitude();
            new_lng =location.getLongitude();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    if(old_lat==0||old_lng==0){
                        old_lat = location.getLatitude();
                        old_lng =location.getLongitude();
                    }
                    utils.print("onLocationChanged_new","lat :"+new_lat+"   lng : "+new_lng);
                    utils.print("onLocationChanged_old",+old_lat+"   lng : "+old_lng);
                    //  distance(old_lat,old_lng,new_lat,new_lng);
                    distance =distance+ distanceBetween(new LatLng(old_lat,old_lng),new LatLng(new_lat,new_lng));
//                    Toast.makeText(LocationTracking.this, "Distance : "+LocationTracking.distance, Toast.LENGTH_SHORT).show();
                    Log.d("onLocation_Distance",""+distance);
                    old_lat = location.getLatitude();
                    old_lng =location.getLongitude();

                }
            }, 5000);
        }

        @Override
        public void onProviderDisabled(String provider)
        {
            utils.print(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider)
        {
            utils.print(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            utils.print(TAG, "onStatusChanged: " + provider);
//            Toast.makeText(LocationTracking.this, "s_Distance : "+LocationTracking.distance, Toast.LENGTH_SHORT).show();
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        utils.print(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate()
    {
        utils.print(TAG, "onCreate");
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy()
    {
        utils.print(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        utils.print(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    public static Double distanceBetween(LatLng point1, LatLng point2) {
        if (point1 == null || point2 == null) {
            return null;
        }

        return SphericalUtil.computeDistanceBetween(point1, point2);
    }
}