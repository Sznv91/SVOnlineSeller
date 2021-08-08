package ru.softvillage.onlineseller.network.user.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class LocalUserTo {
    public static final String USER_PREFIX = "user_";

    @SerializedName(USER_PREFIX + "uuid")
    @Expose
    private String userUuid;

    @SerializedName(USER_PREFIX + "surname")
    @Expose
    private String surname;

    @SerializedName(USER_PREFIX + "name")
    @Expose
    private String name;

    @SerializedName(USER_PREFIX + "patronymic")
    @Expose
    private String patronymic;

    @SerializedName(USER_PREFIX + "pin")
    @Expose
    private String pin;

    @SerializedName(USER_PREFIX + "available_org")
    @Expose
    private List<OrganizationTo> availableOrg;

}
