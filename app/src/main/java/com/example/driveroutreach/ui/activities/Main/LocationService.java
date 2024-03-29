package com.example.driveroutreach.ui.activities.Main;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.driveroutreach.R;
import com.example.driveroutreach.model.DriversNumbers;
import com.example.driveroutreach.ui.app_utility.AppUtility;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

public class LocationService extends Service implements LocationListener {
    private LocationManager locationManager;
    private DatabaseReference locationRef;
    private DriversNumbers driver;
    Map<String, Object> locationMap;
    public final String DRIVER_ID_KEY = "driverId";
    SharedPreferences sp;
    SharedPreferences.Editor edit;
    private static final String CHANNEL_ID = "LocationServiceChannel";
    private static final int PERMISSION_REQUEST_CODE = 1101;

    static Activity activityContext;

    boolean isFirstCall = true;

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationRef = FirebaseDatabase.getInstance().getReference("DriverLocation");
        driver = new DriversNumbers();
        sp = getSharedPreferences("sp", MODE_PRIVATE);
        edit = sp.edit();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Location Service", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

    }


    public static void setActivityContext(Activity activity) {
        activityContext = activity;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Check if background location permission is granted
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                // Permission already granted
                // You can proceed with using background location
                AppUtility.showSnackbar(activityContext.findViewById(android.R.id.content),"Background location permission granted");

            } else {
                // Permission not granted, request it
                ActivityCompat.requestPermissions(activityContext,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                        PERMISSION_REQUEST_CODE);
            }
        } else {
            // Background location permission is not required for earlier versions
            // You can proceed with using location
            AppUtility.showSnackbar(activityContext.findViewById(android.R.id.content),"Background location permission not required");
         //   Toast.makeText(this, "Background location permission not required", Toast.LENGTH_SHORT).show();

        }

        // Perform your location-related tasks here


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "channel name", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }

      //  Intent intent1 = new Intent(getBaseContext(), LocationService.class);
        Intent intent1 = new Intent(getBaseContext(), MainActivity.class);
        //take one value
        intent1.setAction("stop");
      //  PendingIntent pi = PendingIntent.getService(getBaseContext(), 0, intent1, PendingIntent.FLAG_IMMUTABLE);
        PendingIntent pi = PendingIntent.getActivity(getBaseContext(), 0, intent1, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.img_logo_app);
        builder.setContentTitle("Driver");
        builder.setContentText(getString(R.string.notifacation_describtion));
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.addAction(R.drawable.icon_arrive, "show", pi);


        Notification n = builder.build();
        startForeground(1,n);

        if (intent.getAction() != null){
            if (intent.getAction().equals("stop")){
                stopSelf();
            }
        }


        // تشغيل السيرقيس في الفورجراوند
        startForeground(1, n);

        requestLocationUpdates();

        return START_STICKY;
    }



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        Log.d("onLocation",String.valueOf(location.getLongitude()));
        if (latitude != 0.0){
        // تخزين اللوكيشن في الهاشماب
        locationMap = new HashMap<>();
        locationMap.put("latitude", latitude);
        locationMap.put("longitude", longitude);

        String driverId= sp.getString(DRIVER_ID_KEY,null);

        // تحديث على اللوكيشن في الريل تايم
        //locationRef.child(String.valueOf(driverId)).setValue(locationMap);


        //EventBus.getDefault().post(new LocationChanged(location.getLatitude(), location.getLongitude()));

        String PreLat   = sp.getString("latitude",null);
        String Prelong =  sp.getString("longitude",null);




            if (PreLat != null && Prelong !=null && !isFirstCall){


                Location locationA = new Location("point A");

                locationA.setLatitude(Double.parseDouble(PreLat));
                locationA.setLongitude(Double.parseDouble(Prelong));

                Location locationB = new Location("point B");

                locationB.setLatitude(latitude);
                locationB.setLongitude(longitude);

                float distance = locationA.distanceTo(locationB);




                Log.d("distance",String.valueOf(distance));



              //  if (distance>5){
               SvaingLocation(location, driverId);
               // }


            } else {
                SvaingLocation(location, driverId);
                isFirstCall=false;
            }

            Log.d("LocationService", "onLocationChanged:   id  "+driverId);
        }



        Log.d("LocationService", "onLocationChanged:   "+driver.getMobile());


        Log.d("LocationService", "Latitude: " + driver.getMobile() + " " + latitude + ", Longitude: " + longitude);

        Toast.makeText(this, "Latitude: " + latitude + ", Longitude: " + longitude, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    private void requestLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (locationManager != null) {
                try {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void removeLocationUpdates() {
        if (locationManager != null) {
            try {
                locationManager.removeUpdates(this);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        removeLocationUpdates();
        locationRef.child(String.valueOf(driver.getMobile())).setValue(locationMap);
        // توقيف الفورجراوند
        stopForeground(false);

    }

    private void SvaingLocation(Location location, String driverId) {
        if (location.getLatitude() != 0.0 && location.getLongitude() != 0.0){
        edit.putString("latitude", String.valueOf(location.getLatitude()));
        edit.putString("longitude", String.valueOf(location.getLongitude()));
        edit.commit();
    }
        LocationChanged locationChanged = new LocationChanged(location.getLatitude(), location.getLongitude());
        Log.d("locationChanged", locationChanged.toString());
        EventBus.getDefault().post(locationChanged);
         locationRef.child(String.valueOf(driverId)).setValue(locationMap);
    }
}