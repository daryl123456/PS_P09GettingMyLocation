package sg.edu.rp.c346.id19042545.psp09gettingmylocation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.File;
import java.io.FileWriter;

public class MainActivity extends AppCompatActivity {

    TextView tvLat,tvLng;
    Button btnRec,btnRem,btnChk;
    private GoogleMap map;
    LocationRequest mLocationRequest;
    String folderLocation;
    Double lat,lng;
    Marker op;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvLat = findViewById(R.id.tvLat);
        tvLng = findViewById(R.id.tvLong);
        btnRec = findViewById(R.id.btnGet);
        btnRem = findViewById(R.id.btnRemove);
        btnChk = findViewById(R.id.btnCheck);

        checkPermission();

        folderLocation = getFilesDir().getAbsolutePath() + "/P09Location";
        File folder = new File(folderLocation);
        if (folder.exists() == false){
            boolean result = folder.mkdir();

            if (result == true){
                Log.d("File Read/Write", "Folder created");
            }
        }

        FragmentManager fm = getSupportFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment)
                fm.findFragmentById(R.id.map);

        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        checkPermission();
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    tvLat.setText("Latitude : " +location.getLatitude());
                    tvLng.setText("Longitude : " +location.getLatitude());
                    lat = location.getLatitude();
                    lng = location.getLongitude();
                    String msg = "Lat : " +location.getLatitude() +
                            "Log : " +location.getLongitude();
                    Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
                    LatLng poi = new LatLng(lat, lng);
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(poi,
                            15));
                    map.moveCamera(CameraUpdateFactory.zoomTo(10));
                    op = map.addMarker(new
                            MarkerOptions()
                            .position(poi)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    try{
                        File targetFile = new File(folderLocation, "location.txt");
                        FileWriter writer_I= new FileWriter(targetFile, true);
                        writer_I.write(location.getLatitude()+ " "+ location.getLongitude() + "\n");
                        writer_I.flush();
                        writer_I.close();

                    }catch (Exception e){

                    }
                }
                else {
                    String msg = "No Last Known Location found";
                    Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
                }

            }
        });
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;

                //code to map it zoom in to that coori



                UiSettings ui = map.getUiSettings();
                //compass ui
                ui.setCompassEnabled(true);

                //zoom ui
                ui.setZoomControlsEnabled(true);

                int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION);

                if (permissionCheck == PermissionChecker.PERMISSION_GRANTED) {
                    map.setMyLocationEnabled(true);
                } else {
                    Log.e("GMap - Permission", "GPS access has not been granted");
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                }


            }
        });
        LocationCallback mLocationCallback= new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult!= null) {
                    map.clear();
                    Location data = locationResult.getLastLocation();
                    lat= data.getLatitude();
                    lng= data.getLongitude();
                    tvLat.setText("Latitude : " +data.getLatitude());
                    tvLng.setText("Longitude : " +data.getLatitude());
                    LatLng poi = new LatLng(lat, lng);
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(poi,
                            15));
                    map.moveCamera(CameraUpdateFactory.zoomTo(10));
                    op = map.addMarker(new
                            MarkerOptions()
                            .position(poi)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    try{
                        File targetFile = new File(folderLocation, "location.txt");
                        FileWriter writer_I= new FileWriter(targetFile, true);
                        writer_I.write(data.getLatitude()+ " "+ data.getLatitude() + "\n");
                        writer_I.flush();
                        writer_I.close();

                    }catch (Exception e){

                    }

                    Toast.makeText(MainActivity.this,lat+" and "+lng,Toast.LENGTH_SHORT).show();
                }
            };
        };
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(30000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setSmallestDisplacement(500);
        btnRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
                client.requestLocationUpdates(mLocationRequest,mLocationCallback,null);
            }
        });
        btnRem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.removeLocationUpdates(mLocationCallback);
            }
        });
        btnChk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity.this,CheckActivity.class);
                startActivity(intent);

            }
        });


    }
    private void checkPermission(){
        int permissionCheck_Coarse = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionCheck_Fine = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionCheck_WriteExternal = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCheck_ReadExternal = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (
                permissionCheck_Fine == PermissionChecker.PERMISSION_GRANTED||
                        permissionCheck_Coarse == PermissionChecker.PERMISSION_GRANTED||
                permissionCheck_WriteExternal == PermissionChecker.PERMISSION_GRANTED
                || permissionCheck_ReadExternal == PermissionChecker.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);

        }
    }
}