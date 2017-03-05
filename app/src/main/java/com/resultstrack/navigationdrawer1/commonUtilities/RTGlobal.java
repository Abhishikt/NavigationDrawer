package com.resultstrack.navigationdrawer1.commonUtilities;

import android.app.Application;

import com.resultstrack.navigationdrawer1.model.appUser;

import org.json.JSONArray;

/**
 * Created by abhishikt on 1/27/2017.
 */

public class RTGlobal extends Application {
    private static appUser _appUser=null;
    private static String state="";
    private static String district="";
    private static String block="";
    private static String village="";
    private static String anganwadiCode="";
    private static String organizationId = "Org/1";
    private static boolean offlineFlg=false;

    public static JSONArray childSurveyResponse = null;

    public static String getOrganizationId() {
        return organizationId;
    }

    public static void setOrganizationId(String organizationId) {
        RTGlobal.organizationId = organizationId;
    }

    public static boolean isOfflineFlg() {
        return offlineFlg;
    }

    public static void setOfflineFlg(boolean offlineFlg) {
        RTGlobal.offlineFlg = offlineFlg;
    }

    public static appUser get_appUser() {
        return _appUser;
    }

    public static void set_appUser(appUser _appUser) {
        RTGlobal._appUser = _appUser;
    }

    public static String getState() {
        return state;
    }

    public static void setState(String state) {
        RTGlobal.state = state;
    }

    public static String getDistrict() {
        return district;
    }

    public static void setDistrict(String district) {
        RTGlobal.district = district;
    }

    public static String getBlock() {
        return block;
    }

    public static void setBlock(String block) {
        RTGlobal.block = block;
    }

    public static String getVillage() {
        return village;
    }

    public static void setVillage(String village) {
        RTGlobal.village = village;
    }

    public static String getAnganwadiCode() {
        return anganwadiCode;
    }

    public static void setAnganwadiCode(String anganwadiCode) {
        RTGlobal.anganwadiCode = anganwadiCode;
    }
}
