package vn.ngoviethoang.thigiuaky;

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

    public void openCau1(View view) {
        startActivity(new Intent(this, ActivityBai1.class));
    }

    public void openCau2(View view) {
        startActivity(new Intent(this, ActivityBai2.class));
    }

    public void openCau3(View view) {
        startActivity(new Intent(this, ActivityBai3.class));
    }

    public void openCau4(View view) {
        startActivity(new Intent(this, ActivityBai4.class));
    }

}