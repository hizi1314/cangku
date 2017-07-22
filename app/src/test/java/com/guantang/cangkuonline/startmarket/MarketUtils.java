package com.guantang.cangkuonline.startmarket;

/**
 * Android跳转应用市场的应用详情页
 * */

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

public class MarketUtils {
	
	 public static Intent getIntent(Context paramContext) {
	        StringBuilder localStringBuilder = new StringBuilder().append("market://details?id=");
	        String str = paramContext.getPackageName();
	        localStringBuilder.append(str);
	        Uri localUri = Uri.parse(localStringBuilder.toString());
	        Intent intent = new Intent("android.intent.action.VIEW", localUri);
	        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        return intent;
	    }
	 
	 public static boolean judge(Context paramContext, Intent paramIntent) {
	        List localList = paramContext.getPackageManager().queryIntentActivities(paramIntent, PackageManager.GET_INTENT_FILTERS);
	        if ((localList != null) && (localList.size() > 0)) {
	            return true;
	        } else {
	            return false;
	        }
	    }
	 
}
