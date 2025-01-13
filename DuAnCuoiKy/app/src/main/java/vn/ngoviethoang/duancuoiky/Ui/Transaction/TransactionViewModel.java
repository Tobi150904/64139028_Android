package vn.ngoviethoang.duancuoiky.Ui.Transaction;

import android.app.Application;
import android.icu.text.SimpleDateFormat;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import vn.ngoviethoang.duancuoiky.data.entity.DanhMuc;
import vn.ngoviethoang.duancuoiky.data.entity.GiaoDich;
import vn.ngoviethoang.duancuoiky.data.entity.TaiKhoan;
import vn.ngoviethoang.duancuoiky.data.repository.DanhMucRepository;
import vn.ngoviethoang.duancuoiky.data.repository.GiaoDichRepository;
import vn.ngoviethoang.duancuoiky.data.repository.TaiKhoanRepository;

public class TransactionViewModel extends AndroidViewModel {
    private final DanhMucRepository danhMucRepository;
    private final GiaoDichRepository giaoDichRepository;
    private final TaiKhoanRepository taiKhoanRepository;
    private final MutableLiveData<GiaoDich> giaoDichLiveData;
    private final MutableLiveData<List<GiaoDich>> transactions;
    private final MutableLiveData<String> errorMessage;
    private final MutableLiveData<String> dateRange;
    private final MutableLiveData<List<GiaoDich>> transactionsLiveData;
    private LiveData<List<GiaoDich>> giaoDichList;
    private LiveData<List<TaiKhoan>> accounts;
    private Calendar currentCalendar;
    private String selectedType;
    private String selectedRange;
    private Date startDate;
    private Date endDate;
    private List<GiaoDich> allTransactions;
    private List<GiaoDich> filteredTransactions;
    private final MutableLiveData<Double> totalAmountLiveData = new MutableLiveData<>();


    public TransactionViewModel(@NonNull Application application) {
        super(application);
        giaoDichRepository = new GiaoDichRepository(application);
        taiKhoanRepository = new TaiKhoanRepository(application);
        danhMucRepository = new DanhMucRepository(application);
        giaoDichLiveData = new MutableLiveData<>();
        transactions = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
        dateRange = new MutableLiveData<>();
        transactionsLiveData = new MutableLiveData<>();
        accounts = taiKhoanRepository.getAllTaiKhoan();
        currentCalendar = Calendar.getInstance();
        allTransactions = new ArrayList<>();
        filteredTransactions = new ArrayList<>();
        loadTransactions();
        selectedType = "chi_phi";
        selectedRange = "day";
    }

    // Thêm giao dịch mới
    public void addTransaction(GiaoDich giaoDich) {
        giaoDichRepository.insertGiaoDich(giaoDich, giaoDichLiveData, errorMessage);
        updateAccountBalance(giaoDich);
    }

    // Cập nhật số dư tài khoản sau khi thêm giao dịch
    private void updateAccountBalance(GiaoDich giaoDich) {
        LiveData<TaiKhoan> accountLiveData = taiKhoanRepository.getTaiKhoanById(giaoDich.getTaiKhoanId());
        accountLiveData.observeForever(new Observer<TaiKhoan>() {
            @Override
            public void onChanged(TaiKhoan account) {
                if (account != null) {
                    double newBalance = account.getSodu();
                    newBalance += "thu_nhap".equals(giaoDich.getLoai()) ? giaoDich.getSoTien() : -giaoDich.getSoTien();
                    account.setSodu(newBalance);
                    taiKhoanRepository.updateTaiKhoan(account);
                    accountLiveData.removeObserver(this);
                }
            }
        });
    }

    // Lấy giao dịch hiện tại
    public LiveData<GiaoDich> getGiaoDich() {
        return giaoDichLiveData;
    }

    // Lấy thông báo lỗi
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    // Lấy phạm vi ngày hiện tại
    public LiveData<String> getDateRange() {
        return dateRange;
    }

    // Lấy danh sách giao dịch
    public LiveData<List<GiaoDich>> getGiaoDichList() {
        return transactionsLiveData;
    }

    // Tải danh sách giao dịch
    public void loadTransactions() {
        giaoDichRepository.getAllGiaoDich().observeForever(transactions -> {
            if (transactions != null) {
                allTransactions = transactions;
                loadFilteredTransactions();
            }
        });
    }

    // Lấy danh mục theo ID
    public LiveData<DanhMuc> getCategoryById(int categoryId) {
        MutableLiveData<DanhMuc> danhMucLiveData = new MutableLiveData<>();
        danhMucRepository.getDanhMucById(categoryId, danhMucLiveData::setValue);
        return danhMucLiveData;
    }

    // Lấy icon danh mục theo ID
    public void getCategoryIconById(int categoryId, DanhMucRepository.OnDanhMucLoadedListener listener) {
        danhMucRepository.getDanhMucById(categoryId, listener);
    }

    // Cập nhật phạm vi ngày
    public void updateDateRange(String range, int direction) {
        SimpleDateFormat formatter;
        switch (range) {
            case "day":
                formatter = new SimpleDateFormat("dd/MM/yyyy");
                if (direction == 0) {
                    currentCalendar = Calendar.getInstance();
                } else {
                    currentCalendar.add(Calendar.DATE, direction);
                }
                startDate = currentCalendar.getTime();
                endDate = startDate;
                dateRange.setValue(formatter.format(startDate));
                break;

            case "week":
                formatter = new SimpleDateFormat("dd/MM/yyyy");
                if (direction == 0) {
                    currentCalendar = Calendar.getInstance();
                    currentCalendar.set(Calendar.DAY_OF_WEEK, currentCalendar.getFirstDayOfWeek());
                } else {
                    currentCalendar.add(Calendar.WEEK_OF_YEAR, direction);
                }
                startDate = currentCalendar.getTime();
                currentCalendar.add(Calendar.DATE, 6);
                endDate = currentCalendar.getTime();
                dateRange.setValue(formatter.format(startDate) + " - " + formatter.format(endDate));
                break;

            case "month":
                formatter = new SimpleDateFormat("MM/yyyy");
                if (direction == 0) {
                    currentCalendar = Calendar.getInstance();
                } else {
                    currentCalendar.add(Calendar.MONTH, direction);
                }
                startDate = getFirstDateOfMonth(currentCalendar);
                endDate = getLastDateOfMonth(currentCalendar);
                dateRange.setValue(formatter.format(startDate));
                break;

            case "year":
                formatter = new SimpleDateFormat("yyyy");
                if (direction == 0) {
                    currentCalendar = Calendar.getInstance();
                } else {
                    currentCalendar.add(Calendar.YEAR, direction);
                }
                startDate = getFirstDateOfYear(currentCalendar);
                endDate = getLastDateOfYear(currentCalendar);
                dateRange.setValue(formatter.format(startDate));
                break;
        }
        Log.d("DEBUG", "Range: " + range + ", Start Date: " + startDate + ", End Date: " + endDate);
        loadFilteredTransactions();
    }

    // Lấy ngày đầu tiên của tháng
    private Date getFirstDateOfMonth(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    // Lấy ngày cuối cùng của tháng
    private Date getLastDateOfMonth(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    // Lấy ngày đầu tiên của năm
    private Date getFirstDateOfYear(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        return calendar.getTime();
    }

    // Lấy ngày cuối cùng của năm
    private Date getLastDateOfYear(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
        return calendar.getTime();
    }

    // Điều hướng phạm vi ngày
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
                } else if (currentRange.length() == 4) {
                    updateDateRange("year", direction);
                }
            }
        }
    }

    // Cập nhật giao dịch
    public void updateTransactions(String type, String range) {
        selectedType = type;
        selectedRange = range;
        updateDateRange(range, 0);
        loadFilteredTransactions();
    }

    public LiveData<Double> getTotalAmountLiveData() {
        return totalAmountLiveData;
    }

    // Tải danh sách giao dịch đã lọc
    private void loadFilteredTransactions() {
        filteredTransactions.clear();
        double totalAmount = 0;

        for (GiaoDich giaoDich : allTransactions) {
            if (giaoDich.getLoai().equals(selectedType)) {
                if (giaoDich.getNgay() != null && !giaoDich.getNgay().isEmpty()) {
                    try {
                        Date giaoDichDate = new SimpleDateFormat("dd/MM/yyyy").parse(giaoDich.getNgay());
                        if (giaoDichDate != null) {
                            if (selectedRange.equals("day") && isSameDay(giaoDichDate, startDate)) {
                                filteredTransactions.add(giaoDich);
                                totalAmount += giaoDich.getSoTien();
                            } else if (selectedRange.equals("week") && isSameWeek(giaoDichDate, startDate, endDate)) {
                                filteredTransactions.add(giaoDich);
                                totalAmount += giaoDich.getSoTien();
                            } else if (selectedRange.equals("month") && isSameMonth(giaoDichDate, startDate)) {
                                filteredTransactions.add(giaoDich);
                                totalAmount += giaoDich.getSoTien();
                            } else if (selectedRange.equals("year") && isSameYear(giaoDichDate, startDate)) {
                                filteredTransactions.add(giaoDich);
                                totalAmount += giaoDich.getSoTien();
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Log.e("DEBUG", "Lỗi ngày: " + giaoDich.getNgay());
                    }
                } else {
                    Log.e("DEBUG", "Ngày giao dịch rỗng: " + giaoDich.getNgay());
                }
            }
        }

        totalAmountLiveData.setValue(totalAmount);
        transactionsLiveData.setValue(filteredTransactions);
    }

    // Kiểm tra xem hai ngày có cùng ngày không
    private boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }

    // Kiểm tra xem ngày có nằm trong cùng tuần không
    private boolean isSameWeek(Date date, Date startDate, Date endDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Calendar calStart = Calendar.getInstance();
        calStart.setTime(startDate);
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(endDate);
        return cal.get(Calendar.YEAR) == calStart.get(Calendar.YEAR) &&
                cal.get(Calendar.WEEK_OF_YEAR) == calStart.get(Calendar.WEEK_OF_YEAR);
    }

    // Kiểm tra xem ngày có nằm trong cùng tháng không
    private boolean isSameMonth(Date date, Date startDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        cal.setTime(startDate);
        return month == cal.get(Calendar.MONTH) && year == cal.get(Calendar.YEAR);
    }

    // Kiểm tra xem ngày có nằm trong cùng năm không
    private boolean isSameYear(Date date, Date startDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        cal.setTime(startDate);
        return year == cal.get(Calendar.YEAR);
    }
}