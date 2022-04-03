package com.example.menu.A03_TabMenu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.menu.A012_Profile.Edit_Profile;
import com.example.menu.R;
import com.example.menu.services.MyBackgroundService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import static android.content.Context.MODE_PRIVATE;

//import android.support.v4.app.Fragment;

public class A05_ProfileTab extends Fragment {

    private ImageButton Edit;
    private MyBackgroundService myBackgroundService;



    public A05_ProfileTab() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        最原本未改的code - Inflate the layout for this fragment
//        return inflater.inflate(R.layout.a12_profile, container, false);

        View view = inflater.inflate(R.layout.a12_profile, container, false);
        ImageView circleView;
        TextView textView;
        SharedPreferences pref = getActivity().getSharedPreferences("test", MODE_PRIVATE);
        Switch location_noti,open_noti;
        location_noti = view.findViewById(R.id.swh_status1);
        open_noti = view.findViewById(R.id.open_notification);
        DatabaseReference locations = FirebaseDatabase.getInstance().getReference().child("Locations");


        locations.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("trackable").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue()==null){
                    return;
                }else if (snapshot.getValue().equals(true)){
                    location_noti.setChecked(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        location_noti.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    locations.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("trackable").setValue(true);
                }else{
                    locations.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("trackable").setValue(false);
                }
            }
        });

        open_noti.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pref.edit().putBoolean("USR_Noti", isChecked).commit();
            }
        });

        circleView = view.findViewById(R.id.image_data);
        FirebaseUser  user= FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            DatabaseReference profile = FirebaseDatabase.getInstance().getReference().child("Locations");

            if (user.getPhotoUrl()!=null){
                String photoURL = user.getPhotoUrl().toString();
                photoURL = photoURL+"?type=large";
                Picasso.get().load(photoURL).into(circleView);
            }
            if (user.getDisplayName()!=null){
                textView = view.findViewById(R.id.title_content);
                textView.setText(user.getDisplayName());
            }
            profile.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())){

                        TextView textView = view.findViewById(R.id.title_content);
                        if (snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").getValue()!=null){
                            textView.setText(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").getValue().toString());
                        }
                        ImageView imageView = view.findViewById(R.id.image_data);
                        if (snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").getValue()!=null){
                            String s = snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("img").getValue().toString();
                            StorageReference r= FirebaseStorage.getInstance().getReference().child("images");
                            r.child(s).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Got the download URL for 'users/me/profile.png'
                                    Picasso.get().load(uri).into(circleView);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                }
                            });
                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        ImageButton Edit = (ImageButton) view.findViewById(R.id.edit_btn);
        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), Edit_Profile.class);
                startActivity(i);
            }
        });



        return view;

    }
    @Override
    public void onPause() {
        super.onPause();
    }

}




//        Edit = (ImageButton) findViewById(R.id.edit_btn);
//
//        Edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent i = new Intent(getApplicationContext(), Edit_Profile.class);
//                startActivity(i);
//
//            }
//        });