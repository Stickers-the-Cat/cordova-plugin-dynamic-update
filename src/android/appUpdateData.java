//
//  DynamicUpdate.java
//  Copyright (c) 2014 Lee Crossley - http://ilee.co.uk
//

package com.stickersthecat.downloader;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.json.*;

import org.apache.cordova.*;
// import org.apache.http.HttpResponse;
// import org.apache.http.client.methods.HttpGet;
// import org.apache.http.impl.client.DefaultHttpClient;
import java.io.InputStream;
import java.net.URL;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import android.widget.ProgressBar;
import java.net.URL;
import java.net.URLConnection;


import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class appUpdateData extends CordovaPlugin {

	CallbackContext callback;
	Context context;

	 public static int percent = 0;

	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {

		super.initialize(cordova, webView);
		context = cordova.getActivity().getApplicationContext();
	}

	@Override
	public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {

		callback = callbackContext;

		if (action.equals("getData")) {

			percent+=1;
			PluginResult result = new PluginResult(PluginResult.Status.OK, ""+percent);
			callback.sendPluginResult(result);
			return true;
		}

		return false;
	}

	private String getJSONProperty(JSONObject json, String property) throws JSONException {

		if (json.has(property)) {
			return json.getString(property);
		}
		return null;
	}

}
