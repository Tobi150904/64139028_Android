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

    // Khai báo các thành phần giao diện
    private TextInputEditText emailInput, passwordInput, confirmPasswordInput;
    private TextInputLayout emailInputLayout, passwordInputLayout, confirmPasswordInputLayout;
    private Button registerButton;
    private TextView loginLink;

    // Khai báo UserRepository để thao tác với cơ sở dữ liệu
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Ánh xạ các thành phần giao diện
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        emailInputLayout = findViewById(R.id.emailInputLayout);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        confirmPasswordInputLayout = findViewById(R.id.confirmPasswordInputLayout);
        registerButton = findViewById(R.id.registerButton);
        loginLink = findViewById(R.id.loginLink);

        // Khởi tạo UserRepository
        userRepository = new UserRepository(this);

        // Xử lý sự kiện nhấn nút Đăng ký
        registerButton.setOnClickListener(v -> registerUser());

        // Xử lý sự kiện nhấn vào liên kết "Đăng nhập"
        loginLink.setOnClickListener(v -> {
            finish(); // Đóng RegisterActivity để quay lại màn hình LoginActivity
        });
    }

    // Phương thức xử lý logic khi nhấn nút Đăng ký
    private void registerUser() {
        String email = emailInput.getText().toString().trim(); // Lấy email nhập vào
        String password = passwordInput.getText().toString().trim(); // Lấy mật khẩu nhập vào
        String confirmPassword = confirmPasswordInput.getText().toString().trim(); // Lấy mật khẩu xác nhận

        // Kiểm tra tính hợp lệ của email
        if (email.isEmpty()) {
            emailInputLayout.setError("Vui lòng nhập email");
            return;
        } else {
            emailInputLayout.setError(null); // Xóa lỗi nếu nhập đúng
        }

        // Kiểm tra mật khẩu có ít nhất 6 ký tự
        if (password.isEmpty() || password.length() < 6) {
            passwordInputLayout.setError("Mật khẩu phải có ít nhất 6 ký tự");
            return;
        } else {
            passwordInputLayout.setError(null);
        }

        // Kiểm tra mật khẩu và xác nhận mật khẩu có khớp
        if (!password.equals(confirmPassword)) {
            confirmPasswordInputLayout.setError("Mật khẩu không khớp");
            return;
        } else {
            confirmPasswordInputLayout.setError(null);
        }

        // Tạo đối tượng User với thông tin đã nhập
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        // Gọi UserRepository để lưu người dùng vào cơ sở dữ liệu
        userRepository.registerUser(user, new UserRepository.RepositoryCallback() {
            @Override
            public void onSuccess(String message) {
                // Nếu đăng ký thành công
                runOnUiThread(() -> {
                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                    finish(); // Quay lại màn hình đăng nhập
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                // Nếu đăng ký thất bại
                runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "Lỗi: " + errorMessage, Toast.LENGTH_SHORT).show());
            }
        });
    }
}
