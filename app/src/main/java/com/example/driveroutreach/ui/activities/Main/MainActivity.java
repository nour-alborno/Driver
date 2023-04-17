package com.example.driveroutreach.ui.activities.Main;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.driveroutreach.R;
import com.example.driveroutreach.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements MainView {
    ActivityMainBinding binding ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MainPresenter mainPresenter = new MainPresenter(this);

           mainPresenter.AddingFrag(new ScheduleFragment());


        binding.bottomNavigationMain.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

               mainPresenter.SelectingNavIcon(item);

                return true;
            }
        });




    }

    @Override
    public void onSetFragment(Fragment fragment) {
        //         هنا مجرد ما يفتح التطبيق حيكون الديفولت ع الفراقمنت يلي تحت ، ف حطيت تاعي مجرد ما يجهز تاعك الهوم ي نور
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new ScheduleFragment()).commit();
        //        وهان بدي اياه يحدد ع الأيقون يلي تحت تاعت السيكيدجول
        binding.bottomNavigationMain.setSelectedItemId(R.id.page_schedule);
    }

    @Override
    public void onSelectedNavIcon(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
    }


}