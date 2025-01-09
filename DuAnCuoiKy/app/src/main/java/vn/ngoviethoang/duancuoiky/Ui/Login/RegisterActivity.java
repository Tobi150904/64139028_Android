package vn.ngoviethoang.duancuoiky.Ui.Login;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import vn.ngoviethoang.duancuoiky.R;
import vn.ngoviethoang.duancuoiky.data.entity.User;
import vn.ngoviethoang.duancuoiky.data.repository.UserRepository;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText emailInput, passwordInput, confirmPasswordInput;
    private TextInputLayout emailInputLayout, passwordInputLayout, confirmPasswordInputLayout;
    private Button registerButton;
    private TextView loginLink;

    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        emailInputLayout = findViewById(R.id.emailInputLayout);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        confirmPasswordInputLayout = findViewById(R.id.confirmPasswordInputLayout);
        registerButton = findViewById(R.id.registerButton);
        loginLink = findViewById(R.id.loginLink);

        userRepository = new UserRepository(this);

        registerButton.setOnClickListener(v -> registerUser());
        loginLink.setOnClickListener(v -> finish());
    }

    private void registerUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();

        if (email.isEmpty()) {
            emailInputLayout.setError("Vui lòng nhập email");
            return;
        } else if (!email.contains("@")) {
            emailInputLayout.setError("Email không hợp lệ");
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

        userRepository.getUserByEmail(email).observe(this, user -> {
            if (user != null) {
                emailInputLayout.setError("Email đã được sử dụng");
            } else {
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setPassword(password);

                userRepository.insertUser(newUser);
                Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}