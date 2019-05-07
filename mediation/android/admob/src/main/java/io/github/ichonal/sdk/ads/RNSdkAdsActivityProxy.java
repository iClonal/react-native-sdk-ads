package io.github.ichonal.sdk.ads;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.ads.MobileAds;

public class RNSdkAdsActivityProxy {
    public RNSdkAdsActivityProxy() {
    }

    public void destroy() {
    }

    public void onActivityCreated(Activity activity, Bundle bundle) {
        try {
            PackageManager pm = activity.getPackageManager();
            String pkgName = activity.getPackageName();
            ApplicationInfo pInfo = pm.getApplicationInfo(pkgName, PackageManager.GET_META_DATA);
            String appId = pInfo.metaData.getString("com.google.android.gms.ads.APPLICATION_ID");
            MobileAds.initialize(activity, appId);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("[RNSdkAdsActivityProxy]", "The Google Mobile Ads SDK was initialized incorrectly");
        }
    }

    public void onActivityStarted(Activity activity) {

    }

    public void onActivityResumed(Activity activity) {

    }

    public void onActivityPaused(Activity activity) {

    }

    public void onActivityStopped(Activity activity) {

    }

    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    public void onActivityDestroyed(Activity activity) {

    }
}
