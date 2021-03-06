package com.basov.simplerealestateapplication;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class PropertyResultActivity extends AppCompatActivity {

    // define the widget variable layout
    private TableLayout table_layout;
    Button viewOnMapBtn;
    private Cursor c;
    private String rooms = "*";
    // define SQL controller variable
    MyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_list);

        // get rooms value
        Intent intent = getIntent(); // gets the previously created intent
        rooms = intent.getStringExtra("rooms");


        // get "viewOnMapBtn" reference and set onClick listener
        viewOnMapBtn = (Button) findViewById(R.id.viewOnMapBtn);
        viewOnMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // switch to "MapsActivity" with rooms value parameter
                Intent intent = new Intent(PropertyResultActivity.this, MapsActivity.class);
                intent.putExtra("rooms",rooms);
                startActivity(intent);

            }
        });
        // get reference to the layout widget
        table_layout = (TableLayout) findViewById(R.id.tableLayout1);

        // instantiate the handler constructor
        dbHandler = new MyDBHandler(this);

        // call the build table method
        BuildTable(rooms);
    }
    // responsible for building the table
    private void BuildTable(String rooms) {

        dbHandler.open();
        Cursor c = dbHandler.readEntry(rooms);

        // get total number of records
        int rows = c.getCount();
        int cols = 5; // number of first columns to display

        c.moveToFirst();

        // outer for loop
        for (int i = 0; i < rows; i++) {

            // create table row
            TableRow row = new TableRow(this);

            // set row params
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            // inner for loop
            for (int j = 1; j < cols; j++) {

                //create text view
                TextView tv = new TextView(this);

                // set text view params
                tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));

                // text align, size and padding
                tv.setGravity(Gravity.CENTER);
                tv.setTextSize(18);
                tv.setPadding(0, 5, 0, 5);

                // set text view object value
                tv.setText(c.getString(j));

                // add text view to row
                row.addView(tv);

            }

            // move to next property
            c.moveToNext();

            // add row to table layout
            table_layout.addView(row);

        }

        // close the database
        dbHandler.close();
    }
}
