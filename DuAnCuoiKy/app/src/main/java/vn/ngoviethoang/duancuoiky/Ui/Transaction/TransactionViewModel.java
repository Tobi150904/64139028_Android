package vn.ngoviethoang.duancuoiky.Ui.Transaction;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import vn.ngoviethoang.duancuoiky.data.entity.GiaoDich;
import vn.ngoviethoang.duancuoiky.data.entity.TaiKhoan;
import vn.ngoviethoang.duancuoiky.data.repository.GiaoDichRepository;
import vn.ngoviethoang.duancuoiky.data.repository.TaiKhoanRepository;

public class TransactionViewModel extends AndroidViewModel {
    private final GiaoDichRepository giaoDichRepository;
    private final TaiKhoanRepository taiKhoanRepository;
    private final MutableLiveData<GiaoDich> giaoDichLiveData;
    private final MutableLiveData<String> errorMessage;
    private LiveData<List<GiaoDich>> giaoDichList;
    private LiveData<List<TaiKhoan>> accounts;

    // Khởi tạo TransactionViewModel với các repository và LiveData
    public TransactionViewModel(@NonNull Application application) {
        super(application);
        giaoDichRepository = new GiaoDichRepository(application);
        taiKhoanRepository = new TaiKhoanRepository(application);
        giaoDichLiveData = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
        accounts = taiKhoanRepository.getAllTaiKhoan();
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
}