package com.example.thigiuaky;

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

    public void openBMICalculator(View view) {
        startActivity(new Intent(this, BmiActivity.class));
    }

    public void openProfile(View view) {
        startActivity(new Intent(this, ProfileActivity.class));
    }

    public void openListView(View view) {
        startActivity(new Intent(this, ListViewActivity.class));
    }

    public void openRecyclerView(View view) {
        startActivity(new Intent(this, RecyclerViewActivity.class));
    }
}