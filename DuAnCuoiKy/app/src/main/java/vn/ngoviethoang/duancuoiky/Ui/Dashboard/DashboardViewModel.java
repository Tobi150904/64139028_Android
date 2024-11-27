//package vn.ngoviethoang.duancuoiky.ui.dashboard;
//
//import android.app.Application;
//import androidx.lifecycle.AndroidViewModel;
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//import androidx.lifecycle.ViewModel;
//import com.github.mikephil.charting.data.PieEntry;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import vn.ngoviethoang.duancuoiky.Data.Repository.ChiTieuRepository;
//import vn.ngoviethoang.duancuoiky.Data.Repository.ThuNhapRepository;
//import vn.ngoviethoang.duancuoiky.Data.Entity.ChiTieu;
//import vn.ngoviethoang.duancuoiky.Models.TransactionModel;
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
//        // Fetching income and expense data (can be fetched from your thuNhapRepository or API)
//        income.setValue(thuNhapRepository.getTotalIncome());  // You should implement getTotalIncome in ThuNhapRepository
//        expense.setValue(getTotalExpense());  // Fetch the total expense
//        balance.setValue(income.getValue() - expense.getValue());  // Balance = Income - Expense
//
//        // Get recent transactions (e.g., within a specific date range)
//        Date startDate = new Date();  // You can adjust this logic for a custom date range
//        Date endDate = new Date();
//        recentTransactions.setValue(chiTieuRepository.getChiTieuByDateRange(startDate, endDate).getValue());
//
//        // Creating PieChart data for the Pie Chart visualization
//        List<PieEntry> chartDataList = new ArrayList<>();
//        chartDataList.add(new PieEntry((float) income.getValue(), "Income"));
//        chartDataList.add(new PieEntry((float) expense.getValue(), "Expense"));
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
//        List<ChiTieu> chiTieuList = recentTransactions.getValue();  // Get data from LiveData
//        if (chiTieuList != null) {
//            for (ChiTieu chiTieu : chiTieuList) {
//                totalExpense += chiTieu.getAmount();  // Assuming ChiTieu has an 'amount' field
//            }
//        }
//        return totalExpense;
//    }
//}
