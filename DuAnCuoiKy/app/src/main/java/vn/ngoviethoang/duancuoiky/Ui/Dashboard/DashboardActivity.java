package vn.ngoviethoang.duancuoiky.Ui.Dashboard;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;

import vn.ngoviethoang.duancuoiky.R;

public class DashboardActivity extends AppCompatActivity {
    private TextView incomeValue, expenseValue, balanceValue;
    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        incomeValue = findViewById(R.id.incomeValue);
        expenseValue = findViewById(R.id.expenseValue);
        balanceValue = findViewById(R.id.balanceValue);
        pieChart = findViewById(R.id.pieChart);

        // Fetch data from Room Database
        fetchDashboardData();
    }

    private void fetchDashboardData() {
        // Lấy dữ liệu thu nhập, chi tiêu từ database (Room)
        // Giả sử bạn đã có các đối tượng ExpenseDao và IncomeDao trong Repository
        DashboardRepository repository = new DashboardRepository(getApplicationContext());

        // Fetch data from repository (có thể là dữ liệu thu nhập, chi tiêu)
        repository.getDashboardData(new DashboardRepository.DataCallback() {
            @Override
            public void onDataLoaded(double income, double expense) {
                // Cập nhật UI với giá trị thu nhập và chi tiêu
                incomeValue.setText(income + " VND");
                expenseValue.setText(expense + " VND");
                balanceValue.setText((income - expense) + " VND");

                // Cập nhật Pie Chart
                updatePieChart(income, expense);
            }
        });
    }

    private void updatePieChart(double income, double expense) {
        // Tạo dữ liệu cho PieChart
        PieChartData pieData = new PieChartData(income, expense);
        pieChart.setData(pieData);
    }
}
