package com.devtwist.serviceshub.Activities;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.devtwist.serviceshub.Models.Userdata;
import com.devtwist.serviceshub.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private EditText _otpPhoneInput, _otpCodeInput1, _otpCodeInput2, _otpCodeInput3, _otpCodeInput4, _otpCodeInput5, _otpCodeInput6;
    private Button _otpPhoneSubmit, _otpCodeSubmit;
    private LinearLayout _otpPhoneInputLayout, _otpCodeInputLayout;
    private FirebaseAuth mAuth;
    private TextView _resendOtpText,_loginErrrorMsg;
    private String firebaseOtp, phoneNo;
    private ProgressBar _otpProgressbar;
    private Userdata userdata;
    private Intent i, intent;
    private boolean isFound;
    private ImageView _innerLogo, _outerLogo;
    private CountDownTimer forwardAnimation, outerLogoIncrease, innerLogoDecrease, outerLogoDecrease,innerLogoIncrease;
    private SharedPreferences preferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        _innerLogo = findViewById(R.id._innerLogo);
        _outerLogo = findViewById(R.id._outerLogo);


        _otpPhoneInput = findViewById(R.id._otpPhoneInput);
        _otpCodeInput1 = findViewById(R.id._otpCodeInput1);
        _otpCodeInput2 = findViewById(R.id._otpCodeInput2);
        _otpCodeInput3 = findViewById(R.id._otpCodeInput3);
        _otpCodeInput4 = findViewById(R.id._otpCodeInput4);
        _otpCodeInput5 = findViewById(R.id._otpCodeInput5);
        _otpCodeInput6 = findViewById(R.id._otpCodeInput6);

        _loginErrrorMsg = findViewById(R.id._loginErrorMsg);

        _resendOtpText = findViewById(R.id._resendOtpText);
        _otpPhoneSubmit = findViewById(R.id._otpPhoneSubmit);
        _otpCodeSubmit = findViewById(R.id._otpCodeSubmit);
        _otpProgressbar = findViewById(R.id._otpProgressBar);
        _otpPhoneInputLayout = findViewById(R.id._otpPhoneInputLayout);
        _otpCodeInputLayout = findViewById(R.id._otpCodeInputLayout);
        mAuth = FirebaseAuth.getInstance();


        _innerLogo.setScaleX(0f);
        _innerLogo.setScaleY(0f);

        forwardAnimation = new CountDownTimer(1500,1500) {
            @Override
            public void onTick(long millisUntilFinished) {
                _outerLogo.animate().scaleX(0.85f).scaleY(0.85f).setDuration(1500);
                _innerLogo.animate().scaleX(1f).scaleY(1f).setDuration(1500);
            }

            @Override
            public void onFinish() {
                outerLogoIncrease.start();
            }
        };

        outerLogoIncrease = new CountDownTimer(1500,1500) {
            @Override
            public void onTick(long millisUntilFinished) {
                _outerLogo.animate().scaleX(1.15f).scaleY(1.15f).setDuration(1500);
            }

            @Override
            public void onFinish() {
                outerLogoDecrease.start();
            }
        };

        outerLogoDecrease = new CountDownTimer(1500,1500) {
            @Override
            public void onTick(long millisUntilFinished) {
                _outerLogo.animate().scaleX(0.85f).scaleY(0.85f).setDuration(1500);
            }

            @Override
            public void onFinish() {
                innerLogoDecrease.start();
            }
        };

        innerLogoDecrease = new CountDownTimer(1500,1500) {
            @Override
            public void onTick(long millisUntilFinished) {
                _innerLogo.animate().scaleX(0.75f).scaleY(0.75f).setDuration(1500);
            }

            @Override
            public void onFinish() {
                innerLogoIncrease.start();
            }
        };

        innerLogoIncrease = new CountDownTimer(1500,1500) {
            @Override
            public void onTick(long millisUntilFinished) {
                _innerLogo.animate().scaleX(1f).scaleY(1f).setDuration(1500);
            }

            @Override
            public void onFinish() {
                outerLogoIncrease.start();
            }
        };

        forwardAnimation.start();

        _otpCodeInput1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                _otpCodeInput2.requestFocus();
            }
        });

        _otpCodeInput2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                _otpCodeInput3.requestFocus();
            }
        });

        _otpCodeInput3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                _otpCodeInput4.requestFocus();
            }
        });

        _otpCodeInput4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                _otpCodeInput5.requestFocus();
            }
        });

        _otpCodeInput5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                _otpCodeInput6.requestFocus();
            }
        });

        _otpPhoneSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                _otpPhoneSubmit.setVisibility(View.GONE);
                _otpProgressbar.setVisibility(View.VISIBLE);
                if (_otpPhoneInput.getText().length()<10) {
                    _loginErrrorMsg.setVisibility(View.VISIBLE);
                    _otpPhoneSubmit.setVisibility(View.VISIBLE);
                    _otpProgressbar.setVisibility(View.GONE);
                }
                else {
                    sendOtp();
                }

            }
        });

        _resendOtpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOtp();
            }
        });

        _otpCodeSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = _otpCodeInput1.getText().toString() +
                        _otpCodeInput2.getText().toString() +
                        _otpCodeInput3.getText().toString() +
                        _otpCodeInput4.getText().toString() +
                        _otpCodeInput5.getText().toString() +
                        _otpCodeInput6.getText().toString() ;

                if (code.length()==6){
                    verifyCode(code);
                    _otpCodeSubmit.setVisibility(View.GONE);
                    _otpProgressbar.setVisibility(View.VISIBLE);
                }else {
                    Toast.makeText(LoginActivity.this, "Invalid Otp!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //for stop logo animation...
    private void stopAnimation(){
        forwardAnimation.cancel();
        outerLogoIncrease.cancel();
        outerLogoDecrease.cancel();
        innerLogoDecrease.cancel();
        innerLogoDecrease.cancel();
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            /*i = new Intent(LoginActivity.this, CreateProfileActivity.class);
                            stopAnimation();
                            startActivity(i);
                            finish();
                            */

                            // if the code is correct and the task is successful
                            // we are sending our user to new activity.
                            try {

                                String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                FirebaseDatabase.getInstance().getReference().child("Users").child(uID).get()
                                        .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                isFound = false;
                                                if (task.isSuccessful()) {
                                                    DataSnapshot snapshot = task.getResult();
                                                    if (snapshot.exists()) {
                                                        isFound = true;
                                                    }
                                                }
                                                if (isFound) {
                                                    preferences = getSharedPreferences("MyData", MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = preferences.edit();
                                                    editor.putBoolean("isLogedIn", true);
                                                    editor.putBoolean("isProfileCreated", true);
                                                    editor.commit();
                                                    //Toast.makeText(MainActivity.this, "found", Toast.LENGTH_SHORT).show();
                                                    FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            String token = s;
                                                            String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                            HashMap<String, Object> hashMap = new HashMap<>();
                                                            hashMap.put("token", token);
                                                            FirebaseDatabase.getInstance().getReference()
                                                                    .child("Users").child(uId).updateChildren(hashMap);

                                                        }
                                                    });
                                                    intent = new Intent(LoginActivity.this, UserProfileActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                                else{
                                                    preferences = getSharedPreferences("MyData", MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = preferences.edit();
                                                    editor.putBoolean("isLogedIn", true);
                                                    editor.commit();
                                                    //Toast.makeText(MainActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
                                                    intent = new Intent(LoginActivity.this, CreateProfileActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }

                                            }
                                        });


                            } catch (Exception e) {
                                Log.i("Open Activity Error",e.getMessage().toString());
                            }



                        } else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    private void sendOtp() {
        try {
            phoneNo = "+92" + _otpPhoneInput.getText().toString().trim();
            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(mAuth)
                            .setPhoneNumber(phoneNo)		 // Phone number to verify
                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                            .setActivity(this)				 // Activity (for callback binding)
                            .setCallbacks(mCallBack)		 // OnVerificationStateChangedCallbacks
                            .build();
            PhoneAuthProvider.verifyPhoneNumber(options);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Send Otp Error", e.getMessage().toString());
        }
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            // initializing our callbacks for on
            // verification callback method.
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        // below method is used when
        // OTP is sent from Firebase
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            _otpPhoneInputLayout.setVisibility(LinearLayout.GONE);
            _otpProgressbar.setVisibility(View.GONE);
            _otpCodeInputLayout.setVisibility(LinearLayout.VISIBLE);
            firebaseOtp = s;

        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            // below line is used for getting OTP code
            // which is sent in phone auth credentials.
            final String code = phoneAuthCredential.getSmsCode();

            // checking if the code
            // is null or not.
            if (code.length()==6) {
                verifyCode(code);
            }else{
                Toast.makeText(LoginActivity.this, "Invalid Otp!", Toast.LENGTH_SHORT).show();
            }
        }

        // this method is called when firebase doesn't
        // sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(FirebaseException e) {
            // displaying error message with firebase exception.
            Log.i("Send Otp Error", e.getMessage().toString());
        }
    };

    private void verifyCode(String code) {
        // below line is used for getting getting
        // credentials from our verification id and code.
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(firebaseOtp, code);

        // after getting credential we are
        // calling sign in method.
        signInWithCredential(credential);
    }




    /*

    private void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {*/
                            /*i = new Intent(LoginActivity.this, CreateProfileActivity.class);
                            stopAnimation();
                            startActivity(i);
                            finish();
                            */

    // if the code is correct and the task is successful
    // we are sending our user to new activity.
/*                            try {
                                String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                FirebaseDatabase.getInstance().getReference().child("Users").child(uID).get()
                                        .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                if (task.isSuccessful()) {

                                                    DataSnapshot snapshot = task.getResult();
                                                    if (snapshot.exists()) {
                                                        isFound = true;
                                                    }
                                                    if (isFound) {
                                                        preferences = getSharedPreferences("MyData", MODE_PRIVATE);
                                                        SharedPreferences.Editor editor = preferences.edit();
                                                        editor.putBoolean("isLogedIn", true);
                                                        editor.putBoolean("isProfileCreated", true);
                                                        editor.commit();
                                                        //Toast.makeText(MainActivity.this, "found", Toast.LENGTH_SHORT).show();
                                                        intent = new Intent(LoginActivity.this, UserProfileActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    } else {
                                                        preferences = getSharedPreferences("MyData", MODE_PRIVATE);
                                                        SharedPreferences.Editor editor = preferences.edit();
                                                        editor.putBoolean("isLogedIn", true);
                                                        editor.commit();
                                                        //Toast.makeText(MainActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
                                                        intent = new Intent(LoginActivity.this, CreateProfileActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }
                                                else{
                                                    Toast.makeText(LoginActivity.this, "Network Problem\nCheck your internet connection", Toast.LENGTH_LONG).show();

                                                }
                                            }
                                        });
                            } catch (Exception e) {
                                Log.i("Open Activity Error",e.getMessage().toString());
                            }



                        } else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    private void sendOtp() {
        try {
            phoneNo = "+92" + _otpPhoneInput.getText().toString().trim();
            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(mAuth)
                            .setPhoneNumber(phoneNo)		 // Phone number to verify
                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                            .setActivity(this)				 // Activity (for callback binding)
                            .setCallbacks(mCallBack)		 // OnVerificationStateChangedCallbacks
                            .build();
            PhoneAuthProvider.verifyPhoneNumber(options);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Send Otp Error", e.getMessage().toString());
        }
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            // initializing our callbacks for on
            // verification callback method.
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        // below method is used when
        // OTP is sent from Firebase
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            _otpPhoneInputLayout.setVisibility(LinearLayout.GONE);
            _otpProgressbar.setVisibility(View.GONE);
            _otpCodeInputLayout.setVisibility(LinearLayout.VISIBLE);
            firebaseOtp = s;

        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            // below line is used for getting OTP code
            // which is sent in phone auth credentials.
            final String code = phoneAuthCredential.getSmsCode();

            // checking if the code
            // is null or not.
            if (code.length()==6) {
                verifyCode(code);
            }else{
                Toast.makeText(LoginActivity.this, "Invalid Otp!", Toast.LENGTH_SHORT).show();
            }
        }

        // this method is called when firebase doesn't
        // sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(FirebaseException e) {
            // displaying error message with firebase exception.
            Log.i("Send Otp Error", e.getMessage().toString());
        }
    };

    private void verifyCode(String code) {
        // below line is used for getting getting
        // credentials from our verification id and code.
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(firebaseOtp, code);

        // after getting credential we are
        // calling sign in method.
        signInWithCredential(credential);
    }
*/

}