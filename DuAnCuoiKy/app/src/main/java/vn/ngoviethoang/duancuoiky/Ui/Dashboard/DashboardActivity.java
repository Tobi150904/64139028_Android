package vn.ngoviethoang.duancuoiky.Ui.Dashboard;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import vn.ngoviethoang.duancuoiky.R;
import vn.ngoviethoang.duancuoiky.Ui.Account.AccountActivity;
import vn.ngoviethoang.duancuoiky.Ui.Transaction.AddTransactionActivity;
import vn.ngoviethoang.duancuoiky.Ui.Transaction.TransactionDetailActivity;
import vn.ngoviethoang.duancuoiky.data.entity.TaiKhoan;

public class DashboardActivity extends AppCompatActivity {
    private DashboardViewModel dashboardViewModel;
    private TextView totalBalanceAmount, dateRange, tabExpenses, tabIncome, tabDay, tabWeek, tabMonth, tabYear, tabCustom;
    private ImageView iconMenu, iconList, iconAdd, iconEditBalance, iconDown;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initializeViews();
        setupViewModel();
        setupListeners();
        initializeDashboard();
    }

    private void initializeViews() {
        totalBalanceAmount = findViewById(R.id.total_balance_amount);
        dateRange = findViewById(R.id.date_range);
        iconMenu = findViewById(R.id.icon_menu);
        iconList = findViewById(R.id.icon_list);
        iconAdd = findViewById(R.id.icon_add_transaction);
        iconEditBalance = findViewById(R.id.icon_edit_balance);
        iconDown = findViewById(R.id.icon_down);
        tabExpenses = findViewById(R.id.tab_expenses);
        tabIncome = findViewById(R.id.tab_income);
        tabDay = findViewById(R.id.tab_day);
        tabWeek = findViewById(R.id.tab_week);
        tabMonth = findViewById(R.id.tab_month);
        tabYear = findViewById(R.id.tab_year);
        tabCustom = findViewById(R.id.tab_custom);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
    }

    private void setupViewModel() {
        userId = getIntent().getIntExtra("USER_ID", -1);

        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        dashboardViewModel.getDateRange().observe(this, dateRange::setText);
        dashboardViewModel.getTotalBalance().observe(this, balance -> totalBalanceAmount.setText(String.format("%,d VND", balance)));
        dashboardViewModel.getAccounts().observe(this, this::updateAccountUI);
    }

    private void setupListeners() {
        iconMenu.setOnClickListener(v -> drawerLayout.openDrawer(navigationView));
        iconList.setOnClickListener(v -> startActivity(new Intent(this, TransactionDetailActivity.class)));
        iconAdd.setOnClickListener(v -> startActivity(new Intent(this, AddTransactionActivity.class)));
        iconEditBalance.setOnClickListener(v -> showEditBalanceDialog());
        iconDown.setOnClickListener(v -> showAccountsDialog());

        tabDay.setOnClickListener(v -> selectDateRangeTab(tabDay, "day"));
        tabWeek.setOnClickListener(v -> selectDateRangeTab(tabWeek, "week"));
        tabMonth.setOnClickListener(v -> selectDateRangeTab(tabMonth, "month"));
        tabYear.setOnClickListener(v -> selectDateRangeTab(tabYear, "year"));
        tabCustom.setOnClickListener(v -> showDatePickerDialog());

        tabExpenses.setOnClickListener(v -> selectTab(tabExpenses, "expenses"));
        tabIncome.setOnClickListener(v -> selectTab(tabIncome, "income"));

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

    private void initializeDashboard() {
        dashboardViewModel.initializeDashboard();
        selectTab(tabExpenses, "expenses");
        selectDateRangeTab(tabDay, "day");
    }

    private void selectTab(TextView selectedTab, String tab) {
        resetTab(tabExpenses);
        resetTab(tabIncome);

        highlightTab(selectedTab);
        dashboardViewModel.switchTab(tab);
    }

    private void selectDateRangeTab(TextView selectedTab, String range) {
        resetTab(tabDay);
        resetTab(tabWeek);
        resetTab(tabMonth);
        resetTab(tabYear);
        resetTab(tabCustom);

        highlightTab(selectedTab);
        dashboardViewModel.updateDateRange(range);
    }

    private void resetTab(TextView tab) {
        tab.setTextColor(getResources().getColor(R.color.Gray));
        tab.setTypeface(null, Typeface.NORMAL);
        tab.setPaintFlags(tab.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
    }

    private void highlightTab(TextView tab) {
        tab.setTextColor(getResources().getColor(R.color.Red));
        tab.setTypeface(null, Typeface.BOLD);
        tab.setPaintFlags(tab.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    private void showAccountsDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_accounts);

        LinearLayout accountsContainer = dialog.findViewById(R.id.accounts_container);
        LinearLayout totalContainer = dialog.findViewById(R.id.total_container);
        TextView totalBalanceAmount = dialog.findViewById(R.id.text_total_amt);
        TextView btnCancel = dialog.findViewById(R.id.btn_cancel);
        TextView btnChoice = dialog.findViewById(R.id.btn_choice);

        dashboardViewModel.getAccounts().observe(this, accounts -> {
            accountsContainer.removeAllViews();
            double total = 0;
            for (TaiKhoan account : accounts) {
                LinearLayout accountLayout = createAccountLayout(account, dialog);
                accountsContainer.addView(accountLayout);
                total += account.getSodu();
            }
            totalBalanceAmount.setText(String.format("%,.0f đ", total));
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnChoice.setOnClickListener(v -> {
            // Handle choice action
            dialog.dismiss();
        });

        dialog.show();
    }

    private LinearLayout createAccountLayout(TaiKhoan account, Dialog dialog) {
        LinearLayout accountLayout = new LinearLayout(this);
        accountLayout.setOrientation(LinearLayout.HORIZONTAL);
        accountLayout.setPadding(10, 10, 10, 10);
        accountLayout.setGravity(Gravity.CENTER_VERTICAL); // Căn giữa các thành phần theo trục Y
        accountLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // Tạo RadioButton
        RadioButton radioButton = new RadioButton(this);
        LinearLayout.LayoutParams radioParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, // Sử dụng WRAP_CONTENT để kích thước phù hợp
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        radioParams.setMargins(0, 0, 16, 0); // Khoảng cách với các thành phần khác
        radioButton.setLayoutParams(radioParams);

        // Tạo ImageView
        ImageView accountIcon = new ImageView(this);
        Bitmap bitmap = BitmapFactory.decodeByteArray(account.getIcon(), 0, account.getIcon().length);
        accountIcon.setImageBitmap(bitmap);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(
                80, 80
        );
        iconParams.setMargins(16, 0, 16, 0);
        accountIcon.setLayoutParams(iconParams);
        accountIcon.setContentDescription("Account Icon");

        // Tạo container cho TextView
        LinearLayout textContainer = new LinearLayout(this);
        textContainer.setOrientation(LinearLayout.VERTICAL);
        textContainer.setPadding(16, 0, 0, 0);
        textContainer.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1
        ));

        // Tạo TextView cho tên tài khoản
        TextView accountName = new TextView(this);
        accountName.setText(account.getTen());
        accountName.setTextSize(16);
        accountName.setTextColor(getResources().getColor(R.color.Black));
        accountName.setPadding(8, 0, 0, 0);

        // Tạo TextView cho số dư tài khoản
        TextView accountAmount = new TextView(this);
        accountAmount.setText(String.format("%,.0f đ", account.getSodu()));
        accountAmount.setTextSize(14);
        accountAmount.setTextColor(getResources().getColor(R.color.Gray));
        accountAmount.setPadding(8, 0, 0, 0);

        // Thêm TextView vào container
        textContainer.addView(accountName);
        textContainer.addView(accountAmount);

        // Thêm các thành phần vào layout chính
        accountLayout.addView(radioButton);
        accountLayout.addView(accountIcon);
        accountLayout.addView(textContainer);

        return accountLayout;
    }


    private void showEditBalanceDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_edit_balance);

        TextView title = dialog.findViewById(R.id.dialog_title);
        EditText editBalance = dialog.findViewById(R.id.edit_balance_input);
        TextView btnCancel = dialog.findViewById(R.id.btn_cancel);
        TextView btnSave = dialog.findViewById(R.id.btn_save);

        title.setText("Chỉnh sửa số dư");

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnSave.setOnClickListener(v -> {
            try {
                double newBalance = Double.parseDouble(editBalance.getText().toString());
                updateAccountBalance(newBalance);
                dialog.dismiss();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Số dư không hợp lệ!", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void updateAccountBalance(double newBalance) {
        TaiKhoan selectedAccount = (TaiKhoan) totalBalanceAmount.getTag();
        if (selectedAccount != null) {
            selectedAccount.setSodu(newBalance);
            dashboardViewModel.updateBalance(selectedAccount);
            totalBalanceAmount.setText(String.format("%,.0f đ", newBalance));
            Toast.makeText(this, "Số dư đã được cập nhật!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDatePickerDialog() {
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener endDateListener = (view, year, month, dayOfMonth) -> {
            endDate.set(year, month, dayOfMonth);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String start = formatter.format(startDate.getTime());
            String end = formatter.format(endDate.getTime());
            dashboardViewModel.updateCustomDateRange(start, end);
        };

        DatePickerDialog.OnDateSetListener startDateListener = (view, year, month, dayOfMonth) -> {
            startDate.set(year, month, dayOfMonth);
            new DatePickerDialog(this, endDateListener, endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH), endDate.get(Calendar.DAY_OF_MONTH)).show();
        };

        new DatePickerDialog(this, startDateListener, startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateAccountUI(List<TaiKhoan> accounts) {
        // Update UI based on the accounts list
    }
}