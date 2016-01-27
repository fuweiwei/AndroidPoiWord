package com.test.poiword.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import java.text.SimpleDateFormat;
import java.util.UUID;

/**
 * Created by fuweiwei on 2015/9/17.
 * 时间操作类
 */
public class CommonUtils {
    private static final SimpleDateFormat  df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    /**
     * 判断本地时间是否在后台时间之前
     * @param localTime 本地时间
     * @param netTime  后台时间
     * @return
     */
    public static Boolean IsBefore(String localTime,String netTime){
        Boolean flag = false;
       /* try {
            Date date1 = df.parse(localTime);
            Date date2 = df.parse(netTime);
            if(date1.before(date2)){
                flag=true;
            }else {
                flag = false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        if(localTime==null){
            return true;
        }
        if(!localTime.equals(netTime)){
            flag=true;
        }else{
            flag = false;
        }
        return  flag;
    }
    /**
     * 随机生成一个GUID
     * @return
     */
    public static String getGuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
    /**
     * 获取android中的DeviceId
     * @param mContext
     * @return
     */
    public static String getDeviceId(Context mContext){
        return ((TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }
    /**
     * 获取android中的SN号
     * @param mContext
     * @return
     */
    public static String getSN(Context mContext){
        return android.provider.Settings.System.getString(mContext.getContentResolver(), "android_id");
    }
    /*
     * 判断网络是否链接
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cManager != null) {
            NetworkInfo localNetworkInfo = cManager.getActiveNetworkInfo();
            if (localNetworkInfo != null) {
                return localNetworkInfo.isConnectedOrConnecting();
            }
        }
        return false;
    }

}
