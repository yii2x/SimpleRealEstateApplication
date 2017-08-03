package com.basov.simplerealestateapplication;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditPropertyActivity extends Activity {

    MyDBHandler dbHandler;
    Button savePropertyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_property);

        // init save propery button
        savePropertyBtn = findViewById(R.id.savePropertyBtn);
        savePropertyBtn.setOnClickListener(new View.OnClickListener() {

            /*
            Add property record
             */

            @Override
            public void onClick(View view) {

                // Populate property content values and call content provider to create new record
                ContentValues values = new ContentValues();

                values.put(RealEstateProvider.COLUMN_NAME, ((EditText)findViewById(R.id.nameEditText)).getText().toString());
                values.put(RealEstateProvider.COLUMN_ROOMS, ((EditText)findViewById(R.id.roomsEditText)).getText().toString());
                values.put(RealEstateProvider.COLUMN_ADDRESS, ((EditText)findViewById(R.id.addressEditText)).getText().toString());
                values.put(RealEstateProvider.COLUMN_CITY, ((EditText)findViewById(R.id.cityEditText)).getText().toString());
                values.put(RealEstateProvider.COLUMN_STATE, ((EditText)findViewById(R.id.stateEditText)).getText().toString());
                values.put(RealEstateProvider.COLUMN_ZIP, ((EditText)findViewById(R.id.zipEditText)).getText().toString());
                values.put(RealEstateProvider.COLUMN_LAT, ((EditText)findViewById(R.id.latEditText)).getText().toString());
                values.put(RealEstateProvider.COLUMN_LNG, ((EditText)findViewById(R.id.longEditText)).getText().toString());


                // call content provider to create new record
                Uri uri = getContentResolver().insert(
                        RealEstateProvider.CONTENT_URI, values);

                // switch to main activity
                Intent editPropertyActivityIntent = new Intent(EditPropertyActivity.this, MainActivity.class);
                startActivity(editPropertyActivityIntent);

                // display record added message
                Toast.makeText(view.getContext(),
                        "Property added successfully.",
                        Toast.LENGTH_SHORT).show();


            }
        });
    }

    /*
        Add property record via DBHandler - alternative solution
     */
    public void addPropertyDBHandler(){


        dbHandler = new MyDBHandler(this);
        RealEstate realEstate = new RealEstate();

        // get layout fields references

        EditText nameEditText = findViewById(R.id.nameEditText);
        EditText roomsEditText = findViewById(R.id.roomsEditText);
        EditText addressEditText = findViewById(R.id.addressEditText);
        EditText cityEditText = findViewById(R.id.cityEditText);
        EditText stateEditText = findViewById(R.id.stateEditText);
        EditText zipEditText = findViewById(R.id.zipEditText);
        EditText latEditText = findViewById(R.id.latEditText);
        EditText lngEditText = findViewById(R.id.longEditText);


        // populate RealEstate object with values

        realEstate.set_name(nameEditText.getText().toString());
        realEstate.set_rooms(roomsEditText.getText().toString());
        realEstate.set_address(addressEditText.getText().toString());
        realEstate.set_city(cityEditText.getText().toString());
        realEstate.set_state(stateEditText.getText().toString());
        realEstate.set_zip(zipEditText.getText().toString());
        realEstate.set_lat(latEditText.getText().toString());
        realEstate.set_lng(lngEditText.getText().toString());

        // create new property record
        if(dbHandler.addRealEstate(realEstate) != 0){

            Toast.makeText(this,
                    "Property added successfully.",
                    Toast.LENGTH_SHORT).show();

            // switch to main activity
            Intent editPropertyActivityIntent = new Intent(EditPropertyActivity.this, MainActivity.class);
            startActivity(editPropertyActivityIntent);

        }
    }
}
