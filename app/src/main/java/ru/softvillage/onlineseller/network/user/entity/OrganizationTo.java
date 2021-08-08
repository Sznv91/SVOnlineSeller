package ru.softvillage.onlineseller.network.user.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class OrganizationTo {
    public static final String ORG_PREFIX = "org_";

    @SerializedName(ORG_PREFIX + "uuid")
    @Expose
    private String orgUuid;

    @SerializedName(ORG_PREFIX + "name")
    @Expose
    private String orgName;

    @SerializedName(ORG_PREFIX + "inn")
    @Expose
    private Long inn;

    @SerializedName(ORG_PREFIX + "sno")
    @Expose
    private String sno;

    @SerializedName(ORG_PREFIX + "address")
    @Expose
    private String address;

    @SerializedName(ORG_PREFIX + "payment_place")
    @Expose
    private String paymentPlace;

}
