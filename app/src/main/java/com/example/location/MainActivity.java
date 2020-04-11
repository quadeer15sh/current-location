package com.example.location;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    TextView textView;
    TextView textLat;
    TextView textLng;
    Button btnLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textLat = findViewById(R.id.textLat);
        textLng = findViewById(R.id.textLng);
        btnLoc = findViewById(R.id.buttonLoc);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        btnLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("App Status","App started");
                // Continously listen for changes in user's location
                locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        updateLocation(location);
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
                };

                // If the user is opening the app for the first time display permissions for location and retrieve location from network provider
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                }
                else {
                    Log.i("Status","This ran");
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    try {
                        Log.i("check",lastKnownLocation.toString());
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    if (lastKnownLocation!=null){
                        updateLocation(lastKnownLocation);
                    }
                }
            }
        });
    }

    // based on what the user selects the action has to be taken on the permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode==1){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            }
        }
    }

    // function to set location information
    public void updateLocation(Location location){
        Log.i("Location coods",location.toString());
        textLat.setText("Lat : "+location.getLatitude());
        textLng.setText("Long : "+location.getLongitude());
    }
}
