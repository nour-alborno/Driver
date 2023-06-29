package com.example.driveroutreach.ui.fragments.schedule.days;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.driveroutreach.model.JourneyModel;
import com.example.driveroutreach.ui.base_classes.BasePresenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DayPresenter extends BasePresenter {

    DayView view;
    ArrayList<JourneyModel> Trips;
    public DayPresenter(DayView view) {
        super();
        this.view=view;
        Trips = new ArrayList<>();
    }

    void gettingSchedule(String day, String driverId){
        firestore.collection("Journey").whereEqualTo("driver",driverId)
                .whereEqualTo("day",day).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            Log.d("schedule",task.getResult().toString());

                            for(QueryDocumentSnapshot document : task.getResult()){

                                JourneyModel journeyModel = document.toObject(JourneyModel.class);
                                Log.d("schedule",journeyModel.getOrganization()+" "+journeyModel.getDay());

                              Trips.add(journeyModel);
                            }

                              view.onGettingScheduleSuccess(Trips);


                        }   else {
                            view.onGettingScheduleFailure(task.getException());

                        }
                    }
                });
    }
}
