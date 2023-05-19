package com.devtwist.serviceshub.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.devtwist.serviceshub.Adapters.FeedbackAdapter;
import com.devtwist.serviceshub.Models.FeedbackData;
import com.devtwist.serviceshub.Models.MessageData;
import com.devtwist.serviceshub.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;

public class FeedbackActivity extends AppCompatActivity {

    private Intent intent;
    private String myId;
    private ShimmerRecyclerView _preReviewView;
    private TextView _noReviewText;
    private ArrayList<FeedbackData> feedbackDataList;
    private FeedbackAdapter feedbackAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        //initializing views
        _preReviewView = findViewById(R.id._preReviewView);
        _noReviewText = findViewById(R.id.noReviewText);

        //get data from previousactivity
        intent = getIntent();
        myId = intent.getStringExtra("myId");

        Log.i("FeedbackData", myId);

        feedbackDataList = new ArrayList<>(); //initialize Array List
        feedbackAdapter = new FeedbackAdapter(this, feedbackDataList); //initializingAdapter

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        _preReviewView.setLayoutManager(linearLayoutManager); //setting Layout manager for recycle vier
        _preReviewView.setAdapter(feedbackAdapter);
        _preReviewView.showShimmerAdapter();

        readReviewList();//calling function to load into list for recycler view adapter

    }

    private void readReviewList() {
        try {
            FirebaseDatabase.getInstance().getReference().child("Feedback").child(myId)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            feedbackDataList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                FeedbackData feedbackData = dataSnapshot.getValue(FeedbackData.class);
                                feedbackDataList.add(feedbackData);
                                Log.i("ReviewStatus", "Successfull");
                            }
                            if (feedbackDataList.size()<1){
                                _preReviewView.setVisibility(RecyclerView.GONE);
                                _noReviewText.setVisibility(TextView.VISIBLE);
                            }

                            feedbackDataList.sort(Comparator.comparing(FeedbackData::getTimestamp));
                            _preReviewView.hideShimmerAdapter();
                            feedbackAdapter.notifyDataSetChanged();

                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("feedback Eror",e.getMessage().toString());
        }
    }

}