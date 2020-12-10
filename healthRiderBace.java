package com.example.healthrider;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Delayed;

public class healthRiderBace extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView timer, tx2, tx_Speed, tx_Step, tx_Calories, tx_Distance, tx_angle;
    Button start, stop, reset, bt_save, bt_log, bt_guid;
    EditText et_userName, et_age, et_weight, et_height, gender, et_login;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L;
    Handler handler;
    int Seconds, Minutes, MilliSeconds;
    String level;
    DatabaseHelper databaseHelper;
    Spinner spinner;
    private String Gender, UserGender;
    private float angle, rpm, UserAge, UserWeight, UserHeight;
    ProgressBar progressBar;
    SeekBar seekBar, seekBarRPM;
    TextToSpeech textToSpeech;
    private static Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_rider_bace);


        BarChart barChart = findViewById(R.id.barChart);
        ArrayList<BarEntry> dailydetails = new ArrayList<>();
        timer = (TextView) findViewById(R.id.textTimer);
        start = (Button) findViewById(R.id.timerstart);
        stop = (Button) findViewById(R.id.timerpause);
        reset = (Button) findViewById(R.id.timerrestart);
        et_userName = (EditText) findViewById(R.id.username);
        et_age = (EditText) findViewById(R.id.age);
        et_weight = (EditText) findViewById(R.id.weight);
        et_height = (EditText) findViewById(R.id.height);
        et_login = (EditText) findViewById(R.id.loginText);
        bt_save =(Button) findViewById(R.id.buttonSave);
        bt_guid = (Button) findViewById(R.id.buttonGuid);
        bt_log = (Button) findViewById(R.id.button6);
        tx_Step = (TextView) findViewById(R.id.textStep);
        tx_Speed = (TextView) findViewById(R.id.textSpeed);
        tx_Calories = (TextView) findViewById(R.id.textCalories);
        tx_Distance = (TextView) findViewById(R.id.textDistance);
        tx_angle = (TextView) findViewById(R.id.textangle);
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
        handler = new Handler();
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBarRPM = (SeekBar) findViewById(R.id.seekBar4);
        databaseHelper = new DatabaseHelper(this);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS){
                    int lang = textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartTime = SystemClock.uptimeMillis();
                handler.postDelayed(runnable,0);
                reset.setEnabled(false);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeBuff += MillisecondTime;
                handler.removeCallbacks(runnable);
                reset.setEnabled(true);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                MillisecondTime = 0L;
                StartTime = 0L;
                TimeBuff = 0L;
                Seconds = 0;
                Minutes = 0;
                MilliSeconds = 0;
                timer.setText("00:00:00");
            }
        });

        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readTable1();
            }
        });

        bt_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_login.getText().toString();
                Cursor res = databaseHelper.getAllData("TABLE1");
                while (res.moveToNext()){
                    if(res.getString(1).equals(name)){
                        UserWeight = Float.parseFloat(res.getString(2));
                        UserHeight = Float.parseFloat(res.getString(3));
                        UserAge = Float.parseFloat(res.getString(4));
                        UserGender = res.getString(5);
                        showMessage("Successful...", "You are Successfully logged");

                    }
                }

            }
        });

        bt_guid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                h_testRun();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                angle = progress;
                float angle_number = 0;
                angle_number = Float.parseFloat(String.format("%.3f", 4*angle - 20));
                tx_angle.setText(String.valueOf(angle_number));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarRPM.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float speed = 0;
                rpm = progress;
                speed = Float.parseFloat(String.format("%.3f", (300 * rpm * 3.14 * 0.02) / 30));
                tx_Speed.setText(String.valueOf(speed));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        progressBar.setProgress(50);

    }

    public Runnable runnable = new Runnable() {
        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        @Override
        public void run() {
            MillisecondTime = SystemClock.uptimeMillis() - StartTime;
            UpdateTime = TimeBuff + MillisecondTime;
            Seconds = (int) (UpdateTime/1000);
            calculate(Seconds);
            Minutes = Seconds/60;
            Seconds = Seconds%60;
            MilliSeconds = (int) (UpdateTime%1000);
            timer.setText(" " + Minutes + " : " + String.format("%02d", Seconds) + " : " + String.format("%03d", MilliSeconds));
            handler.postDelayed(this, 0);
        }
    };

    public void saveTable1(){
        String name = et_userName.getText().toString();
        float age = Float.parseFloat(et_age.getText().toString());
        float height = Float.parseFloat(et_height.getText().toString());
        float weight = Float.parseFloat(et_weight.getText().toString());

        String Uname = "";
        Cursor res = databaseHelper.getAllData("TABLE1");
        while (res.moveToNext()){
            if(res.getString(1).equals(name)){
                Uname = name;
            }
        }
        if (Uname.equals(name)){
            showMessage("Error...", "You Entered User Name already exist");
        }
        else{
            databaseHelper.insertData(name, weight, height, age, Gender);
            showMessage("Successful...", "Detail are Entered");
        }

    }

    public void readTable1(){
        Cursor res = databaseHelper.getAllData("TABLE1");
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()){
            buffer.append("ID :"+ res.getString(0)+"\n");
            buffer.append("Name :"+ res.getString(1)+"\n");
            buffer.append("Weight(kg) :"+ res.getString(2)+"\n");
            buffer.append("Height(m) :"+ res.getString(3)+"\n");
            buffer.append("Age :"+ res.getString(4)+"\n");
            buffer.append("Gender :"+ res.getString(5)+"\n");
        }
        showMessage("Data",buffer.toString());
    }
    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
        if (text.equals("Male")){
            Gender = "Male";
        }
        else if(text.equals("Female")){
            Gender = "Female";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {


    }

    @SuppressLint("DefaultLocale")
    public void calculate(int timeS){
        String name = et_login.getText().toString();
        float CFF = 0, calories = 0;
        float angle = Float.parseFloat(tx_angle.getText().toString());
        float speed = Float.parseFloat(tx_Speed.getText().toString());
        float distance = speed*timeS;
        float step = (distance*250)/(speed*UserHeight*52);
        float MHR = (float) (208 - (0.7 * UserAge));
        float RHR = (float) 78.2;
        float VO2max = (float) (15.3 * (MHR / RHR));
        if (VO2max >= 56){
            CFF = 1;
        }
        else if ((56 >= VO2max) && (VO2max >= 54)){
            CFF = (float) 1.01;
        }
        else if ((54 >= VO2max) && (VO2max >= 52)){
            CFF = (float) 1.02;
        }
        else if ((52 >= VO2max) && (VO2max >= 50)){
            CFF = (float) 1.03;
        }
        else if ((50 >= VO2max) && (VO2max >= 48)){
            CFF = (float) 1.04;
        }
        else if ((48 >= VO2max) && (VO2max >= 46)){
            CFF = (float) 1.05;
        }
        else if ((46 >= VO2max) && (VO2max >= 42)){
            CFF = (float) 1.06;
        }
        else {
            CFF = (float) 1.07;
        }

        float TF = 0;

        if ((-20 <= angle) && (angle <= -15)){
            calories = (float) ((((-0.01 * angle) + 0.5) * UserWeight + TF) * distance * CFF*0.001);
        }
        else if ((-15 <= angle) && (angle <= -10)){
            calories = (float) ((((-0.02 * angle) + 0.35) * UserWeight + TF) * distance * CFF*0.001);
        }
        else if ((-10 <= angle) && (angle <= 0)){
            calories = (float) ((((0.04 * angle) + 0.95) * UserWeight + TF) * distance * CFF*0.001);
        }
        else if ((0 <= angle) && (angle <= 10)){
            calories = (float) ((((0.05 * angle) + 0.95) * UserWeight + TF) * distance * CFF*0.001);
        }
        else if ((10 <= angle) && (angle <= 15)){
            calories = (float) ((((0.07 * angle) + 0.75) * UserWeight + TF) * distance * CFF*0.001);
        }
        else{
            calories = (float) ((((0.09 * angle) + 0.55) * UserWeight + TF) * distance * CFF*0.001);
        }
        tx_Calories.setText(String.format("%.2f",calories));
        tx_Step.setText(String.format("%.2f", step));
        tx_Distance.setText(String.format("%.2f", distance));

        String date = "a";
        Calendar calender = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance().format(calender.getTime());
        Cursor res = databaseHelper.getAllData("TABLE2");
        while (res.moveToNext()){
            if(!res.getString(2).equals(currentDate)){

            }
        }


        //tx_Speed.setText(String.valueOf(speed));
    }

    public void h_testRun(){
        seekBar.setProgress(5);
        seekBarRPM.setProgress(2);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                textToSpeech.speak("Now, We can start sir", TextToSpeech.QUEUE_ADD, null);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        StartTime = SystemClock.uptimeMillis();
                        handler.postDelayed(runnable,0);
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                TimeBuff += MillisecondTime;
                                handler.removeCallbacks(runnable);
                                reset.setEnabled(true);
                                textToSpeech.speak("Time up, Sir... I'll tell you... what is your level after calculation...please... wait about 2 seconds", TextToSpeech.QUEUE_ADD, null);
                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        List<String> list = new ArrayList<>();
                                        list.add("92.5");
                                        list.add("87.5");
                                        list.add("83.4");
                                        list.add("76.8");
                                        list.add("60.5");
                                        list.add("59.21");
                                        int index = random.nextInt(list.size());
                                        float heartRate = Float.parseFloat(list.get(index));
                                        if (heartRate >= 90){
                                            level = "Level1";
                                        }
                                        else if ((90 >= heartRate) && (heartRate >= 85 )){
                                            level = "Level2";
                                        }
                                        else if ((85 >= heartRate) && (heartRate >= 80 )){
                                            level = "Level3";
                                        }
                                        else if ((80 >= heartRate) && (heartRate >= 72 )){
                                            level = "Level4";
                                        }
                                        else if ((72 >= heartRate) && (heartRate >= 60 )){
                                            level = "Level5";
                                        }
                                        else{
                                            level = "Level1";
                                        }
                                        textToSpeech.speak("You have been selected to " + level, TextToSpeech.QUEUE_ADD, null);
                                    }},2000);
                            }}, 180005);
                    }}, 2000);
            }}, 1000);

    }

    public void h_Level(){
    }


}