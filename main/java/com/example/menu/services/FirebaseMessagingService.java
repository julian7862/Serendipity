package com.example.menu.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.menu.A011_Event.Event;
import com.example.menu.R;
import com.example.menu.SendNotificationPack.ActionReceiver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.RemoteMessage;


public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String CHANNEL_ID = "my_channel";
    String title,message,Invitation_ID;
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser!=null){
            sendNotification(remoteMessage);
        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        if (FirebaseAuth.getInstance().getCurrentUser()!=null){
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
                            DatabaseReference tokenRef = FirebaseDatabase.getInstance().getReference().child("Tokens");
                            tokenRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Token").setValue(token);
                        }
                    });
        }
    }



    private void sendNotification(RemoteMessage remoteMessage) {
        title=remoteMessage.getData().get("Title");
        message=remoteMessage.getData().get("Message");
        Invitation_ID = remoteMessage.getData().get("IID");
        //This is the intent of PendingIntent
        Intent intentActionAdd = new Intent(this, ActionReceiver.class);
        intentActionAdd.putExtra("action",true);
        intentActionAdd.putExtra("IID",Invitation_ID);
        Intent intentActionDeny = new Intent(this,ActionReceiver.class);
        intentActionDeny.putExtra("action",false);
        intentActionDeny.putExtra("IID",Invitation_ID);
        PendingIntent pIntentAdd = PendingIntent.getBroadcast(this,1,intentActionAdd,PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pIntentDeny = PendingIntent.getBroadcast(this,2,intentActionDeny,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent toFCMresult = new Intent(this, Event.class);
        toFCMresult.putExtra("IID",Invitation_ID);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0, toFCMresult,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder= new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher).setContentTitle(title).setContentText(message)
                .addAction(R.drawable.edit_back, "參加", pIntentAdd)
                .addAction(R.drawable.edit_back, "拒絕", pIntentDeny)
                .setAutoCancel(true).setDefaults(1)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID);
        }
        notificationManager.notify(1, builder.build());

    }
}
