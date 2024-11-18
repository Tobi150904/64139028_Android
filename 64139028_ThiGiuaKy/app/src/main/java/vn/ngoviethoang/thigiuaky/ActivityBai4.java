package vn.ngoviethoang.thigiuaky;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
public class ActivityBai4 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai4);

        TextView title = findViewById(R.id.title);
        ImageView profileImage = findViewById(R.id.profile_image);
        TextView profileName = findViewById(R.id.tv_profile_name);
        TextView profileAge = findViewById(R.id.tv_profile_age);
        TextView profileClass = findViewById(R.id.tv_profile_class);
        TextView profileSkills = findViewById(R.id.tv_profile_skills);

        // Set profile data
        title.setText("Thông tin cá nhân");
        profileImage.setImageResource(R.drawable.img);
        profileName.setText("Tên: Ngô Việt Hoàng");
        profileAge.setText("Năm sinh: 2004");
        profileClass.setText("Lớp: 64139028");
        profileSkills.setText("Ngôn ngữ lập trình: C, Java, Css, JavaScript");
    }
}
