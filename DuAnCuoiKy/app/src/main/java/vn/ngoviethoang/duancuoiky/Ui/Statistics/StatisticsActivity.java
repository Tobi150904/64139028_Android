package vn.ngoviethoang.duancuoiky.Ui.Statistics;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import vn.ngoviethoang.duancuoiky.R;
import vn.ngoviethoang.duancuoiky.Ui.Account.AccountActivity;
import vn.ngoviethoang.duancuoiky.Ui.Category.CategoryActivity;
import vn.ngoviethoang.duancuoiky.Ui.Dashboard.DashboardActivity;
import vn.ngoviethoang.duancuoiky.Ui.Transaction.TransactionDetailActivity;

public class StatisticsActivity extends AppCompatActivity {

    private StatisticsViewModel statisticsViewModel;
    private TextView dateRange;
    private TextView tabDay, tabWeek, tabMonth, tabYear;
    private ImageView iconMenu, iconPrevious, iconNext;
    private BarChart barChart;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        statisticsViewModel = new ViewModelProvider(this).get(StatisticsViewModel.class);

        dateRange = findViewById(R.id.date_range);
        tabDay = findViewById(R.id.tab_day);
        tabWeek = findViewById(R.id.tab_week);
        tabMonth = findViewById(R.id.tab_month);
        tabYear = findViewById(R.id.tab_year);
        iconPrevious = findViewById(R.id.icon_previous);
        iconNext = findViewById(R.id.icon_next);
        barChart = findViewById(R.id.barChart);
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        iconMenu = findViewById(R.id.icon_menu);

        setupListeners();
        observeData();
        configureChart();

        selectDateRangeTab(tabDay, "day");
    }

    private void setupListeners() {
        iconMenu.setOnClickListener(v -> drawerLayout.openDrawer(navigationView));

        tabDay.setOnClickListener(v -> {
            selectDateRangeTab(tabDay, "day");
            updateChart();
        });

        tabWeek.setOnClickListener(v -> {
            selectDateRangeTab(tabWeek, "week");
            updateChart();
        });

        tabMonth.setOnClickListener(v -> {
            selectDateRangeTab(tabMonth, "month");
            updateChart();
        });

        tabYear.setOnClickListener(v -> {
            selectDateRangeTab(tabYear, "year");
            updateChart();
        });

        iconPrevious.setOnClickListener(v -> {
            statisticsViewModel.navigateDateRange(-1);
            updateChart();
        });

        iconNext.setOnClickListener(v -> {
            statisticsViewModel.navigateDateRange(1);
            updateChart();
        });

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
                startActivity(new Intent(this, StatisticsActivity.class));
                return true;
            }
            if (item.getItemId() == R.id.nav_category) {
                startActivity(new Intent(this, CategoryActivity.class));
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

    private void observeData() {
        statisticsViewModel.getDateRange().observe(this, date -> dateRange.setText(date));

        statisticsViewModel.getTotalIncome().observe(this, income -> updateChart());
        statisticsViewModel.getTotalExpenses().observe(this, expenses -> updateChart());
        statisticsViewModel.getProfitLoss().observe(this, profitLoss -> updateChart());

        statisticsViewModel.getFilteredTransactions().observe(this, transactions -> {
            updateChart();
        });
    }

    private void selectDateRangeTab(TextView selectedTab, String range) {
        resetTab(tabDay);
        resetTab(tabWeek);
        resetTab(tabMonth);
        resetTab(tabYear);
        highlightTab(selectedTab);
        statisticsViewModel.updateDateRange(range, 0);
        updateChart();
    }

    private void resetTab(TextView tab) {
        tab.setTextColor(Color.GRAY);
        tab.setSelected(false);
    }

    private void highlightTab(TextView tab) {
        tab.setTextColor(Color.RED);
        tab.setSelected(true);
    }

    private void updateChart() {
        Float totalIncome = statisticsViewModel.getTotalIncome().getValue();
        Float totalExpenses = statisticsViewModel.getTotalExpenses().getValue();
        Float profitLoss = statisticsViewModel.getProfitLoss().getValue();

        if (totalIncome == null || totalExpenses == null || profitLoss == null) {
            barChart.clear();
            return;
        }

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, totalIncome));
        entries.add(new BarEntry(1, totalExpenses));
        entries.add(new BarEntry(2, profitLoss));

        BarDataSet dataSet = new BarDataSet(entries, "Thống kê");
        dataSet.setColors(new int[]{Color.GREEN, Color.RED, Color.BLUE});
        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.3f); // Điều chỉnh độ rộng của cột
        barChart.setData(barData);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[]{"Thu nhập", "Chi phí", "Lãi/Lỗ"}));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);

        barChart.invalidate();
    }

    private void configureChart() {
        barChart.getDescription().setEnabled(false);
        barChart.setDrawValueAboveBar(true);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.setFitBars(true);
        barChart.animateY(1000);
    }
}