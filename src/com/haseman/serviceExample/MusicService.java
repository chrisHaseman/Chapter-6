package com.haseman.serviceExample;

import java.lang.ref.WeakReference;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MusicService extends Service{

	private void pause(){
	}
	private void play(){
	}
	
	public void setDataSource(long id){	
	}
	public String getSongTitle(){
		return null;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	 static class MusicServiceStub extends IMusicService.Stub {
	        WeakReference<MusicService> mService;
	        
	        MusicServiceStub(MusicService service) {
	            mService = new WeakReference<MusicService>(service);
	        }
	        public void pause(){
	        	mService.get().pause();
	        }
	        public void play(){
	        	mService.get().play();
	        }
	        public void setDataSource(long id){
	        	mService.get().setDataSource(id);
	        }
	        public String getSongTitle(){
	        	return mService.get().getSongTitle();
	        }
	 }
	 private final IBinder mBinder = new MusicServiceStub(this);
}
