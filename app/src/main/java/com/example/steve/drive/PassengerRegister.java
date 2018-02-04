package com.example.steve.drive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PassengerRegister extends AppCompatActivity {
    private EditText mFirstName, mLastName, mEmail, mPhoneNumber, mPassword, mConfirmPassword;
    private Button mNext, mBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_register);

        mFirstName = (EditText) findViewById(R.id.fName);
        mLastName = (EditText) findViewById(R.id.lName);
        mEmail = (EditText) findViewById(R.id.eEmail);
        mPassword = (EditText) findViewById(R.id.pass);
        mConfirmPassword = (EditText) findViewById(R.id.confirmPass);

        mNext = (Button) findViewById(R.id.next);
        mBack = (Button) findViewById(R.id.back);

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PassengerRegister.this, PassengerConfirmationActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });
    }
}
