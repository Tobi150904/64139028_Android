package vn.ngoviethoang.bmi_basic;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText height, weight;
    private CheckBox checkBoxAsian;
    private Button buttonCalculate;
    private TextView textResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);
        checkBoxAsian = findViewById(R.id.checkBoxAsian);
        buttonCalculate = findViewById(R.id.buttonCalculate);
        textResult = findViewById(R.id.textResult);

        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateBMI();
            }
        });
    }

    private void calculateBMI() {
        double heightValue = Double.parseDouble(height.getText().toString()) / 100;
        double weightValue = Double.parseDouble(weight.getText().toString());
        double bmi = weightValue / (heightValue * heightValue);
        String result;

        if (checkBoxAsian.isChecked()) {
            if (bmi < 17.5) {
                result = "Thiếu cân";
            } else if (bmi < 22.99) {
                result = "Bình thường";
            } else if (bmi < 27.99) {
                result = "Thừa cân";
            } else {
                result = "Béo phì";
            }
        } else {
            if (bmi < 18.5) {
                result = "Thiếu cân";
            } else if (bmi < 24.99) {
                result = "Bình thường";
            } else if (bmi < 29.99) {
                result = "Thừa cân";
            } else {
                result = "Béo phì";
            }
        }

        textResult.setText(String.format("Kết quả: %.2f - %s", bmi, result));
    }
}