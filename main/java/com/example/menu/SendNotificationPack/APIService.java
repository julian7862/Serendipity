package com.example.menu.SendNotificationPack;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAjzoDlnc:APA91bFep4ITPDEcZZBFVTuwbd_tKnvzsN72f_-KyyNH7rK-4Xfkxq5YFglM8Zkv0c5-zra99FGkWZkITxBOcCbFBgI6ALjXkmkCcBvS-C9NdtoZECFB2MGiSrR39iAKQFMRFfPwCr7R"
            }
    )

//    @POST("fcm/send")
//    Call<com.example.a611.SendNotificationPack.MyResponse> sendNotifcation(@Body com.example.a611.SendNotificationPack.NotificationSender body);
    @POST("fcm/send")
    Call<com.example.menu.SendNotificationPack.MyResponse> sendInvitation(@Body com.example.menu.SendNotificationPack.InvitationSender body);
}

