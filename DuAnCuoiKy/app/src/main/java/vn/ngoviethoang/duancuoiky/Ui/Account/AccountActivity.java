// AccountActivity.java
package vn.ngoviethoang.duancuoiky.Ui.Account;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

import vn.ngoviethoang.duancuoiky.R;
import vn.ngoviethoang.duancuoiky.Ui.Dashboard.DashboardActivity;
import vn.ngoviethoang.duancuoiky.Ui.Transaction.TransactionDetailActivity;
import vn.ngoviethoang.duancuoiky.data.entity.TaiKhoan;

public class AccountActivity extends AppCompatActivity {
    private AccountViewModel viewModel;
    private TextView totalBalance;
    private LinearLayout accountsContainer;
    private NavigationView navigationView;
    private ImageView iconMenu;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        totalBalance = findViewById(R.id.total_balance);
        accountsContainer = findViewById(R.id.accounts_container);
        ImageView addAccountButton = findViewById(R.id.ic_add);
        navigationView = findViewById(R.id.nav_view);
        iconMenu = findViewById(R.id.ic_menu);
        drawerLayout = findViewById(R.id.drawer_layout);

        viewModel = new ViewModelProvider(this).get(AccountViewModel.class);

        iconMenu.setOnClickListener(v -> drawerLayout.openDrawer(navigationView));

        viewModel.getTotalBalance().observe(this, balance -> totalBalance.setText(String.format("%,d VND", balance)));

        viewModel.getAccounts().observe(this, this::updateAccountsUI);

        addAccountButton.setOnClickListener(v -> startActivity(new Intent(this, AddAccountActivity.class)));

        viewModel.loadAccounts();

        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_dashboard) {
                startActivity(new Intent(this, DashboardActivity.class));
                return true;
            }
            if (item.getItemId() == R.id.nav_account) {
                startActivity(new Intent(this, AccountActivity.class));
                return true;
            }
            if (item.getItemId() == R.id.nav_chart) {
                startActivity(new Intent(this, TransactionDetailActivity.class));
                return true;
            }
            if (item.getItemId() == R.id.nav_category) {
                startActivity(new Intent(this, TransactionDetailActivity.class));
                return true;
            }
            if (item.getItemId() == R.id.nav_settings) {
                startActivity(new Intent(this, TransactionDetailActivity.class));
                return true;
            }
            drawerLayout.closeDrawer(navigationView);
            return false;
        });
    }

    private void updateAccountsUI(List<TaiKhoan> accounts) {
        accountsContainer.removeAllViews();
        for (TaiKhoan account : accounts) {
            LinearLayout accountLayout = new LinearLayout(this);
            accountLayout.setOrientation(LinearLayout.HORIZONTAL);
            accountLayout.setPadding(10, 10, 10, 10);
            accountLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            ImageView accountIcon = new ImageView(this);
            Bitmap bitmap = BitmapFactory.decodeByteArray(account.getIcon(), 0, account.getIcon().length);
            accountIcon.setImageBitmap(bitmap);
            LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(64, 64);
            iconParams.setMargins(0, 0, 16, 0);
            accountIcon.setLayoutParams(iconParams);
            accountIcon.setContentDescription("Account Icon");

            LinearLayout textContainer = new LinearLayout(this);
            textContainer.setOrientation(LinearLayout.VERTICAL);
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

            accountLayout.addView(accountIcon);
            accountLayout.addView(textContainer);

            accountsContainer.addView(accountLayout);
        }
    }
}