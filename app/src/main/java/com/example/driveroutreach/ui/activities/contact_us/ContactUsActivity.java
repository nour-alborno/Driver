package com.example.driveroutreach.ui.activities.contact_us;

import android.os.Bundle;
import android.view.View;

import com.example.driveroutreach.databinding.ActivityContactUsBinding;
import com.example.driveroutreach.model.ContactUs;
import com.example.driveroutreach.ui.app_utility.AppUtility;
import com.example.driveroutreach.ui.base_classes.BaseActivity;


public class ContactUsActivity extends BaseActivity implements ContactUsView{

    ActivityContactUsBinding binding;
  //  SharedPreferences sp;
   // public final String DRIVER_ID_KEY = "driverId";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityContactUsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sp = getSharedPreferences("sp", MODE_PRIVATE);
        String driverId= sp.getString(DRIVER_ID_KEY,null);

        ContactUsPresenter contactUsPresenter = new ContactUsPresenter(this);

        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContactUs contactUs = new ContactUs();
                contactUs.setDate(AppUtility.getDate());
                contactUs.setDriverId(driverId);
                
                if (binding.etMessage.getText().toString().isEmpty()){
                    binding.etMessage.setError("Fill The Message");
                } else if (binding.etSubject.getText().toString().isEmpty()){
                    binding.etSubject.setError("What's the subject?");
                } else {
                    contactUs.setMessage(binding.etMessage.getText().toString());
                    contactUs.setTitle(binding.etSubject.getText().toString());
                    contactUsPresenter.sendingMessage(contactUs);


                }
             

            }
        });



    }


    @Override
    public void onSendingMessageSuccess() {
        AppUtility.showSnackbar(binding.getRoot(),"Message");


    }

    @Override
    public void onSendingMessageFailure(Exception e) {
        AppUtility.showSnackbar(binding.getRoot(),e.getMessage());

    }
}