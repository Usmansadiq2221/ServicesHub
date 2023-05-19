package com.devtwist.serviceshub.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.devtwist.serviceshub.Models.NotificationData;
import com.devtwist.serviceshub.Models.SendNotification;
import com.devtwist.serviceshub.Models.Userdata;
import com.devtwist.serviceshub.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateProfileActivity extends AppCompatActivity {

    private ImageView imageView,_profileimage, _signupBackButton;

    private EditText _fname, _lname, _cnicNo, _dateOfBirth,
            _eMail, _ratePerDay, _sikllsDetails, _address;

    private Uri proPicFilePath;
    private Bitmap bitmap;
    private String token, uId, username, lName,cnicNo,gender,dateOfBirthString,province,
            city,address,profession, eMail,phoneNo,skillDetails,emailpattern,
            errorMessage, rating,ratePerDay,earned;
    private int badReview;
    private Spinner _gender, _province,_city,_profession;
    private Button _signUpButton;
    private ArrayAdapter<CharSequence> adapter,professionAdapter;
    private Userdata userdata;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Intent userProfileActivity;
    private FirebaseUser mAuth;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        _signupBackButton = findViewById(R.id._signupBackButton);
        imageView = findViewById(R.id._homeTutor);
        _profileimage = findViewById(R.id._profileimage);
        _fname = findViewById(R.id._fName);
        _lname = findViewById(R.id._lName);
        _cnicNo = findViewById(R.id._cnicNo);
        _gender = findViewById(R.id._gender);
        _eMail = findViewById(R.id._eMail);
        _province = findViewById(R.id._province);
        _city = findViewById(R.id._city);
        _address = findViewById(R.id._address);
        _profession = findViewById(R.id._profession);
        _ratePerDay = findViewById(R.id._ratePerDay);
        _sikllsDetails = findViewById(R.id._skillsDetails);
        _signUpButton = findViewById(R.id._signUpButton);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        errorMessage = "";
        skillDetails = " ";

        mAuth = FirebaseAuth.getInstance().getCurrentUser();

        emailpattern = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$";


        /*AlertDialog.Builder builder1 = new AlertDialog.Builder(CreateProfileActivity.this);
        builder1.setMessage("Write your message here.");
        builder1.setCancelable(false);
        builder1.setTitle("App Agreement");
        builder1.setPositiveButton(
                "I Agree",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();*/


        _signupBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        _profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage();
            }
        });

        adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, R.layout.spinner_style_file);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _gender.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(this,
                R.array.province_array, R.layout.spinner_style_file);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _province.setAdapter(adapter);
        _province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                province = _province.getSelectedItem().toString();
                if (i==0){
                    adapter = ArrayAdapter.createFromResource(CreateProfileActivity.this,
                            R.array.punjab_array, R.layout.spinner_style_file);
                }
                else if (i==1){
                    adapter = ArrayAdapter.createFromResource(CreateProfileActivity.this,
                            R.array.balochistan_array, R.layout.spinner_style_file);
                }
                else if (i==2){
                    adapter = ArrayAdapter.createFromResource(CreateProfileActivity.this,
                            R.array.kpk_array, R.layout.spinner_style_file);
                }
                else if (i==3){
                    adapter = ArrayAdapter.createFromResource(CreateProfileActivity.this,
                            R.array.sindh_array, R.layout.spinner_style_file);
                }
                else{
                    adapter = ArrayAdapter.createFromResource(CreateProfileActivity.this,
                            R.array.punjab_array, R.layout.spinner_style_file);
                }
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                _city.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        professionAdapter= ArrayAdapter.createFromResource(this,
                R.array.profession_array, R.layout.spinner_style_file);
        professionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _profession.setAdapter(professionAdapter);

        _signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = _fname.getText().toString().trim();
                eMail = _eMail.getText().toString().trim();

                if ( username.length()<1 && _lname.getText().length()<1 && _cnicNo.getText().length()<1
                        &&_eMail.getText().length() < 1 && _ratePerDay.getText().length() < 1 && _address.getText().length()<1) {

                    Toast.makeText(CreateProfileActivity.this, "Enter All Values", Toast.LENGTH_SHORT).show();

                } else if (username.length()>3 && _lname.getText().length()>3 && _cnicNo.getText().length()>12
                        && eMail.matches(emailpattern) && _ratePerDay.getText().length() > 1 && _address.getText().length()>1) {

                    if (proPicFilePath.toString().length()>1){
                        uploadToFirebase();
                    }
                    else{
                        Toast.makeText(CreateProfileActivity.this, "Profile Picture not found", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    if (eMail.matches(emailpattern)) {

                    } else {

                        errorMessage = errorMessage + "Invalid e-mail\n";

                    }
                    if (username.length()<3){
                        errorMessage = "Invalid First name";
                    }

                    if (_lname.getText().length()<3){
                        errorMessage = "Invalid Last name";
                    }
                    if (_cnicNo.getText().length()<13){
                        errorMessage = "Invalid CNIC No";
                    }
                    Toast.makeText(CreateProfileActivity.this, "testing", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void captureImage() {
        Dexter.withActivity(CreateProfileActivity.this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {

            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {

                try {
                    Intent select = new Intent(Intent.ACTION_PICK);
                    select.setType("image/*");
                    startActivityForResult(Intent.createChooser(select,"Select an Image"), 1);
                } catch (Exception e) {
                    Log.i("CropImage Error",e.getMessage().toString());
                }


            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toast.makeText(CreateProfileActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1 && resultCode == RESULT_OK ) {
            proPicFilePath = data.getData();
            try {
                CropImage.activity(proPicFilePath)
                        .setAspectRatio(1,1)
                        .start(CreateProfileActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                proPicFilePath = result.getUri();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(proPicFilePath);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    _profileimage.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }


    }

    private void uploadToFirebase() {
        try {
            if (proPicFilePath == null){
                new AlertDialog.Builder(this)
                        .setTitle("Services Hub")
                        .setIcon(R.drawable.appicon)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).setCancelable(false)
                        .show();
            }
            else {
                Log.i("Upload msg", "entered");
                username = _fname.getText().toString().trim();
                lName = _lname.getText().toString().trim();
                cnicNo = _cnicNo.getText().toString().trim();
                gender = _gender.getSelectedItem().toString().trim();
                //dateOfBirthString = _dateOfBirth.getText().toString().trim();
                eMail = _eMail.getText().toString().trim();
                province = _province.getSelectedItem().toString().trim();
                address = _address.getText().toString().trim();
                city = _city.getSelectedItem().toString().trim();
                profession = _profession.getSelectedItem().toString().trim();
                ratePerDay = _ratePerDay.getText().toString().trim();
                skillDetails = skillDetails + _sikllsDetails.getText().toString().trim();
                rating = "0";
                earned = "0";
                badReview = 0;


                ProgressDialog dialog = new ProgressDialog(this);
                dialog.setTitle("Signing Up");
                dialog.show();
                try {
                    if (proPicFilePath!=null) {
                        Log.i("Profile Pic Path", proPicFilePath.toString());
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference uploadfile = storage.getReference().child("Profile").child("ProfilePic" + new Random());
                        uploadfile.putFile(proPicFilePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                uploadfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri profileUri) {
                                        uId = mAuth.getUid().toString();
                                        phoneNo = mAuth.getPhoneNumber();
                                        Date date = new Date();
                                        double timeStamp = date.getTime();
                                        userdata = new Userdata(uId, cnicNo, username, lName, eMail, gender, phoneNo, province, city, address, profession, skillDetails, profileUri.toString(), rating, ratePerDay, earned, token, badReview, timeStamp, 0.0, 0.0);
                                        databaseReference.child("Users").child(uId).setValue(userdata);

                                        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
                                            @Override
                                            public void onSuccess(String s) {
                                                token = s;
                                                uId = mAuth.getUid();
                                                HashMap<String, Object> hashMap = new HashMap<>();
                                                hashMap.put("token", token);
                                                databaseReference.child("Users").child(uId).updateChildren(hashMap);
                                                String notificationId = FirebaseDatabase.getInstance().getReference().push().getKey();
                                                String notificationMessage = "Your account has been successfully registered";
                                                NotificationData notificationData = new NotificationData(notificationId, uId, "signUp", "Registration Successfull", notificationMessage, true, timeStamp);
                                                FirebaseDatabase.getInstance().getReference().child("Notifications").child(uId).child(notificationId).setValue(notificationData);

                                                SendNotification sendNotification = new SendNotification("Registration Successfull", notificationMessage, token, uId, CreateProfileActivity.this);
                                                sendNotification.sendNotification();
                                                preferences = getSharedPreferences("MyData", MODE_PRIVATE);
                                                SharedPreferences.Editor editor = preferences.edit();
                                                editor.putBoolean("isProfileCreated", true);
                                                editor.commit();
                                            }
                                        });


                                        userProfileActivity = new Intent(CreateProfileActivity.this, UserProfileActivity.class);
                                        dialog.dismiss();
                                        startActivity(userProfileActivity);

                                        _fname.setText("");
                                        _lname.setText("");
                                        _cnicNo.setText("");
                                        _eMail.setText("");
                                        _sikllsDetails.setText("");
                                        _address.setText("");
                                        _ratePerDay.setText("");
                                        _sikllsDetails.setText("");
                                        _profileimage.setImageResource(R.drawable.enter_profile_icon);

                                        Toast.makeText(CreateProfileActivity.this, "Signup Successfull!", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                float percent = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                                dialog.setMessage("Upload :" + (int) percent + "%");
                            }
                        });
                    }
                    else{
                        Toast.makeText(this, "Enter Profile Picture", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    Log.i("Upload Data Error", e.getMessage().toString());
                }
            }


        } catch (Exception e) {
            Log.i("UFC Error",e.getMessage().toString());
        }
    }

}