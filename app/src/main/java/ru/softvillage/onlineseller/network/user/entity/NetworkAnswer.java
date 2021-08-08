package ru.softvillage.onlineseller.network.user.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class NetworkAnswer {

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("error")
    @Expose
    private String error;

    @SerializedName("users")
    @Expose
    private List<LocalUserTo> userTos;
}
