package com.example.api;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText txtUrl;
    private TextView txtResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtUrl = findViewById(R.id.txtUrl);
        txtResponse = findViewById(R.id.txtResponse);
        Button btnSend = findViewById(R.id.btnSend);

        btnSend.setOnClickListener(view -> {
            String apiUrl = txtUrl.getText().toString();
            new Thread(() -> {
                try {
                    String response = sendRequest(apiUrl);
                    runOnUiThread(() -> txtResponse.setText(response));
                } catch (Exception e) {
                    runOnUiThread(() -> txtResponse.setText("Error: " + e.getMessage()));
                }
            }).start();
        });
    }

    private String sendRequest(String apiUrl) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder content = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        conn.disconnect();
        return content.toString();
    }
}
