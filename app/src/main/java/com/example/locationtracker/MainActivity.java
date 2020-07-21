package com.example.locationtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


//AIzaSyAf6OS289y8rTclCHg8QYazA-tJ_mHUJzM
public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private Double lati,longi;

    private Geocoder geocoder;
    private List<Address> addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        geocoder = new Geocoder(this, Locale.ENGLISH);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);


                if (locationResult!=null){
                    for (Location location:locationResult.getLocations()){
                        lati = location.getLatitude();
                        longi = location.getLongitude();

                        try {
                            addresses = geocoder.getFromLocation(lati,longi,1);

                            String info = addresses.get(0).getCountryName()+", "+addresses.get(0).getAdminArea()+", "
                                        +addresses.get(0).getSubAdminArea()+", "+addresses.get(0).getLocality()+", "+addresses.get(0).getPostalCode();

                            Toast.makeText(MainActivity.this, info, Toast.LENGTH_LONG).show();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }


            }
        };


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        getLocationRequest();
    }


    private void getLocationRequest() {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(5000);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},101);
            return;
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

    }

    public void start_map(View view) {
        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
        intent.putExtra("LAT", lati);
        intent.putExtra("LON", longi);
        startActivity(intent);


    }
}