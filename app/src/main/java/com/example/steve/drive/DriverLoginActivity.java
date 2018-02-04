package com.example.steve.drive;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DriverLoginActivity extends AppCompatActivity {
    private EditText mEmail, mPassword;
    private Button mlogin, mRegistration;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("We in the driver login", "yo");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("WHY ", "WHY ARE YOU GOING HERE");
                    if(UserState.state.equals("driver")){
                        Intent intent = new Intent(DriverLoginActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        Log.d("WHY ", "WHY ARE YOU GOING HERE");
                    }else{
                        Intent intent = new Intent(DriverLoginActivity.this, PassengerDashboardActivity.class);
                        startActivity(intent);
                    }

                    finish();
                    return;
                } //else {
                    // User is signed out
                    //Log.d(TAG, "onAuthStateChanged:signed_out");
                //}
                // ...
            }
        };

        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);

        mlogin = (Button) findViewById(R.id.login);
        mRegistration = (Button) findViewById(R.id.registration);

        mRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriverLoginActivity.this, Register.class);
                startActivity(intent);
                //finish();
                //return;
            }
        });

        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString().trim();
                final String password = mPassword.getText().toString();

                //first we check if the user who's trying to login is either part of
                // the driver database or passenger database
                if(UserState.state.equals("driver")){


                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    String emai = email.replace('.', '_');
                    Log.d("The ema after chop", emai);
                    Log.d("But the email is ", email);
                    DatabaseReference myRef = database.getReference("Users").child("DriversEmail").child(emai);

                    myRef.addValueEventListener(new ValueEventListener(){
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot){

                            String value =  String.valueOf(dataSnapshot.getValue());
                            String temp = value;
                            if(value == null){
                                Log.d("this object ", "is null");
                                Log.d("this object class is ", value.getClass()+"");
                            }else{
                                Log.d("this object class is ", value.getClass()+"");
                                Log.d("this value temp is  ", temp);
                            }
                            //Log.d("yoo th evalue is ", value);

                            if(value.equals(email)){
                                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(DriverLoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(!task.isSuccessful()){
                                            Toast.makeText(DriverLoginActivity.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
                                        }else{

                                            Intent intent = new Intent(DriverLoginActivity.this, DashboardActivity.class);
                                            startActivity(intent);

                                        }
                                    }
                                });
                            }else{
                                Toast.makeText(DriverLoginActivity.this, "No such Email exits", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError firebaseError){
                            Toast.makeText(DriverLoginActivity.this, "Dont work", Toast.LENGTH_SHORT).show();
                            Log.d("Failed","The snapshot dont work ");
                        }
                    });




                }else{
                    // we check the passenger email list

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    String emai = email.replace('.', '_');
                    Log.d("The ema after chop", emai);
                    Log.d("But the email is ", email);
                    DatabaseReference myRef = database.getReference("Users").child("PassengersEmail").child(emai);

                    myRef.addValueEventListener(new ValueEventListener(){
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot){

                            String value =  String.valueOf(dataSnapshot.getValue());
                            String temp = value;
                            if(value == null){
                                Log.d("this object ", "is null");
                                Log.d("this object class is ", value.getClass()+"");
                            }else{
                                Log.d("this object class is ", value.getClass()+"");
                                Log.d("this value temp is  ", temp);
                            }
                            //Log.d("yoo th evalue is ", value);

                            if(value.equals(email)){
                                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(DriverLoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(!task.isSuccessful()){
                                            Toast.makeText(DriverLoginActivity.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
                                        }else{

                                            Intent intent = new Intent(DriverLoginActivity.this, PassengerDashboardActivity.class);
                                            startActivity(intent);

                                        }
                                    }
                                });
                            }else{
                                Toast.makeText(DriverLoginActivity.this, "No Such Email exits", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError firebaseError){
                            Toast.makeText(DriverLoginActivity.this, "Dont work", Toast.LENGTH_SHORT).show();
                            Log.d("Failed","The snapshot dont work ");
                        }
                    });

                }



            }
        });


    }
    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    protected void onStop(){
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }
}
