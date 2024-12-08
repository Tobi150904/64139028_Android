package vn.ngoviethoang.duancuoiky.Ui.Account;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import vn.ngoviethoang.duancuoiky.R;

public class AccountActivity extends AppCompatActivity {
    private AccountViewModel viewModel;
    private TextView totalBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        totalBalance = findViewById(R.id.total_balance);
        ImageView addAccountButton = findViewById(R.id.ic_add);

        viewModel = new ViewModelProvider(this).get(AccountViewModel.class);

        viewModel.getTotalBalance().observe(this, balance -> totalBalance.setText(balance + " VND"));

        addAccountButton.setOnClickListener(v -> {
            Intent intent = new Intent(AccountActivity.this, AddAccountActivity.class);
            startActivity(intent);
        });

        // Load accounts and update UI
        viewModel.loadAccounts();
    }
}