package vn.ngoviethoang.thigiuaky;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
public class ActivityBai1 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai1);

        EditText etHeight = findViewById(R.id.et_height);
        EditText etWeight = findViewById(R.id.et_weight);
        Button btnCalculate = findViewById(R.id.btn_calculate);
        TextView tvResult = findViewById(R.id.tv_result);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String heightStr = etHeight.getText().toString();
                String weightStr = etWeight.getText().toString();

                if (!heightStr.isEmpty() && !weightStr.isEmpty()) {
                    float height = Float.parseFloat(heightStr) / 100;
                    float weight = Float.parseFloat(weightStr);
                    float bmi = weight / (height * height);
                    tvResult.setText("Kết quả: " + String.format("%.2f", bmi));
                } else {
                    tvResult.setText("Vui lòng nhập chiều cao và cân nặng hợp lệ");
                }
            }
        });
    }
}