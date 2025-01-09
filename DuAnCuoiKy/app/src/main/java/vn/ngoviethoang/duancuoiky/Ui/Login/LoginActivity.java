package vn.ngoviethoang.duancuoiky.Ui.Login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import vn.ngoviethoang.duancuoiky.R;
import vn.ngoviethoang.duancuoiky.Ui.Dashboard.DashboardActivity;
import vn.ngoviethoang.duancuoiky.data.repository.UserRepository;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText emailInput, passwordInput;
    private TextInputLayout emailInputLayout, passwordInputLayout;
    private Button loginButton;
    private TextView registerLink;

    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        emailInputLayout = findViewById(R.id.emailInputLayout);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        loginButton = findViewById(R.id.loginButton);
        registerLink = findViewById(R.id.registerLink);

        userRepository = new UserRepository(this);

        loginButton.setOnClickListener(v -> loginUser());
        registerLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void loginUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

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

        userRepository.getUserByEmailAndPassword(email, password).observe(this, user -> {
            if (user != null) {
                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                intent.putExtra("USER_ID", user.getId());
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Sai email hoặc mật khẩu", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
