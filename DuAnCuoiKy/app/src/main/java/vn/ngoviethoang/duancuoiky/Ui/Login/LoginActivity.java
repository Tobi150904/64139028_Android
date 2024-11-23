package vn.ngoviethoang.duancuoiky.Ui.Login;

package com.example.moneymanager.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moneymanager.R;
import com.example.moneymanager.ui.dashboard.DashboardActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button loginButton, googleSignInButton;
    private TextView forgotPassword, registerLink;
    private GoogleSignInClient mGoogleSignInClient;

    // Google Sign-In request code
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Khởi tạo các thành phần giao diện
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        googleSignInButton = findViewById(R.id.googleSignInButton);
        forgotPassword = findViewById(R.id.forgotPassword);
        registerLink = findViewById(R.id.registerLink);

        // Cấu hình Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Bắt sự kiện khi nhấn nút đăng nhập
        loginButton.setOnClickListener(v -> handleLogin());

        // Bắt sự kiện khi nhấn nút Google Sign-In
        googleSignInButton.setOnClickListener(v -> signInWithGoogle());

        // Bắt sự kiện khi nhấn liên kết đăng ký
        registerLink.setOnClickListener(v -> {
            // Chuyển đến màn hình đăng ký
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    // Xử lý đăng nhập thông thường
    private void handleLogin() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Kiểm tra dữ liệu đầu vào
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            // Hiển thị thông báo lỗi nếu có trường nhập liệu trống
            return;
        }

        // Thực hiện xác thực thông tin người dùng (giả sử xác thực thông qua Room Database hoặc API)
        if (email.equals("test@example.com") && password.equals("password")) {
            // Đăng nhập thành công, chuyển đến Dashboard
            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Thông báo lỗi nếu thông tin không hợp lệ
        }
    }

    // Xử lý đăng nhập bằng Google
    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // Nhận kết quả từ Google Sign-In
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    // Xử lý kết quả đăng nhập Google
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Đăng nhập thành công, chuyển đến Dashboard
            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        } catch (ApiException e) {
            // Xử lý lỗi khi đăng nhập Google
        }
    }
}

