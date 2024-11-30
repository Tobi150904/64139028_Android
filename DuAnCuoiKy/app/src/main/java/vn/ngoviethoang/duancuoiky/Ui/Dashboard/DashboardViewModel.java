//package vn.ngoviethoang.duancuoiky.Ui.Dashboard;
//
//import android.app.Application;
//import androidx.lifecycle.AndroidViewModel;
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//import com.github.mikephil.charting.data.PieEntry;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import vn.ngoviethoang.duancuoiky.data.repository.ChiTieuRepository;
//import vn.ngoviethoang.duancuoiky.data.repository.ThuNhapRepository;
//import vn.ngoviethoang.duancuoiky.data.entity.ChiTieu;
//
//public class DashboardViewModel extends AndroidViewModel {
//
//    private ChiTieuRepository chiTieuRepository;
//    private ThuNhapRepository thuNhapRepository;
//
//    private final MutableLiveData<Double> income = new MutableLiveData<>();
//    private final MutableLiveData<Double> expense = new MutableLiveData<>();
//    private final MutableLiveData<Double> balance = new MutableLiveData<>();
//    private final MutableLiveData<List<ChiTieu>> recentTransactions = new MutableLiveData<>();
//    private final MutableLiveData<List<PieEntry>> chartData = new MutableLiveData<>();
//
//    public DashboardViewModel(Application application) {
//        super(application);
//        chiTieuRepository = new ChiTieuRepository(application);
//        thuNhapRepository = new ThuNhapRepository(application);
//
//        loadData();
//    }
//
//    // Method to load data from repositories
//    private void loadData() {
//        // Fetching income and expense data
//        income.setValue(thuNhapRepository.getTotalIncome());
//        expense.setValue(getTotalExpense());
//        balance.setValue(income.getValue() - expense.getValue());
//
//        // Get recent transactions
//        Date startDate = new Date();  // Adjust this logic for a custom date range
//        Date endDate = new Date();
//        recentTransactions.setValue(chiTieuRepository.getChiTieuByDateRange(startDate, endDate).getValue());
//
//        // Creating PieChart data for the Pie Chart visualization
//        List<PieEntry> chartDataList = new ArrayList<>();
//        chartDataList.add(new PieEntry(income.getValue().floatValue(), "Income"));
//        chartDataList.add(new PieEntry(expense.getValue().floatValue(), "Expense"));
//        chartData.setValue(chartDataList);
//    }
//
//    public LiveData<Double> getIncome() {
//        return income;
//    }
//
//    public LiveData<Double> getExpense() {
//        return expense;
//    }
//
//    public LiveData<Double> getBalance() {
//        return balance;
//    }
//
//    public LiveData<List<ChiTieu>> getRecentTransactions() {
//        return recentTransactions;
//    }
//
//    public LiveData<List<PieEntry>> getChartData() {
//        return chartData;
//    }
//
//    // Calculate the total expense from the repository
//    private double getTotalExpense() {
//        double totalExpense = 0;
//        List<ChiTieu> chiTieuList = chiTieuRepository.getAllChiTieu();  // Fetch all ChiTieu
//        if (chiTieuList != null) {
//            for (ChiTieu chiTieu : chiTieuList) {
//                totalExpense += chiTieu.getAmount();  // Assuming ChiTieu has an 'amount' field
//            }
//        }
//        return totalExpense;
//    }
//}