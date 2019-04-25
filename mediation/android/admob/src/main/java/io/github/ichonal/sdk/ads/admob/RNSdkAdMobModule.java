package io.github.ichonal.sdk.ads.admob;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.google.android.gms.ads.MobileAds;

public class RNSdkAdMobModule extends ReactContextBaseJavaModule {
    public static final String REACT_CLASS = "RNSdkAdMob";

    public RNSdkAdMobModule(ReactApplicationContext reactContext) {
        super(reactContext);

        try {
            PackageManager pm = reactContext.getPackageManager();
            String pkgName = reactContext.getPackageName();
            ApplicationInfo pInfo = pm.getApplicationInfo(pkgName, PackageManager.GET_META_DATA);
            String admobAppId = pInfo.metaData.getString("com.google.android.gms.ads.APPLICATION_ID");
            MobileAds.initialize(reactContext, admobAppId);
        } catch (PackageManager.NameNotFoundException e) {
            //e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }
}
