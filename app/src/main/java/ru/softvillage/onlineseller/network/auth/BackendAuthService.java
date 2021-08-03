package ru.softvillage.onlineseller.network.auth;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import ru.softvillage.onlineseller.network.auth.entity.ReceiveToApp;
import ru.softvillage.onlineseller.network.auth.entity.SendFromApp;

public interface BackendAuthService {

    @POST("mobile_KKT/start_auth.php")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<ReceiveToApp> registrationDevice(@Body SendFromApp data);
}
