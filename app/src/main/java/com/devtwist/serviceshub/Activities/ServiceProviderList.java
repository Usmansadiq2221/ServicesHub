package com.devtwist.serviceshub.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.devtwist.serviceshub.Adapters.UserAdapter;
import com.devtwist.serviceshub.Models.Userdata;
import com.devtwist.serviceshub.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ServiceProviderList extends AppCompatActivity {

    private Intent intent;
    private Bundle bundle;
    private String serviceName, myId;
    private TextView _userListheading, _noUserText;
    private RecyclerView _preUsersList;
    private UserAdapter _userListAdapter;
    private List<Userdata> userdataList;
    private AdView _sProviderAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_list);

        _userListheading = findViewById(R.id._userListHeeading);
        _preUsersList = findViewById(R.id._preUsersList);
        _noUserText = findViewById(R.id._noUserText);
        _sProviderAdView = findViewById(R.id._sProviderdAdView);

        AdRequest adRequest = new AdRequest.Builder().build();
        _sProviderAdView.loadAd(adRequest);

        intent = getIntent();
        bundle = intent.getExtras();
        serviceName = bundle.getString("serviceName");
        myId = bundle.getString("myId");
        _userListheading.setText(serviceName);

        _preUsersList.setLayoutManager(new LinearLayoutManager(this));

        userdataList = new ArrayList<>();
        _userListAdapter = new UserAdapter(getApplicationContext(), userdataList,myId);
        _preUsersList.setAdapter(_userListAdapter);
        readUsersData();
    }

    private void readUsersData(){
        myId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference().child("Users");
        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userdataList.clear();
                try {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Userdata userdata = dataSnapshot.getValue(Userdata.class);
                        if (userdata.getProfession().equals(serviceName) && !userdata.getuId().equals(myId)){
                            userdataList.add(userdata);
                        }
                    }
                    if (userdataList.size() == 0) {
                        _noUserText.setVisibility(TextView.VISIBLE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("Userlist error", e.getMessage().toString());
                }
                _userListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}