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
    private final MutableLiveData<Double> totalBalance = new MutableLiveData<Double>();
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

    public LiveData<Double> getTotalBalance() {
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
                dateRange.setValue("Hôm nay: " + formatter.format(calendar.getTime()));
                break;
            case "week":
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                String weekStart = formatter.format(calendar.getTime());
                calendar.add(Calendar.DAY_OF_WEEK, 6);
                dateRange.setValue(weekStart + " - " + formatter.format(calendar.getTime()));
                break;
            case "month":
                dateRange.setValue((calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR));
                break;
            case "year":
                dateRange.setValue("Năm: " + calendar.get(Calendar.YEAR));
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

    public void loadTransactions() {
        giaoDichRepository.getAllGiaoDich().observeForever(transactions::setValue);
    }

    public LiveData<DanhMuc> getCategoryById(int categoryId) {
        MutableLiveData<DanhMuc> danhMucLiveData = new MutableLiveData<>();
        danhMucRepository.getDanhMucById(categoryId, danhMucLiveData::setValue);
        return danhMucLiveData;
    }

    public void getCategoryIconById(int categoryId, DanhMucRepository.OnDanhMucLoadedListener listener) {
        danhMucRepository.getDanhMucById(categoryId, listener);
    }

    public LiveData<List<GiaoDich>> getTransactionsByDateRange(String range) {
        MutableLiveData<List<GiaoDich>> filteredTransactions = new MutableLiveData<>();
        giaoDichRepository.getAllGiaoDich().observeForever(transactions -> {
            filteredTransactions.setValue(filterTransactionsByDateRange(transactions, range));
        });
        return filteredTransactions;
    }

    private List<GiaoDich> filterTransactionsByDateRange(List<GiaoDich> transactions, String range) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        switch (range) {
            case "day":
                return filterTransactionsByDay(transactions, formatter.format(calendar.getTime()));
            case "week":
                return filterTransactionsByWeek(transactions, calendar, formatter);
            case "month":
                return filterTransactionsByMonth(transactions, calendar, formatter);
            case "year":
                return filterTransactionsByYear(transactions, calendar, formatter);
            default:
                return transactions;
        }
    }

    private List<GiaoDich> filterTransactionsByDay(List<GiaoDich> transactions, String today) {
        return transactions.stream()
                .filter(transaction -> transaction.getNgay().equals(today))
                .collect(Collectors.toList());
    }

    private List<GiaoDich> filterTransactionsByWeek(List<GiaoDich> transactions, Calendar calendar, SimpleDateFormat formatter) {
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String weekStart = formatter.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_WEEK, 6);
        String weekEnd = formatter.format(calendar.getTime());
        return transactions.stream()
                .filter(transaction -> {
                    String transactionDate = transaction.getNgay();
                    return transactionDate.compareTo(weekStart) >= 0 && transactionDate.compareTo(weekEnd) <= 0;
                })
                .collect(Collectors.toList());
    }

    private List<GiaoDich> filterTransactionsByMonth(List<GiaoDich> transactions, Calendar calendar, SimpleDateFormat formatter) {
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        return transactions.stream()
                .filter(transaction -> {
                    try {
                        Date transactionDate = formatter.parse(transaction.getNgay());
                        Calendar transactionCalendar = Calendar.getInstance();
                        transactionCalendar.setTime(transactionDate);
                        return transactionCalendar.get(Calendar.MONTH) == month && transactionCalendar.get(Calendar.YEAR) == year;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    private List<GiaoDich> filterTransactionsByYear(List<GiaoDich> transactions, Calendar calendar, SimpleDateFormat formatter) {
        int currentYear = calendar.get(Calendar.YEAR);
        return transactions.stream()
                .filter(transaction -> {
                    try {
                        Date transactionDate = formatter.parse(transaction.getNgay());
                        Calendar transactionCalendar = Calendar.getInstance();
                        transactionCalendar.setTime(transactionDate);
                        return transactionCalendar.get(Calendar.YEAR) == currentYear;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    public LiveData<List<GiaoDich>> getFilteredTransactions(String loai, String range) {
        MutableLiveData<List<GiaoDich>> filteredTransactions = new MutableLiveData<>();
        giaoDichRepository.getAllGiaoDich().observeForever(transactions -> {
            List<GiaoDich> filteredList = transactions.stream()
                    .filter(transaction -> transaction.getLoai().equals(loai))
                    .filter(transaction -> filterByDate(transaction, range))
                    .collect(Collectors.toList());
            filteredTransactions.setValue(filteredList);
        });
        return filteredTransactions;
    }

    private boolean filterByDate(GiaoDich transaction, String range) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date transactionDate = formatter.parse(transaction.getNgay());
            Calendar calendar = Calendar.getInstance();

            switch (range) {
                case "day":
                    boolean isSameDay = formatter.format(transactionDate).equals(formatter.format(calendar.getTime()));
                    return isSameDay;
                case "week":
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                    Date weekStart = calendar.getTime();
                    calendar.add(Calendar.DAY_OF_WEEK, 6);
                    Date weekEnd = calendar.getTime();
                    boolean isInWeek = !transactionDate.before(weekStart) && !transactionDate.after(weekEnd);
                    return isInWeek;
                case "month":
                    Calendar transactionCalendar = Calendar.getInstance();
                    transactionCalendar.setTime(transactionDate);
                    boolean isSameMonth = transactionCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                            transactionCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR);
                    return isSameMonth;
                case "year":
                    Calendar transactionCalendarYear = Calendar.getInstance();
                    transactionCalendarYear.setTime(transactionDate);
                    boolean isSameYear = transactionCalendarYear.get(Calendar.YEAR) == calendar.get(Calendar.YEAR);
                    return isSameYear;
                default:
                    return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}