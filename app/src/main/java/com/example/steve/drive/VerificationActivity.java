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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VerificationActivity extends AppCompatActivity {
    private EditText mEmailCode, mEmail, mPassword;
    private Button mNext;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        //setContentView(R.layout.activity_register);
        //setContentView(R.layout.activity_password_creation);

        mNext = (Button) findViewById(R.id.next);
       // mEmail = (EditText) findViewById(R.id.eEmail);
        //mPassword = (EditText) findViewById(R.id.pass);
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmailCode = (EditText) findViewById(R.id.emailCode);
                String eCode = mEmailCode.getText().toString();
                if(eCode.equals("131")|| eCode.equals("132")||eCode.equals("250")||eCode.equals("216")||eCode.equals("330")||eCode.equals("351")){
                    //final String email = mEmail.getText().toString();
                    //final String password = mPassword.getText().toString();
                    mAuth = FirebaseAuth.getInstance();
                    //now we create a user with authentication
                    mAuth.createUserWithEmailAndPassword(UserState.email, UserState.password).addOnCompleteListener(VerificationActivity.this, new OnCompleteListener<AuthResult>() {
                        //Toast.makeText(VerificationActivity.this, "User creation Unsuccessful", Toast.LENGTH_SHORT);
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(VerificationActivity.this, "User creation Unsuccessful", Toast.LENGTH_SHORT).show();
                            }else{
                                //driver user was selected, so now we delete the user from the pending database and add himto the list of drivers
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference("Users").getRef().child("PendingDrivers").child(UserState.name);
                                myRef.setValue(null);

                                // we add him to the the driver app
                                myRef = database.getReference("Users").getRef().child("Drivers").child(UserState.name).child("Email");
                                myRef.setValue(UserState.email);
                                myRef = database.getReference("Users").getRef().child("Drivers").child(UserState.name).child("DOB");
                                myRef.setValue(UserState.dob);
                                myRef = database.getReference("Users").getRef().child("Drivers").child(UserState.name).child("Phone");
                                myRef.setValue(UserState.phone);

                                //we get the UID of the user that was created.
                                //String user_id = mAuth.getCurrentUser().getUid();

                                //we add the user email in the "all drivers email database"
                                String emai1 = UserState.email;
                                String emai = emai1.replace('.', '_');
                                myRef = database.getReference("Users").getRef().child("DriversEmail").child(emai);
                                myRef.setValue(UserState.email);
                                Log.d("the email frm ver chop ", emai);
                                //we now proceed to the dashboard activity
                                Intent intent = new Intent(VerificationActivity.this, DashboardActivity.class);
                                startActivity(intent);
                            }
                        }
                    });


                }else{
                    Toast.makeText(VerificationActivity.this, "Wrong Code", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
