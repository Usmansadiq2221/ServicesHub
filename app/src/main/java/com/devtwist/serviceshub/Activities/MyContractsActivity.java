package com.devtwist.serviceshub.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.devtwist.serviceshub.Adapters.ContractListAdapter;
import com.devtwist.serviceshub.Models.BlockListData;
import com.devtwist.serviceshub.Models.ChatListData;
import com.devtwist.serviceshub.Models.ContractData;
import com.devtwist.serviceshub.Models.FeedbackData;
import com.devtwist.serviceshub.Models.MessageData;
import com.devtwist.serviceshub.Models.NotificationData;
import com.devtwist.serviceshub.Models.SendNotification;
import com.devtwist.serviceshub.Models.Userdata;
import com.devtwist.serviceshub.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class MyContractsActivity extends AppCompatActivity {

    private ShimmerRecyclerView _preContractView;
    private Intent intent;
    private String myId;
    private float rating;
    private int badReview, totalEarned;
    private TextView _noContractText;
    private ContractData contractData;
    private ContractListAdapter contractListAdapter;
    private ArrayList<ContractData> myContractList;
    private LinearLayout _feedbackLayout;
    private RatingBar _ratingBar;
    private EditText _feedbackInput;
    private Button _submitFeedback;
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contracts);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        setUpAds();
        runAds();

        try {
            intent = getIntent();
            myId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            _preContractView = findViewById(R.id._preContractsView);
            _noContractText = findViewById(R.id._noContractText);
            _feedbackLayout = findViewById(R.id._feedbackLayout);
            myContractList = new ArrayList<>();
            _ratingBar = findViewById(R.id._ratingBar);
            _feedbackInput = findViewById(R.id._feedbackInput);
            _submitFeedback = findViewById(R.id._submitFeedback);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);
            _preContractView.setLayoutManager(linearLayoutManager);
            contractListAdapter = new ContractListAdapter(MyContractsActivity.this, myContractList, myId);
            _preContractView.setAdapter(contractListAdapter);
            _preContractView.showShimmerAdapter();

            readContractsData();

            _submitFeedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    String my_Id, user_Id, contract_Status, contract_ID, comment, randomKey, token, notificationMessage, title;

                    my_Id = contractListAdapter.getArrayList().get(0);
                    user_Id = contractListAdapter.getArrayList().get(1);
                    contract_Status = contractListAdapter.getArrayList().get(2);
                    token  = contractListAdapter.getArrayList().get(3);
                    notificationMessage = contractListAdapter.getArrayList().get(4);
                    title = contractListAdapter.getArrayList().get(5);
                    contract_ID = contractListAdapter.getArrayList().get(6);
                    int earned = Integer.parseInt(contractListAdapter.getArrayList().get(7));
                    rating = ((float) _ratingBar.getRating());
                    comment = "" + _feedbackInput.getText().toString().trim();
                    randomKey = database.getReference().push().getKey().toString();
                    Date date = new Date();
                    double timeStamp = date.getTime();
                    FeedbackData feedbackData = new FeedbackData(my_Id, "" + rating, comment,timeStamp);
                    database.getReference().child("Feedback").child(user_Id)
                            .child(randomKey).setValue(feedbackData)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    database.getReference().child("Contracts").child(my_Id)
                                            .child(contract_ID).child("contractStatus")
                                            .setValue(contract_Status).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    database.getReference().child("Contracts").child(user_Id)
                                                            .child(contract_ID).child("contractStatus")
                                                            .setValue(contract_Status).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    database.getReference().child("Users").child(user_Id).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                            Userdata userdata = snapshot.getValue(Userdata.class);
                                                                            float userrating = (float) Float.parseFloat(userdata.getRating());
                                                                            badReview = userdata.getBadReview();
                                                                            int tEearned = Integer.parseInt(userdata.getEarned());
                                                                            int totalEarned = tEearned + earned;
                                                                            if (userrating > 0.0) {
                                                                                userrating = (rating + userrating) / 2;
                                                                            }

                                                                            if (badReview >= 5) {
                                                                                BlockListData blockListData = new BlockListData (user_Id);
                                                                                database.getReference().child("BlockList").child(user_Id).setValue(blockListData);
                                                                            }

                                                                            String finalRating = new DecimalFormat("#.#").format(rating);
                                                                            Log.i("rating", finalRating);
                                                                            database.getReference().child("Users").child(user_Id).child("rating").setValue(finalRating);
                                                                            database.getReference().child("Users").child(user_Id).child("earned").setValue(totalEarned + "");
                                                                            _preContractView.setVisibility(RecyclerView.VISIBLE);
                                                                            _feedbackLayout.setVisibility(LinearLayout.GONE);
                                                                            _ratingBar.setRating(0f);
                                                                            _feedbackInput.setText("");
                                                                            Log.i("Bad Review", ""+badReview);

                                                                            String notificationId = FirebaseDatabase.getInstance().getReference().push().getKey();
                                                                            NotificationData notificationData = new NotificationData(notificationId, my_Id,"contract", title, notificationMessage, false,timeStamp);
                                                                            FirebaseDatabase.getInstance().getReference().child("Notifications").child(user_Id).child(notificationId).setValue(notificationData);

                                                                            if (rating < 2.0) {
                                                                                ++badReview;
                                                                            }
                                                                            database.getReference().child("Users").child(user_Id).child("badReview").setValue(badReview);

                                                                            SendNotification sendNotification = new SendNotification(title, notificationMessage, token, my_Id, MyContractsActivity.this);
                                                                            sendNotification.sendNotification();
                                                                            finish();
                                                                            runAds();

                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                        }
                                                                    });

                                                                }
                                                            });
                                                }
                                            });
                                }
                            });

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Log.i("My Contract Error", e.getMessage().toString());
        }

    }

    private void readContractsData() {
        FirebaseDatabase.getInstance().getReference().child("Contracts").child(myId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        myContractList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            contractData = dataSnapshot.getValue(ContractData.class);
                            myContractList.add(contractData);
                        }
                        if (myContractList.size()<1){
                            _noContractText.setVisibility(TextView.VISIBLE);
                        }
                        myContractList.sort(Comparator.comparing(ContractData::getTimestamp));
                        _preContractView.hideShimmerAdapter();
                        contractListAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

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
                    mInterstitialAd.show(MyContractsActivity.this);
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