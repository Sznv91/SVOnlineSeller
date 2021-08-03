package ru.softvillage.onlineseller.network.auth.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class ReceiveToApp {
    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("error")
    @Expose
    private boolean error;

    @SerializedName("code")
    @Expose
    private String authCode;

    @SerializedName("time")
    @Expose
    private String generateTime;
}