package vn.ngoviethoang.duancuoiky.Ui.Account;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import vn.ngoviethoang.duancuoiky.R;
import vn.ngoviethoang.duancuoiky.data.entity.TaiKhoan;

public class AccountActivity extends AppCompatActivity {
    private static final int ADD_ACCOUNT_REQUEST_CODE = 1;
    private AccountViewModel viewModel;
    private TextView totalBalance;
    private LinearLayout accountsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        totalBalance = findViewById(R.id.total_balance);
        accountsContainer = findViewById(R.id.accounts_container); // Initialize accountsContainer
        ImageView addAccountButton = findViewById(R.id.ic_add);

        viewModel = new ViewModelProvider(this).get(AccountViewModel.class);

        viewModel.getTotalBalance().observe(this, balance -> totalBalance.setText(String.format("%,d VND", balance)));

        viewModel.getAccounts().observe(this, this::updateAccountsUI);

        addAccountButton.setOnClickListener(v -> startActivity(new Intent(this, AddAccountActivity.class)));

        // Load accounts and update UI
        viewModel.loadAccounts();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_ACCOUNT_REQUEST_CODE && resultCode == RESULT_OK) {
            String accountName = data.getStringExtra("accountName");
            int amount = data.getIntExtra("amount", 0);
            int iconResId = data.getIntExtra("iconResId", -1);

            if (iconResId != -1) {
                TaiKhoan account = new TaiKhoan(accountName, amount, iconResId);
                viewModel.addAccount(account);
            }
        }
    }

    private void updateAccountsUI(List<TaiKhoan> accounts) {
        accountsContainer.removeAllViews();
        for (TaiKhoan account : accounts) {
            addAccountToUI(account.getTen(), (int) account.getSodu(), account.getIconId());
        }
    }

    private void addAccountToUI(String accountName, int amount, int iconResId) {
        LinearLayout accountLayout = new LinearLayout(this);
        accountLayout.setOrientation(LinearLayout.HORIZONTAL);
        accountLayout.setPadding(10, 10, 10, 10);

        ImageView accountIcon = new ImageView(this);
        accountIcon.setImageResource(iconResId);
        accountIcon.setLayoutParams(new LinearLayout.LayoutParams(24, 24));

        TextView accountInfo = new TextView(this);
        accountInfo.setText(accountName);
        accountInfo.setTextSize(14);
        accountInfo.setTextColor(getResources().getColor(R.color.Black));
        accountInfo.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        accountInfo.setPadding(8, 0, 0, 0);

        TextView accountAmount = new TextView(this);
        accountAmount.setText(String.format("%,d đ", amount));
        accountAmount.setTextSize(14);
        accountAmount.setTextColor(getResources().getColor(R.color.Gray));

        accountLayout.addView(accountIcon);
        accountLayout.addView(accountInfo);
        accountLayout.addView(accountAmount);

        accountsContainer.addView(accountLayout);
    }
}