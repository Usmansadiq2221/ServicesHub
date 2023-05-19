package com.devtwist.serviceshub.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.devtwist.serviceshub.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.squareup.picasso.Picasso;

public class ViewImage extends AppCompatActivity {

    private ImageView _viewChatImage;
    private Intent intent;
    private Bundle bundle;
    private String imageUrl;
    private TextView _textView;
    private WebView _viewWebView;
    private ScrollView _textScrollView;
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        setUpAds();
        runAds();

        _viewChatImage = findViewById(R.id._viewChatImage);
        _textView = findViewById(R.id._textView);
        _viewWebView = findViewById(R.id._viewWebView);
        _textScrollView = findViewById(R.id._textScrollView);


        intent = getIntent();
        bundle = intent.getExtras();


        if (bundle.getString("tag").equals("i")) {
            imageUrl = bundle.getString("imgUrl");
            Picasso.get().load(imageUrl).into(_viewChatImage);
            _viewChatImage.setVisibility(ImageView.VISIBLE);
        }

        else if (bundle.getString("tag").equals("t")){
            _textView.setText(bundle.getString("text"));
            _textScrollView.setVisibility(View.VISIBLE);
        }

        else if (bundle.getString("tag").equals("c")) {
            _viewWebView.getSettings().setJavaScriptEnabled(true);
            _viewWebView.loadUrl("https://doc-hosting.flycricket.io/services-hub/3df6b771-cc2d-44d7-a5be-f8715ff61279/terms");
            _viewWebView.setVisibility(View.VISIBLE);
        }

        else if (bundle.getString("tag").equals("p")) {
            _viewWebView.getSettings().setJavaScriptEnabled(true);
            _viewWebView.loadUrl("https://doc-hosting.flycricket.io/services-hub/11cad289-cb89-4488-a5c5-417110ca7b84/privacy");
            _viewWebView.setVisibility(View.VISIBLE);
        }

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
                    mInterstitialAd.show(ViewImage.this);
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