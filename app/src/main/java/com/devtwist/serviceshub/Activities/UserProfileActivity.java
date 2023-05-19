package com.devtwist.serviceshub.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.devtwist.serviceshub.Models.Userdata;
import com.devtwist.serviceshub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UserProfileActivity extends AppCompatActivity {

    private TextView _editProfileText, _nameText, _professionText, _rateText, _ratingText, _earnedText, _servicesTextview, _postTextview, _userProfileError;
    private ImageView _userProfilePic, _contractIcon, _notificationIcon, _messageIcon, _feedbackIcon, _option;
    private Intent intent;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userReference;
    private String imagestring;
    private String uId, userId, userCity;
    private Bundle extras;
    private ConstraintLayout _userProfileLayout;
    private ProgressBar _userprofileProgress;
    private LinearLayout _retryLayoutUP, _mMenuLayout;
    private ImageView _innerLogoUP, _outerLogoUP;
    private CountDownTimer forwardAnimation, outerLogoIncrease, innerLogoDecrease, outerLogoDecrease,innerLogoIncrease;
    private TextView _mPrivacyText, _mTermsText;
    private boolean isMenuVisible;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        _editProfileText = findViewById(R.id._editProfileText);
        _userProfilePic = findViewById(R.id._userProfilePic);
        _nameText = findViewById(R.id._nameText);
        _professionText = findViewById(R.id._professionText);
        _rateText = findViewById(R.id._rateDetail);
        _ratingText = findViewById(R.id._ratingDetail);
        _earnedText = findViewById(R.id._earnedDetail);
        _servicesTextview = findViewById(R.id._servicesTextview);
        _postTextview = findViewById(R.id._postTextview);
        _contractIcon = findViewById(R.id._contractIcon);
        _notificationIcon = findViewById(R.id._notificationIcon);
        _feedbackIcon = findViewById(R.id._blockListIcon);
        _messageIcon = findViewById(R.id._messageIcon);
        _userProfileLayout = findViewById(R.id._userProfileLayout);
        _userProfileError = findViewById(R.id._userProfileError);
        _userprofileProgress = findViewById(R.id._userprofileProgress);
        _retryLayoutUP = findViewById(R.id._retryLayoutUP);
        _innerLogoUP = findViewById(R.id._innerLogoUP);
        _outerLogoUP = findViewById(R.id._outerLogoUP);
        _mPrivacyText = findViewById(R.id._mPrivacyText);
        _mTermsText = findViewById(R.id._mTermsText);
        _option = findViewById(R.id._option);
        _mMenuLayout = findViewById(R.id._mMenuLayout);
        isMenuVisible = false;

        startAnimation();

        extras = new Bundle();

        firebaseDatabase = FirebaseDatabase.getInstance();
        userReference = firebaseDatabase.getReference();
        uId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        /*ProgressDialog dialog = new ProgressDialog(UserProfileActivity.this);
        dialog.setIcon(R.drawable.appicon);
        dialog.setTitle("Easy Service");
        dialog.setMessage("Please Wait...");
        dialog.setCancelable(false);
        dialog.show();*/

        //for geting user data from firebase...
        setUserProfile();

        _userProfileError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _retryLayoutUP.setVisibility(View.GONE);
                _userprofileProgress.setVisibility(View.VISIBLE);
                setUserProfile();
            }
        });


        _editProfileText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(UserProfileActivity.this, EditProfileActivity.class);
                intent.putExtra("myId",userId);
                startActivity(intent);
            }
        });

        _servicesTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(UserProfileActivity.this,SelectServiceTypeActivity.class);
                extras.putString("userId", userId);
                extras.putString("userCity", userCity);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        _postTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(UserProfileActivity.this, PostActivity.class);
                extras.putString("userId", userId);
                extras.putString("userCity", userCity);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        _contractIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(UserProfileActivity.this,MyContractsActivity.class);
                //intent.putExtra("myId", userId);
                startActivity(intent);
            }
        });

        _notificationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(UserProfileActivity.this,NotificationActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        _feedbackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(UserProfileActivity.this, FeedbackActivity.class);
                intent.putExtra("myId", userId);
                startActivity(intent);
            }
        });

        _messageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(UserProfileActivity.this,MessengerActivity.class);
                intent.putExtra("myId", userId);
                startActivity(intent);
            }
        });

        /*new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                dialog.dismiss();
            }
        }.start();*/

        _option.setOnClickListener(new View.OnClickListener() {
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

        _mPrivacyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("tag", "p");
                Intent intent = new Intent(UserProfileActivity.this, ViewImage.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        _mTermsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("tag", "c");
                Intent intent = new Intent(UserProfileActivity.this, ViewImage.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    private void startAnimation() {
        _innerLogoUP.setScaleX(0f);
        _innerLogoUP.setScaleY(0f);

        forwardAnimation = new CountDownTimer(1500,1500) {
            @Override
            public void onTick(long millisUntilFinished) {
                _outerLogoUP.animate().scaleX(0.85f).scaleY(0.85f).setDuration(1500);
                _innerLogoUP.animate().scaleX(1f).scaleY(1f).setDuration(1500);
            }

            @Override
            public void onFinish() {
                outerLogoIncrease.start();
            }
        };

        outerLogoIncrease = new CountDownTimer(1500,1500) {
            @Override
            public void onTick(long millisUntilFinished) {
                _outerLogoUP.animate().scaleX(1.15f).scaleY(1.15f).setDuration(1500);
            }

            @Override
            public void onFinish() {
                outerLogoDecrease.start();
            }
        };

        outerLogoDecrease = new CountDownTimer(1500,1500) {
            @Override
            public void onTick(long millisUntilFinished) {
                _outerLogoUP.animate().scaleX(0.85f).scaleY(0.85f).setDuration(1500);
            }

            @Override
            public void onFinish() {
                innerLogoDecrease.start();
            }
        };

        innerLogoDecrease = new CountDownTimer(1500,1500) {
            @Override
            public void onTick(long millisUntilFinished) {
                _innerLogoUP.animate().scaleX(0.75f).scaleY(0.75f).setDuration(1500);
            }

            @Override
            public void onFinish() {
                innerLogoIncrease.start();
            }
        };

        innerLogoIncrease = new CountDownTimer(1500,1500) {
            @Override
            public void onTick(long millisUntilFinished) {
                _innerLogoUP.animate().scaleX(1f).scaleY(1f).setDuration(1500);
            }

            @Override
            public void onFinish() {
                outerLogoIncrease.start();
            }
        };

        forwardAnimation.start();
    }

    private void setUserProfile() {
        userReference.child("Users").child(uId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Userdata userdata = snapshot.getValue(Userdata.class);
                imagestring = userdata.getProfilePic();
                Picasso.get().load(imagestring).placeholder(R.drawable.placeholder).into(_userProfilePic);
                _nameText.setText(userdata.getUsername());
                _professionText.setText(userdata.getProfession());
                _ratingText.setText(userdata.getRating());
                _rateText.setText(userdata.getRatePerDay()+ "/Day");
                _earnedText.setText(userdata.getEarned());
                userId = userdata.getuId();
                userCity = userdata.getCity();
                _userprofileProgress.setVisibility(View.GONE);
                _userProfileLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                _userProfileError.setVisibility(View.GONE);
                _retryLayoutUP.setVisibility(View.VISIBLE);
                Toast.makeText(UserProfileActivity.this, "Network Error\nPlease check your internet\nconnection", Toast.LENGTH_LONG).show();
                Log.i("UserProfileEror",error.getMessage().toString());
            }
        });
    }
}