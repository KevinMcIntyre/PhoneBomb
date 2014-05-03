package com.kevmac.phonebomb;

import java.io.IOException;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options, menu);
		return true;
	}
	
	String[] setTimes =new String[] {"5 seconds","10 seconds","15 seconds","20 seconds","30 seconds"};
	int selectedTime = 0;
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.timer){
			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			builder.setTitle("Set bomb timer:");
			builder.setCancelable(true);
			int time = selectedTime;
			
			builder.setSingleChoiceItems(setTimes, time, new DialogInterface.OnClickListener() {
	               
	              @Override
	              public void onClick(DialogInterface dialog,int which) {
	               selectedTime=which;
	               Toast.makeText(MainActivity.this,"You Select Letter "+setTimes[selectedTime],Toast.LENGTH_SHORT).show();
	                  dialog.dismiss();
	              }
	          });
			
			AlertDialog timer = builder.create();
			timer.show();
		}
		else if(item.getItemId() == R.id.about){
			Intent aboutIntent = new Intent(MainActivity.this, About.class);
			startActivity(aboutIntent);
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final Button redButton = (Button) findViewById(R.id.backButton);
		final Button exitButton = (Button) findViewById(R.id.button2);
		
		redButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setMessage("Are you sure you want to blow up your phone?!");
				builder.setCancelable(false);
				builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					
					@SuppressWarnings("deprecation")
					@Override
					public void onClick(DialogInterface dialog, int which) {
						exitButton.setVisibility(View.GONE);
						redButton.setEnabled(false);
						NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
						Notification notify = new Notification(android.R.drawable.stat_sys_warning, "RUN FOR COVER!!!", System.currentTimeMillis());
						Context context = MainActivity.this;
						CharSequence title = "Explosion is imminent!";
						CharSequence details = "Throw your phone at your target.";
						Intent intent = new Intent(context, MainActivity.class);
						PendingIntent pending = PendingIntent.getActivity(context, 0, intent, 0);
						notify.setLatestEventInfo(context, title, details, pending);
						nm.notify(0, notify);
						final MediaPlayer beep = MediaPlayer.create(MainActivity.this, R.raw.beep);
						final MediaPlayer bomb = MediaPlayer.create(MainActivity.this, R.raw.bomb);
						beep.setLooping(true);
					    try {
							beep.prepare();
						} catch (IllegalStateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					    beep.start();
					    CountDownTimer timer = new CountDownTimer(4000, 1000) {

					        @Override
					        public void onTick(long millisUntilFinished) {
					           // Nothing to do
					        }

					        @Override
					        public void onFinish() {
					            if (beep.isPlaying()) {
					                 beep.stop();
					                 beep.release();
					                 bomb.start();
					                 Toast toast = Toast.makeText(MainActivity.this, "Your phone has successfully exploded.", 5000);
					                 toast.setGravity(Gravity.CENTER_VERTICAL, 0, -120);
					                 toast.show();
					                 
					                 CountDownTimer exitTimer = new CountDownTimer(3000, 1000) {

									        @Override
									        public void onTick(long millisUntilFinished) {
									           // Nothing to do
									        }

									        @Override
									        public void onFinish() {
									        	exitButton.setVisibility(View.VISIBLE);
									        	redButton.setEnabled(true);
									        }
									    };
									    exitTimer.start();
					            }
					        }
					    };
					    timer.start();
					}
				});
				builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
		
		exitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
		
	}
}
