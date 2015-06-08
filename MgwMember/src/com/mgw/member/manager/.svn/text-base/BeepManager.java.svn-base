/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mgw.member.manager;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;

import com.mgw.member.R;

/**
 * Manages beeps and vibrations for {@link CaptureActivity}.
 */
public final class BeepManager implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

	private static final String TAG = BeepManager.class.getSimpleName();

	private static final float BEEP_VOLUME = 0.10f;
	private static final long VIBRATE_DURATION = 200L;

	private final Activity activity;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private boolean vibrate;

	public BeepManager(Activity activity) {
		this.activity = activity;
		this.mediaPlayer = null;
		updatePrefs();
	}

	synchronized void updatePrefs() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
		playBeep = shouldBeep(prefs, activity);
		//vibrate = prefs.getBoolean(PreferencesActivity.KEY_VIBRATE, false);
		vibrate = prefs.getBoolean(PreferencesActivity.KEY_VIBRATE, true);
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = buildMediaPlayer(activity);
		}
	}

	/**
	 * 播放震动及音乐
	 */
	public synchronized void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		// TODO
//		if (vibrate) {
//			Vibrator vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
//			vibrator.vibrate(VIBRATE_DURATION);
//			//vibrator.vibrate(new long[]{300,500,500,50}, 0);
//			// 震动一次
//			// vibrator.vibrate(VIBRATE_DURATION);
//			// 第一个参数，指代一个震动的频率数组。每两个为一组，每组的第一个为等待时间，第二个为震动时间。
//			// 比如 [2000,500,100,400],会先等待2000毫秒，震动500，再等待100，震动400
//			// 第二个参数，repest指代从 第几个索引（第一个数组参数） 的位置开始循环震动。
//			// 会一直保持循环，我们需要用 vibrator.cancel()主动终止
//			// vibrator.vibrate(new long[]{300,500},0);
//
//		}
	}

	private static boolean shouldBeep(SharedPreferences prefs, Context activity) {
		// 如果当前是铃音模式，则继续准备下面的 蜂鸣提示音操作，如果是静音或者震动模式。就不要继续了。因为用户选择了无声的模式，我们就也不要出声了。
		boolean shouldPlayBeep = prefs.getBoolean(PreferencesActivity.KEY_PLAY_BEEP, true);
		if (shouldPlayBeep) {
			// See if sound settings overrides this
			AudioManager audioService = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
			if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
				shouldPlayBeep = false;
			}
		}
		return shouldPlayBeep;
	}

	/**
	 * 构建播放器
	 * 
	 * @param activity
	 * @return
	 */
	private MediaPlayer buildMediaPlayer(Context activity) {
		MediaPlayer mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mediaPlayer.setOnCompletionListener(this);
		mediaPlayer.setOnErrorListener(this);
		AssetFileDescriptor file = activity.getResources().openRawResourceFd(R.raw.beep);
		try {
			mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
			file.close();
			mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
			mediaPlayer.prepare();
		} catch (IOException ioe) {
			Log.w(TAG, ioe);
			mediaPlayer = null;
		}
		return mediaPlayer;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// When the beep has finished playing, rewind to queue up another one.
		mp.seekTo(0);
	}

	@Override
	public synchronized boolean onError(MediaPlayer mp, int what, int extra) {
		if (what == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
			// we are finished, so put up an appropriate error toast if required
			// and finish
			activity.finish();
		} else {
			// possibly media player error, so release and recreate
			mp.release();
			mediaPlayer = null;
			updatePrefs();
		}
		return true;
	}

}
