package com.example.menu.A02_SignupScreen;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.menu.A01_SigninScreen.SigninScreen;
import com.example.menu.A03_TabMenu.TabMenu;
import com.example.menu.R;
import com.example.menu.SendNotificationPack.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

//import android.support.v7.app.AppCompatActivity;

public class SignupScreen extends AppCompatActivity {
    Button register,signin;
    private FirebaseAuth mAuth;
    private EditText reg_email;
    private EditText reg_pwd;
    private EditText reg_vali_pwd;
    DatabaseReference TokenRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a02_signup_screen);

        mAuth = FirebaseAuth.getInstance();
        reg_email = (EditText)findViewById(R.id.input_email);
        reg_pwd = (EditText)findViewById(R.id.input_password);
        reg_vali_pwd = (EditText)findViewById(R.id.input_confirmpassword);




        //LOGIN BUTTON


        register =(Button)findViewById(R.id.register_press);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = reg_email.getText().toString();
                String pwd = reg_pwd.getText().toString();
                String pwd_vali = reg_vali_pwd.getText().toString();
                if (!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(pwd)&&!TextUtils.isEmpty(pwd_vali)){
                    if (pwd.equals(pwd_vali)){
                        mAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()){
                                    Log.d("ABCD","ll  "+mAuth.getCurrentUser().getEmail());
                                    sendtoTab();
                                    TokenRef = FirebaseDatabase.getInstance().getReference().child("Tokens");
                                    FirebaseInstanceId.getInstance().getInstanceId()
                                            .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                                    if (!task.isSuccessful()) { return;  }
                                                    if( task.getResult() == null)
                                                        return;
                                                    // Get new Instance ID token
                                                    String token = task.getResult().getToken();

                                                    // Log and toast
                                                    Log.i("FCM","firebase token " + token);
                                                    Token newToken = new Token();
                                                    newToken.setEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                                                    newToken.setToken(token);

                                                    TokenRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(newToken);
                                                }
                                            });
                                }else {
                                    String err = task.getException().getMessage();
                                    Log.d("ABCD",""+err);
                                }

                            }
                        });
                    }
                }
            }
        });


//        //FACEBOOK BUTTON
//
//        facebook =(Button)findViewById(R.id.facebook_press);
//        facebook.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Toast.makeText(getApplicationContext(),"Facebook Pressed",Toast.LENGTH_SHORT).show();
//            }
//        });


        //SIGNUP BUTTON

        signin =(Button)findViewById(R.id.signin_press);
        signin.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), SigninScreen.class);
                startActivity(i);

            }
        });

    }

    private void sendtoTab() {
        Intent i = new Intent(getApplicationContext(), TabMenu.class);
        startActivity(i);
    }
}
