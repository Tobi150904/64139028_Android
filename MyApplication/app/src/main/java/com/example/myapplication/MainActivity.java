package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void chuyensangmanhinh2(View view) {
        Intent iMH2 = new Intent(MainActivity.this, MainActivity2.class);
        iMH2.putExtra("ten", "Ngo Viet Hoang");
        startActivity(iMH2);
    }
}
