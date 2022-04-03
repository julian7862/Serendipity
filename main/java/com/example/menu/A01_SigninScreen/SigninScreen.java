package com.example.menu.A01_SigninScreen;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDex;

import com.example.menu.A02_SignupScreen.SignupScreen;
import com.example.menu.A03_TabMenu.TabMenu;
import com.example.menu.R;
import com.example.menu.SendNotificationPack.Token;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

//import android.support.annotation.NonNull;
//import android.support.multidex.MultiDex;
//import android.support.v7.app.AppCompatActivity;

public class SigninScreen extends AppCompatActivity {
    Button login,signup;
    LoginButton fb_loginButton;
    CallbackManager callbackManager;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    AccessTokenTracker accessTokenTracker;

    DatabaseReference onlineRef,counterRef,currentUserRef,locations,TokenRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MultiDex.install(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.a01_signin_screen);
        callbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();
        TextView loginemailtxt = findViewById(R.id.input_email) ;
        TextView loginpwdtxt =findViewById(R.id.input_password);
//                try {
//            PackageInfo info = getPackageManager().getPackageInfo("com.example.menu", PackageManager.GET_SIGNATURES);
//            for (Signature signature:info.signatures){
//                MessageDigest messageDigest= MessageDigest.getInstance("SHA");
//                messageDigest.update(signature.toByteArray());
//                Log.d("TUSK", Base64.encodeToString(messageDigest.digest(),Base64.DEFAULT));
//            }
//        }catch (Exception e){
//
//        }

        //LOGIN BUTTON

        login =(Button)findViewById(R.id.login_press);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String loginemail = loginemailtxt.getText().toString();
                String loginpwd = loginpwdtxt.getText().toString();
                if (!TextUtils.isEmpty(loginemail)&&!TextUtils.isEmpty(loginpwd)){
                    mAuth.signInWithEmailAndPassword(loginemail,loginpwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                //Log.d("AAAA","Login");
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
                                                Log.d("AAAA","firebase token " + token);
                                                Token newToken = new Token();
                                                newToken.setEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                                                newToken.setToken(token);
                                                TokenRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(newToken);
                                            }
                                        });
                                Intent intent = new Intent(SigninScreen.this, TabMenu.class);
                                sendtoTab();
                            }

                        }
                    });
                }

                //Toast.makeText(getApplicationContext(),"Login Pressed",Toast.LENGTH_SHORT).show();
            }
        });

        //FACEBOOK BUTTON

        fb_loginButton = (LoginButton) findViewById(R.id.facebook_press);
        fb_loginButton.setReadPermissions("email");

        // Callback registration
        fb_loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Toast.makeText(SigninScreen.this,"FB_onSuccess"+loginResult,Toast.LENGTH_LONG);
                handleFacebookAccessToken(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(SigninScreen.this,"FB_onCancel",Toast.LENGTH_LONG);
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(SigninScreen.this,"FB_onError"+exception,Toast.LENGTH_LONG);
            }
        });

        TokenRef = FirebaseDatabase.getInstance().getReference().child("Tokens");

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user!=null){
//                    updatePhoto(user);
                }else{
//                    updatePhoto(null);
                }

            }
        };

        mAuth.addAuthStateListener(authStateListener);

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null){
//                    currentUserRef.removeValue();
                    mAuth.signOut();
                }else {
//                    Loginbtn.setEnabled(false);
                }
            }
        };



        //SIGNUP BUTTON

        signup =(Button)findViewById(R.id.signup_press);
        signup.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(getApplicationContext(), SignupScreen.class);
                startActivity(i);
            }
        });


    }

    private void sendtoTab() {
        Intent i = new Intent(getApplicationContext(), TabMenu.class);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        if (mAuth.getCurrentUser()!=null){
            //currentUserRef.removeValue();
        }

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
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
                            Intent intent = new Intent(SigninScreen.this, TabMenu.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            // Log.w("TUSK", "signInWithCredential:failure", task.getException());
                            Toast.makeText(SigninScreen.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
}
