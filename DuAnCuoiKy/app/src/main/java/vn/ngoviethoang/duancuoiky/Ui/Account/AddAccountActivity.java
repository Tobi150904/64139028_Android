package vn.ngoviethoang.duancuoiky.Ui.Account;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.io.ByteArrayOutputStream;

import vn.ngoviethoang.duancuoiky.R;
import vn.ngoviethoang.duancuoiky.data.entity.TaiKhoan;

public class AddAccountActivity extends AppCompatActivity {
    private AccountViewModel viewModel;
    private EditText accountNameEditText, amountEditText;
    private Bitmap selectedIconBitmap = null;
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

            if (accountName.isEmpty() || amountString.isEmpty() || selectedIconBitmap == null) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin và chọn biểu tượng", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount = Double.parseDouble(amountString);

            // Chuyển bitmap thành byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            selectedIconBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            byte[] iconBytes = outputStream.toByteArray();

            TaiKhoan newAccount = new TaiKhoan(accountName, amount, iconBytes);
            viewModel.addAccount(newAccount);

            Toast.makeText(this, "Tài khoản đã được thêm!", Toast.LENGTH_SHORT).show();
            finish();
        });

        for (int i = 0; i < iconGrid.getChildCount(); i++) {
            ImageView icon = (ImageView) iconGrid.getChildAt(i);
            icon.setOnClickListener(v -> {
                if (selectedIconView != null) {
                    selectedIconView.setBackgroundResource(0);
                }
                selectedIconBitmap = ((BitmapDrawable) icon.getDrawable()).getBitmap();
                selectedIconView = (ImageView) v;
                selectedIconView.setBackgroundResource(R.drawable.selected_icon); // Highlight selected icon
            });
        }
    }
}