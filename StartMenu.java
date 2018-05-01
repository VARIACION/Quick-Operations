package com.example.variacion.quickoperations;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.content.pm.ActivityInfo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Locale;

public class StartMenu extends AppCompatActivity {

    InputStream inputStream;
    OutputStream outputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Button startBtn = (Button) findViewById(R.id.startButton);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartMenu.this, OperationsMenu.class);
                startActivity(intent);
            }
        });

        final Button introBtn = (Button) findViewById(R.id.optionsButton);
        introBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartMenu.this, OptionsMenu.class);
                startActivity(intent);
            }
        });

        Button quitBtn = (Button) findViewById(R.id.quitButton);
        quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });

        TextView basicInformationTxtView = (TextView) findViewById(R.id.basicInformationTextView);
        basicInformationTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.facebook.com/anhminh0412");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        TextView highscoreView = (TextView) findViewById(R.id.lastHighScoreTextView);
        String lastHighScore = "0";
        try {
            inputStream = openFileInput("saveOperations.save");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            lastHighScore = bufferedReader.readLine();
            inputStream.close();
        }
        catch (Exception error) {
            error.printStackTrace();
            lastHighScore = "0";
        }

        int printLastHighScore = 0;
        try {
            printLastHighScore = Integer.parseInt(lastHighScore);
        } catch (Exception error) {
            error.printStackTrace();
            printLastHighScore = 0;
        }

        highscoreView.setText(String.format(Locale.US, "Highscore: %d", printLastHighScore));

        String state = "0";
        try {
            outputStream = openFileOutput("saveState.save", Context.MODE_PRIVATE);
            outputStream.write(state.getBytes());
            outputStream.close();
        } catch (Exception error) {
            error.printStackTrace();
        }
    }
}
