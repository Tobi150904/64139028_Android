package vn.ngoviethoang.duancuoiky.Ui.Transaction;

import android.app.Application;
import android.icu.text.SimpleDateFormat;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import vn.ngoviethoang.duancuoiky.data.entity.DanhMuc;
import vn.ngoviethoang.duancuoiky.data.entity.GiaoDich;
import vn.ngoviethoang.duancuoiky.data.entity.TaiKhoan;
import vn.ngoviethoang.duancuoiky.data.repository.DanhMucRepository;
import vn.ngoviethoang.duancuoiky.data.repository.GiaoDichRepository;
import vn.ngoviethoang.duancuoiky.data.repository.TaiKhoanRepository;
import vn.ngoviethoang.duancuoiky.data.repository.DanhMucRepository;

public class TransactionViewModel extends AndroidViewModel {
    private final DanhMucRepository danhMucRepository;
    private final GiaoDichRepository giaoDichRepository;
    private final TaiKhoanRepository taiKhoanRepository;
    private final MutableLiveData<GiaoDich> giaoDichLiveData;
    private final MutableLiveData<List<GiaoDich>> transactions;
    private final MutableLiveData<String> errorMessage;
    private final MutableLiveData<String> dateRange;
    private LiveData<List<GiaoDich>> giaoDichList;
    private LiveData<List<TaiKhoan>> accounts;
    private Calendar currentCalendar;

    public TransactionViewModel(@NonNull Application application) {
        super(application);
        giaoDichRepository = new GiaoDichRepository(application);
        taiKhoanRepository = new TaiKhoanRepository(application);
        danhMucRepository = new DanhMucRepository(application);
        giaoDichLiveData = new MutableLiveData<>();
        transactions = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
        dateRange = new MutableLiveData<>();
        accounts = taiKhoanRepository.getAllTaiKhoan();
        currentCalendar = Calendar.getInstance();
        giaoDichList = new MutableLiveData<>();
        loadTransactions();
    }

    // Thêm giao dịch mới và cập nhật số dư tài khoản
    public void addTransaction(GiaoDich giaoDich) {
        giaoDichRepository.insertGiaoDich(giaoDich, giaoDichLiveData, errorMessage);
        updateAccountBalance(giaoDich);
    }

    // Cập nhật số dư tài khoản dựa trên loại giao dịch
    private void updateAccountBalance(GiaoDich giaoDich) {
        LiveData<TaiKhoan> accountLiveData = taiKhoanRepository.getTaiKhoanById(giaoDich.getTaiKhoanId());
        accountLiveData.observeForever(new Observer<TaiKhoan>() {
            @Override
            public void onChanged(TaiKhoan account) {
                if (account != null) {
                    double newBalance = account.getSodu();
                    if ("thu_nhap".equals(giaoDich.getLoai())) {
                        newBalance += giaoDich.getSoTien();
                    } else if ("chi_phi".equals(giaoDich.getLoai())) {
                        newBalance -= giaoDich.getSoTien();
                    }
                    account.setSodu(newBalance);
                    taiKhoanRepository.updateTaiKhoan(account);
                    accountLiveData.removeObserver(this); // Remove the observer after updating the balance
                }
            }
        });
    }

    // Lấy danh sách giao dịch theo loại
    public LiveData<List<GiaoDich>> getGiaoDichByLoai(String loai) {
        if (giaoDichList == null) {
            giaoDichList = giaoDichRepository.getGiaoDichByLoai(loai);
        }
        return giaoDichList;
    }

    // Lấy giao dịch hiện tại
    public LiveData<GiaoDich> getGiaoDich() {
        return giaoDichLiveData;
    }

    // Lấy thông báo lỗi
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    // Lấy danh sách tài khoản
    public LiveData<List<TaiKhoan>> getAccounts() {
        return accounts;
    }

    public LiveData<String> getDateRange() {
        return dateRange;
    }

    public LiveData<List<GiaoDich>> getGiaoDichList() {
        return giaoDichList;
    }

    public void updateTransactions(String tab) {
        giaoDichList = giaoDichRepository.getGiaoDichByLoai(tab);
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
                dateRange.setValue(formatter.format(currentCalendar.getTime()));
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
                } else if (currentRange.length() == 4) {
                    updateDateRange("year", direction);
                }
            }
        }
    }
}