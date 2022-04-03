package com.example.menu.A05_Time;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.menu.A011_Event.Event;
import com.example.menu.R;
import com.example.menu.SendNotificationPack.APIService;
import com.example.menu.SendNotificationPack.Client;
import com.example.menu.SendNotificationPack.Invitation;
import com.example.menu.SendNotificationPack.InvitationSender;
import com.example.menu.SendNotificationPack.MyResponse;
import com.example.menu.SendNotificationPack.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Time extends Activity implements View.OnClickListener {


    private EditText event_Time;

    String T,subT,img;
    DatabaseReference locations,tokenRef,invititations;


    private Date eventTime= new Date();
    private TimePickerView pvTime;
    ArrayList<String> InviteeList=null;

    private APIService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.a05_time);

        locations = FirebaseDatabase.getInstance().getReference().child("Locations");
        tokenRef = FirebaseDatabase.getInstance().getReference("Tokens") ;
        invititations = FirebaseDatabase.getInstance().getReference("Invitations");
        InviteeList = getIntent().getStringArrayListExtra("Invitee");
        T = getIntent().getStringExtra("T");
        subT = getIntent().getStringExtra("subT");
        img = getIntent().getStringExtra("img");

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        event_Time = findViewById(R.id.event_Time);
        event_Time.setOnClickListener(this);
        initTimePicker();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.event_Time:
                if (pvTime != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(eventTime);
                    pvTime.setDate(calendar);
                    pvTime.show(view);
                }
                break;
        }
    }

    private void initTimePicker() {

        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                //如果是開始時間的EditText
                if(v.getId() == R.id.event_Time){
                    eventTime = date;
                }
                EditText editText = (EditText)v;
                editText.setText(getTime(date));
            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {

                    }
                })
                .setType(new boolean[]{true, true, true, true, true, false})
                .isDialog(true)
                .build();


        Dialog mDialog = pvTime.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            pvTime.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改動畫樣式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部顯示
            }
        }
    }

    private String getTime(Date date) {//可根據需要自行擷取資料顯示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy年  MM月dd日  HH:mm");
        return format.format(date);
    }

    //  返回imagebutton，要在xml的img寫onclick
    public void BackButton(View v) {
        com.example.menu.A05_Time.Time.this.finish();
    }

    public void SubmitButton(View v) {
        final String randaomString = random();

        if (event_Time.getText()==null){
            Toast.makeText(this,"起碼你選個時間吧...",Toast.LENGTH_SHORT);
            return;
        }
        //inviter newinviter  =new inviter(FirebaseAuth.getInstance().getCurrentUser().getUid());
        invititations.child(randaomString).child("invitee").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
        invititations.child(randaomString).child("PlaceName").setValue(T);
        invititations.child(randaomString).child("PlaceSubT").setValue(subT);
        invititations.child(randaomString).child("PlaceImg").setValue(img);
        invititations.child(randaomString).child("Inviter").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
        invititations.child(randaomString).child("Time").setValue(event_Time.getText().toString());
        for (String uid:InviteeList){
            invititations.child(randaomString).child("invitee").child(uid).setValue("");
            tokenRef.orderByKey().equalTo(uid).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Token token = dataSnapshot.getValue(Token.class);
                    //Log.d("TUSK",""+token.getToken());
                    sendInvitations(token.getToken(),"邀請",FirebaseAuth.getInstance().getCurrentUser().getDisplayName()+" Test Message:"+T,randaomString);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        Intent browserIntent = new Intent(getApplicationContext(), Event.class);
        browserIntent.putExtra("IID",randaomString);
        startActivity(browserIntent);
    }

    public static String random() {
        Random generator = new Random();
        char[] chars= "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = 12;
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = chars[generator.nextInt(chars.length)];
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    public void sendInvitations(String usertoken, String title, String message,String IID) {
        Invitation data = new Invitation(title, message,IID);
        InvitationSender sender = new InvitationSender(data,usertoken);
        //NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendInvitation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(Time.this, "Failed ", Toast.LENGTH_LONG);
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }


}



