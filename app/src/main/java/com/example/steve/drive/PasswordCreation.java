package com.example.steve.drive;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Console;

public class PasswordCreation extends AppCompatActivity {

    private EditText mFirstName, mLastName, mEmail, mPhoneNumber, mPassword, mConfirmPassword;
    private Button mNext, mBack;

    private FirebaseAuth mAuth;

    //private Firebase test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_creation);

        mPassword = (EditText) findViewById(R.id.pass);
        mConfirmPassword = (EditText) findViewById(R.id.confirmPass);

        mNext = (Button) findViewById(R.id.next);
        //mBack = (Button) findViewById(R.id.back);


        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = mPassword.getText().toString();
                String confirmPass = mConfirmPassword.getText().toString();

                if(pass == "" || confirmPass == "" || !(pass.equals(confirmPass)) ){
                    Toast.makeText(PasswordCreation.this, "Password do not match", Toast.LENGTH_SHORT).show();
                }else{
                    UserState.password = pass;
                    if(UserState.state.equals("driver")){

                        //create a user and place thje user in the Pending drivers database
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("Users").getRef().child("PendingDrivers").child(UserState.name);
                        myRef.setValue(true);
                        /*DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("PendingDrivers").child("YOO");
                        current_user_db.setValue(true);*/
                        Intent intent = new Intent(PasswordCreation.this, UploadDocumentActivity.class);
                        startActivity(intent);
                    }else{


                        mAuth = FirebaseAuth.getInstance();
                        //now we create a user with authentication
                        mAuth.createUserWithEmailAndPassword(UserState.email, UserState.password).addOnCompleteListener(PasswordCreation.this, new OnCompleteListener<AuthResult>() {
                            //Toast.makeText(VerificationActivity.this, "User creation Unsuccessful", Toast.LENGTH_SHORT);
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(!task.isSuccessful()){
                                    Toast.makeText(PasswordCreation.this, "User creation Unsuccessful", Toast.LENGTH_SHORT);
                                }else{

                                    //create the passenger user  in the database here.
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference myRef = database.getReference("Users").getRef().child("Passengers").child("Email");
                                    myRef.setValue(UserState.email);
                                    myRef = database.getReference("Users").getRef().child("Passengers").child(UserState.name).child("DOB");
                                    myRef.setValue(UserState.dob);
                                    myRef = database.getReference("Users").getRef().child("Passengers").child(UserState.name).child("Phone");
                                    myRef.setValue(UserState.phone);

                                    //we get the UID of the user that was created.
                                    String user_id = mAuth.getCurrentUser().getUid();

                                    //we add the user email in the "all Passengers UID database"
                                    String emai1 = UserState.email;
                                    String emai = emai1.replace('.', '_');
                                    myRef = database.getReference("Users").getRef().child("PassengersEmail").child(emai);
                                    myRef.setValue(UserState.email);

                                    Intent intent = new Intent(PasswordCreation.this, PassengerDashboardActivity.class);
                                    startActivity(intent);
                                }
                            }
                        });

                        //Toast.makeText(PasswordCreation.this, "PLeaseee go", Toast.LENGTH_SHORT).show();

                    }
                }

            }
        });




    }
}
