package com.haseman.serviceExample;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.IBinder;
import android.provider.MediaStore;
import android.widget.RemoteViews;

public class PhotoListenerService extends Service{

	Intent currentIntent = null;
	private String key = null;
	
	private long lastUpdateTime;
	
	public final static String EXTRA_IMGURL_KEY = "imgurl_key";
	
	public final static String ACTION_PHOTO_TAKEN = "com.haseman.ServiceExample.PictureTaken";
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		if(intent != null)
			key = intent.getStringExtra(EXTRA_IMGURL_KEY);
		
		lastUpdateTime = System.currentTimeMillis();
		
		setForegroundState(true);
		return Service.START_STICKY;
	}
	
	@Override
	public void onCreate(){
		super.onCreate();
		getContentResolver().registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true, observer);	
	}

	public void onDestory(){
		super.onDestroy();
		setForegroundState(false);
		getContentResolver().unregisterContentObserver(observer);
	}
	private void setForegroundState(boolean enable){
		
		if(enable){
			Notification n = new Notification(R.drawable.icon, "Service Is uploading all your photos", System.currentTimeMillis());
			
			n.contentView = new RemoteViews("com.haseman.serviceExample", R.layout.notification);
			Intent clickIntent = new Intent(getApplicationContext(), ServiceExampleActivity.class);
			n.contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, clickIntent , 0);
			
			startForeground(1, n);
		}
		else{
			stopForeground(true);
		}
			
	}
	
ContentObserver observer = new ContentObserver(null) {
	public void onChange(boolean self){
		
		Cursor cursor = null;
		try{
			cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media.DATE_TAKEN + " > "+lastUpdateTime, null, null);
			lastUpdateTime = System.currentTimeMillis();
			if(cursor.moveToFirst()){
				Intent i = new Intent(ACTION_PHOTO_TAKEN);
				int dataIDX = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
				i.putExtra("path", cursor.getString(dataIDX));
				sendBroadcast(i);
			}
		}finally{
			if(cursor!=null)
				cursor.close();
		}
	}
};

}
