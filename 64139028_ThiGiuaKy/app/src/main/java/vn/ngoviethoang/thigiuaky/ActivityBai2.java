package vn.ngoviethoang.thigiuaky;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityBai2 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai2);

        ListView listView = findViewById(R.id.listView);
        String[] items = {"Java", "Python", "C++", "C#", "JavaScript", "PHP", "Ruby", "Swift", "Go", "Kotlin"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String content = items[position];
                Intent intent = new Intent(ActivityBai2.this, ItemActivity.class);
                intent.putExtra("content", content);
                startActivity(intent);
            }
        });
    }
}