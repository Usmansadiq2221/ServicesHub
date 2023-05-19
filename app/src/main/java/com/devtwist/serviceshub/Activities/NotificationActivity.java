package com.devtwist.serviceshub.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.devtwist.serviceshub.Adapters.NotificationAdapter;
import com.devtwist.serviceshub.Models.MessageData;
import com.devtwist.serviceshub.Models.NotificationData;
import com.devtwist.serviceshub.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;

public class NotificationActivity extends AppCompatActivity {

    private String myId;
    private Intent intent;
    private ArrayList<NotificationData> notificationList;
    private NotificationAdapter notificationAdapter;
    private ShimmerRecyclerView _preNotifyView;
    private AdView _notifyAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        _preNotifyView = findViewById(R.id._preNotifyView);
        _notifyAdView = findViewById(R.id._notifyAdView);

        AdRequest adRequest = new AdRequest.Builder().build();
        _notifyAdView.loadAd(adRequest);

        myId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        try {
            notificationList = new ArrayList<>();
            notificationAdapter = new NotificationAdapter(this, notificationList, myId);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);
            _preNotifyView.setLayoutManager(linearLayoutManager);
            _preNotifyView.setAdapter(notificationAdapter);
            _preNotifyView.showShimmerAdapter();

            readNotificationList();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Block List Error", e.getMessage().toString());
        }
    }

    private void readNotificationList() {
        FirebaseDatabase.getInstance().getReference().child("Notifications").child(myId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        notificationList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            NotificationData notificationData = dataSnapshot.getValue(NotificationData.class);
                            notificationList.add(notificationData);
                        }
                        notificationList.sort(Comparator.comparing(NotificationData::getTimestamp));
                        _preNotifyView.hideShimmerAdapter();
                        notificationAdapter.notifyDataSetChanged();

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

}