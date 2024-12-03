package th.ngoviethoang.simplemath;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText numberA, numberB;
    private RadioButton radioAdd, radioSubtract, radioMultiply, radioDivide;
    private Button buttonCalculate;
    private TextView textResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numberA = findViewById(R.id.numberA);
        numberB = findViewById(R.id.numberB);
        radioAdd = findViewById(R.id.radioAdd);
        radioSubtract = findViewById(R.id.radioSubtract);
        radioMultiply = findViewById(R.id.radioMultiply);
        radioDivide = findViewById(R.id.radioDivide);
        buttonCalculate = findViewById(R.id.buttonCalculate);
        textResult = findViewById(R.id.textResult);

        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateResult();
            }
        });
    }

    private void calculateResult() {
        double a = Double.parseDouble(numberA.getText().toString());
        double b = Double.parseDouble(numberB.getText().toString());
        double result = 0;

        if (radioAdd.isChecked()) {
            result = a + b;
        } else if (radioSubtract.isChecked()) {
            result = a - b;
        } else if (radioMultiply.isChecked()) {
            result = a * b;
        } else if (radioDivide.isChecked()) {
            if (b != 0) {
                result = a / b;
            } else {
                textResult.setText("Không thể chia cho 0");
                return;
            }
        }

        textResult.setText("Kết quả: " + result);
    }
}