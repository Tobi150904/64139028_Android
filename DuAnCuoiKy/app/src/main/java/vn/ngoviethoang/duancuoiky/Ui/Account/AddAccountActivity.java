package vn.ngoviethoang.duancuoiky.Ui.Account;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import vn.ngoviethoang.duancuoiky.R;
import vn.ngoviethoang.duancuoiky.data.entity.TaiKhoan;

public class AddAccountActivity extends AppCompatActivity {
    private AccountViewModel viewModel;
    private EditText accountNameEditText, amountEditText;
    private int selectedIconResId = R.drawable.ic_account1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);

        accountNameEditText = findViewById(R.id.account_name);
        amountEditText = findViewById(R.id.amount);
        GridLayout iconGrid = findViewById(R.id.account_icons);
        Button saveButton = findViewById(R.id.save_button);
        ImageView backButton = findViewById(R.id.ic_back);

        viewModel = new ViewModelProvider(this).get(AccountViewModel.class);

        backButton.setOnClickListener(v -> finish());

        saveButton.setOnClickListener(v -> {
            String accountName = accountNameEditText.getText().toString().trim();
            String amountString = amountEditText.getText().toString().trim();

            if (accountName.isEmpty() || amountString.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            int amount = Integer.parseInt(amountString);
            TaiKhoan account = new TaiKhoan(accountName, amount, selectedIconResId);
            viewModel.addAccount(account);
            Toast.makeText(this, "Thêm tài khoản thành công", Toast.LENGTH_SHORT).show();
            finish();
        });

        // Set click listeners for icons
        for (int i = 0; i < iconGrid.getChildCount(); i++) {
            ImageView icon = (ImageView) iconGrid.getChildAt(i);
            icon.setOnClickListener(v -> selectedIconResId = (int) v.getTag());
        }
    }
}