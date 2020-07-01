package com.example.foodorderingapp.notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RetrofitAPI {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAZ5ijqm8:APA91bF-6Jc1OcIC0a5awJlmQ8QKj-psI1E-R8lpXVNzKmR0hajILn09Hqtaw6sNxGc6hSTcJiB87SFDJnP5Zddem5kClX1QcCNbdnJ32KX-mz7sN-aXq3EVDM6XjQW9k3n_hwfkOJgB"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
