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

public class appDownloader extends CordovaPlugin {

	CallbackContext callback;
	Context context;

	String www;
	String downloadZip;
	String indexHtml;

	int pre;

	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {

		super.initialize(cordova, webView);
		context = cordova.getActivity().getApplicationContext();
	}

	@Override
	public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {

		callback = callbackContext;

		www = context.getFilesDir().getPath() + "/";

		if (action.equals("download")) {

			downloadZip = www + "update.zip";

			JSONObject json = args.getJSONObject(0);
			final String url = getJSONProperty(json, "url");

			try {

				cordova.getThreadPool().execute(new Runnable() {

            		@Override
            		public void run() {	

						/* dunno why, but this is needed */
						try {
							appDownloader.this.download(url);
						} catch (Exception e) {
							e.printStackTrace();
						}
						PluginResult result = new PluginResult(PluginResult.Status.OK);
						callback.sendPluginResult(result);
            		}
        		});
				return true;

			} catch (Exception e) {

				PluginResult result = new PluginResult(PluginResult.Status.ERROR, e.getMessage());
				callback.sendPluginResult(result);
				return false;
			}
		}

		if (action.equals("deploy")) {
			indexHtml = www + "index.html";

			File indexFile = new File(indexHtml);

			if (!indexFile.exists()) {
				PluginResult result = new PluginResult(PluginResult.Status.ERROR, "index.html not found");
				callback.sendPluginResult(result);
				return false;
			}

			super.webView.loadUrl("file://" + indexHtml);
			return true;
		}

		PluginResult result = new PluginResult(PluginResult.Status.ERROR, "Unknown action");
		callback.sendPluginResult(result);
		return false;
	}

	private String getJSONProperty(JSONObject json, String property) throws JSONException {
		if (json.has(property)) {
			return json.getString(property);
		}
		return null;
	}

	private void download(String url) throws Exception {

		//DefaultHttpClient httpClient = new DefaultHttpClient();

		URL get = new URL(url);

		HttpURLConnection httpClient = (HttpURLConnection) get.openConnection();
		//InputStream is = httpClient.getInputStream();

		httpClient.setRequestMethod("GET");
		//httpClient.setDoInput(true);

		//httpClient.connect();

		//HttpResponse response = httpClient.execute(get);

		//BufferedInputStream download = new BufferedInputStream(response.getEntity().getContent());

		InputStream download = httpClient.getInputStream();

		File downloadPath = new File(downloadZip);

		if (downloadPath.exists()) {

			downloadPath.delete();
		}

		downloadPath.getParentFile().mkdirs();

		FileOutputStream file = new FileOutputStream(downloadZip);

		float bytesRead = 0;
		float contentLength = httpClient.getContentLength();
		float total = 0;
		float _sublast = 0;

		byte[] bytes = new byte[1024];

		if (contentLength == 0) {
			file.close();
		}

		while ((bytesRead = download.read(bytes, 0, 1024)) != -1) {

			total += bytesRead;
			float percent = ((total*100)/contentLength);
			

			file.write(bytes, 0,(int)bytesRead);
			file.flush();

			//pre = test;
			if( _sublast != percent ) {

				appUpdateData.percent = percent;
				_sublast += 1;
			}
		}

		//httpClient.consumeContent();
		file.close();

		httpClient.disconnect();

		this.unzip();
	}

	private void unzip() throws Exception {
		
		ZipFile zip = new ZipFile(downloadZip);
		Enumeration<? extends ZipEntry> entries = zip.entries();

		while (entries.hasMoreElements()) {

			ZipEntry entry = entries.nextElement();
			File zipFile = new File(www + entry.getName());

			if (!entry.isDirectory()) {
				zipFile.getParentFile().mkdirs();

				FileOutputStream fileOutput = new FileOutputStream(www + entry.getName());
				BufferedOutputStream bufferedOutput = new BufferedOutputStream(fileOutput);

				copyStream(zip.getInputStream(entry), bufferedOutput);
			}
		}

		zip.close();

		copyStream(context.getAssets().open("www/cordova.js"),
		new FileOutputStream(www + "cordova.js"));

		copyStream(context.getAssets().open("www/cordova_plugins.js"),
		new FileOutputStream(www + "cordova_plugins.js"));

		this.copyDir("plugins");

		PluginResult result = new PluginResult(PluginResult.Status.OK);
		callback.sendPluginResult(result);
	}

	private void copyDir(String path) throws Exception {
		AssetManager assetManager = context.getAssets();
		String assets[] = assetManager.list("www/" + path);

		if (assets.length == 0) {
			copyStream(context.getAssets().open("www/" + path),
			new FileOutputStream(www + path));
			return;
		}

		File dir = new File(www + path);

		if (!dir.exists()) {
			dir.mkdir();
		}

		for (int i = 0; i < assets.length; i++) {
			copyDir(path + "/" + assets[i]);
		}
	}

	private void copyStream(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int len;

		while ((len = in.read(buffer)) >= 0) {
			out.write(buffer, 0, len);
		}

		in.close();
		out.close();
	}

	private void sendUpdate(String text) {

		PluginResult msg = new PluginResult(PluginResult.Status.OK, text);
		msg.setKeepCallback(true);
		callback.sendPluginResult(msg);
	}
}
