package com.ariana.myapplication.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ariana.myapplication.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void onBtnGradeView(View view){
        Intent intent = new Intent(this, GradeActivity.class);
        startActivity(intent);
    }

    public void onBtnHealthView(View view){
        Intent intent = new Intent(this, QQHealthActivity.class);
        startActivity(intent);
    }
}
