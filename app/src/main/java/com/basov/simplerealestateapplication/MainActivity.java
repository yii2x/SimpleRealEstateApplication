package com.basov.simplerealestateapplication;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {


    MyDBHandler dbHandler;

    Button addPropertyBtn;
    Button oneBedroomsBtn;
    Button twoBedroomsBtn;
    Button threeBedroomsBtn;
    Button allPropertiesBtn;
    Button deleteAllPropertiesBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // create database handler object
        dbHandler = new MyDBHandler(this);

        // get main navigation buttons references.
        addPropertyBtn = (Button) findViewById(R.id.addPropertyBtn);
        oneBedroomsBtn = (Button) findViewById(R.id.oneBedroomsBtn);
        twoBedroomsBtn = (Button) findViewById(R.id.twoBedroomsBtn);
        threeBedroomsBtn = (Button) findViewById(R.id.threeBedroomsBtn);
        allPropertiesBtn = (Button) findViewById(R.id.allPropertiesBtn);
        deleteAllPropertiesBtn = (Button) findViewById(R.id.deleteAllPropertiesBtn);


        // set buttons onclick listeners.

        addPropertyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //switch to property form activity "EditPropertyActivity"

                Intent intent = new Intent(MainActivity.this, EditPropertyActivity.class);
                startActivity(intent);

            }
        });

        oneBedroomsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //switch to results activity "PropertyResultActivity" and request 1 bedrooms

                Intent intent = new Intent(MainActivity.this, PropertyResultActivity.class);
                intent.putExtra("rooms","1");
                startActivity(intent);
            }
        });

        twoBedroomsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //switch to results activity "PropertyResultActivity" and request 2 bedrooms

                Intent intent = new Intent(MainActivity.this, PropertyResultActivity.class);
                intent.putExtra("rooms","2");
                startActivity(intent);
            }
        });

        threeBedroomsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //switch to results activity "PropertyResultActivity" and request 3 bedrooms

                Intent intent = new Intent(MainActivity.this, PropertyResultActivity.class);
                intent.putExtra("rooms","3");
                startActivity(intent);
            }
        });

        allPropertiesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //switch to results activity "PropertyResultActivity" and request all bedrooms

                Intent intent = new Intent(MainActivity.this, PropertyResultActivity.class);
                intent.putExtra("rooms","*");
                startActivity(intent);
            }
        });

        deleteAllPropertiesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //delete all properties from database table.

                dbHandler = new MyDBHandler(MainActivity.this);
                dbHandler.deleteAll();
                Toast.makeText(view.getContext(),
                        "All Properties are deleted successfully.",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }
}
