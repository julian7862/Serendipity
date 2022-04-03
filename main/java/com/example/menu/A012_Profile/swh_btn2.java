package com.example.menu.A012_Profile;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.menu.R;

public class swh_btn2 extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private Switch swh_status2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a12_profile);

        swh_status2 = (Switch) findViewById(R.id.swh_status1);
        swh_status2.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.open_notification:
                if (compoundButton.isChecked())
                    Toast.makeText(this, "开关:ON", Toast.LENGTH_SHORT).show();
                else Toast.makeText(this, "开关:OFF", Toast.LENGTH_SHORT).show();
                break;

        }
    }
}