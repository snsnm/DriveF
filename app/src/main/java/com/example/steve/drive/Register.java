package com.example.steve.drive;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;

public class Register extends AppCompatActivity {
    private EditText mFirstName, mLastName, mEmail, mPhoneNumber, mDob, mPassword, mConfirmPassword;
    private Button mNext, mBack;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_driver_login);
        setContentView(R.layout.activity_register);

        mFirstName = (EditText) findViewById(R.id.fName);
        mLastName = (EditText) findViewById(R.id.lName);
        mEmail = (EditText) findViewById(R.id.eEmail);
        mPhoneNumber = (EditText) findViewById(R.id.phoneNumber);
        mDob = (EditText) findViewById(R.id.dob);
        mPassword = (EditText) findViewById(R.id.pass);
        mConfirmPassword = (EditText) findViewById(R.id.confirmPass);

        mNext = (Button) findViewById(R.id.next);
        mBack = (Button) findViewById(R.id.back);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, DriverLoginActivity.class);
                startActivity(intent);
            }
        });

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(Register.this, "Registration error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/
                String email = mEmail.getText().toString().trim();
                String fName = mFirstName.getText().toString().trim();
                String lName = mLastName.getText().toString().trim();
                String mPhone = mPhoneNumber.getText().toString().trim();
                String dob = mDob.getText().toString().trim();

                if(email.equals("") || fName.equals("") || lName.equals("")){
                    Toast.makeText(Register.this, "Please fill in all the required fields.", Toast.LENGTH_SHORT).show();
                }else{
                    UserState.email = email;
                    // we send an email to the the driver with a confirmation code.


                    sendEmail();

                    UserState.name = fName + lName;
                    UserState.fullName = fName + " " + lName;
                    UserState.phone = mPhone;
                    UserState.dob = dob;

                    Intent intent = new Intent(Register.this, ConfirmationActivity.class);
                    startActivity(intent);
                    //finish();
                    //return;
                }

            }
        });
    }
    /*protected void sendEmail(){
        Log.i("Send email", "");
        mEmail = (EditText) findViewById(R.id.eEmail);
        final String email = mEmail.getText().toString();
        String[] TO ={email};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Confirmation");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hey new driver. Thank you for joining DriveFar. Your confirmation code is 1234");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Register.this, "There is no such email client installed.", Toast.LENGTH_SHORT).show();
        }

    }*/
    private void sendEmail() {

        mEmail = (EditText) findViewById(R.id.eEmail);
        String email = mEmail.getText().toString().trim();

        mFirstName = (EditText) findViewById(R.id.fName);
        String name = mFirstName.getText().toString();

        String subject = "Confirmation Code";
        String message = name + ",\nWelcome to DriveFar,\nHere is your confirmation code: 1234. Enter this code into the app to continue with your registration.\n\nThe Support Team,\n" +
                "\n" +"\n"+
                "DriveFar Inc.\n" +
                "1 SteveOzzy Rd,\n" +
                "Riverdale, MD 20737";

        //Creating SendMail object
        SendMail sm = new SendMail(this, email, subject, message);

        //Executing sendmail to send email
        sm.execute();

    }
}
