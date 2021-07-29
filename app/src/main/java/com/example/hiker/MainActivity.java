package com.example.hiker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length == PackageManager.PERMISSION_GRANTED && grantResults.length > 0) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textView = findViewById(R.id.textView2);

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                final Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> addressList = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                    if (addressList != null && addressList.size() > 0) {
                        String info = "";
                        info = info + "Latitude: "  + addressList.get(0).getLatitude() + "\n\n";
                        info = info + "Longitude: "  + addressList.get(0).getLongitude() + "\n\n";
                        info = info + "Accuracy: "  + Double.toString(location.getAccuracy()) + "\n\n";
                        if (location.getAltitude() > 0){
                            info = info + "Altitude: "  + Double.toString(location.getAltitude()) + "\n\n";
                        }else{
                            info = info + "Altitude:\n\n";
                        }
                        info = info + "Address:\n";
                        if (addressList.get(0).getAdminArea() != null){
                            info += addressList.get(0).getAdminArea()+ " ";
                        }
                        if (addressList.get(0).getLocality() != null) {
                            info += addressList.get(0).getLocality() + " ";
                        }
                        if (addressList.get(0).getCountryName() != null) {
                            info += addressList.get(0).getCountryName();
                        }
                            textView.setText(info);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        }
    }
}