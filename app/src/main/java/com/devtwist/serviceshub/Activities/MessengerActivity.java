package com.devtwist.serviceshub.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.devtwist.serviceshub.Adapters.CallListAdapter;
import com.devtwist.serviceshub.Adapters.ChatListAdapter;
import com.devtwist.serviceshub.Models.CallListData;
import com.devtwist.serviceshub.Models.ChatListData;
import com.devtwist.serviceshub.Models.Userdata;
import com.devtwist.serviceshub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MessengerActivity extends AppCompatActivity {

    private float listPosition;
    private TextView _chatText, _callText;
    private LinearLayout _chatListLayout, _callListLayout;
    private ShimmerRecyclerView _preChatListView, _preCallsListView;
    private View _chatView, _callView;
    private ArrayList<ChatListData> chatArrayList;
    private ArrayList<CallListData> callArrayList;
    private ChatListAdapter chatListAdapter;
    private CallListAdapter callListAdapter;
    private ConstraintLayout _preChatsLayout;

    private String myId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

        _chatText = findViewById(R.id._chatText);
        _callText = findViewById(R.id._callText);
        _preChatsLayout = findViewById(R.id._preChatsLayout);
        _chatListLayout = findViewById(R.id._chatListLayout);
        _callListLayout = findViewById(R.id._callListLayout);
        _preChatListView = findViewById(R.id._preChatListView);
        _preCallsListView = findViewById(R.id._preCallsListView);
        _chatView = findViewById(R.id._chatView);
        _callView = findViewById(R.id._callView);

        listPosition = 1000f;
        _callListLayout.setTranslationX(listPosition);


        myId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        _preChatsLayout.setVisibility(View.VISIBLE);

        /*
        _mMenuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isMenuVisible){
                    _mMenuLayout.setVisibility(View.VISIBLE);
                    _mMenuLayout.animate().alpha(1f).setDuration(500);
                    new CountDownTimer(3000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            _mMenuLayout.animate().alpha(0f).setDuration(500);
                            _mMenuLayout.setVisibility(View.GONE);
                            isMenuVisible = false;
                        }
                    }.start();
                }

            }
        });

        _mContactText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase.getInstance().getReference().child("Users").child("+923053073543").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Userdata userdata = snapshot.getValue(Userdata.class);
                        intent = new Intent(ChatListActivity.this, ChatActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("myId", myId);
                        bundle.putString("userId", userdata.getuId());
                        bundle.putString("username", userdata.getUsername());
                        bundle.putString("profileUri", userdata.getProfilePic());
                        bundle.putString("token", userdata.getToken());
                        intent.putExtras(bundle);
                        isMenuVisible = false;
                        startActivity(intent);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        */

        _chatText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listPosition != 1000f){
                    listPosition = 1000f;
                    _callView.setAlpha(0f);
                    _callText.setAlpha(0.6f);
                    _chatText.setAlpha(1f);
                    _chatView.setAlpha(1f);
                    _chatListLayout.animate().translationXBy(listPosition).setDuration(300).start();
                    _callListLayout.animate().translationXBy(listPosition).setDuration(300).start();

                }
            }
        });

        _callText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listPosition == 1000f){
                    listPosition = -1000f;
                    _chatView.setAlpha(0f);
                    _chatText.setAlpha(0.6f);
                    _callText.setAlpha(1f);
                    _callView.setAlpha(1f);
                    _chatListLayout.animate().translationXBy(listPosition).setDuration(300).start();
                    _callListLayout.animate().translationXBy(listPosition).setDuration(300).start();

                }
            }
        });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        _preChatListView.setLayoutManager(linearLayoutManager);

        chatArrayList = new ArrayList<>();
        chatListAdapter = new ChatListAdapter(this, chatArrayList, myId);
        _preChatListView.setAdapter(chatListAdapter);
        _preChatListView.showShimmerAdapter();
        readChatList();


        LinearLayoutManager callLayoutmanager = new LinearLayoutManager(this);
        callLayoutmanager.setReverseLayout(true);
        callLayoutmanager.setStackFromEnd(true);
        _preCallsListView.setLayoutManager(callLayoutmanager);

        callArrayList = new ArrayList<>();
        callListAdapter = new CallListAdapter(MessengerActivity.this, callArrayList);
        _preCallsListView.setAdapter(callListAdapter);
        _preCallsListView.showShimmerAdapter();
        readCallList();

    }


    private void readCallList() {
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            database.getReference().child("CallList").child(myId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    callArrayList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        CallListData callListData = dataSnapshot.getValue(CallListData.class);
                        callArrayList.add(callListData);
                    }
                    callArrayList.sort(Comparator.comparing(CallListData::getTimeStamp));
                    _preCallsListView.hideShimmerAdapter();
                    callListAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            Log.i("Call Error", e.getMessage().toString());
        }
    }

    private void readChatList(){
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            database.getReference().child("ChatList").child(myId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    chatArrayList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        ChatListData chatListData = dataSnapshot.getValue(ChatListData.class);
                        chatArrayList.add(chatListData);
                    }
                    chatArrayList.sort(Comparator.comparing(ChatListData::getTimeStamp));
                    _preChatListView.hideShimmerAdapter();
                    chatListAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("ChatLstError", e.getMessage().toString());
        }
    }

}