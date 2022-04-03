package com.example.menu.A03_TabMenu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.menu.R;
import com.example.menu.fetcher.OnTaskCompleted;
import com.example.menu.fetcher.fetchData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;

public class A01_SearchTab extends Fragment implements OnTaskCompleted<Boolean> {

    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    EditText input_text;
    ImageButton mic_btn;
    String text;
    SharedPreferences pref;

    public A01_SearchTab() {
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
        View view =  inflater.inflate(R.layout.a04_home_menu, container, false);

        // speech to text
        input_text = view.findViewById(R.id.input_text);
        mic_btn = view.findViewById(R.id.mic_btn);

        pref = getContext().getSharedPreferences("test", MODE_PRIVATE);

        mic_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });

        //LOGIN BUTTON

        Button search =(Button)view.findViewById(R.id.search_press);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Date currentTime = Calendar.getInstance().getTime();
                Log.d("TIME TEST", currentTime.toString());
                //A03_MainTab.clearList = true;
                text = input_text.getText().toString();
                pref.edit()
                        .putString("Text", text)
                        .apply();
                ResultCilcked();

            }
        });



//        search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent i = new Intent(getActivity(), HomeMenu.class);
//                startActivity(i);
//
//            }
//        });
        return view;
    }

    public void ResultCilcked()
    {
        fetchData fTask = new fetchData(getActivity());
        fTask.listener = this;
        fTask.execute();
        //connectionTask.connectionTestResult = this;
        //connectionTask.execute( "" );
    }


    private void speak() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi speak something");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {

            Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case REQUEST_CODE_SPEECH_INPUT:{
                if(resultCode==RESULT_OK&& null!=data){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    input_text.setText(result.get(0));
                }
                break;
            }
        }
    }

    @Override
    public void onTaskCompleted(Boolean s) {
        if (s){
            TabMenu.viewPager.setCurrentItem(2); // MainTab
        }else{
            Log.d("ERRORA","我不知道peko");
        }
    }
}

