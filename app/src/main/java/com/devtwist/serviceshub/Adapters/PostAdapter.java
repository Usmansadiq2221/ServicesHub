package com.devtwist.serviceshub.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devtwist.serviceshub.Activities.ChatActivity;
import com.devtwist.serviceshub.Activities.ViewImage;
import com.devtwist.serviceshub.Models.PostData;
import com.devtwist.serviceshub.Models.Userdata;
import com.devtwist.serviceshub.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private String postUserIdString, myId, postUsername, profileUri, token;
    private DatabaseReference postRefrence;
    private Context context;
    private ArrayList<PostData> postList;

    public PostAdapter(Context context, ArrayList<PostData> postList, String myId) {
        this.context = context;
        this.postList = postList;
        this.myId = myId;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_items, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        try {
            Log.i("PostAdapter","successfull");
            PostData model = postList.get(position);
            postUserIdString = model.getUserId();
            holder._postServiceText.setText(model.getServiceName());
            holder._postBudgetText.setText(model.getServiceBudget());
            holder._postTimeText.setText(model.getServiceTime());
            holder._postWorkplaceText.setText(model.getServiceWorkplace());

            holder._postWorkplaceText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("tag","t");
                    bundle.putString("text", model.getServiceWorkplace());
                    Intent intent = new Intent(view.getContext(), ViewImage.class);
                    intent.putExtras(bundle);
                    view.getContext().startActivity(intent);
                }
            });

            postRefrence = FirebaseDatabase.getInstance().getReference().child("Users").child(postUserIdString);
            postRefrence.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Userdata userdata = snapshot.getValue(Userdata.class);
                    profileUri = userdata.getProfilePic();
                    postUsername = userdata.getUsername();
                    token = userdata.getToken();
                    Picasso.get().load(profileUri).placeholder(R.drawable.placeholder).into(holder._postUserProfile);
                    holder._postUsernameText.setText(postUsername);
                    holder._postUserCityText.setText(userdata.getCity());

                    if(myId.equals(userdata.getuId())){
                        holder._applyPostButton.setVisibility(Button.GONE);
                    }

                    holder._applyPostButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Intent intent = new Intent(v.getContext(), ChatActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("userId", userdata.getuId());
                                bundle.putString("profileUri", userdata.getProfilePic());
                                bundle.putString("username", userdata.getUsername());
                                bundle.putString("myId", myId);
                                bundle.putString("token", userdata.getToken());
                                intent.putExtras(bundle);
                                v.getContext().startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.i("AdapterError", e.getMessage().toString());
                            }
                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder{
        private ImageView _postUserProfile;
        private TextView _postUsernameText, _postUserCityText, _postServiceText, _postBudgetText, _postTimeText, _postWorkplaceText;
        private Button _applyPostButton;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            try {
                _postUserProfile = (ImageView) itemView.findViewById(R.id._postUserProfile);
                _postUsernameText = (TextView) itemView.findViewById(R.id._postUsernameText);
                _postUserCityText = (TextView) itemView.findViewById(R.id._postUserCityText);
                _postServiceText = (TextView) itemView.findViewById(R.id._postServiceText);
                _postBudgetText = (TextView) itemView.findViewById(R.id._postBudgetText);
                _postTimeText = (TextView) itemView.findViewById(R.id._postTimeText);
                _postWorkplaceText = (TextView) itemView.findViewById(R.id._postWorkplaceText);
                _applyPostButton = (Button) itemView.findViewById(R.id._applyPostButton);
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("PostViewHolder Error", e.getMessage().toString());
            }

        }
    }
}
