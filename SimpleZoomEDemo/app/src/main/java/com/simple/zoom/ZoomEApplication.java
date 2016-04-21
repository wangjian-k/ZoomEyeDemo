package com.simple.zoom;

import android.app.Application;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.base.volley.utils.Profile;
import com.base.volley.utils.logger.Logger;

import org.json.JSONObject;

/**
 * Created by kart0l on 2016/4/20.
 */
public class ZoomEApplication extends Application {

    private static final String TAG = ZoomEApplication.class.getSimpleName();

    public static String User_Agent;
    public static String _versionName;
    private static String PREF_NAME = "zoom.pref";
    private static String access_token;

    @Override
    public void onCreate() {
        super.onCreate();

        initConfig();
    }

    private void initConfig() {
        Logger.setDebugMode(true);
        try {
            //init versionname and UA
            _versionName = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_META_DATA).versionName;
            Logger.d(TAG, "versionName : " + _versionName);

            User_Agent = "SimpleZoomDemo" + _versionName
                    + ";Android;" + android.os.Build.VERSION.RELEASE + ";"
                    + android.os.Build.MODEL;
        } catch (PackageManager.NameNotFoundException e) {

        }

        //init network lib config.
        try {
            Profile.initProfile(TAG, User_Agent, getApplicationContext());
            // need preload a image for use cache.
//            Profile.getImageLoader().get(
//                    "http://www.wurener.com/upload/avatars/1446990235_216142009_360_360.jpg",
//                    ImageLoader.getImageListener(null, 0, 0));

            Logger.e(TAG, "Profile simpleSize : " + Profile.getSampleSize());
            Logger.e(TAG, "Profile CompressRatio : " + Profile.getCompressRatio());
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
    }

    public static void setAccessToken(JSONObject tokenJson) {
        try {
            if(tokenJson != null) {
                access_token = tokenJson.getString("access_token");
                Logger.e(TAG,"set access_token : " + access_token);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static String getAccess_token() {
        if(!TextUtils.isEmpty(access_token)) {
            return access_token;
        }
        return null;
    }

}
