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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.devtwist.serviceshub.Adapters.PostAdapter;
import com.devtwist.serviceshub.Models.MessageData;
import com.devtwist.serviceshub.Models.NotificationData;
import com.devtwist.serviceshub.Models.PostData;
import com.devtwist.serviceshub.Models.SendNotification;
import com.devtwist.serviceshub.Models.Userdata;
import com.devtwist.serviceshub.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class PostActivity extends AppCompatActivity {

    private ImageView _postBackButton;
    private Spinner _enterPostProfession;
    private EditText _enterPostBudget, _enterPostTime, _enterPostWorkplace;
    private Button _postSubmitButton;
    private ArrayAdapter<CharSequence> postProfessionAdapter;
    private Intent intent;
    private Bundle extras;
    private String myId, userCity, postId, serviceName, serviceTime, serviceBudget, serviceWorkplace;
    private DatabaseReference databaseReference;
    private ArrayList<PostData> postList;
    private ShimmerRecyclerView _prePostView;
    private PostAdapter _postAdapter;
    private InterstitialAd mInterstitialAd;
    private AdView _postAdView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        setUpAds();
        runAds();

        try {
            _postBackButton = findViewById(R.id._postBackButton);
            _enterPostBudget = findViewById(R.id._enterPostBudget);
            _enterPostTime = findViewById(R.id._enterPostTime);
            _enterPostWorkplace = findViewById(R.id._enterPostWorkplace);
            _enterPostProfession = findViewById(R.id._enterPostProfession);
            _postSubmitButton = findViewById(R.id._postSubmitButton);
            _prePostView = findViewById(R.id._prePostView);
            _postAdView = findViewById(R.id._postAdView);

            AdRequest adRequest = new AdRequest.Builder().build();
            _postAdView.loadAd(adRequest);

            intent = getIntent();
            extras = intent.getExtras();
            myId = extras.getString("userId");
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);

            _prePostView.setLayoutManager(linearLayoutManager);
            postList = new ArrayList<>();
            _postAdapter = new PostAdapter(this, postList, myId);
            _prePostView.setAdapter(_postAdapter);
            _prePostView.showShimmerAdapter();
            readPostList();

        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Post Error", e.getMessage().toString());
        }

        postProfessionAdapter = ArrayAdapter.createFromResource(this,
                R.array.profession_array,R.layout.post_spinner_style);
        postProfessionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _enterPostProfession.setAdapter(postProfessionAdapter);

        _postBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        _postSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ( _enterPostBudget.getText().length()<1 &&_enterPostTime.getText().length()<1 && _enterPostWorkplace.getText().length()<1){
                    Toast.makeText(PostActivity.this, "Enter all details!", Toast.LENGTH_LONG).show();
                }
                else if (_enterPostProfession.getSelectedItem().toString().matches("Consumer")){
                    Toast.makeText(PostActivity.this, "Select Service", Toast.LENGTH_LONG).show();
                }
                else {
                    uploadPost();
                }

            }
        });

    }

    private void readPostList() {
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            database.getReference().child("Posts").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    postList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        PostData postData = dataSnapshot.getValue(PostData.class);
                        postList.add(postData);
                    }
                    postList.sort(Comparator.comparing(PostData::getTimestamp));
                    _prePostView.hideShimmerAdapter();
                    _postAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("PostListError", e.getMessage().toString());
        }
    }

    private void uploadPost() {
        try {
            intent = getIntent();
            extras = intent.getExtras();
            myId = extras.getString("userId");
            userCity = extras.getString("userCity");
            serviceName = _enterPostProfession.getSelectedItem().toString().trim();
            serviceBudget = _enterPostBudget.getText().toString().trim();
            serviceTime = _enterPostTime.getText().toString().trim();
            serviceWorkplace = _enterPostWorkplace.getText().toString().trim();

            databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
            postId = databaseReference.push().getKey();
            Date date = new Date();
            double timeStamp = date.getTime();

            PostData postData = new PostData(postId, serviceBudget, serviceName, serviceTime, serviceWorkplace, userCity, myId, timeStamp);
            databaseReference.child(postId).setValue(postData).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                Userdata userdata = dataSnapshot.getValue(Userdata.class);
                                if (serviceName.equals(userdata.getProfession())){
                                    String userToken, notificationMessage;
                                    userToken = userdata.getToken();
                                    notificationMessage = userdata.getUsername() + " is looking for a " + serviceName;

                                    String notificationId = FirebaseDatabase.getInstance().getReference().push().getKey();

                                    NotificationData notificationData = new NotificationData(notificationId, myId, "post", "Job Alert", notificationMessage, false, timeStamp);
                                    FirebaseDatabase.getInstance().getReference().child("Notifications").child(userdata.getuId()).child(notificationId).setValue(notificationData);

                                    SendNotification sendNotification = new SendNotification("Job Alert", notificationMessage, userToken, myId, PostActivity.this);
                                    sendNotification.sendNotification();
                                    runAds();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PostActivity.this, "Network Problem\nCheck your Internet\nconnection!", Toast.LENGTH_SHORT).show();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            Log.i("PostError", e.getMessage().toString());
        }

        _enterPostBudget.setText("");
        _enterPostTime.setText("");
        _enterPostWorkplace.setText("");
        _enterPostProfession.setAdapter(postProfessionAdapter);
    }

    private void setUpAds() {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-8385601672345207/4369747004", adRequest,
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
                    mInterstitialAd.show(PostActivity.this);
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