package com.devtwist.serviceshub.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devtwist.serviceshub.Activities.ChatActivity;
import com.devtwist.serviceshub.Activities.MyContractsActivity;
import com.devtwist.serviceshub.Activities.PostActivity;
import com.devtwist.serviceshub.Models.NotificationData;
import com.devtwist.serviceshub.Models.Userdata;
import com.devtwist.serviceshub.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>{
    private Context context;
    private ArrayList<NotificationData> notificationList;
    private String myId, notificationType, senderId, username, userProfileUrl, userToken;

    public NotificationAdapter(Context context, ArrayList<NotificationData> notificationList, String myId) {
        this.context = context;
        this.notificationList = notificationList;
        this.myId = myId;
    }


    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_items, parent,false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationData model = notificationList.get(position);
        notificationType = model.getNotificationType();

        holder._notificationTitleItem.setText(model.getNotificationTitle());
        holder._notificationMessageItem.setText(model.getNotificationMessage());

        if (notificationType.equals("post") || notificationType.equals("contract") || notificationType.equals("message")) {

            senderId = model.getSenderId();
            FirebaseDatabase.getInstance().getReference().child("Users")
                    .child(senderId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Userdata userdata = snapshot.getValue(Userdata.class);
                    userProfileUrl = userdata.getProfilePic();
                    username = userdata.getUsername();
                    userToken = userdata.getToken();
                    String notificationId = model.getNotificationId();
                    if (model.getSeen()){
                        holder.itemView.setAlpha(0.5f);
                    }

                    Picasso.get().load(userProfileUrl).placeholder(R.drawable.placeholder).into(holder._notificationProfileItem);

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.itemView.setAlpha(0.7f);
                            FirebaseDatabase.getInstance().getReference().child("Notifications").child(myId)
                                    .child(notificationId).child("seen").setValue(true);
                            if (model.getNotificationType().equals("message")) {
                                Intent intent = new Intent(v.getContext(), ChatActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("myId", myId);
                                bundle.putString("userId", userdata.getuId());
                                bundle.putString("username", userdata.getUsername());
                                bundle.putString("profileUri", userdata.getProfilePic());
                                bundle.putString("token", userdata.getToken());
                                intent.putExtras(bundle);
                                v.getContext().startActivity(intent);

                                Toast.makeText(v.getContext(), "Clicked on:" + model.getNotificationType(), Toast.LENGTH_SHORT).show();
                            }
                            if (model.getNotificationType().equals("post")){
                                Intent intent = new Intent(v.getContext(), PostActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("userId", myId);
                                bundle.putString("userCity", userdata.getCity());
                                intent.putExtras(bundle);
                                v.getContext().startActivity(intent);
                            }
                            if (model.getNotificationType().equals("contract")){
                                Intent intent = new Intent(v.getContext(), MyContractsActivity.class);
                                intent.putExtra("myId", myId);
                                v.getContext().startActivity(intent);
                            }
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else {
            holder._notificationProfileItem.setImageResource(R.drawable.appicon);
        }




    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder{

        private ImageView _notificationProfileItem;
        private TextView _notificationTitleItem, _notificationMessageItem;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            _notificationProfileItem = (ImageView) itemView.findViewById(R.id._notificationProfileItem);
            _notificationTitleItem = (TextView) itemView.findViewById(R.id._notificationTitleItem);
            _notificationMessageItem = (TextView) itemView.findViewById(R.id._notificationMessageItem);
        }
    }
}
