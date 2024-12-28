package vn.ngoviethoang.duancuoiky.Ui.Dashboard;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import vn.ngoviethoang.duancuoiky.R;
import vn.ngoviethoang.duancuoiky.data.entity.DanhMuc;
import vn.ngoviethoang.duancuoiky.data.entity.GiaoDich;
import vn.ngoviethoang.duancuoiky.data.entity.TaiKhoan;
import vn.ngoviethoang.duancuoiky.data.repository.DanhMucRepository;
import vn.ngoviethoang.duancuoiky.data.repository.GiaoDichRepository;
import vn.ngoviethoang.duancuoiky.data.repository.TaiKhoanRepository;

public class DashboardViewModel extends AndroidViewModel {
    private final MutableLiveData<String> dateRange = new MutableLiveData<>();
    private final MutableLiveData<String> tabSelected = new MutableLiveData<>();
    private final MutableLiveData<List<TaiKhoan>> accounts = new MutableLiveData<>();
    private final MutableLiveData<Integer> totalBalance = new MutableLiveData<>();
    private final MutableLiveData<List<GiaoDich>> transactions = new MutableLiveData<>();

    private final TaiKhoanRepository taiKhoanRepository;
    private final GiaoDichRepository giaoDichRepository;
    private final DanhMucRepository danhMucRepository;

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        taiKhoanRepository = new TaiKhoanRepository(application);
        giaoDichRepository = new GiaoDichRepository(application);
        danhMucRepository = new DanhMucRepository(application);
        loadAccounts();
        loadTransactions();
    }

    public LiveData<Integer> getTotalBalance() {
        return totalBalance;
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

    public LiveData<List<GiaoDich>> getTransactions() {
        return transactions;
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

    public void initializeDashboard() {
        updateDateRange("day");
        switchTab("expenses");
    }

    public void loadAccounts() {
        taiKhoanRepository.getAllTaiKhoan().observeForever(accounts -> {
            this.accounts.setValue(accounts);
            updateTotalBalance(accounts);
        });
    }

    private void updateTotalBalance(List<TaiKhoan> accounts) {
        int total = 0;
        for (TaiKhoan account : accounts) {
            total += account.getSodu();
        }
        totalBalance.setValue(total);
    }

    public void updateBalance(TaiKhoan taiKhoan) {
        taiKhoanRepository.updateTaiKhoan(taiKhoan);
        loadAccounts();
    }

    public byte[] getAccountIconBytes() {
        Bitmap bitmap = BitmapFactory.decodeResource(getApplication().getResources(), R.drawable.ic_account1);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }

    public TaiKhoan getAccountByName(String accountName) {
        if (accounts.getValue() != null) {
            for (TaiKhoan account : accounts.getValue()) {
                if (account.getTen().equals(accountName)) {
                    return account;
                }
            }
        }
        return null;
    }

    public void loadTransactions() {
        giaoDichRepository.getAllGiaoDich().observeForever(transactions::setValue);
    }

    public String getCategoryNameById(int categoryId) {
        DanhMuc category = danhMucRepository.getDanhMucById(categoryId);
        return category != null ? category.getTenDanhMuc() : "Unknown";
    }

    public Bitmap getCategoryIconById(int categoryId) {
        DanhMuc category = danhMucRepository.getDanhMucById(categoryId);
        if (category != null) {
            byte[] iconBytes = category.getIcon();
            return BitmapFactory.decodeByteArray(iconBytes, 0, iconBytes.length);
        }
        return BitmapFactory.decodeResource(getApplication().getResources(), R.drawable.ic_unknown);
    }
}