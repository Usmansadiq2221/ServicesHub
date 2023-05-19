package com.devtwist.serviceshub.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.devtwist.serviceshub.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class SearchServicesActivity extends AppCompatActivity {

    private ImageView _servicesBackButton;

    private GridLayout _hSservicesContainer, _mSservicesContainer, _cSservicesContainer, _dSservicesContainer;

    private ImageView _acService, _autoRepairService, _babysitterService, _bikeRepairService, _carWashService, _caretakerService,
            _electricianService, _homeMaidService, _homeTutorService, _laundryServices, _lawyerService, _plumberService,
            _doctorService, _nurseService,
            _graphicDesignService, _androidDevServices, _webDevServices,
            _architectureEngServices, _carpenterService, _concreter, _labourServices, _painterService;

    private TextView textView4;

    private Intent intent;
    private Bundle bundle;
    private String serviceName, myId, serviceType;
    private AdView _sServicesAdview;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_services);

        _hSservicesContainer = findViewById(R.id._hSservicesContainer);
        _mSservicesContainer = findViewById(R.id._mServicesContainer);
        _dSservicesContainer = findViewById(R.id._dServicesContainer);
        _cSservicesContainer = findViewById(R.id._cServicesContainer);
        _servicesBackButton = findViewById(R.id._servicesBackButton);

        _acService = findViewById(R.id._acService);
        _autoRepairService = findViewById(R.id._autoRepairService);
        _babysitterService = findViewById(R.id._babysitterService);
        _bikeRepairService = findViewById(R.id._bikeRepairService);
        _carWashService = findViewById(R.id._carWashService);
        _caretakerService = findViewById(R.id._caretakerService);
        _electricianService = findViewById(R.id._electricianService);
        _homeMaidService = findViewById(R.id._homeMaidService);
        _homeTutorService = findViewById(R.id._homeTutorService);
        _laundryServices = findViewById(R.id._laundryServices);
        _lawyerService = findViewById(R.id._lawyerService);
        _plumberService = findViewById(R.id._plumberService);

        _doctorService = findViewById(R.id._doctorService);
        _nurseService = findViewById(R.id._nurseService);

        _graphicDesignService = findViewById(R.id._graphicDesignService);
        _androidDevServices = findViewById(R.id._androidDevServices);
        _webDevServices = findViewById(R.id._webDevServices);

        _architectureEngServices = findViewById(R.id._architectureEngServices);
        _carpenterService = findViewById(R.id._carpenterService);
        _concreter = findViewById(R.id._concreter);
        _labourServices = findViewById(R.id._labourServices);
        _painterService = findViewById(R.id._painterService);

        textView4 =findViewById(R.id.textView4);
        _sServicesAdview = findViewById(R.id._sServicesAdView);

        AdRequest adRequest = new AdRequest.Builder().build();
        _sServicesAdview.loadAd(adRequest);

        intent = getIntent();
        bundle = intent.getExtras();
        serviceType = bundle.getString("serviceType");

        try {

            if (serviceType.equals("h")) {

                textView4.setText("Home Services");

                _hSservicesContainer.setVisibility(View.VISIBLE);
                _cSservicesContainer.setVisibility(View.GONE);
                _dSservicesContainer.setVisibility(View.GONE);
                _mSservicesContainer.setVisibility(View.GONE);

                _acService.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        accessService(_acService);
                    }
                });
                _autoRepairService.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        accessService(_autoRepairService);
                    }
                });
                _babysitterService.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        accessService(_babysitterService);
                    }
                });
                _bikeRepairService.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        accessService(_bikeRepairService);
                    }
                });
                _carWashService.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        accessService(_carWashService);
                    }
                });
                _caretakerService.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        accessService(_caretakerService);
                    }
                });
                _electricianService.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        accessService(_electricianService);
                    }
                });
                _homeMaidService.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        accessService(_homeMaidService);
                    }
                });
                _homeTutorService.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        accessService(_homeTutorService);
                    }
                });
                _laundryServices.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        accessService(_laundryServices);
                    }
                });
                _lawyerService.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        accessService(_lawyerService);
                    }
                });
                _plumberService.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        accessService(_plumberService);
                    }
                });

            } else if (serviceType.equals("c")) {

                textView4.setText("Construction Services");

                _cSservicesContainer.setVisibility(View.VISIBLE);
                _hSservicesContainer.setVisibility(View.GONE);
                _dSservicesContainer.setVisibility(View.GONE);
                _mSservicesContainer.setVisibility(View.GONE);

                _architectureEngServices.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        accessService(_architectureEngServices);
                    }
                });
                _carpenterService.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        accessService(_carpenterService);
                    }
                });
                _concreter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        accessService(_concreter);
                    }
                });
                _labourServices.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        accessService(_labourServices);
                    }
                });
                _painterService.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        accessService(_painterService);
                    }
                });

            } else if (serviceType.equals("m")) {

                textView4.setText("Medical Services");

                _mSservicesContainer.setVisibility(View.VISIBLE);
                _cSservicesContainer.setVisibility(View.GONE);
                _dSservicesContainer.setVisibility(View.GONE);
                _hSservicesContainer.setVisibility(View.GONE);

                _doctorService.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        accessService(_doctorService);
                    }
                });
                _nurseService.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        accessService(_nurseService);
                    }
                });

            } else if (serviceType.equals("d")) {

                textView4.setText("Dev & Design Services");

                _dSservicesContainer.setVisibility(View.VISIBLE);
                _cSservicesContainer.setVisibility(View.GONE);
                _hSservicesContainer.setVisibility(View.GONE);
                _mSservicesContainer.setVisibility(View.GONE);

                _graphicDesignService.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        accessService(_graphicDesignService);
                    }
                });
                _androidDevServices.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        accessService(_androidDevServices);
                    }
                });
                _webDevServices.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        accessService(_webDevServices);
                    }
                });

            }

        } catch (Exception e) {
            Log.i("Cond Error",e.getMessage().toString());
        }


        _servicesBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void accessService(ImageView service) {
        serviceName = service.getTag().toString();
        intent = new Intent(SearchServicesActivity.this, ServiceProviderList.class);
        bundle = new Bundle();
        bundle.putString("serviceName", serviceName);
        bundle.putString("myId", myId);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /*private void selectType(GridLayout services, int totalServices) {
        for (int j = 0; j < 16; j++) {
            ImageView imageView = (ImageView) services.getChildAt(j);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent = getIntent();
                    bundle = intent.getExtras();
                    myId = bundle.getString("userId");
                    serviceName = imageView.getTag().toString();
                    intent = new Intent(SearchServicesActivity.this, ServiceProviderList.class);
                    bundle = new Bundle();
                    bundle.putString("serviceType", serviceName);
                    bundle.putString("myId", myId);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }
        }

     */

}