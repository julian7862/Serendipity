package com.example.menu.A03_TabMenu;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import com.example.menu.A01_SigninScreen.SigninScreen;
import com.example.menu.R;
import com.example.menu.classes.SendLocationActivitiy;
import com.example.menu.classes.Tracking;
import com.example.menu.services.MyBackgroundService;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TabMenu extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{
    private FirebaseAuth mAuth;
    private Toolbar toolbar;
    TabLayout tabLayout;
    public static ViewPager viewPager;
    AccessToken accessToken;
    Boolean usr_noti;

    MyBackgroundService myBackgroundService = null;
    boolean mBound =false;

    LocationManager lm;
    private Location mLastLocation;

    private static final String CHANNEL_ID = "my_channel";

    private int[] tabIcons = {
//            R.drawable.ic_home_selector,
//            R.drawable.ic_order_selector,
//            R.drawable.ic_favorite_selector,
//            R.drawable.ic_notifications_selector,
//            R.drawable.ic_profile_selector
            R.drawable.ic_search_selector,
            R.drawable.ic_map_selector,
            R.drawable.ic_main_selector,
            R.drawable.ic_favorite_selector,
            R.drawable.ic_profile_selector
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a03_tab_menu);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        accessToken = AccessToken.getCurrentAccessToken();

        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // Insert your code here
                            Log.d("TUSK",object.toString());
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "gender,birthday,location,age_range,email");
        request.setParameters(parameters);
        request.executeAsync();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);


        tabLayout =  findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        lm = (LocationManager) TabMenu.this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!gps_enabled&&!network_enabled){
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            Task<LocationSettingsResponse> result =
                    LocationServices.getSettingsClient(TabMenu.this).checkLocationSettings(builder.build());
            result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
                @Override
                public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                    try {
                        LocationSettingsResponse response = task.getResult(ApiException.class);
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                    } catch (ApiException exception) {
                        switch (exception.getStatusCode()) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                // Location settings are not satisfied. But could be fixed by showing the
                                // user a dialog.
                                try {
                                    // Cast to a resolvable exception.
                                    ResolvableApiException resolvable = (ResolvableApiException) exception;
                                    // Show the dialog by calling startResolutionForResult(),
                                    // and check the result in onActivityResult().
                                    resolvable.startResolutionForResult(
                                            TabMenu.this,
                                            LocationRequest.PRIORITY_HIGH_ACCURACY);
                                } catch (IntentSender.SendIntentException e) {
                                    // Ignore the error.
                                } catch (ClassCastException e) {
                                    // Ignore, should be an impossible error.
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                // Location settings are not satisfied. However, we have no way to fix the
                                // settings so we won't show the dialog.
                                break;
                        }
                    }
                }
            });
        }
        setupDistance();
    }

    private  final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyBackgroundService.LocalBinder binder = (MyBackgroundService.LocalBinder)service;
            myBackgroundService = binder.getService();
            mBound = true;
            myBackgroundService.requestLocationUpdates();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            myBackgroundService = null;
            mBound = false;
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if(requestCode == LocationRequest.PRIORITY_HIGH_ACCURACY){
            switch (resultCode) {
                case Activity.RESULT_OK:
                    // All required changes were successfully made
                    break;
                case Activity.RESULT_CANCELED:
                    // The user was asked to change settings, but chose not to
                    break;
                default:
                    break;
            }
        }
    }




    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(4).setIcon(tabIcons[4]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new A01_SearchTab(), "Search");
        adapter.addFrag(new A02_MapTab(), "Map");
        adapter.addFrag(new A03_MainTab(), "MAin");
        adapter.addFrag(new A04_FavoriteTab(), "Fav");
        adapter.addFrag(new A05_ProfileTab(), "Profile");
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1){
                    A02_MapTab.first_ZoomIn = true;
                    A02_MapTab.displayLocation();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


//        adapter.addFrag(new A03_FavoriteTab(), "Fav");
//        adapter.addFrag(new A04_NotificationTab(), "Notify");
//        adapter.addFrag(new A05_ProfileTab(), "Profile");

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {

            return super.getItemPosition(object);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }
    @Override
    protected void onStart() {
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }

        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            sendToLogin();
        }
        Dexter.withContext(this).withPermissions(Arrays.asList(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
                //, Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                bindService(new Intent(TabMenu.this,MyBackgroundService.class),serviceConnection,Context.BIND_AUTO_CREATE);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

            }
        }).check();
    }
    @Override
    protected void onResume(){
        super.onResume();


    }


    @Override
    protected void onStop() {
        if(mBound){
            unbindService(serviceConnection);
            mBound = false;
        }
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
        EventBus.getDefault().unregister(this);

        super.onStop();
    }

    private void sendToLogin() {
        Intent intent = new Intent(TabMenu.this, SigninScreen.class);
        startActivity(intent);
        finish();
    }
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void  onListenLocation(SendLocationActivitiy event){
        if (event != null){
//            String data = new StringBuilder()
//                    .append(event.getLocation().getLatitude()).append("/**/")
//                    .append(event.getLocation().getLongitude()).toString();
//            Toast.makeText(myBackgroundService,data,Toast.LENGTH_SHORT).show();
            DatabaseReference locations = FirebaseDatabase.getInstance().getReference().child("Locations");
            locations.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("email").setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            locations.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("lat").setValue(event.getLocation().getLatitude());
            locations.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("long").setValue(event.getLocation().getLongitude());
        }
    }

    private void setupDistance(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Locations");

        databaseReference.orderByChild("trackable").equalTo(true).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Tracking tracking= snapshot.getValue(Tracking.class);

                if (FirebaseAuth.getInstance()!=null){
                    return;
                }
                if (tracking.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                    return;
                }

                cauculateDistance(tracking);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

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
    }

    private void cauculateDistance(final Tracking t){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED )
        {
            return;
        }
        LocationServices.getFusedLocationProviderClient(this).getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()){
                    mLastLocation = task.getResult();
                    if (mLastLocation != null &&t.getTrackable()){
                        LatLng start = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
                        LatLng end = new LatLng(t.getLat(),t.getLong());
                        double dis = getDistance(start,end);

                        if (dis <= 500){
                            String notiText = "用戶"+t.getEmail()+"在附近，要約嗎?";
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(TabMenu.this,CHANNEL_ID)
                                    .setContentText(notiText).setContentTitle("Serendipitii").setSmallIcon(R.mipmap.ic_launcher)
                                    .setPriority(Notification.PRIORITY_HIGH).setAutoCancel(true);
                            PendingIntent pendingIntent = PendingIntent.getActivity(TabMenu.this,0, new Intent(TabMenu.this,TabMenu.class),0);
                            builder.setContentIntent(pendingIntent);
                            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            notificationManager.notify(0,builder.build());
                        }
                    }else {
                    }
                }else{
                }
            }
        });

    }

    public double getDistance(LatLng start, LatLng end){
        double lat1 = (Math.PI/180)*start.latitude;
        double lat2 = (Math.PI/180)*end.latitude;

        double lon1 = (Math.PI/180)*start.longitude;
        double lon2 = (Math.PI/180)*end.longitude;

        //地球半徑
        double R = 6371;

        //兩點間距離 km，如果想要米的話，結果*1000就可以了
        double d =  Math.acos(Math.sin(lat1)*Math.sin(lat2)+Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon2-lon1))*R;
        return d*1000;
    }


}
