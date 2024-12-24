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
import android.widget.RadioGroup;
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
import vn.ngoviethoang.duancuoiky.data.entity.GiaoDich;
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

        GiaoDich newTransaction = (GiaoDich) getIntent().getSerializableExtra("NEW_TRANSACTION");
        if (newTransaction != null) {
            addTransactionToUI(newTransaction);
        }
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

        Integer totalBalance = dashboardViewModel.getTotalBalance().getValue();
        if (totalBalance != null) {
            updateMainUI("Tổng cộng", totalBalance);
        } else {
            updateMainUI("Tổng cộng", 0);
        }
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

    private RadioButton lastCheckedRadioButton = null; // Lưu RadioButton được chọn cuối cùng

    private void showAccountsDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_accounts);

        RadioGroup accountsContainer = dialog.findViewById(R.id.accounts_container);
        LinearLayout totalContainer = dialog.findViewById(R.id.total_container);
        TextView totalBalanceAmount = dialog.findViewById(R.id.text_total_amt);
        TextView btnCancel = dialog.findViewById(R.id.btn_cancel);
        TextView btnChoice = dialog.findViewById(R.id.btn_choice);
        RadioButton radioTotal = dialog.findViewById(R.id.radio_total);

        // Set "Tổng cộng" as the default selected option
        radioTotal.setChecked(true);
        lastCheckedRadioButton = radioTotal;

        // Theo dõi danh sách tài khoản từ ViewModel
        dashboardViewModel.getAccounts().observe(this, accounts -> {
            accountsContainer.removeAllViews(); // Xóa các tài khoản cũ trước khi thêm mới
            accountsContainer.addView(totalContainer); // Add the totalContainer back after clearing

            double total = 0;
            for (TaiKhoan account : accounts) {
                // Tạo layout chứa thông tin tài khoản
                LinearLayout accountLayout = createAccountLayout(account, dialog);

                // Tìm RadioButton từ layout được tạo
                RadioButton radioButton = (RadioButton) accountLayout.getChildAt(0);

                // Thiết lập sự kiện để xử lý chọn một tài khoản duy nhất
                radioButton.setOnClickListener(v -> {
                    if (lastCheckedRadioButton != null) {
                        lastCheckedRadioButton.setChecked(false); // Bỏ chọn RadioButton trước đó
                    }
                    lastCheckedRadioButton = radioButton; // Cập nhật RadioButton đang được chọn
                    radioButton.setChecked(true); // Đảm bảo RadioButton này luôn được chọn
                });

                accountsContainer.addView(accountLayout); // Thêm layout tài khoản vào container
                total += account.getSodu(); // Tính tổng số dư
            }

            // Hiển thị tổng số dư
            totalBalanceAmount.setText(String.format("%,.0f đ", total));
        });

        // Hủy dialog khi nhấn nút "Hủy"
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        // Xử lý logic khi nhấn nút "Chọn"
        btnChoice.setOnClickListener(v -> {
            if (lastCheckedRadioButton != null) {
                if (lastCheckedRadioButton == radioTotal) {
                    // Hiển thị tổng cộng
                    updateMainUI("Tổng cộng", dashboardViewModel.getTotalBalance().getValue());
                    Toast.makeText(this, "Bạn đã chọn: Tổng cộng", Toast.LENGTH_SHORT).show();
                } else {
                    // Lấy tài khoản được chọn từ RadioButton
                    LinearLayout selectedLayout = (LinearLayout) lastCheckedRadioButton.getParent();
                    LinearLayout textContainer = (LinearLayout) selectedLayout.getChildAt(2);
                    TextView selectedAccountName = (TextView) textContainer.getChildAt(0);
                    TextView selectedAccountAmount = (TextView) textContainer.getChildAt(1);
                    updateMainUI(selectedAccountName.getText().toString(), Double.parseDouble(selectedAccountAmount.getText().toString().replace(" đ", "").replace(",", "")));
                    Toast.makeText(this, "Bạn đã chọn: " + selectedAccountName.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Vui lòng chọn tài khoản", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });

        // Thiết lập sự kiện để xử lý chọn "Tổng cộng"
        radioTotal.setOnClickListener(v -> {
            if (lastCheckedRadioButton != null) {
                lastCheckedRadioButton.setChecked(false); // Bỏ chọn RadioButton trước đó
            }
            lastCheckedRadioButton = radioTotal; // Cập nhật RadioButton đang được chọn
            radioTotal.setChecked(true); // Đảm bảo RadioButton này luôn được chọn
        });

        dialog.show();
    }

    private void updateMainUI(String accountName, double balance) {
        TextView mainAccountName = findViewById(R.id.total_balance_label);
        TextView mainAccountBalance = findViewById(R.id.total_balance_amount);
        ImageView iconEditBalance = findViewById(R.id.icon_edit_balance);

        mainAccountName.setText(accountName);
        mainAccountBalance.setText(String.format("%,.0f đ", balance));

        if ("Tổng cộng".equals(accountName)) {
            iconEditBalance.setVisibility(View.GONE);
            totalBalanceAmount.setTag(null);
        } else {
            iconEditBalance.setVisibility(View.VISIBLE);
            TaiKhoan selectedAccount = dashboardViewModel.getAccountByName(accountName);
            totalBalanceAmount.setTag(selectedAccount);
        }
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


    private void addTransactionToUI(GiaoDich transaction) {
        LinearLayout transactionContainer = findViewById(R.id.transaction_list); // Assuming you have a LinearLayout with this ID

        LinearLayout transactionLayout = new LinearLayout(this);
        transactionLayout.setOrientation(LinearLayout.HORIZONTAL);
        transactionLayout.setPadding(10, 10, 10, 10);

        ImageView transactionIcon = new ImageView(this);
        transactionIcon.setLayoutParams(new LinearLayout.LayoutParams(24, 24));
        transactionIcon.setImageResource(R.drawable.ic_entertain); // Set the appropriate icon

        TextView transactionName = new TextView(this);
        transactionName.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        transactionName.setText(transaction.getGhiChu());
        transactionName.setTextColor(getResources().getColor(R.color.Black));
        transactionName.setTextSize(14);

        TextView transactionAmount = new TextView(this);
        transactionAmount.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        transactionAmount.setText(String.format("%,.0f đ", transaction.getSoTien()));
        transactionAmount.setTextColor(getResources().getColor(R.color.Gray));
        transactionAmount.setTextSize(14);

        transactionLayout.addView(transactionIcon);
        transactionLayout.addView(transactionName);
        transactionLayout.addView(transactionAmount);

        transactionContainer.addView(transactionLayout);
    }

}