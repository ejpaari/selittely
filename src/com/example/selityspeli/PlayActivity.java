package com.example.selityspeli;

import java.io.IOException;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.superjustus.selityspeli.R;

public class PlayActivity extends Activity {

	private int time;
	private long timeLeftInMilliseconds;
	private ProgressBar timeBar;
	private TextView timeLeftText;
	private TextView explainableText;
	private long interval = 60;
	private int passedWords = 0;
	private int explainedWords = 0;
	private String topic;
	ArrayList<String> selectedWords = new ArrayList<String>();
	private int wordsIndex = 0;
	private Button passButton;
	private Button nextButton;
	private Button okButton;
	private MediaPlayer audio;
	private CountDownTimer timer;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);
		
		timeBar = (ProgressBar) findViewById(R.id.bar_time_left);
		timeLeftText = (TextView) findViewById(R.id.text_time_left);
		explainableText = (TextView) findViewById(R.id.text_explainable);
		passButton = (Button) findViewById(R.id.button_pass);
		nextButton = (Button) findViewById(R.id.button_next);
		okButton = (Button) findViewById(R.id.button_ok);
		okButton.setVisibility(View.GONE);
		
		Intent intent = getIntent();
		time = intent.getIntExtra("com.example.myfirstapp.time", 30);
		topic = intent.getStringExtra("com.example.myfirstapp.topic");
				
		if (topic.equals("words")) {
			selectedWords.addAll(intent.getStringArrayListExtra("com.example.myfirstapp.wordsList"));
		} else if (topic.equals("people")) {
			selectedWords.addAll(intent.getStringArrayListExtra("com.example.myfirstapp.peopleList"));
		} else {
			explainableText.setText("Error: motherfucking snakes in this motherfucking plane");
		}		
		
		timeLeftInMilliseconds = (long) (time * 1000.0);		
		timeLeftText.setText("Aika: " + Integer.toString(time) + " s");
		showNextWord();
		
		timer = new CountDownTimer(timeLeftInMilliseconds, interval) {
			public void onTick(long millisUntilFinished) {
				timeLeftInMilliseconds -= interval;		
		    	timeLeftText.setText("Aika: " + String.format("%.2f", millisUntilFinished / 1000.0) + " s");
		    	int progress = (int) ((timeLeftInMilliseconds / ((long) time * 1000.0) * 100.0));
		    	timeBar.setProgress(progress);
			}

			public void onFinish() {
				endGame();
			}			
		}.start();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            onBackPressed();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public void onBackPressed() {
		timer.cancel();
		this.finish();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	public void ButtonNextOnClick(View view) {		
		++explainedWords;
		playSound(R.raw.right);
		showNextWord();
	}
	
	public void ButtonPassOnClick(View view) {
		++passedWords;
		playSound(R.raw.wrong);
		showNextWord();
	}
	
	public void ButtonOkOnClick(View view) {
		this.finish();
	}
	
	private void playSound(int id) {
		audio = MediaPlayer.create(this, id);
		audio.start();
	}
	
	private void showNextWord() {
		if (wordsIndex >= selectedWords.size()) {
			wordsIndex = 0;
		}
		explainableText.setText(selectedWords.get(wordsIndex));
		++wordsIndex;		
	}
	
	private void endGame() {
		playSound(R.raw.alarm);
		timeLeftText.setText("Aika: 0,00 s");
		timeBar.setProgress(0);
		passButton.setVisibility(View.GONE);
		nextButton.setVisibility(View.GONE);
		okButton.setVisibility(View.VISIBLE);		
		explainableText.setText(explainableText.getText() + "\n\nOikein: " + Integer.toString(explainedWords) + "\nOhi: " + Integer.toString(passedWords));
	}
}
