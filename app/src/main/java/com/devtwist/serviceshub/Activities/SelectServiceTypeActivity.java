package com.devtwist.serviceshub.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.devtwist.serviceshub.R;

public class SelectServiceTypeActivity extends AppCompatActivity {

    private ImageView _servicesBackButton, imageView;
    private ImageView _homeServices, _desAndDevService, _medicalServices, _constructionServices;
    private Intent intent;
    private Bundle bundle;
    private GridLayout _serviceType;
    private String serviceType, myId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_service_type);

        _serviceType = findViewById(R.id._serviceType);
        _servicesBackButton = findViewById(R.id._servicesBackButton);

        _homeServices = findViewById(R.id._homeServices);
        _desAndDevService = findViewById(R.id._desAndDevService);
        _medicalServices = findViewById(R.id._medicalServices);
        _constructionServices = findViewById(R.id._constructionServices);

        //selectType(_serviceType);

        _homeServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectService(_homeServices);
            }
        });

        _desAndDevService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectService(_desAndDevService);
            }
        });

        _medicalServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectService(_medicalServices);
            }
        });

        _constructionServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectService(_constructionServices);
            }
        });

        _servicesBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void selectService(ImageView service) {
        serviceType = service.getTag().toString();
        intent = new Intent(SelectServiceTypeActivity.this, SearchServicesActivity.class);
        bundle = new Bundle();
        bundle.putString("serviceType", serviceType);
        intent.putExtras(bundle);
        startActivity(intent);;
    }

    /*private void selectType(GridLayout services) {
        for (int j = 0; j < 4; j++) {
            imageView = (ImageView) services.getChildAt(j);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        serviceType = imageView.getTag().toString();
                        intent = new Intent(SelectServiceTypeActivity.this, SearchServicesActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        Log.i("SST loop Error", e.getMessage().toString());
                    }
                }
            });
        }
    }
     */


}