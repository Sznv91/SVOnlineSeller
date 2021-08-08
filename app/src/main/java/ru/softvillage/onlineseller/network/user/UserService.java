package ru.softvillage.onlineseller.network.user;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import ru.softvillage.onlineseller.network.user.entity.NetworkAnswer;

public interface UserService {

    @GET("mobile_KKT/get_user.php")
    Call<NetworkAnswer> getUsers(@Query("device") String deviceId, @Header("token") String fireBaseToken);
}
