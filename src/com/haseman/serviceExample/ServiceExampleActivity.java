package com.haseman.serviceExample;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ServiceExampleActivity extends Activity implements OnClickListener, ServiceConnection{
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button btn = (Button)findViewById(R.id.start_service);
		btn.setOnClickListener(this);

		btn = (Button)findViewById(R.id.stop_service);
		btn.setOnClickListener(this);

		btn = (Button)findViewById(R.id.stop_binder_service);
		btn.setOnClickListener(this);

		btn = (Button)findViewById(R.id.start_binder_service);
		btn.setOnClickListener(this);

		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(PhotoListenerService.ACTION_PHOTO_TAKEN);


		registerReceiver(photoListener, iFilter);
	}

	BroadcastReceiver photoListener = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Toast.makeText(getApplicationContext(), intent.getStringExtra("path"), Toast.LENGTH_LONG).show();
		}
	};


	@Override 
	public void onClick(View v) {
		Intent serviceIntent = new Intent(getApplicationContext(), PhotoListenerService.class);
		Intent bindServiceIntent = new Intent(getApplicationContext(), MusicService.class);
		if(v.getId() == R.id.start_service){
			startService(serviceIntent);
		}
		else if(v.getId() == R.id.stop_service){
			stopService(serviceIntent);
		}
		else if(v.getId() == R.id.start_binder_service){
			bindService(bindServiceIntent, this, Service.START_NOT_STICKY);
		}
		else if(v.getId() == R.id.stop_binder_service){
			unbindService(this);
		}
	}
	IMusicService mService;
	@Override
	public void onDestroy(){
		super.onDestroy();
		unregisterReceiver(photoListener);

	}
	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		mService  = IMusicService.Stub.asInterface(service);
		try{
			mService.setDataSource(0);
		}catch(RemoteException re){}

	}
	@Override
	public void onServiceDisconnected(ComponentName name) {
	}


}