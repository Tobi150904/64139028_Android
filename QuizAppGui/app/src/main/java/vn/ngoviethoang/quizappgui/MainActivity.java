package vn.ngoviethoang.quizappgui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.cppCard).setOnClickListener(v -> openActivity(CppActivity.class));
        findViewById(R.id.javaCard).setOnClickListener(v -> openActivity(JavaActivity.class));
        findViewById(R.id.pythonCard).setOnClickListener(v -> openActivity(PythonActivity.class));
    }

    private void openActivity(Class<?> activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }
}