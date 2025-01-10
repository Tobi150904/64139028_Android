package vn.ngoviethoang.duancuoiky.Ui.Account;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import vn.ngoviethoang.duancuoiky.R;
import vn.ngoviethoang.duancuoiky.data.entity.TaiKhoan;

public class AddTransferActivity extends AppCompatActivity {

    private AccountViewModel accountViewModel;
    private TextView tvFromAccount, tvToAccount, tvDate;
    private EditText etTransferAmount;
    private TaiKhoan selectedFromAccount, selectedToAccount;
    private Calendar selectedDate;
    private RadioButton lastCheckedRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitvy_add_transfer);

        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);

        ImageView icBack = findViewById(R.id.ic_back);
        tvFromAccount = findViewById(R.id.tv_from_account);
        tvToAccount = findViewById(R.id.tv_to_account);
        etTransferAmount = findViewById(R.id.et_transfer_amount);
        tvDate = findViewById(R.id.tv_date);
        Button btnAdd = findViewById(R.id.btn_add);

        icBack.setOnClickListener(v -> {
            Intent intent = new Intent(AddTransferActivity.this, AccountActivity.class);
            startActivity(intent);
            finish();
        });

        tvFromAccount.setOnClickListener(v -> showAccountsDialog(true));
        tvToAccount.setOnClickListener(v -> showAccountsDialog(false));

        tvDate.setOnClickListener(v -> showDatePickerDialog());

        btnAdd.setOnClickListener(v -> performTransfer());

        selectedDate = Calendar.getInstance();
        updateDateDisplay();

        // Tải danh sách tài khoản khi Activity được tạo
        accountViewModel.loadAccounts();
    }

    private void showAccountsDialog(boolean isFromAccount) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_accounts);

        RadioGroup accountsContainer = dialog.findViewById(R.id.accounts_container);
        TextView btnCancel = dialog.findViewById(R.id.btn_cancel);
        TextView btnChoice = dialog.findViewById(R.id.btn_choice);

        accountViewModel.getAccounts().observe(this, accounts -> {
            accountsContainer.removeAllViews();

            for (TaiKhoan account : accounts) {
                LinearLayout accountLayout = createAccountLayout(account, dialog);
                RadioButton radioButton = (RadioButton) accountLayout.getChildAt(0);

                radioButton.setOnClickListener(v -> {
                    if (lastCheckedRadioButton != null) {
                        lastCheckedRadioButton.setChecked(false);
                    }
                    lastCheckedRadioButton = radioButton;
                    radioButton.setChecked(true);
                });

                accountsContainer.addView(accountLayout);
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnChoice.setOnClickListener(v -> {
            if (lastCheckedRadioButton != null) {
                LinearLayout selectedLayout = (LinearLayout) lastCheckedRadioButton.getParent();
                LinearLayout textContainer = (LinearLayout) selectedLayout.getChildAt(2);
                TextView selectedAccountName = (TextView) textContainer.getChildAt(0);

                if (isFromAccount) {
                    selectedFromAccount = getSelectedTaiKhoan(selectedAccountName.getText().toString());
                    tvFromAccount.setText(selectedAccountName.getText().toString());
                } else {
                    if (selectedFromAccount != null && selectedFromAccount.getTen().equals(selectedAccountName.getText().toString())) {
                        Toast.makeText(this, "Không thể chọn cùng tài khoản", Toast.LENGTH_SHORT).show();
                    } else {
                        selectedToAccount = getSelectedTaiKhoan(selectedAccountName.getText().toString());
                        tvToAccount.setText(selectedAccountName.getText().toString());
                    }
                }
            } else {
                Toast.makeText(this, "Vui lòng chọn tài khoản", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });

        dialog.show();
    }

    private LinearLayout createAccountLayout(TaiKhoan account, Dialog dialog) {
        LinearLayout accountLayout = new LinearLayout(this);
        accountLayout.setOrientation(LinearLayout.HORIZONTAL);
        accountLayout.setPadding(10, 10, 10, 10);
        accountLayout.setGravity(Gravity.CENTER_VERTICAL);
        accountLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        RadioButton radioButton = new RadioButton(this);
        LinearLayout.LayoutParams radioParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        radioParams.setMargins(0, 0, 16, 0);
        radioButton.setLayoutParams(radioParams);

        ImageView accountIcon = new ImageView(this);
        Bitmap bitmap = BitmapFactory.decodeByteArray(account.getIcon(), 0, account.getIcon().length);
        accountIcon.setImageBitmap(bitmap);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(
                80, 80
        );
        iconParams.setMargins(16, 0, 16, 0);
        accountIcon.setLayoutParams(iconParams);
        accountIcon.setContentDescription("Account Icon");

        LinearLayout textContainer = new LinearLayout(this);
        textContainer.setOrientation(LinearLayout.VERTICAL);
        textContainer.setPadding(32, 0, 0, 0);
        textContainer.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1
        ));

        TextView accountName = new TextView(this);
        accountName.setText(account.getTen());
        accountName.setTextSize(16);
        accountName.setTextColor(getResources().getColor(R.color.Black));
        accountName.setPadding(8, 0, 0, 0);

        TextView accountAmount = new TextView(this);
        accountAmount.setText(String.format("%,.0f đ", account.getSodu()));
        accountAmount.setTextSize(14);
        accountAmount.setTextColor(getResources().getColor(R.color.Gray));
        accountAmount.setPadding(8, 0, 0, 0);

        textContainer.addView(accountName);
        textContainer.addView(accountAmount);

        accountLayout.addView(radioButton);
        accountLayout.addView(accountIcon);
        accountLayout.addView(textContainer);

        return accountLayout;
    }

    private TaiKhoan getSelectedTaiKhoan(String accountName) {
        List<TaiKhoan> accounts = accountViewModel.getAccounts().getValue();
        if (accounts != null) {
            for (TaiKhoan account : accounts) {
                if (account.getTen().equals(accountName)) {
                    return account;
                }
            }
        }
        return null;
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    selectedDate = calendar;
                    updateDateDisplay();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void updateDateDisplay() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        tvDate.setText(sdf.format(selectedDate.getTime()));
    }

    private void performTransfer() {
        if (selectedFromAccount == null || selectedToAccount == null) {
            Toast.makeText(this, "Vui lòng chọn tài khoản nguồn và đích", Toast.LENGTH_SHORT).show();
            return;
        }

        String amountStr = etTransferAmount.getText().toString();
        if (amountStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập số tiền", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);
        if (amount <= 0) {
            Toast.makeText(this, "Số tiền phải lớn hơn 0", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedFromAccount.getSodu() < amount) {
            Toast.makeText(this, "Số dư không đủ", Toast.LENGTH_SHORT).show();
            return;
        }

        selectedFromAccount.setSodu(selectedFromAccount.getSodu() - amount);
        selectedToAccount.setSodu(selectedToAccount.getSodu() + amount);

        accountViewModel.updateAccount(selectedFromAccount);
        accountViewModel.updateAccount(selectedToAccount);

        Toast.makeText(this, "Chuyển khoản thành công", Toast.LENGTH_SHORT).show();
        finish();
    }
}