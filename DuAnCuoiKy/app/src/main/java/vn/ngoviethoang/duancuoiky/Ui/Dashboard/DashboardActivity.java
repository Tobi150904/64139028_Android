package vn.ngoviethoang.duancuoiky.Ui.Dashboard;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import vn.ngoviethoang.duancuoiky.R;
//import vn.ngoviethoang.duancuoiky.Models.TransactionModel;
//import vn.ngoviethoang.duancuoiky.Ui.Profile.ProfileActivity;
//import vn.ngoviethoang.duancuoiky.Ui.Transaction.TransactionAdapter;

public class DashboardActivity extends AppCompatActivity {

//    private TextView incomeValue, expenseValue, balanceValue;
//    private PieChart pieChart;
//    private RecyclerView recentTransactionsList;
//    private FloatingActionButton addTransactionButton;
//    private DashboardViewModel dashboardViewModel;
//    private TransactionAdapter transactionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

//        // Initialize Views
//        incomeValue = findViewById(R.id.incomeValue);
//        expenseValue = findViewById(R.id.expenseValue);
//        balanceValue = findViewById(R.id.balanceValue);
//        pieChart = findViewById(R.id.pieChart);
//        recentTransactionsList = findViewById(R.id.recentTransactionsList);
//        addTransactionButton = findViewById(R.id.addTransactionButton);
//
//        // Setup RecyclerView
//        recentTransactionsList.setLayoutManager(new LinearLayoutManager(this));
//        transactionAdapter = new TransactionAdapter(new ArrayList<>());
//        recentTransactionsList.setAdapter(transactionAdapter);
//
//        // Setup ViewModel
//        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
//
//        // Observe ViewModel data
//        dashboardViewModel.getIncome().observe(this, income -> {
//            incomeValue.setText(income + " VND");
//        });
//
//        dashboardViewModel.getExpense().observe(this, expense -> {
//            expenseValue.setText(expense + " VND");
//        });
//
//        dashboardViewModel.getBalance().observe(this, balance -> {
//            balanceValue.setText(balance + " VND");
//        });
//
//        dashboardViewModel.getRecentTransactions().observe(this, transactions -> {
//            transactionAdapter.updateTransactions(transactions);
//        });
//
//        dashboardViewModel.getChartData().observe(this, chartData -> {
//            updatePieChart(chartData);
//        });
//
//        // Add Transaction Button Listener
//        addTransactionButton.setOnClickListener(v -> {
//            // Open Add Transaction Activity or Fragment
//            Toast.makeText(DashboardActivity.this, "Add transaction clicked", Toast.LENGTH_SHORT).show();
//        });
//
//        // Setup Bottom Navigation
//        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
//        bottomNav.setOnItemSelectedListener(item -> {
//            switch (item.getItemId()) {
//                case R.id.action_profile:
//                    // Open Profile Activity
//                    startActivity(ProfileActivity.newIntent(DashboardActivity.this));
//                    return true;
//                // Add more cases for other menu items if necessary
//                default:
//                    return false;
//            }
//        });
//    }
//
//    // Method to update Pie Chart with data
//    private void updatePieChart(List<PieEntry> chartData) {
//        PieDataSet pieDataSet = new PieDataSet(chartData, "Expenses vs Income");
//        pieDataSet.setColors(new int[] {R.color.green, R.color.red});
//        PieData pieData = new PieData(pieDataSet);
//        pieChart.setData(pieData);
//        pieChart.invalidate();  // Refresh the chart
    }
}
