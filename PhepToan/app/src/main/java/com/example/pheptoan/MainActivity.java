package com.example.pheptoan;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    Button bCong, bTru, bNhan, bChia;
    EditText edtTextA, edtTextB;
    TextView txtKetQua;

    void getControl() {
        bCong = findViewById(R.id.button_add);
        bTru = findViewById(R.id.button_subtract);
        bNhan = findViewById(R.id.button_multiply);
        bChia = findViewById(R.id.button_divide);
        edtTextA = findViewById(R.id.number1);
        edtTextB = findViewById(R.id.number2);
        txtKetQua = findViewById(R.id.result);
    }
    public void XulyCong(View view) {
        int a = Integer.parseInt(edtTextA.getText().toString());
        int b = Integer.parseInt(edtTextB.getText().toString());
        int c = a + b;
        txtKetQua.setText(c);
    }

    public void XulyTru(View view) {
        int a = Integer.parseInt(edtTextA.getText().toString());
        int b = Integer.parseInt(edtTextB.getText().toString());
        int c = a - b;
        txtKetQua.setText(c);
    }

    public void XulyNhan(View view) {
        int a = Integer.parseInt(edtTextA.getText().toString());
        int b = Integer.parseInt(edtTextB.getText().toString());
        int c = a * b;
        txtKetQua.setText(c);
    }

    public void XulyChia(View view) {
        int a = Integer.parseInt(edtTextA.getText().toString());
        int b = Integer.parseInt(edtTextB.getText().toString());
        if (b == 0) {
            txtKetQua.setText("Không thể chia cho 0");
        } else {
            int c = a / b;
            txtKetQua.setText(c);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        getControl();
        bCong.setOnClickListener(langNgheCong);
        bTru.setOnClickListener(langNgheTru);
        bNhan.setOnClickListener(langNgheNhan);
        bChia.setOnClickListener(langNgheChia);
    }

    View.OnClickListener langNgheCong = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            XulyCong(v);
        }
    };
    View.OnClickListener langNgheTru = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            XulyTru(v);
        }
    };
    View.OnClickListener langNgheNhan = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            XulyNhan(v);
        }
    };
    View.OnClickListener langNgheChia = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            XulyChia(v);
        }
    };
}