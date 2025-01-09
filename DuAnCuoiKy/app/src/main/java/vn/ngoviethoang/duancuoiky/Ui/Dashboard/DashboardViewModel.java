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
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
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
    private final MutableLiveData<Double> totalBalance = new MutableLiveData<>();
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

    public LiveData<String> getDateRange() {
        return dateRange;
    }

    public LiveData<String> getTabSelected() {
        return tabSelected;
    }

    public LiveData<List<TaiKhoan>> getAccounts() {
        return accounts;
    }

    public LiveData<Double> getTotalBalance() {
        return totalBalance;
    }

    public LiveData<List<GiaoDich>> getTransactions() {
        return transactions;
    }

    public void updateDateRange(String range, int direction) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();

        switch (range) {
            case "day":
                calendar.add(Calendar.DAY_OF_MONTH, direction); // Thêm hoặc bớt ngày
                dateRange.setValue("Hôm nay: " + formatter.format(calendar.getTime()));
                break;
            case "week":
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                calendar.add(Calendar.DAY_OF_WEEK, direction * 7); // Thêm hoặc bớt tuần
                String weekStart = formatter.format(calendar.getTime());
                calendar.add(Calendar.DAY_OF_WEEK, 6);
                dateRange.setValue(weekStart + " - " + formatter.format(calendar.getTime()));
                break;
            case "month":
                calendar.add(Calendar.MONTH, direction); // Thêm hoặc bớt tháng
                dateRange.setValue((calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR));
                break;
            case "year":
                calendar.add(Calendar.YEAR, direction); // Thêm hoặc bớt năm
                dateRange.setValue("Năm: " + calendar.get(Calendar.YEAR));
                break;
            default:
                dateRange.setValue("Chọn thời gian");
                break;
        }
    }


    public void navigateDateRange(int direction) {
        String currentRange = dateRange.getValue();
        if (currentRange != null) {
            if (currentRange.contains("-")) {
                updateDateRange("week", direction); // Xử lý tuần
            } else if (currentRange.contains("/")) {
                if (currentRange.length() == 10) {
                    updateDateRange("day", direction); // Xử lý ngày
                } else if (currentRange.length() == 7) {
                    updateDateRange("month", direction); // Xử lý tháng
                } else if (currentRange.length() == 4) {
                    updateDateRange("year", direction); // Xử lý năm
                }
            }
        }
    }

    public void updateCustomDateRange(String start, String end) {
        dateRange.setValue(start + " - " + end);
    }

    public void switchTab(String tab) {
        tabSelected.setValue(tab);
    }

    public void initializeDashboard() {
        updateDateRange("day", 0);
        switchTab("expenses");
    }

    public void loadAccounts() {
        taiKhoanRepository.getAllTaiKhoan().observeForever(accounts::setValue);
        taiKhoanRepository.getAllTaiKhoan().observeForever(this::updateTotalBalance);
    }

    public void loadTransactions() {
        giaoDichRepository.getAllGiaoDich().observeForever(transactions::setValue);
    }

    private void updateTotalBalance(List<TaiKhoan> accounts) {
        double total = accounts.stream().mapToDouble(TaiKhoan::getSodu).sum();
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
        return accounts.getValue() != null ? accounts.getValue().stream()
                .filter(account -> account.getTen().equals(accountName))
                .findFirst()
                .orElse(null) : null;
    }

    public LiveData<DanhMuc> getCategoryById(int categoryId) {
        MutableLiveData<DanhMuc> danhMucLiveData = new MutableLiveData<>();
        danhMucRepository.getDanhMucById(categoryId, danhMucLiveData::setValue);
        return danhMucLiveData;
    }

    public void getCategoryIconById(int categoryId, DanhMucRepository.OnDanhMucLoadedListener listener) {
        danhMucRepository.getDanhMucById(categoryId, listener);
    }

    public LiveData<List<GiaoDich>> getFilteredTransactions(String loai, String range) {
        MutableLiveData<List<GiaoDich>> filteredTransactions = new MutableLiveData<>();
        giaoDichRepository.getAllGiaoDich().observeForever(transactions -> {
            filteredTransactions.setValue(filterTransactions(transactions, range, loai));
        });
        return filteredTransactions;
    }

    private List<GiaoDich> filterTransactions(List<GiaoDich> transactions, String range, String loai) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        return transactions.stream()
                .filter(transaction -> loai == null || transaction.getLoai().equals(loai))
                .filter(transaction -> {
                    try {
                        Date transactionDate = formatter.parse(transaction.getNgay());
                        Calendar transactionCalendar = Calendar.getInstance();
                        transactionCalendar.setTime(transactionDate);

                        switch (range) {
                            case "day":
                                return isSameDay(transactionCalendar, calendar);
                            case "week":
                                return isSameWeek(transactionCalendar, calendar);
                            case "month":
                                return isSameMonth(transactionCalendar, calendar);
                            case "year":
                                return isSameYear(transactionCalendar, calendar);
                            default:
                                return true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    private boolean isSameDay(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }

    private boolean isSameWeek(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR);
    }

    private boolean isSameMonth(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
    }

    private boolean isSameYear(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
    }


}
