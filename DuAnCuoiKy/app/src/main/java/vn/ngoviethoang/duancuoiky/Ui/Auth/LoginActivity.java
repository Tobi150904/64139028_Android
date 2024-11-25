package vn.ngoviethoang.duancuoiky.Ui.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import vn.ngoviethoang.duancuoiky.R;
import vn.ngoviethoang.duancuoiky.Ui.Dashboard.DashboardActivity;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText emailInput, passwordInput;
    private TextInputLayout emailInputLayout, passwordInputLayout;
    private Button loginButton;
    private TextView forgotPassword, registerLink;
    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 9001; // Request code for Google Sign-In

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Views
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        forgotPassword = findViewById(R.id.forgotPassword);
        registerLink = findViewById(R.id.registerLink);

        // Google Sign-In setup
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // Handle Login Button click
        loginButton.setOnClickListener(v -> loginUser());

        // Handle Forgot Password click
        forgotPassword.setOnClickListener(v -> {
            // TODO: Handle forgot password flow
            Toast.makeText(LoginActivity.this, "Forgot Password", Toast.LENGTH_SHORT).show();
        });

        // Handle Register Link click
        registerLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void loginUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Validate email and password
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

        // TODO: Call your authentication API here (e.g., Firebase Auth)
        // If login is successful, navigate to Dashboard
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    // Google Sign-In logic (if applicable)
    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            navigateToDashboard();
        }
    }

    private void navigateToDashboard() {
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }
}
