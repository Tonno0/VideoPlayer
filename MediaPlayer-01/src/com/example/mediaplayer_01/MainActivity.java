package com.example.mediaplayer_01;



import java.io.IOException;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends ActionBarActivity implements OnClickListener{
	private SurfaceView mSurface;
	private Button start,stop,pre;
	private MediaPlayer mediaPlayer;
	private Context mContext=null;
	private int position=0;
	public String tag="MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mContext=this;
		mSurface=(SurfaceView) findViewById(R.id.surface);
		start=(Button) findViewById(R.id.start);
		stop=(Button) findViewById(R.id.stop);
		pre=(Button) findViewById(R.id.pre);

		mediaPlayer=new MediaPlayer();
		mSurface.getHolder().setKeepScreenOn(true);
		SurfaceViewLis mv = new SurfaceViewLis();
		mSurface.getHolder().addCallback(mv);
		start.setOnClickListener(this);
		stop.setOnClickListener(this);
		pre.setOnClickListener(this);
	}
	
	public void onClick(View v) {

		switch(v.getId()){
		case R.id.start:
			try{
				//play();
				httpPlay();
			}catch(IllegalArgumentException e){
				e.printStackTrace();
			}catch (SecurityException e) {
				e.printStackTrace();
			}catch (IllegalStateException e) {
				e.printStackTrace();
			}
			break;
		case R.id.pre:
			if(mediaPlayer.isPlaying()){
				pre.setText("播放");
				mediaPlayer.pause();
			}else{
				pre.setText("暂停");
				mediaPlayer.setDisplay(mSurface.getHolder());
				mediaPlayer.start();
			}
			break;
		case R.id.stop:
			if(mediaPlayer.isPlaying()){
				mediaPlayer.stop();
			}
			break;
		default:
			break;
		}
	}
	//播放本地视频
	public void play() {
		position = 0;
		mediaPlayer.reset();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mediaPlayer.setDisplay(mSurface.getHolder());
		AssetFileDescriptor fd = null;
		try {
			fd = this.getAssets().openFd("video.flv");
			mediaPlayer.setDataSource(fd.getFileDescriptor(),
					fd.getStartOffset(), fd.getLength());
			mediaPlayer.prepare();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mediaPlayer.start();
	}
	
	//播放网络视频
	public void httpPlay() {
		//mediaPlayer.reset();
		String url = "http://172.16.3.149:4000/group1/M00/1E/20/rBADlVcY_RiAEnXxAF7LT0yFZvs712.mp4";
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			mediaPlayer.setDataSource(url);
			mediaPlayer.prepare(); 
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		mediaPlayer.start();
	}
	
	private class SurfaceViewLis implements SurfaceHolder.Callback {

		public void surfaceCreated(SurfaceHolder holder) {
			if(position==0){
				try{
					//播放本地视频
					//play();
					//播放网络视频
					httpPlay();
				}catch(IllegalArgumentException e){
					e.printStackTrace();
				}
			}else if(position>0){
				try {
					mediaPlayer.setDisplay(mSurface.getHolder());
					mediaPlayer.start();
				} catch (IllegalStateException e) {				
					e.printStackTrace();
				}
	        }
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			Log.d(tag, "surfaceChanged");
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			Log.d(tag, "surfaceDestroyed");
		}

	}

	@Override
	protected void onResume() {
		if (position > 0) {
			try {
				mediaPlayer.prepare();
				mediaPlayer.seekTo(position);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		if(mediaPlayer.isPlaying()){
			position=mediaPlayer.getCurrentPosition();
			Log.d(tag, "onPause mediaPlayer.pause()");
			mediaPlayer.pause();
		}
		super.onPause();
		
	}

	@Override
	protected void onDestroy() {
		if (mediaPlayer.isPlaying())
			mediaPlayer.stop();
		Log.d(tag, "onDestroy mediaPlayer.release() ");
		mediaPlayer.release();
		Log.d(tag, "mediaPlayer release: ");
		super.onDestroy();
	}

	
}
