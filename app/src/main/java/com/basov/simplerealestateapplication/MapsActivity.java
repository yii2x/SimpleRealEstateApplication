package com.basov.simplerealestateapplication;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button viewOnListBtn;
    private String rooms = "*";

    // define SQL controller variable
    MyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //create databse handler
        dbHandler = new MyDBHandler(this);

        // get intent and get rooms request value
        Intent intent = getIntent(); // gets the previously created intent
        rooms = intent.getStringExtra("rooms");

        // get "viewOnListBtn" reference
        viewOnListBtn = (Button) findViewById(R.id.viewOnListBtn);

        //set onclick listener
        viewOnListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // switch to "PropertyResultActivity" and pass rooms value

                Intent intent = new Intent(MapsActivity.this, PropertyResultActivity.class);
                intent.putExtra("rooms", rooms);
                startActivity(intent);

            }
        });

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

        // define markers array
        ArrayList<Marker> markers = new ArrayList<>();

        // get rooms value
        Intent intent = getIntent(); // gets the previously created intent
        String rooms = intent.getStringExtra("rooms");

        // if no rooms value then it is request for all properties
        if(rooms == null){
            rooms = "*";
        }

        // get properties from database
        dbHandler.open();
        Cursor c = dbHandler.readEntry(rooms);

        c.moveToFirst();

        // define total number of found records
        int rows = c.getCount();
        if(rows > 0){
            // loop propeties
            for (int i = 0; i < rows; i++) {


                String name = c.getString(1);

                // get map coordinates as double type values
                Double lat = new Double(c.getString(8));
                Double lng = new Double(c.getString(7));

                // create map marker
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lat, lng))
                        .title(name));

                // add marker to markers list
                markers.add(marker);

                // move to next property
                c.moveToNext();
            }

            // define bounds builder
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Marker marker : markers) {
                //add marker positions to bounds builder
                builder.include(marker.getPosition());
            }

            //build bounds
            LatLngBounds bounds = builder.build();


            int padding = 0; // offset from edges of the map in pixels

            //create "camera update" object with built bounds
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

            // move camera to markers bounds
            googleMap.moveCamera(cu);

            // zoom camera
            googleMap.animateCamera( CameraUpdateFactory.zoomTo( 13.0f ) );
        }


        // close the database
        dbHandler.close();





    }

}
