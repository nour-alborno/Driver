package com.example.driveroutreach.ui.fragments.schedule;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.driveroutreach.R;
import com.example.driveroutreach.adapters.DayVBAdapter;
import com.example.driveroutreach.databinding.FragmentScheduleBinding;
import com.example.driveroutreach.ui.base_classes.BaseFragment;
import com.example.driveroutreach.ui.fragments.schedule.days.DayFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class ScheduleFragment extends BaseFragment {

    FragmentScheduleBinding binding;


    public ScheduleFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         binding = FragmentScheduleBinding.inflate(inflater,container,false);




        ArrayList<String> days_tabs = new ArrayList<>();
        days_tabs.add(getString(R.string.tab_saturday));
        days_tabs.add(getString(R.string.tab_sunday));
        days_tabs.add(getString(R.string.tab_monday));
        days_tabs.add(getString(R.string.tab_tuesday));
        days_tabs.add(getString(R.string.tab_wednesday));
        days_tabs.add(getString(R.string.tab_thursday));


        ArrayList<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < days_tabs.size(); i++) {
            Log.d("array",days_tabs.get(i));
            fragments.add(DayFragment.newInstance(days_tabs.get(i))) ;
        }

        DayVBAdapter dayVBAdapter = new DayVBAdapter(getActivity() ,fragments);

        binding.VB2.setAdapter(dayVBAdapter);

        new TabLayoutMediator(binding.TL, binding.VB2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(days_tabs.get(position));
            }
        }).attach();



        return binding.getRoot();
    }

}