package com.example.menu.A03_TabMenu;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.menu.A011_Notification.Notification;
import com.example.menu.R;
import com.example.menu.classes.Tracking;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

//import android.support.v4.app.Fragment;

public class A02_MapTab extends Fragment implements OnMapReadyCallback {

    //  加入image button
    private ImageButton noti;
    private static GoogleMap mMap;
    static Marker Cmarker;
    DatabaseReference locations;
    public static boolean first_ZoomIn = true;


    private static Location mLastLocation;
    public boolean createView;
    private static FragmentActivity mInstance;
    HashMap<String,Marker> hashMapMarker = new HashMap<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mInstance = getActivity();
        //first_ZoomIn = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        createView = false;
        Log.d("Map","Des");
        first_ZoomIn = true;
        displayLocation();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.a11_map, container, false);

        createView = true;
//      noti的連結
        ImageButton noti = (ImageButton) view.findViewById(R.id.map_btn);
        noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), Notification.class);
                startActivity(i);

            }
        });
        locations = FirebaseDatabase.getInstance().getReference("Locations");

        SupportMapFragment mapFragment = ((SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map));
        mapFragment.getMapAsync(this);
        Location currentUser =new Location("");
        locations.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (!createView || snapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    return;
                }else{
                    Marker marker = hashMapMarker.get(snapshot.getKey());
                    if (marker!=null){
                        marker.remove();
                    }
                    hashMapMarker.remove(snapshot.getKey());
                    Location Friendlocation = new Location("");
                    Tracking t = snapshot.getValue(Tracking.class);
                    if (t.getTrackable()==null|| !t.getTrackable()){
                        return;
                    }
                    LatLng FriendlatLng = new LatLng(t.getLat(),t.getLong());
                    Friendlocation.setLatitude(t.getLat());
                    Friendlocation.setLongitude(t.getLong());
                    Marker newmarker = mMap.addMarker(new MarkerOptions().position(FriendlatLng).title(t.getEmail()+
                            t.getLat()+t.getLong()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                    hashMapMarker.put(snapshot.getKey(),newmarker);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (!createView || snapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    return;
                }else{
                    Marker marker = hashMapMarker.get(snapshot.getKey());
                    if (marker!=null){
                        marker.remove();
                    }
                    hashMapMarker.remove(snapshot.getKey());
                    Location Friendlocation = new Location("");
                    Tracking t = snapshot.getValue(Tracking.class);
                    if (!t.getTrackable()){
                        return;
                    }
                    LatLng FriendlatLng = new LatLng(t.getLat(),t.getLong());
                    Friendlocation.setLatitude(t.getLat());
                    Friendlocation.setLongitude(t.getLong());
                    Marker newmarker = mMap.addMarker(new MarkerOptions().position(FriendlatLng).title(t.getEmail()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                    hashMapMarker.put(snapshot.getKey(),newmarker);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        locations.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!createView){
                    return;
                }
                displayLocation();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }

    public static void displayLocation() {

        if ( ActivityCompat.checkSelfPermission(mInstance, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mInstance, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED )
        {
            return;
        }
        LocationServices.getFusedLocationProviderClient(mInstance).getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()){
                    mLastLocation = task.getResult();

                    if (mLastLocation != null){

                        //currentUser.setLatitude(mLastLocation.getLatitude());currentUser.setLongitude(mLastLocation.getLongitude());
                        LatLng myLatLng = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
                        if (Cmarker != null) {
                            Cmarker.remove();
                        }
                        Cmarker = mMap.addMarker(new MarkerOptions().position(myLatLng).title("Me"));
                        if (first_ZoomIn){
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLng,18.0f));
                            first_ZoomIn = false;
                        }


                        //mMap.addMarker(new MarkerOptions().position(myLatLng).title("Me"));
                    }else {
                        //Toast.makeText(getContext(),"I just can't.",Toast.LENGTH_LONG).show();
                    }
                }else{
                   // Toast.makeText(getContext(),"I just cannot.",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    //    **
//            * Manipulates the map once available.
//     * This callback is triggered when the map is ready to be used.
//            * This is where we can add markers or lines, add listeners or move the camera. In this case,
//            * we just add a marker near Sydney, Australia.
//     * If Google Play services is not installed on the device, the user will be prompted to install
//     * it inside the SupportMapFragment. This method will only be triggered once the user has
//     * installed Google Play services and returned to the app.
//     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }



}
