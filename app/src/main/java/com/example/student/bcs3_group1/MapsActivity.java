package com.example.student.bcs3_group1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

//import android.location.LocationListener;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Marker currentLocationMarker;
    private static final int REQUEST_LOCATION_CODE=99;

    private Location lastlocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            checkLocationPermission();
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public  void  onRequestPermissionsResult(int requestCode, @NonNull String[] permissions , @NonNull int[] grantResults){
        switch (requestCode){
            case REQUEST_LOCATION_CODE:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //permission in granted
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                        if (client==null){
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else{
                    Toast.makeText(this,"permission denied",Toast.LENGTH_LONG).show();
                }
                return;
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {


            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

    }

    public  void onClick(View v){
        if (v.getId()==R.id.B_search){
            EditText tf_location =(EditText) findViewById(R.id.TF_location);
            String location = tf_location.getText().toString();
            List<Address> addressList= null;
            MarkerOptions mo = new MarkerOptions();




            if (!location.equals("")){
                Geocoder geocoder=new Geocoder(this);
                try {
                    addressList= geocoder.getFromLocationName(location, 10);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                for (int i=0; i<addressList.size(); i++){
                    Address myaddress = addressList.get(i);
                    LatLng latLng = new LatLng(myaddress.getLatitude(), myaddress.getLongitude());
                    mo.position(latLng);
                    mo.title("This is your searched place");
                    mMap.addMarker(mo);
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));



                }
            }
        }
    }

    protected  synchronized void buildGoogleApiClient(){
        client=new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        client.connect();


    }

    @Override
    public void onLocationChanged(Location location) {
        lastlocation=location;

        if (currentLocationMarker !=null){
            currentLocationMarker.remove();
        }
        LatLng latLng=new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions=new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("This is your Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        currentLocationMarker= mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(5));

        if (client!=null){
            LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
        }
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest=new LocationRequest();
        locationRequest.setInterval(300);
        locationRequest.setFastestInterval(1500);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            LocationServices.FusedLocationApi.requestLocationUpdates(client,locationRequest,this);
        }


    }

    public  boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_CODE);
            }
            else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_CODE);
            }
            return false;
        }
        else
            return  true;
    }



    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
