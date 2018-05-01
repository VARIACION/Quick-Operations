package com.example.variacion.quickoperations;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.OutputStream;

public class OptionsMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_menu);

        Button resetBtn = (Button) findViewById(R.id.resetButton);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    OutputStream outputStream = openFileOutput("saveOperations.save", Context.MODE_PRIVATE);
                    outputStream.close();
                } catch (Exception error) {
                    error.printStackTrace();
                }
            }
        });

        Button aboutBtn = (Button) findViewById(R.id.aboutButton);
        aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OptionsMenu.this, AboutMenu.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(OptionsMenu.this, StartMenu.class);
        startActivity(intent);
    }
}
