// AddAccountActivity.java
package vn.ngoviethoang.duancuoiky.Ui.Account;

import android.content.Intent;
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
    private int selectedIconResId = -1;
    private ImageView selectedIconView = null;

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

            if (accountName.isEmpty() || amountString.isEmpty() || selectedIconResId == -1) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin và chọn biểu tượng", Toast.LENGTH_SHORT).show();
                return;
            }

            int amount = Integer.parseInt(amountString);
            TaiKhoan account = new TaiKhoan(accountName, amount, selectedIconResId);
            viewModel.addAccount(account);
            Toast.makeText(this, "Thêm tài khoản thành công", Toast.LENGTH_SHORT).show();

            Intent resultIntent = new Intent();
            resultIntent.putExtra("accountName", accountName);
            resultIntent.putExtra("amount", amount);
            resultIntent.putExtra("iconResId", selectedIconResId);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        for (int i = 0; i < iconGrid.getChildCount(); i++) {
            ImageView icon = (ImageView) iconGrid.getChildAt(i);
            icon.setTag(icon.getId());
            icon.setOnClickListener(v -> {
                if (selectedIconView != null) {
                    selectedIconView.setBackgroundResource(0);
                }
                selectedIconResId = (int) v.getTag();
                selectedIconView = (ImageView) v;
                selectedIconView.setBackgroundResource(R.drawable.selected_icon); // Highlight selected icon
            });
        }
    }
}