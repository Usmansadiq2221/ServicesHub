package com.devtwist.serviceshub.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.devtwist.serviceshub.Models.ContractData;
import com.devtwist.serviceshub.Models.NotificationData;
import com.devtwist.serviceshub.Models.SendNotification;
import com.devtwist.serviceshub.Models.Userdata;
import com.devtwist.serviceshub.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Date;

public class ContractActivity extends AppCompatActivity {

    private Intent intent;
    private Bundle bundle;
    private String consumerId, serviceProviderId, serviceBudget, serviceTime, serviceWorkPlace, contractStatus, consumerUsername, spToken;
    private TextView _consumerUsername, _consumerCnicNo, _consumerAddress, _serviceProviderUsername, _serviceProviderCnicNo, _serviceProviderAddress;
    private ImageView _contractConsumerProfile, _contractsProviderProfile;
    private EditText _contractBudgetInput, _contractTimeInput, _contractWorkplaceInput;
    private Button _createContractButton;
    private FirebaseDatabase database;
    private Userdata userdata;
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract);

        _contractsProviderProfile = findViewById(R.id._contractsProviderProfile);
        _contractConsumerProfile = findViewById(R.id._contractConsumerProfile);
        _consumerUsername = findViewById(R.id._consumerUsername);
        _consumerCnicNo = findViewById(R.id._consumerCnicNo);
        _consumerAddress = findViewById(R.id._consumerAddress);
        _createContractButton = findViewById(R.id._createContractButton);
        _serviceProviderUsername = findViewById(R.id._serviceProviderUsername);
        _serviceProviderCnicNo = findViewById(R.id._serviceProviderCnicNo);
        _serviceProviderAddress = findViewById(R.id._serviceProviderAddress);
        _contractBudgetInput = findViewById(R.id._contractBudgetInput);
        _contractTimeInput = findViewById(R.id._contractTimeInput);
        _contractWorkplaceInput = findViewById(R.id._contractWorkplaceInput);
        database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();

        intent = getIntent();
        bundle = intent.getExtras();
        consumerId = bundle.getString("consumerId");
        serviceProviderId = bundle.getString("serviceProviderId");



        databaseReference.child("Users").child(consumerId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    userdata = new Userdata();
                    userdata = snapshot.getValue(Userdata.class);
                    Picasso.get().load(userdata.getProfilePic()).placeholder(R.drawable.placeholder).into(_contractConsumerProfile);
                    consumerUsername = userdata.getUsername();
                    _consumerUsername.setText(consumerUsername);
                    _consumerCnicNo.setText(userdata.getCnicNo());
                    _consumerAddress.setText(userdata.getAddress());
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("Contract Data Retriving", e.getMessage().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        databaseReference.child("Users").child(serviceProviderId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userdata = new Userdata();
                userdata = snapshot.getValue(Userdata.class);
                Picasso.get().load(userdata.getProfilePic()).placeholder(R.drawable.placeholder).into(_contractsProviderProfile);
                spToken = userdata.getToken();
                _serviceProviderUsername.setText(userdata.getUsername());
                _serviceProviderCnicNo.setText(userdata.getCnicNo());
                _serviceProviderAddress.setText(userdata.getAddress());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        _createContractButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceBudget = _contractBudgetInput.getText().toString().trim();
                serviceTime = _contractTimeInput.getText().toString().trim();
                serviceWorkPlace = _contractWorkplaceInput.getText().toString().trim();
                if (serviceBudget.length()>1 && serviceTime.length()>1 && serviceWorkPlace.length()>1) {
                    String randomKey = databaseReference.push().getKey();
                    contractStatus = "Requesting...";
                    Date date = new Date();
                    double timeStamp = date.getTime();
                    ContractData contractData = new ContractData(randomKey, consumerId, serviceProviderId, serviceBudget, serviceTime, serviceWorkPlace, contractStatus, timeStamp);
                    databaseReference.child("Contracts")
                            .child(consumerId)
                            .child(randomKey)
                            .setValue(contractData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    databaseReference.child("Contracts")
                                            .child(serviceProviderId)
                                            .child(randomKey)
                                            .setValue(contractData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(ContractActivity.this, "Contract has been\nsuccessfully generated!", Toast.LENGTH_LONG).show();

                                                    String notificationMessage = consumerUsername + " wants to build a contract with you";

                                                    String notificationId = FirebaseDatabase.getInstance().getReference().push().getKey();
                                                    NotificationData notificationData = new NotificationData(notificationId, consumerId, "contract", "Contract Request", notificationMessage, false, timeStamp);
                                                    FirebaseDatabase.getInstance().getReference().child("Notifications").child(serviceProviderId).child(notificationId).setValue(notificationData);

                                                    runAds();
                                                    SendNotification sendNotification = new SendNotification("Contract Request", notificationMessage, spToken, consumerId, ContractActivity.this);
                                                    sendNotification.sendNotification();
                                                    finish();
                                                }
                                            });
                                }
                            });
                }else {
                    Toast.makeText(ContractActivity.this, "Enter all values!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void setUpAds() {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,getString(R.string.interstitialAd_string), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i("AdError", loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });
    }

    private void runAds() {
        new CountDownTimer(7000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(ContractActivity.this);
                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent();

                        }

                    });
                } else {

                }
            }
        }.start();
    }
}