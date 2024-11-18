package vn.ngoviethoang.thigiuaky;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ItemActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        ImageView imageView = findViewById(R.id.imageView);
        TextView textView = findViewById(R.id.textView);

        String content = getIntent().getStringExtra("content");
        int imageResId = getIntent().getIntExtra("imageResId", 0);

        textView.setText(content);
        imageView.setImageResource(imageResId);
    }
}