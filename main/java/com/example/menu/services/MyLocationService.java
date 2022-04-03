package com.example.menu.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.widget.Toast;

import com.google.android.gms.location.LocationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyLocationService extends BroadcastReceiver {
    public static final String ACTION_PROCESS_UPDATE = "com.example.kiss123.UPDATE_ACTION";
    DatabaseReference databaseReference;
    public MyLocationService() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Locations");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent!=null){
            final String action = intent.getAction();
            if (action.equals(ACTION_PROCESS_UPDATE)){
                LocationResult result = LocationResult.extractResult(intent);
                if (result!=null){
                    Location location = result.getLastLocation();
                    try {
                        DatabaseReference locations = FirebaseDatabase.getInstance().getReference().child("Locations");
                        locations.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("email").setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                        locations.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("lat").setValue(String.valueOf(location.getLatitude()));
                        locations.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("long").setValue(String.valueOf(location.getLongitude()));
//                        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(new Tracking(
//                                FirebaseAuth.getInstance().getCurrentUser().getEmail(),
//                                String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude())
//                        ));

                    }catch (Exception e){
                        Toast.makeText(context,String.valueOf(location.getLatitude())+String.valueOf(location.getLongitude()),Toast.LENGTH_LONG).show();
                    }
                }
            }
        }

    }


}
