package com.example.menu.A07_RestaurantDetail;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.menu.A011_Invite.Invite;
import com.example.menu.A11_Finish.Finish;
import com.example.menu.R;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.GeoPoint;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RestaurantDetailView extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    ListView list,list2;
    RestaurantDetailListview adapter;
//    RestaurantDetailListview2 adapter2;
    String[] name,message,title,time,content;

    ImageView imageView;

    int[] image,images;
    private ImageButton Back;
    TextView titleV,subtitleV;
    String T=null,subT=null,img=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.a07_restaurant_detail_view);

        titleV = findViewById(R.id.title_content);
        subtitleV = findViewById(R.id.subtitle_content);
        imageView = findViewById(R.id.slider);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        if (getIntent().getStringExtra("name")!=null){
            T= getIntent().getStringExtra("name");
            subT= getIntent().getStringExtra("note");
            img = getIntent().getStringExtra("imgname");
            Picasso.get().load(img).into(imageView);
            titleV.setText(T);
            subtitleV.setText(subT);
        }else{
            T= titleV.getText().toString();subT= subtitleV.getText().toString();
        }



        name = new String[]{ "Spicy Fried Rice &amp; Bacon Amet", "Shrimp Curry Burger Ipsam dolor Sit", "Masala Spiced Chickpeas Dolor Amet", "Kung Pao Pastrami ad Minima Sit", "Chicken Doro Wat Nisi Commodo Amet","Mango Cile Chutney Et Dolore Sit"};

        message = new String[]{ "Lorem ipsum dolor sit amet", "Lorem ipsum dolor sit amet", "Lorem ipsum dolor sit amet", "Lorem ipsum dolor sit amet", "Lorem ipsum dolor sit amet", "Lorem ipsum dolor sit amet", "Lorem ipsum dolor sit amet","Lorem ipsum dolor sit amet","Lorem ipsum dolor sit amet"};


        image = new int[] { R.drawable.listimg1,R.drawable.listimg2,
                R.drawable.listimg3,R.drawable.listimg4,
                R.drawable.listimg5,R.drawable.listimg6};

        title = new String[]{ "Shagor Doe","Zahid Doe","Rakib Smith"};

        content = new String[]{ "Amorim ipsum dolor sit amet, consectetur adipiscing elit. Cras vitae nibh nisl. Cras etitikis mauris eget lorem ultricies ferme ntum a inti diam. ","Amorim ipsum dolor sit amet, consectetur adipiscing elit. Cras vitae nibh nisl. Cras etitikis mauris eget lorem ultricies ferme ntum a inti diam. ","Amorim ipsum dolor sit amet, consectetur adipiscing elit. Cras vitae nibh nisl. Cras etitikis mauris eget lorem ultricies ferme ntum a inti diam. "};

        time = new String[]{ "2  hours ago","3 hours ago","4 hours ago"};

        images = new int[] { R.drawable.pic2,R.drawable.pic3,
                R.drawable.pic4};




//        // Locate the ListView in listview_main.xml
//        list = (ListView) findViewById(R.id.mylist);
//
//        // Pass results to ListViewAdapter Class
//        adapter = new RestaurantDetailListview(this, name,message,image);
//        // Binds the Adapter to the ListView
//        list.setAdapter(adapter);
//        // Capture ListView item click
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//
//                Toast.makeText(RestaurantDetailView.this,"You have selected :"+name[position], Toast.LENGTH_SHORT).show();
//
//
//            }
//
//        });


        //list2 = (ListView) findViewById(R.id.mylist2);

        // Pass results to ListViewAdapter Class
//        adapter2 = new RestaurantDetailListview2(this, title,time,content,image);
//        // Binds the Adapter to the ListView
//        list2.setAdapter(adapter2);
//        // Capture ListView item click
//        list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//
//                Toast.makeText(RestaurantDetailView.this,"You have selected :"+title[position], Toast.LENGTH_SHORT).show();
//
//
//            }
//
//        });

    }

    // back
    public void Back(View view) {
        RestaurantDetailView.this.finish();
    }

    // go
    public void GoFinish(View view) {
        Intent browserIntent = new Intent(RestaurantDetailView.this, Finish.class);
        startActivity(browserIntent);
    }

    // invite
    public void GoInvite(View view) {
        Intent intent = new Intent(RestaurantDetailView.this, Invite.class);
        if (T!=null){
            intent.putExtra("Title",T);
            intent.putExtra("Subtitle",subT);
            intent.putExtra("imgname",img);
        }
        startActivity(intent);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng l =getLocationFromAddress(subT);
        if (l!=null){
            mMap.addMarker(new MarkerOptions().position(l).title(T).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(l,16.0f));
        }

        if ( ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED )
        {
            return;
        }
        LocationServices.getFusedLocationProviderClient(this).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location mLastLocation) {
                LatLng myLatLng = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
                mMap.addMarker(new MarkerOptions().position(myLatLng).title("Me"));
                PolylineOptions s = new PolylineOptions().add(myLatLng).add(l).color(0);
                mMap.addPolyline(s);
                zoomRoute(s.getPoints());
            }
        });

//        LatLng origin = new LatLng(-7.789000, 110.338000);
//        LatLng destination = new LatLng(-7.781000, 110.350000);
////        DrawRouteMaps.getInstance(this)
////                .draw(origin, destination, mMap);
////        DrawMarker.getInstance(this).draw(mMap, origin, R.drawable.marker_a, "Origin Location");
////        DrawMarker.getInstance(this).draw(mMap, destination, R.drawable.marker_b, "Destination Location");
//
//        LatLngBounds bounds = new LatLngBounds.Builder()
//                .include(origin)
//                .include(destination).build();
//        Point displaySize = new Point();
//        getWindowManager().getDefaultDisplay().getSize(displaySize);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, displaySize.x, 250, 30));
    }

    public LatLng getLocationFromAddress(String strAddress){

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        GeoPoint p1 = null;

        try {
            address = coder.getFromLocationName(strAddress,5);
            if (address==null) {
                return null;
            }
            Address location=address.get(0);
            location.getLatitude();
            location.getLongitude();

            return new LatLng(location.getLatitude(),location.getLongitude());
        }catch (Exception e){
            return null;
        }
    }
    public void zoomRoute(List<LatLng> lstLatLngRoute) {

        if (mMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return;

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng latLngPoint : lstLatLngRoute)
            boundsBuilder.include(latLngPoint);

        int routePadding = 120;
        LatLngBounds latLngBounds = boundsBuilder.build();

        mMap.animateCamera(
                CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding),
                600,
                null
        );
    }

}