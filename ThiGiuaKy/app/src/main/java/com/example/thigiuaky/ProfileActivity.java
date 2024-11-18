package com.example.thigiuaky;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ImageView profileImage = findViewById(R.id.profile_image);
        TextView profileName = findViewById(R.id.tv_profile_name);
        TextView profileAge = findViewById(R.id.tv_profile_age);
        TextView profileClass = findViewById(R.id.tv_profile_class);
        TextView profileSkills = findViewById(R.id.tv_profile_skills);
        Button editProfileButton = findViewById(R.id.btn_edit_profile);

        // Set profile data
        profileName.setText("Name: John Doe");
        profileAge.setText("Age: 20");
        profileClass.setText("Class: 12A");
        profileSkills.setText("Skills: Java, Android, SQL");

        // Set edit profile button click listener
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle edit profile button click
            }
        });
    }
}