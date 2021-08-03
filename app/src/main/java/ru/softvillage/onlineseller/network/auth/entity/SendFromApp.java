package ru.softvillage.onlineseller.network.auth.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SendFromApp {
    @SerializedName("id_device")
    @Expose
    private String deviceId;

    @SerializedName("firebase_token")
    @Expose
    private String fireBaseToken;
}
