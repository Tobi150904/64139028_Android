package vn.ngoviethoang.duancuoiky.Ui.Dashboard;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import vn.ngoviethoang.duancuoiky.R;
import vn.ngoviethoang.duancuoiky.Ui.AddTransaction.AddTransactionActivity;
import vn.ngoviethoang.duancuoiky.Ui.TransactionDetail.TransactionDetailActivity;

public class DashboardActivity extends AppCompatActivity {
    private DashboardViewModel dashboardViewModel;
    private TextView totalBalanceAmount, dateRange, tabExpenses, tabIncome, tabDay, tabWeek, tabMonth, tabYear, tabCustom;
    private ImageView iconMenu, iconList, iconAdd, iconEditBalance;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize Views
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

        // Initialize ViewModel
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        // Observe ViewModel
        dashboardViewModel.getDateRange().observe(this, dateRange::setText);
        dashboardViewModel.getTabSelected().observe(this, this::updateTabUI);

        // Set click listeners
        iconMenu.setOnClickListener(v -> drawerLayout.openDrawer(findViewById(R.id.nav_view)));
        iconList.setOnClickListener(v -> startActivity(new Intent(this, TransactionDetailActivity.class)));
        iconAdd.setOnClickListener(v -> startActivity(new Intent(this, AddTransactionActivity.class)));
        iconEditBalance.setOnClickListener(v -> enableBalanceEditing());
        tabExpenses.setOnClickListener(v -> dashboardViewModel.switchTab("expenses"));
        tabIncome.setOnClickListener(v -> dashboardViewModel.switchTab("income"));
        tabDay.setOnClickListener(v -> dashboardViewModel.updateDateRange("day"));
        tabWeek.setOnClickListener(v -> dashboardViewModel.updateDateRange("week"));
        tabMonth.setOnClickListener(v -> dashboardViewModel.updateDateRange("month"));
        tabYear.setOnClickListener(v -> dashboardViewModel.updateDateRange("year"));
        tabCustom.setOnClickListener(v -> showDatePicker());
    }

    private void enableBalanceEditing() {
        totalBalanceAmount.setFocusableInTouchMode(true);
        totalBalanceAmount.setInputType(InputType.TYPE_CLASS_NUMBER);
        totalBalanceAmount.requestFocus();
    }

    private void updateTabUI(String tab) {
        if (tab.equals("expenses")) {
            tabExpenses.setTextColor(getResources().getColor(R.color.Red));
            tabIncome.setTextColor(getResources().getColor(R.color.Gray));
            Toast.makeText(this, "Hiển thị Chi tiêu", Toast.LENGTH_SHORT).show();
        } else {
            tabIncome.setTextColor(getResources().getColor(R.color.Red));
            tabExpenses.setTextColor(getResources().getColor(R.color.Gray));
            Toast.makeText(this, "Hiển thị Thu nhập", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDatePicker() {
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener endDateListener = (view, year, month, dayOfMonth) -> {
            endDate.set(year, month, dayOfMonth);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String start = formatter.format(startDate.getTime());
            String end = formatter.format(endDate.getTime());
            dashboardViewModel.updateDateRange(start + " - " + end);
        };

        DatePickerDialog.OnDateSetListener startDateListener = (view, year, month, dayOfMonth) -> {
            startDate.set(year, month, dayOfMonth);
            new DatePickerDialog(DashboardActivity.this, endDateListener, endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH), endDate.get(Calendar.DAY_OF_MONTH)).show();
        };

        new DatePickerDialog(DashboardActivity.this, startDateListener, startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH)).show();
    }
}