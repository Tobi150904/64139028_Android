package com.example.thigiuaky;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Student> students = new ArrayList<>();
        students.add(new Student("Student 1", 20, "12A", "Java, Android"));
        students.add(new Student("Student 2", 21, "12B", "Kotlin, SQL"));
        // Add more students as needed

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(students);
        recyclerView.setAdapter(adapter);
    }
}