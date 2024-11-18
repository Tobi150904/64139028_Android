package vn.ngoviethoang.thigiuaky;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class ActivityBai3 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai3);

        ListView listView = findViewById(R.id.listView);
        List<Item> items = new ArrayList<>();
        items.add(new Item("Hoa hồng", R.drawable.image1));
        items.add(new Item("Hoa tulip", R.drawable.image2));
        items.add(new Item("Hoa hướng dương", R.drawable.image3));

        ItemAdapter adapter = new ItemAdapter(this, items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = items.get(position);
                Intent intent = new Intent(ActivityBai3.this, ItemActivity.class);
                intent.putExtra("content", item.getName());
                intent.putExtra("imageResId", item.getImageResId());
                startActivity(intent);
            }
        });
    }
}