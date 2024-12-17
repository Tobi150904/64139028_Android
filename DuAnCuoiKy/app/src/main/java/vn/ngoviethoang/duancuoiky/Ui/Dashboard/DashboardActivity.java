package vn.ngoviethoang.duancuoiky.Ui.Dashboard;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import vn.ngoviethoang.duancuoiky.R;
import vn.ngoviethoang.duancuoiky.Ui.Account.AccountActivity;
import vn.ngoviethoang.duancuoiky.Ui.Transaction.AddTransactionActivity;
import vn.ngoviethoang.duancuoiky.Ui.Transaction.TransactionDetailActivity;
import vn.ngoviethoang.duancuoiky.data.entity.TaiKhoan;
import vn.ngoviethoang.duancuoiky.data.repository.TaiKhoanRepository;

public class DashboardActivity extends AppCompatActivity {
    private DashboardViewModel dashboardViewModel;
    private TextView totalBalanceAmount, dateRange, tabExpenses, tabIncome, tabDay, tabWeek, tabMonth, tabYear, tabCustom;
    private ImageView iconMenu, iconList, iconAdd, iconEditBalance;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TaiKhoanRepository taiKhoanRepository;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        totalBalanceAmount = findViewById(R.id.total_balance_amount);
        dateRange = findViewById(R.id.date_range);
        iconMenu = findViewById(R.id.icon_menu);
        iconList = findViewById(R.id.icon_list);
        iconAdd = findViewById(R.id.icon_add_transaction);
        iconEditBalance = findViewById(R.id.icon_edit_balance);
        tabExpenses = findViewById(R.id.tab_expenses);
        tabIncome = findViewById(R.id.tab_income);
        tabDay = findViewById(R.id.tab_day);
        tabWeek = findViewById(R.id.tab_week);
        tabMonth = findViewById(R.id.tab_month);
        tabYear = findViewById(R.id.tab_year);
        tabCustom = findViewById(R.id.tab_custom);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        taiKhoanRepository = new TaiKhoanRepository(getApplication());
        userId = getIntent().getIntExtra("USER_ID", -1);

        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        dashboardViewModel.getDateRange().observe(this, dateRange::setText);
        dashboardViewModel.getTotalBalance().observe(this, balance -> totalBalanceAmount.setText(String.format("%,d VND", balance)));

        iconMenu.setOnClickListener(v -> drawerLayout.openDrawer(navigationView));
        iconList.setOnClickListener(v -> startActivity(new Intent(this, TransactionDetailActivity.class)));
        iconAdd.setOnClickListener(v -> startActivity(new Intent(this, AddTransactionActivity.class)));
        iconEditBalance.setOnClickListener(v -> showEditBalanceDialog());

        tabDay.setOnClickListener(v -> dashboardViewModel.updateDateRange("day"));
        tabWeek.setOnClickListener(v -> dashboardViewModel.updateDateRange("week"));
        tabMonth.setOnClickListener(v -> dashboardViewModel.updateDateRange("month"));
        tabYear.setOnClickListener(v -> dashboardViewModel.updateDateRange("year"));
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

        // Initialize dashboard
        dashboardViewModel.initializeDashboard();
    }

    private void selectTab(TextView selectedTab, String tab) {
        resetTab(tabExpenses);
        resetTab(tabIncome);

        selectedTab.setTextColor(getResources().getColor(R.color.Red));
        selectedTab.setTypeface(null, Typeface.BOLD);
        selectedTab.setPaintFlags(selectedTab.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        dashboardViewModel.switchTab(tab);
    }

    private void resetTab(TextView tab) {
        tab.setTextColor(getResources().getColor(R.color.White));
        tab.setTypeface(null, Typeface.NORMAL);
        tab.setPaintFlags(tab.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
    }

    private void showAccountsDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_accounts);

        LinearLayout accountsContainer = dialog.findViewById(R.id.accounts_container);

        dashboardViewModel.getAccounts().observe(this, accounts -> {
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
                LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(64, 64); // Adjust size
                iconParams.setMargins(0, 0, 16, 0); // Add margin to the right
                accountIcon.setLayoutParams(iconParams);
                accountIcon.setContentDescription("Account Icon");

                TextView accountName = new TextView(this);
                accountName.setText(account.getTen());
                accountName.setTextSize(16); // Adjust text size
                accountName.setTextColor(getResources().getColor(R.color.Black));
                accountName.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                accountName.setPadding(8, 0, 0, 0);

                accountLayout.addView(accountIcon);
                accountLayout.addView(accountName);

                accountLayout.setOnClickListener(v -> {
                    totalBalanceAmount.setText(account.getTen());
                    iconEditBalance.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                });

                accountsContainer.addView(accountLayout);
            }
        });

        dialog.show();
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

                // Chuyển Id thành byte array
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_account1);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                byte[] iconBytes = outputStream.toByteArray();

                TaiKhoan taiKhoan = new TaiKhoan("Tài khoản chính", newBalance, iconBytes);
                dashboardViewModel.updateBalance(taiKhoan);
                totalBalanceAmount.setText(String.valueOf(newBalance));

                Toast.makeText(this, "Số dư đã được cập nhật!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Vui lòng nhập số tiền hợp lệ!", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
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
}