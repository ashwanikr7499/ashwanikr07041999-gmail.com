package com.example.kisan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CardView cardweather=findViewById(R.id.cardweather);
        CardView cardaskexpert=findViewById(R.id.cardaskexpert);
        CardView cardupload=findViewById(R.id.cardupload);
        CardView cardmycrops=findViewById(R.id.cardmycrops);

        cardweather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this, Weather.class);
                startActivity(i);
            }
        });
        cardaskexpert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this, Askexpert.class);
                startActivity(i);

            }
        });
        cardupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this, Upload.class);
                startActivity(i);

            }
        });
        cardmycrops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this, ShowImagesActivity.class);
                startActivity(i);

            }
        });

    }
}
