package com.example.healthrider;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button bt_microphone, bt_guid;
    TextView textView;
    TextToSpeech textToSpeech;
    healthRiderBace healthRiderBace;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_microphone = (Button) findViewById(R.id.buttonMicP);
        bt_guid = (Button) findViewById(R.id.button3);
        textView = (TextView) findViewById(R.id.textView2);

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS){
                    int lang = textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });

        bt_guid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guid();
            }
        });



    }

    public void getSpeechInput(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, 10);
        }
        else{
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        assert result != null;
        textView.setText(result.get(0));


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void openhealthRiderBaceActivity() {
        Intent intent = new Intent(this, healthRiderBace.class);
        startActivity(intent);

    }

    public void guid(){
        Calendar calender = Calendar.getInstance();
        int currentHour = calender.get(Calendar.HOUR_OF_DAY);
        String Greeting = "";
        if (currentHour <= 12){
            Greeting = "Hello...Good morning, Sir...";
        }
        else{
            Greeting = "Hello...Good evening, Sir...";
        }
        String text1 = "I'm your health guider, Rudy";
        textToSpeech.speak(Greeting + text1, TextToSpeech.QUEUE_ADD, null);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                String Hspeeh1 = textView.getText().toString();
                if (textSearch(Hspeeh1, "morning", "evening", "hello", "hi")){
                    textToSpeech.speak("Thank you... sir...Can I know your name?", TextToSpeech.QUEUE_ADD, null);
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            String Hspeeh2 = textView.getText().toString();
                            final String[] list2 = Hspeeh2.split(" ");
                            textToSpeech.speak("You're welcome..." + list2[list2.length -1], TextToSpeech.QUEUE_ADD, null);
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    textToSpeech.speak(list2[list2.length -1] + "..., you have three options...First one...you can go with me...Second one you can choose. what your prefer level...Third one... you can have full authority", TextToSpeech.QUEUE_ADD, null);
                                    new Timer().schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            String Hspeeh2 = textView.getText().toString();
                                            if (textSearch(Hspeeh2, "with", "1", "one", "first")){
                                                textToSpeech.speak( "Your choice is good as a beginner", TextToSpeech.QUEUE_ADD, null);
                                                testRun(list2[list2.length -1]);
                                            }
                                            else if (textSearch(Hspeeh2, "level", "second", "two", "option")){
                                                textToSpeech.speak( "What is your prefer level?", TextToSpeech.QUEUE_ADD, null);
                                            }
                                            else if (textSearch(Hspeeh2, "authority", "without", "third", "three")){
                                                textToSpeech.speak( "If that is your choice...I'll open full control panel to you...", TextToSpeech.QUEUE_ADD, null);
                                                openhealthRiderBaceActivity();
                                            }
                                            else{
                                                textToSpeech.speak( "Hey..." + list2[list2.length -1] + "you need to give me an answer", TextToSpeech.QUEUE_ADD, null);
                                            }
                                        }}, 15000);
                                }}, 2000);
                        }}, 10000);
                }

            }}, 10000);
    }

    public boolean textSearch(String text, String search1, String search2, String search3, String search4){
        String[] list = text.split(" ");
        boolean run = false;
        for(String word: list){
            if (word.equals(search1) || word.equals(search2) || word.equals(search3) || word.equals(search4)){
                run = true;
            }
        }
        return run;
    }
    public void testRun(String name){
        textToSpeech.speak( name + "...I want to get... somewhat understand about you health...Do you mind it", TextToSpeech.QUEUE_ADD, null);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                String Hspeeh3 = textView.getText().toString();
                if(textSearch(Hspeeh3, "yes", "like", "actually", "clearly")){
                    openhealthRiderBaceActivity();
                    textToSpeech.speak("Sir...speed is 1.2m/s,and...track angle is 0 degree...on the test run... ...Wear the heart rate meter... and start walking", TextToSpeech.QUEUE_ADD, null);
                }
            }}, 12000);
    }
}