package com.example.toolbar.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class IntentUtils {

	private static Intent intent;

	public static void startActivity(Context context, Class activituclass) {

		intent = new Intent(context, activituclass);
		context.startActivity(intent);
	}
	
	
	public static void startActivityForString(Context context,
			Class activituclass, String key, String value) {

		intent = new Intent(context, activituclass);
		intent.putExtra(key, value);
		context.startActivity(intent);
	}

	public static void startActivityForString_Dubble(Context context,
			Class activituclass, String key, String value, String key1,
			String value1) {

		intent = new Intent(context, activituclass);
		intent.putExtra(key, value);
		intent.putExtra(key1, value1);
		context.startActivity(intent);
	}
	
}
