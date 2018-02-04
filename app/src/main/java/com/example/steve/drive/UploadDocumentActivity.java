package com.example.steve.drive;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class UploadDocumentActivity extends AppCompatActivity {
    private Button mUploadPP, mChoosePP, mChooseLicense, mUploadLicense, mNext;
    private String dest;

    private final int RESULT_LOAD_IMAGE = 71;
    private Uri filepath;
    private ImageView imageView;

    FirebaseStorage storage;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_document);

        storage = FirebaseStorage.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        mUploadPP = (Button) findViewById(R.id.uploadPP);
        mChoosePP = (Button) findViewById(R.id.choosePP);
        mChooseLicense = (Button) findViewById(R.id.chooseLicense);
        mUploadLicense = (Button) findViewById(R.id.uploadLicense);

        mNext = (Button) findViewById(R.id.next);



        mChoosePP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView = (ImageView) findViewById(R.id.imgViewProfile);
                chooseImage();
            }
        });


        mUploadPP.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                imageView = (ImageView) findViewById(R.id.imgViewProfile);
                dest = "ProfilePicture";
                uploadImage();
            }
        });
        mChooseLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView = (ImageView) findViewById(R.id.imgViewLicense);
                chooseImage();
            }
        });
        mUploadLicense.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                dest = "DriverLicense";
                uploadImage();
            }
        });
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UploadDocumentActivity.this, VerificationActivity.class);
                startActivity(intent);
            }
        });


        /*mPP.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);


            }
        });*/

    }

    private void uploadImage() {

        if(filepath !=null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            String folderName = UserState.name;
            StorageReference ref = mStorageRef.child(folderName+"/"+dest);
            ref.putFile((filepath)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(UploadDocumentActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(UploadDocumentActivity.this, "Failed", Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded"+(int)progress+" %");

                }
            });


        }

    }

    private void chooseImage() {
        Intent intent = new Intent();

        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent,"Select Picture"), RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filepath = data.getData();
            try{
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                imageView.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }

        }
    }

    //code fofr the old upload pic but dont work

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.imgViewProfile);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

            Intent intent = new Intent(UploadDocumentActivity.this, UploadDocumentActivity.class);
            startActivity(intent);
        }


    }*/
}
