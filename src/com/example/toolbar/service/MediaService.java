package com.example.toolbar.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;

import com.example.toolbar.common.utils.LogHelper;

/**
 * 播放音乐服务-需要传进一个url
 * 
 * @author hxc
 * 
 */
public class MediaService extends Service {
	private MediaPlayer player;
	private String path = "";
	private final LocalBinder binder = new LocalBinder();
	public static AudioAnimationListener mAnimationListener;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		LogHelper.e("MediaService_onBind");
		if (player == null) {
			player = new MediaPlayer();
		}
		if (intent != null && intent.getStringExtra("path") != null) {
			String temp = intent.getStringExtra("path");
			if (player.isPlaying()) {
				if (path.equals(temp)) {
					pause();
				} else {
					path = temp;
					player.stop();
					musicplay();
				}
			} else {
				path = temp;
				musicplay();
			}
		}

		return binder;
	}

	public class LocalBinder extends Binder {
		public MediaService getService() {
			return MediaService.this;
		}
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		player = new MediaPlayer();
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		if (player == null) {
			player = new MediaPlayer();
		}
		if (intent != null && intent.getStringExtra("path") != null) {
			String temp = intent.getStringExtra("path");
			if (player.isPlaying()) {
				if (path.equals(temp)) {
					pause();
				} else {
					path = temp;
					player.stop();
					musicplay();
				}
			} else {
				path = temp;
				musicplay();
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		stop();
		super.onDestroy();
	}

	private void musicplay() {

		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				try {
					if (path != null) {
						player.reset();
						player.setDataSource(path);
						player.prepare();
						player.start();
					}else{
						player = new MediaPlayer();
						player.reset();
						player.setDataSource(path);
						player.prepare();
						player.start();
						
					}

				} catch (Exception e) {

				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
			}
		}.execute();

		player.setOnCompletionListener(new OnCompletionListener() {
			public void onCompletion(MediaPlayer arg0) {
				StopAudio();
				try {
					mAnimationListener.stopAnimation();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}

		});

	}

	public void openMusic(String music_path) {

		if (player == null) {
			player = new MediaPlayer();
		}
		if (path != null) {
			if (player.isPlaying()) {
				if (path.equals(music_path)) {
					pause();
				} else {
					path = music_path;
					player.stop();
					musicplay();
				}
			} else {
				path = music_path;
				musicplay();
			}
		}
	}

	public void pause() {
		if (player.isPlaying())
			player.pause();
		else
			player.start();
	}

	public void stop() {
		if (player != null) {
			if (player.isPlaying()) {
				player.stop();
				player.release();
				player = null;
				StopAudio();
			}
		}

		try {
			if (mAnimationListener != null) {
				mAnimationListener.stopAnimation();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void StopAudio() {

		// if (TrafficTextList.handler != null) {
		// TrafficTextList.handler.sendEmptyMessage(0);
		// }
		//
		// if (TrafficTextDetail.hanlerAudio != null) {
		// TrafficTextDetail.hanlerAudio.sendEmptyMessage(0);
		// }
		//
		// if (TrafficDatialCommentAdapter.handler != null) {
		// TrafficDatialCommentAdapter.handler.sendEmptyMessage(0);
		// }
		//
		// if (TrafficOfficialCommentAdapter.handler != null) {
		// TrafficOfficialCommentAdapter.handler.sendEmptyMessage(0);
		// }
		//
		// if (TalkList.leftHandler != null) {
		// TalkList.leftHandler.sendEmptyMessage(0);
		// }
		//
		// if (TalkList.rightHandler != null) {
		// TalkList.rightHandler.sendEmptyMessage(0);
		// }
		//
		// if (TalkListAdapter.leftHandler != null) {
		// TalkListAdapter.leftHandler.sendEmptyMessage(0);
		// }
		//
		// if (TalkListAdapter.rightHandler != null) {
		// TalkListAdapter.rightHandler.sendEmptyMessage(0);
		// }
		//
		
		//是否播放语音
//		if (TalkListGroupAdapter.leftHandler != null) {
//			TalkListGroupAdapter.leftHandler.sendEmptyMessage(0);
//		}
//
//		if (TalkListGroupAdapter.rightHandler != null) {
//			TalkListGroupAdapter.rightHandler.sendEmptyMessage(0);
//		}
//		
		
		
		//
		// if (UserPrivateLetterList.handler != null) {
		// UserPrivateLetterList.handler.sendEmptyMessage(0);
		// }
		//
		// if (SendPrivateMessageList.leftHandler != null) {
		// SendPrivateMessageList.leftHandler.sendEmptyMessage(0);
		// }
		//
		// if (SendPrivateMessageList.rightHandler != null) {
		// SendPrivateMessageList.rightHandler.sendEmptyMessage(0);
		// }
		// if (AskWayDetial.hanlerAudio != null) {
		// AskWayDetial.hanlerAudio.sendEmptyMessage(0);
		// }
		//
		// if (AskWayList.handler != null) {
		// AskWayList.handler.sendEmptyMessage(0);
		// }
		//
		// if (ReportInfoList.handler != null) {
		// ReportInfoList.handler.sendEmptyMessage(0);
		// }

	}

	public void setAnimationListener(AudioAnimationListener listener) {
		mAnimationListener = listener;
	}

	/**
	 * 语音动画
	 * 
	 * @author hxc
	 * 
	 */
	public interface AudioAnimationListener {
		public void stopAnimation();
	}

}
