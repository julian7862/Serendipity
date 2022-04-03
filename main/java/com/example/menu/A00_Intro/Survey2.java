package com.example.menu.A00_Intro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.menu.A03_TabMenu.TabMenu;
import com.example.menu.R;

public class Survey2 extends Activity {
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.a00_survey2);

        Button done=(Button)findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), TabMenu.class);
                startActivity(i);

            }
        });

        checkBox = (CheckBox)findViewById(R.id.prefer1);
        checkBox = (CheckBox)findViewById(R.id.prefer2);
        checkBox = (CheckBox)findViewById(R.id.prefer3);
        checkBox = (CheckBox)findViewById(R.id.prefer4);
        checkBox = (CheckBox)findViewById(R.id.prefer5);
        checkBox = (CheckBox)findViewById(R.id.prefer6);
        checkBox = (CheckBox)findViewById(R.id.prefer7);
        checkBox = (CheckBox)findViewById(R.id.prefer8);
        checkBox = (CheckBox)findViewById(R.id.prefer9);
    }
}
