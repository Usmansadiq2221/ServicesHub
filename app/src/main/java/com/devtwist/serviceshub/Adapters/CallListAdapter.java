package com.devtwist.serviceshub.Adapters;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devtwist.serviceshub.Models.CallListData;
import com.devtwist.serviceshub.Models.Userdata;
import com.devtwist.serviceshub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CallListAdapter extends RecyclerView.Adapter<CallListAdapter.CallListViewHolder>{

    private Context context;
    private ArrayList<CallListData> callArrayList;
    private String myId, callId;
    private boolean isOpened;
    private CountDownTimer timer;

    public CallListAdapter(Context context, ArrayList<CallListData> callArrayList) {
        this.context = context;
        this.callArrayList = callArrayList;
        isOpened = false;
    }


    @NonNull
    @Override
    public CallListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.call_list_items, parent, false);
        return new CallListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CallListViewHolder holder, int position) {
        CallListData model = callArrayList.get(position);
        String userId = model.getUserId();
        callId = model.getCallId();
        myId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        holder._callListTimeItem.setText(model.getDate()+"  "+model.getTime());

        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Userdata userdata = snapshot.getValue(Userdata.class);
                holder._callListUsernameItem.setText(userdata.getUsername());
                 if (userdata.getProfilePic().length()>1) {
                    Picasso.get().load(userdata.getProfilePic()).placeholder(R.drawable.placeholder).into(holder._callListProfileItem);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if (model.getType().equals("AC")){
            holder._callListTypeItem.setImageResource(R.drawable.call_list_videocam_24);
        }
        if (model.getType().equals("VC")){
            holder._callListTypeItem.setImageResource(R.drawable.call_list_videocam_24);
        }
        isOpened = false;
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!isOpened) {
                    isOpened = true;
                    holder._userCallLayout.animate().translationXBy(-100).setDuration(500);
                    new CountDownTimer(500, 500) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            holder._callDeleteItem.setVisibility(View.VISIBLE);
                        }
                    }.start();
                    new CountDownTimer(3500, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            holder._callDeleteItem.setVisibility(View.GONE);
                            holder._userCallLayout.animate().translationXBy(100).setDuration(500);
                            isOpened = false;

                        }
                    }.start();
                    holder._callDeleteItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                                new CountDownTimer(500, 500) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        holder._callDeleteItem.setVisibility(View.GONE);
                                        holder._userCallLayout.animate().translationXBy(1000).setDuration(500);
                                    }

                                    @Override
                                    public void onFinish() {
                                        FirebaseDatabase.getInstance().getReference().child("CallList").child(myId)
                                                .child(model.getCallId()).removeValue();
                                    }
                                }.start();
                            isOpened = false;
                        }
                    });
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return callArrayList.size();
    }

    public class CallListViewHolder extends RecyclerView.ViewHolder{
        private ImageView _callListProfileItem, _callListTypeItem, _callDeleteItem;
        private TextView _callListUsernameItem,_callListTimeItem;
        private LinearLayout _userCallLayout;
        public CallListViewHolder(@NonNull View itemView) {
            super(itemView);
            _callListProfileItem = (ImageView) itemView.findViewById(R.id._callListProfileItem);
            _callListTypeItem = (ImageView) itemView.findViewById(R.id._callListTypeItem);
            _callListUsernameItem = (TextView) itemView.findViewById(R.id._callListUsernameItem);
            _callListTimeItem = (TextView) itemView.findViewById(R.id._callListTimeItem);
            _callDeleteItem = (ImageView) itemView.findViewById(R.id._callDeleteItem);
            _userCallLayout = (LinearLayout) itemView.findViewById(R.id._userCallLayout);
        }
    }
}
