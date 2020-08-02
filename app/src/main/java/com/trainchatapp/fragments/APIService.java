package com.trainchatapp.fragments;

import com.trainchatapp.notifications.MyResponse;
import com.trainchatapp.notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAypzU1hw:APA91bF0Ls7zeLTUEc5CAtJYsazYcBB5D5rdAGhtmTnQc4YxgxLfx_4E3wDqUgDM0tUYdNMDBoNwL59eSZpZi2R_iQ6mkVomcpEolYcKjC38xAGwqG2M0PbhYzNCQ_XGSjh8QkyWqQ_c"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
