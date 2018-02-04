package com.example.steve.drive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ConfirmationActivity extends AppCompatActivity {
    private EditText mEmailCode, mPhoneCode;
    private Button mNext, mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        mNext = (Button) findViewById(R.id.next);
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmailCode = (EditText) findViewById(R.id.emailCode);
                String eCode = mEmailCode.getText().toString();

                if(eCode.equals("1234")){
                    Intent intent = new Intent(ConfirmationActivity.this, PasswordCreation.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(ConfirmationActivity.this, "Code do not match", Toast.LENGTH_SHORT).show();


                }
            }
        });
    }
}
