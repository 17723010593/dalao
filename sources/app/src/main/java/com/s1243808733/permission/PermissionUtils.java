package com.s1243808733.permission;

import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.content.res.Resources;
import com.s1243808733.util.Utils;
import java.util.List;
import android.content.SharedPreferences;

public class PermissionUtils {

	public static boolean isPeril(String permission) {
		for (String permissions:Manifest.PERIL_PERMISSION) {
			if (permissions.equals(permission)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean hasAdded(List<Permission> list, String permission) {
		for (Permission permissions:list) {
			if (!(permissions instanceof CommonItem)) {
				if (permissions.getPermission().equals(permission)) {
					return true;
				}
			}
		}
		return false;
	}
    
    public static boolean hasAdded(List<Permission> list, Permission permission) {
        for (Permission p:list) {
            if (!(p instanceof CommonItem)) {
                if (permission.getPermission().equals(p.getPermission())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean isAnnotationPermission() {
        return getSp().getBoolean("annotation_permission", true);
	}
    
   public static  SharedPreferences getSp() {
        return Utils.getSp("PermissionEditor");
    }
    

	public static int dp2px(float dpValue) {
		float scale = Resources.getSystem().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
	
	public static PackageManager getPackageManager() {
        return Utils.getApp().getPackageManager();
    }

    public static PermissionInfo getPermissionInfo(String name) {
        try {
            return getPackageManager().getPermissionInfo(name, 0);
        } catch (Exception e) {}
        return null;
    }

	public static String getPermissionLabel(String name) {
        try {
            return getPermissionInfo(name).loadLabel(getPackageManager()).toString();
        } catch (Exception e) {}
        return null;
    }

    public static String getPermissionDescription(String code) {
        try {
            return getPermissionInfo(code).loadDescription(getPackageManager()).toString();
        } catch (Exception e) {}
        return null;
    }

    public static String getPermissionNameSuffix(String name) {
        int index=name.lastIndexOf(".");
        return index < 0 ?name: name.substring(index + 1);
    }

}
