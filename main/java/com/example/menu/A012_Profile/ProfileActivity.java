package com.example.menu.A012_Profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.menu.R;



public class ProfileActivity extends AppCompatActivity {
       private ImageButton edit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a12_profile);


        edit = (ImageButton) findViewById(R.id.edit_btn);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(),Edit_Profile.class);
                startActivity(i);

            }
        });
    }


//    public void EditButtonOnClick(View v) {
//        Intent browserIntent = new Intent(getApplicationContext(), Edit_Profile.class);
//        startActivity(browserIntent);
//    }

}


