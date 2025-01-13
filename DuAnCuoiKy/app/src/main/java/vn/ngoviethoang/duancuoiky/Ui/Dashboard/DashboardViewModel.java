package vn.ngoviethoang.duancuoiky.Ui.Dashboard;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    private Calendar currentCalendar;

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        currentCalendar = Calendar.getInstance();
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
        SimpleDateFormat formatter;

        switch (range) {
            case "day":
                formatter = new SimpleDateFormat("dd/MM/yyyy");
                currentCalendar.add(Calendar.DATE, direction); // Tăng/giảm 1 ngày
                dateRange.setValue(formatter.format(currentCalendar.getTime()));
                break;
            case "week":
                formatter = new SimpleDateFormat("dd/MM/yyyy");
                currentCalendar.add(Calendar.WEEK_OF_YEAR, direction); // Tăng/giảm 1 tuần
                currentCalendar.set(Calendar.DAY_OF_WEEK, currentCalendar.getFirstDayOfWeek());
                String startOfWeek = formatter.format(currentCalendar.getTime());
                currentCalendar.add(Calendar.DATE, 6);
                String endOfWeek = formatter.format(currentCalendar.getTime());
                dateRange.setValue(startOfWeek + " - " + endOfWeek);
                break;
            case "month":
                formatter = new SimpleDateFormat("MM/yyyy");
                currentCalendar.add(Calendar.MONTH, direction); // Tăng/giảm 1 tháng
                dateRange.setValue(formatter.format(currentCalendar.getTime()));
                break;
            case "year":
                formatter = new SimpleDateFormat("yyyy");
                currentCalendar.add(Calendar.YEAR, direction); // Tăng/giảm 1 năm
                dateRange.setValue(String.valueOf(currentCalendar.get(Calendar.YEAR)));
                break;
        }
    }

    public void navigateDateRange(int direction) {
        String currentRange = dateRange.getValue();
        if (currentRange != null) {
            if (currentRange.contains("-")) {
                updateDateRange("week", direction);
            } else if (currentRange.contains("/")) {
                if (currentRange.length() == 10) {
                    updateDateRange("day", direction);
                } else if (currentRange.length() == 7) {
                    updateDateRange("month", direction);
                }
            } else if (currentRange.length() == 4) {
                updateDateRange("year", direction);
            }
        }
    }

    public LiveData<List<GiaoDich>> getFilteredTransactions(String loai, String range) {
        MutableLiveData<List<GiaoDich>> filteredTransactions = new MutableLiveData<>();

        giaoDichRepository.getAllGiaoDich().observeForever(transactions -> {
            List<GiaoDich> filtered = new ArrayList<>();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

            try {
                if (range.contains("-")) {
                    // Lọc theo tuần
                    String[] dates = range.split(" - ");
                    Date startDate = formatter.parse(dates[0]);
                    Date endDate = formatter.parse(dates[1]);

                    for (GiaoDich transaction : transactions) {
                        if (transaction.getLoai().equals(loai)) {
                            Date transactionDate = formatter.parse(transaction.getNgay());
                            if (transactionDate != null && !transactionDate.before(startDate) && !transactionDate.after(endDate)) {
                                filtered.add(transaction);
                            }
                        }
                    }
                } else if (range.contains("/")) {
                    if (range.length() == 10) {
                        // Lọc theo ngày
                        Date rangeDate = formatter.parse(range);

                        for (GiaoDich transaction : transactions) {
                            if (transaction.getLoai().equals(loai)) {
                                Date transactionDate = formatter.parse(transaction.getNgay());
                                if (transactionDate != null && transactionDate.equals(rangeDate)) {
                                    filtered.add(transaction);
                                }
                            }
                        }
                    } else if (range.length() == 7) {
                        // Lọc theo tháng
                        String[] parts = range.split("/");
                        int month = Integer.parseInt(parts[0]) - 1; // Tháng bắt đầu từ 0
                        int year = Integer.parseInt(parts[1]);

                        for (GiaoDich transaction : transactions) {
                            if (transaction.getLoai().equals(loai)) {
                                Date transactionDate = formatter.parse(transaction.getNgay());
                                if (transactionDate != null) {
                                    Calendar transactionCalendar = Calendar.getInstance();
                                    transactionCalendar.setTime(transactionDate);

                                    if (transactionCalendar.get(Calendar.MONTH) == month &&
                                            transactionCalendar.get(Calendar.YEAR) == year) {
                                        filtered.add(transaction);
                                    }
                                }
                            }
                        }
                    }
                } else if (range.length() == 4) {
                    // Lọc theo năm
                    try {
                        int year = Integer.parseInt(range); // Chuyển đổi range thành số nguyên

                        for (GiaoDich transaction : transactions) {
                            if (transaction.getLoai().equals(loai)) {
                                Date transactionDate = formatter.parse(transaction.getNgay());
                                if (transactionDate != null) {
                                    Calendar transactionCalendar = Calendar.getInstance();
                                    transactionCalendar.setTime(transactionDate);

                                    if (transactionCalendar.get(Calendar.YEAR) == year) {
                                        filtered.add(transaction);
                                    }
                                }
                            }
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        // Xử lý lỗi nếu range không phải là số hợp lệ
                        Log.e("DashboardViewModel", "Invalid year format: " + range);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            filteredTransactions.setValue(filtered);
        });

        return filteredTransactions;
    }

    public void updateCustomDateRange(String start, String end) {
        dateRange.setValue(start + " - " + end);
    }

    public void switchTab(String tab) {
        tabSelected.setValue(tab);
    }

    public void initializeDashboard() {
        updateDateRange("day", 0); // Mặc định phạm vi thời gian là ngày
        switchTab("expenses"); // Mặc định tab là chi phí
        loadAccounts(); // Tải danh sách tài khoản
        loadTransactions(); // Tải danh sách giao dịch
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
}