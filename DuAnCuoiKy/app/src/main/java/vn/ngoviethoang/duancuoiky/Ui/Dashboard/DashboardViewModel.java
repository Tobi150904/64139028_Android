// DashboardViewModel.java
package vn.ngoviethoang.duancuoiky.Ui.Dashboard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import vn.ngoviethoang.duancuoiky.data.entity.TaiKhoan;
import vn.ngoviethoang.duancuoiky.data.repository.TaiKhoanRepository;

public class DashboardViewModel extends AndroidViewModel {
    private final MutableLiveData<String> dateRange = new MutableLiveData<>();
    private final MutableLiveData<String> tabSelected = new MutableLiveData<>();
    private final MutableLiveData<List<TaiKhoan>> accounts = new MutableLiveData<>();

    private final TaiKhoanRepository taiKhoanRepository;
    private final LiveData<List<TaiKhoan>> allAccounts;
    private final MutableLiveData<TaiKhoan> balance = new MutableLiveData<>();

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        taiKhoanRepository = new TaiKhoanRepository(application);
        allAccounts = taiKhoanRepository.getAllTaiKhoan();
        allAccounts.observeForever(accounts -> {
            this.accounts.setValue(accounts);
            if (accounts != null && !accounts.isEmpty()) {
                balance.setValue(accounts.get(0)); // Assuming the first account is the main account
            }
        });
    }

    public LiveData<TaiKhoan> getBalance() {
        return balance;
    }

    public void updateBalance(TaiKhoan taiKhoan) {
        taiKhoanRepository.updateTaiKhoan(taiKhoan);
    }

    public LiveData<String> getDateRange() {
        return dateRange;
    }

    public LiveData<String> getTabSelected() {
        return tabSelected;
    }

    public LiveData<List<TaiKhoan>> getAccounts() {
        return accounts;
    }

    public void updateDateRange(String range) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();

        switch (range) {
            case "day":
                String today = formatter.format(calendar.getTime());
                dateRange.setValue("Hôm nay: " + today);
                break;

            case "week":
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                String weekStart = formatter.format(calendar.getTime());
                calendar.add(Calendar.DAY_OF_WEEK, 6);
                String weekEnd = formatter.format(calendar.getTime());
                dateRange.setValue(weekStart + " - " + weekEnd);
                break;

            case "month":
                int month = calendar.get(Calendar.MONTH) + 1;
                int year = calendar.get(Calendar.YEAR);
                dateRange.setValue(month + "/" + year);
                break;

            case "year":
                int currentYear = calendar.get(Calendar.YEAR);
                dateRange.setValue("Năm: " + currentYear);
                break;

            default:
                dateRange.setValue("Chọn thời gian");
        }
    }

    public void updateCustomDateRange(String start, String end) {
        dateRange.setValue(start + " - " + end);
    }

    public void switchTab(String tab) {
        tabSelected.setValue(tab);
    }
}