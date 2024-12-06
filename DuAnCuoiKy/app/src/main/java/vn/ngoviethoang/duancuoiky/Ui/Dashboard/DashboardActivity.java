package vn.ngoviethoang.duancuoiky.Ui.Dashboard;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import vn.ngoviethoang.duancuoiky.R;
import vn.ngoviethoang.duancuoiky.Ui.AddTransaction.AddTransactionActivity;
import vn.ngoviethoang.duancuoiky.Ui.TransactionDetail.TransactionDetailActivity;
import vn.ngoviethoang.duancuoiky.data.entity.SoDu;

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

        // Observe LiveData
        dashboardViewModel.getDateRange().observe(this, dateRange::setText);
        dashboardViewModel.getBalance().observe(this, soDu -> {
            if (soDu != null) {
                totalBalanceAmount.setText(String.valueOf(soDu.getBalance()));
            }
        });
//        dashboardViewModel.getItems().observe(this, this::updateItems);
//        dashboardViewModel.getPieChartData().observe(this, this::updatePieChart);

        // Set click listeners
        iconMenu.setOnClickListener(v -> drawerLayout.openDrawer(findViewById(R.id.nav_view)));
        iconList.setOnClickListener(v -> startActivity(new Intent(this, TransactionDetailActivity.class)));
        iconAdd.setOnClickListener(v -> startActivity(new Intent(this, AddTransactionActivity.class)));
        iconEditBalance.setOnClickListener(v -> showEditBalanceDialog());

        // Tab chọn khoảng thời gian
        tabDay.setOnClickListener(v -> dashboardViewModel.updateDateRange("day"));
        tabWeek.setOnClickListener(v -> dashboardViewModel.updateDateRange("week"));
        tabMonth.setOnClickListener(v -> dashboardViewModel.updateDateRange("month"));
        tabYear.setOnClickListener(v -> dashboardViewModel.updateDateRange("year"));
        tabCustom.setOnClickListener(v -> showDatePickerDialog());

        // Tab chi phí và thu nhập
        tabExpenses.setOnClickListener(v -> selectTab(tabExpenses, "expenses"));
        tabIncome.setOnClickListener(v -> selectTab(tabIncome, "income"));
    }

    private void selectTab(TextView selectedTab, String tab) {
        // Reset all tabs
        resetTab(tabExpenses);
        resetTab(tabIncome);

        // Highlight selected tab
        selectedTab.setTextColor(getResources().getColor(R.color.Green));
        selectedTab.setTypeface(null, Typeface.BOLD);
        selectedTab.setPaintFlags(selectedTab.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        // Switch tab in ViewModel
        dashboardViewModel.switchTab(tab);
    }

    private void resetTab(TextView tab) {
        tab.setTextColor(getResources().getColor(R.color.White));
        tab.setTypeface(null, Typeface.NORMAL);
        tab.setPaintFlags(tab.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
    }

//    private void updateItems(List<Item> items) {
//        // Update the items in the UI
//    }
//
//    private void updatePieChart(PieChartData pieChartData) {
//        // Update the pie chart in the UI
//    }

    private void showEditBalanceDialog() {
        // Hiển thị dialog chỉnh sửa số dư
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
                int newBalance = Integer.parseInt(editBalance.getText().toString());
                SoDu soDu = new SoDu(newBalance);
                dashboardViewModel.updateBalance(soDu);
                totalBalanceAmount.setText(String.valueOf(newBalance)); // Cập nhật TextView

                Toast.makeText(this, "Số dư đã được cập nhật!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Vui lòng nhập số tiền hợp lệ!", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void showDatePickerDialog() {
        // Hiển thị dialog chọn khoảng thời gian
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