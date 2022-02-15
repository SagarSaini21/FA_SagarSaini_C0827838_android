package com.example.fa_sagarsaini_c0827838_android;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.security.PrivateKey;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LocationActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener {

    private static final String TAG = "LocationActivity";
    private GoogleMap mMap;
    TextView update;
    public final int REQUEST_CODE = 1;
    MyDatabaseHelper myDB = new MyDatabaseHelper(LocationActivity.this);
    private FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    LocationRequest locationRequest;
    String name, desc, con,id;
    FloatingActionButton home,direction,delt;
    String lati,longi;
    double a,b,lati2,longi2;
    private Marker src,dest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        home = findViewById(R.id.btnhome);
        direction=findViewById(R.id.btndirection);
        delt=findViewById(R.id.btndelt);
        direction.setVisibility(View.INVISIBLE);
        delt.setVisibility(View.INVISIBLE);

        update = findViewById(R.id.tvupdate);
        update.setVisibility(View.INVISIBLE);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // initializing the location request
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10);
        getLocation();


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(LocationActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        delt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(LocationActivity.this);
                builder.setTitle(name.toString());
                builder.setMessage("Are you sure you want to delete this location?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myDB.ondelete(id);
                        finish();
                    }
                });
                builder.setNegativeButton("No",null);
                builder.create().show();


            }
        });
    direction.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//
//            Uri uri = Uri.parse("https://www.google.com/maps/dir/"+src.getPosition()+","+dest.getPosition());
//            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
//            intent.setPackage("com.google.android.apps.maps");
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
            drawLine();
        }
    });

    }

    public void drawLine()
    {
        PolylineOptions options = new PolylineOptions()
                .color(Color.RED)
                .add(src.getPosition(),dest.getPosition())
                .width(6);
        mMap.addPolyline(options);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        getIntentData();
        if (!checkPermission())
            requestPermission();
        else
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull LatLng latLng) {
                setMarker(latLng);
            }


        });
        mMap.setOnMarkerDragListener(this);

    }

    private void setMarker(LatLng latLng) {

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Log.i(TAG, "onLocationResult: " + addresses.get(0));
                String address = "";
                if (addresses.get(0).getPremises() != null) {
                    name = addresses.get(0).getPremises();
                } else if (addresses.get(0).getThoroughfare() != null) {
                    name = addresses.get(0).getPremises();
                } else {
                    Date c = Calendar.getInstance().getTime();
                    System.out.println("Current time => " + c);

                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                    String formattedDate = df.format(c);;
                    name = formattedDate;
                }



            }

        } catch (IOException e) {
            e.printStackTrace();
            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
            String formattedDate = df.format(c);;
            name = formattedDate;
        }
        if (name == null){
            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
            String formattedDate = df.format(c);;
            name = formattedDate;
        }
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(name)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                .snippet(desc + ", " + con);
        mMap.addMarker(options);

        myDB.additem(name,latLng.latitude,latLng.longitude);

    }

    private boolean checkPermission() {
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        return permission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


                    return;
                }
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
            }
        }
    }

    private void getLocation() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location: locationResult.getLocations()) {
                    //mMap.clear();
                    LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                   src = mMap.addMarker(new MarkerOptions().position(userLocation).title("You were here!"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14));



                }
            }
        };
    }

    void getIntentData()
    {
        if(  (getIntent().hasExtra("id"))&&(getIntent().hasExtra("name"))&&(getIntent().hasExtra("lati")) &&(getIntent().hasExtra("longi"))      )
        {
            id = getIntent().getStringExtra("id");
            name = getIntent().getStringExtra("name");
            lati = getIntent().getStringExtra("lati");
            longi = getIntent().getStringExtra("longi");
            a=Double.parseDouble(lati);
            b=Double.parseDouble(longi);
            LatLng l=new LatLng(a,b);

            setMarker2(l,name);
            direction.setVisibility(View.VISIBLE);
            delt.setVisibility(View.VISIBLE);
            update.setVisibility(View.VISIBLE);

        }else{
           // Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        }

    }
    private void setMarker2(LatLng latLng,String n) {

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Log.i(TAG, "onLocationResult: " + addresses.get(0));
                String address = "";

                if (addresses.get(0).getLocality() != null) {
                    desc = addresses.get(0).getLocality();
                }
                if (addresses.get(0).getCountryName() != null)
                    con = addresses.get(0).getCountryName();

                if (addresses.get(0).getPremises() != null)
                    address += addresses.get(0).getPremises() + " ";
                if (addresses.get(0).getCountryName() != null)
                    address += addresses.get(0).getCountryName() + " ";
                if (addresses.get(0).getLocality() != null)
                    address += addresses.get(0).getLocality() + " ";
                if (addresses.get(0).getPostalCode() != null)
                    address += addresses.get(0).getPostalCode() + " ";
                if (addresses.get(0).getThoroughfare() != null)
                    address += addresses.get(0).getThoroughfare();
                Toast.makeText(LocationActivity.this, address, Toast.LENGTH_SHORT).show();


            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(n)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                .draggable(true)
                .snippet(desc + ", " + con);
       dest = mMap.addMarker(options);


    }

    @Override
    public void onMarkerDrag(@NonNull Marker marker) {
    }

    @Override
    public void onMarkerDragEnd(@NonNull Marker marker) {

        lati2=marker.getPosition().latitude;
        longi2=marker.getPosition().longitude;

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lati2,longi2, 1);
            if (addresses != null && addresses.size() > 0) {
                Log.i(TAG, "onLocationResult: " + addresses.get(0));
                String address = "";
                if (addresses.get(0).getPremises() != null) {
                    name = addresses.get(0).getPremises();
                } else if (addresses.get(0).getThoroughfare() != null) {
                    name = addresses.get(0).getPremises();
                } else {
                    Date c = Calendar.getInstance().getTime();
                    System.out.println("Current time => " + c);

                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                    String formattedDate = df.format(c);;
                    name = formattedDate;
                }



            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (name == null){
            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
            String formattedDate = df.format(c);;
            name = formattedDate;
        }

       try{myDB.updateData(id.toString(),name.toString(),lati2,longi2);
       marker.setTitle(name);}
       catch(Exception e){
           Toast.makeText(this, "Updation Failed", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onMarkerDragStart(@NonNull Marker marker) {


    }
}