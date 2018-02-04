package com.example.steve.drive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mDriver, mPassenger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDriver = (Button) findViewById(R.id.driver);
        mPassenger = (Button) findViewById(R.id.passenger);

        mDriver.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                UserState.state = "driver";
                Log.d("Driver being executed", "yo");
                Intent intent = new Intent(MainActivity.this, DriverLoginActivity.class);
                startActivity(intent);
                //finish();
                //return;
            }
        });


        mPassenger.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                UserState.state = "passenger";
                Intent intent = new Intent(MainActivity.this, DriverLoginActivity.class);
                startActivity(intent);
                //finish();
               // return;
            }
        });
    }
}
