package com.example.variacion.quickoperations;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.media.RingtoneManager;
import android.media.Ringtone;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Random;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

public class OperationsMenu extends AppCompatActivity {

    int count = 0;
    public static int score = 0;
    int[] operrand = new int[4];
    String[] operrandString = new String[4];
    boolean[] operator = new boolean[4];
    boolean[] available = new boolean[4];
    Vector<Integer> inputOperrand = new Vector<>();
    final String saveFile = "saveOperations.save";
    FileOutputStream outputStream;
    FileInputStream inputStream;
    boolean finish = true;

    public int Result() {
        for (int i = 0; i < 4; ++i)
            available[i] = true;

        Random rand = new Random();

        int result = 0, randomOperator, randomNumberOfOperrand, randomPosition;

        for (int i = 0; i < 4; ++i) {
            randomOperator = rand.nextInt(2) + 1;
            operrand[i] = rand.nextInt(10) + 1;
            operator[i] = randomOperator != 1;
            if (operator[i])
                operrandString[i] = String.format(Locale.US, "-%d", operrand[i]);
            else
                operrandString[i] = String.format(Locale.US, "+%d", operrand[i]);
        }

        randomNumberOfOperrand = rand.nextInt(3) + 2;
        for (int i = 0; i < randomNumberOfOperrand; ++i) {
            do {
                randomPosition = rand.nextInt(4);
            } while (!available[randomPosition]);

            available[randomPosition] = false;

            if (operator[randomPosition])
                result -= operrand[randomPosition];
            else
                result += operrand[randomPosition];
        }

        return result;
    }

    public boolean CheckResult(int result) {
        if (inputOperrand.isEmpty() || inputOperrand.size() > 4)
            return false;

        int userResult = 0;
        for (int i = 0; i < inputOperrand.size(); ++i)
            if (operator[inputOperrand.get(i)])
                userResult -= operrand[inputOperrand.get(i)];
            else
                userResult += operrand[inputOperrand.get(i)];

        return result == userResult;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operations_menu);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TextView viewScore = (TextView) findViewById(R.id.scoreTextView);
        viewScore.setText(String.format(Locale.US, "%d", score));

        final ProgressBar countDownPrgBar = (ProgressBar)findViewById(R.id.countDownBar);
        count = 0;
        countDownPrgBar.setProgress(count);
        finish = true;
        final CountDownTimer countDown = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                ++count;
                countDownPrgBar.setProgress((int) count * 100 / (5000 / 1000));
            }
            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {
                ++count;
                countDownPrgBar.setProgress(100);
                TextView resultTxtView = (TextView) findViewById(R.id.resultView);
                resultTxtView.setText("YOU LOSE");
                int previousScore = 0;

                try {
                    inputStream = openFileInput("saveState.save");
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String getState = bufferedReader.readLine();
                    inputStream.close();
                    finish = getState.equals("0");
                } catch (Exception error) {
                    error.printStackTrace();
                }

                if (finish) {
                    try {
                        inputStream = openFileInput(saveFile);
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        String getSaveScore;
                        getSaveScore = bufferedReader.readLine();
                        inputStream.close();
                        if (getSaveScore == null)
                            previousScore = 0;
                        else
                            previousScore = Integer.parseInt(getSaveScore);
                    } catch (Exception error) {
                        error.printStackTrace();
                    }
                }

                if (score > previousScore) {
                    TextView highscore = (TextView) findViewById(R.id.viewHighScore);
                    highscore.setText("NEW HIGHSCORE");
                    if (finish) {
                        try {
                            outputStream = openFileOutput(saveFile, Context.MODE_PRIVATE);
                            outputStream.write(Integer.toString(score).getBytes());
                            outputStream.close();
                        } catch (Exception error) {
                            error.printStackTrace();
                        }
                    }
                    try {
                        outputStream = openFileOutput("saveState.save", Context.MODE_PRIVATE);
                        outputStream.write(" ".getBytes());
                        outputStream.close();
                    } catch (Exception error) {
                        error.printStackTrace();
                    }
                }
            }
        };

        countDown.start();
        TextView resultTextView = (TextView)findViewById(R.id.resultTextView);
        final int getResult = Result();
        resultTextView.setText(String.format(Locale.US, "%d", getResult));

        Button firstInt = (Button) findViewById(R.id.firstIntButton);
        Button secondInt = (Button) findViewById(R.id.secondIntButton);
        Button thirdInt = (Button) findViewById(R.id.thirdIntButton);
        Button fourthInt = (Button) findViewById(R.id.fouthIntButton);

        firstInt.setText(operrandString[0]);
        secondInt.setText(operrandString[1]);
        thirdInt.setText(operrandString[2]);
        fourthInt.setText(operrandString[3]);

        firstInt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputOperrand.add(0);
                if (CheckResult(getResult)) {
                    finish();
                    inputOperrand.clear();
                    ++score;
                    startActivity(getIntent());
                }
            }
        });

        secondInt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputOperrand.add(1);
                if (CheckResult(getResult)) {
                    finish();
                    inputOperrand.clear();
                    ++score;
                    startActivity(getIntent());
                }
            }
        });

        thirdInt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputOperrand.add(2);
                if (CheckResult(getResult)) {
                    finish();
                    inputOperrand.clear();
                    ++score;
                    startActivity(getIntent());
                }
            }
        });

        fourthInt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputOperrand.add(3);
                if (CheckResult(getResult)) {
                    finish();
                    inputOperrand.clear();
                    ++score;
                    startActivity(getIntent());
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        score = 0;
        Intent intent = new Intent(OperationsMenu.this, StartMenu.class);
        startActivity(intent);
    }
}
