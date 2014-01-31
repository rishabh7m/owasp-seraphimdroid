package org.owasp.seraphimdroid.customclasses;

import org.owasp.seraphimdroid.MainScreen;

import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class PermissionGetter {
	String permission;
	PermissionData permissionData;

	public PermissionGetter() {
	}

	public void setPermission(String per) {
		permission = per;
	}

	public PermissionData getPermissionData() {
		generatePermissionData();
		return permissionData;
	}

	public void generatePermissionData() {

		Log.d("Getting permission: ", permission);

		SQLiteDatabase db = MainScreen.helper.getReadableDatabase();
		String sql = "Select * from permissions_data where Permission=\'"
				+ permission + "\'";

		Cursor cursor = db.rawQuery(sql, null);

		// Creating the object required.
		if (cursor.getCount() != 0) {
			cursor.moveToFirst();
			PermissionData pd = new PermissionData(cursor.getString(1),
					cursor.getString(2), cursor.getString(3),
					cursor.getString(4), cursor.getInt(5));
			cursor.close();

			try {
				db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			permissionData = pd;
		}

		PackageManager pm = MainScreen.context.getPackageManager();
		PermissionInfo perInfo = null;
		try {
			perInfo = pm.getPermissionInfo(permission,
					PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (perInfo != null) {
			String label = (String) perInfo.loadLabel(pm);
			String desc = (String) perInfo.loadDescription(pm);
			permissionData = new PermissionData(permission, label, desc,
					"No malicious use known", 0);
		}

		permissionData = new PermissionData(permission, permission,
				"No Description Available", "No Description Available", 0);

	}
}

