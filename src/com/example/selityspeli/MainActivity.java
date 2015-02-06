package com.example.selityspeli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.content.res.AssetManager;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MainActivity extends Activity 
{	
	private final int NUM_SELECTED_WORDS = 100;
	
	private SeekBar bar;
	private TextView timeText;
	private RadioButton wordsRadio;
	private RadioButton peopleRadio;
	private int time = 0;
	private DatabaseAdapter databaseHelper;
	ArrayList<String> wordsList = new ArrayList<String>();
	ArrayList<String> peopleList = new ArrayList<String>();
	Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
    	//openDatabase();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        timeText = (TextView) findViewById(R.id.time);
        bar = (SeekBar) findViewById(R.id.set_time);
        wordsRadio = (RadioButton) findViewById(R.id.radio_words);
        peopleRadio = (RadioButton) findViewById(R.id.radio_people);
        random = new Random();
        
        timeText.setText(Integer.toString(time) + " s");
        wordsRadio.setChecked(true);
        peopleRadio.setChecked(false);
        
        wordsList = readFile("words.txt");
        peopleList = readFile("people.txt");
        
        bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
        {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				time = progress;
				timeText.setText(Integer.toString(progress) + " s");
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}        	
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_info:
            	showInfo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    public void onRadioButtonClicked(View view) {        
        switch(view.getId()) {
            case R.id.radio_words:
                wordsRadio.setChecked(true);
                peopleRadio.setChecked(false);
                break;
            case R.id.radio_people:
            	wordsRadio.setChecked(false);
                peopleRadio.setChecked(true);
                break;
        }
    }
      
    public void start(View view) {
    	Intent intent = new Intent(this, PlayActivity.class);
    	intent.putExtra("com.example.myfirstapp.time", time);
    	intent.putExtra("com.example.myfirstapp.topic", wordsRadio.isChecked() ? "words" : "people");
    	
    	if (wordsRadio.isChecked()) {
    		ArrayList<String> selectedWords = new ArrayList<String>();
    		int max = wordsList.size() / NUM_SELECTED_WORDS;
    		int index = 0;
    		for (int i = 0; i < NUM_SELECTED_WORDS; ++i) {
    			index += Math.max(1, random.nextInt(max));
    			selectedWords.add(wordsList.get(index));
    		}
    		Collections.shuffle(selectedWords);
    		intent.putStringArrayListExtra("com.example.myfirstapp.wordsList", selectedWords); 
    	} else {
    		ArrayList<String> selectedWords = new ArrayList<String>();
    		int max = peopleList.size() / NUM_SELECTED_WORDS;
    		int index = 0;
    		for (int i = 0; i < NUM_SELECTED_WORDS; ++i) {
    			index += Math.max(1, random.nextInt(max));
    			selectedWords.add(peopleList.get(index));
    		}
    		Collections.shuffle(selectedWords);
    		intent.putStringArrayListExtra("com.example.myfirstapp.peopleList", selectedWords);
    	}
    	
    	startActivity(intent);
    }
    
    private void showInfo() {
    	Intent intent = new Intent(this, InfoActivity.class);
    	startActivity(intent);
    }
    
	private ArrayList<String> readFile(String filename) {
		AssetManager assetManager = getAssets();
		ArrayList<String> list = new ArrayList<String>();
			
        try {
        	String line = null;
        	InputStream input = assetManager.open(filename);        	
        	BufferedReader br = new BufferedReader(new InputStreamReader(input, "UTF-8"));
        	        	
        	while ((line = br.readLine()) != null) {
                list.add(line);
            }
        	
        	br.close();
        	input.close();        	
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
        	System.out.println(e);
        }
        
        return list;
	}
	
    private void openDatabase() {
        databaseHelper = new DatabaseAdapter(this);
        databaseHelper.open();
    }
}
