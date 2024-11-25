package vn.ngoviethoang.duancuoiky.Ui.Auth;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import vn.ngoviethoang.duancuoiky.R;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText emailInput, passwordInput, confirmPasswordInput;
    private TextInputLayout emailInputLayout, passwordInputLayout, confirmPasswordInputLayout;
    private Button registerButton;
    private TextView loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Views
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        emailInputLayout = findViewById(R.id.emailInputLayout);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        confirmPasswordInputLayout = findViewById(R.id.confirmPasswordInputLayout);
        registerButton = findViewById(R.id.registerButton);
        loginLink = findViewById(R.id.loginLink);

        // Handle Register Button click
        registerButton.setOnClickListener(v -> registerUser());

        // Handle Login Link click
        loginLink.setOnClickListener(v -> {
            // Navigate to login screen
            finish(); // Finish RegisterActivity to go back to LoginActivity
        });
    }

    private void registerUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();

        // Validate fields
        if (email.isEmpty()) {
            emailInputLayout.setError("Vui lòng nhập email");
            return;
        } else {
            emailInputLayout.setError(null);
        }

        if (password.isEmpty() || password.length() < 6) {
            passwordInputLayout.setError("Mật khẩu phải có ít nhất 6 ký tự");
            return;
        } else {
            passwordInputLayout.setError(null);
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordInputLayout.setError("Mật khẩu không khớp");
            return;
        } else {
            confirmPasswordInputLayout.setError(null);
        }

        // TODO: Call your registration API here
        // If registration is successful
        Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
        finish(); // Navigate back to LoginActivity
    }
}
