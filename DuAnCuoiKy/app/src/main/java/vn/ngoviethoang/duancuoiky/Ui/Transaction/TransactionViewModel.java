package vn.ngoviethoang.duancuoiky.Ui.Transaction;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import vn.ngoviethoang.duancuoiky.data.entity.GiaoDich;
import vn.ngoviethoang.duancuoiky.data.entity.TaiKhoan;
import vn.ngoviethoang.duancuoiky.data.repository.GiaoDichRepository;

public class TransactionViewModel extends AndroidViewModel {
    private final GiaoDichRepository giaoDichRepository;
    private final MutableLiveData<GiaoDich> giaoDichLiveData;
    private final MutableLiveData<String> errorMessage;
    private LiveData<List<GiaoDich>> giaoDichList;
    private LiveData<List<TaiKhoan>> accounts;

    public TransactionViewModel(@NonNull Application application) {
        super(application);
        giaoDichRepository = new GiaoDichRepository(application);
        giaoDichLiveData = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
    }

    public void addTransaction(GiaoDich giaoDich) {
        try {
            giaoDichRepository.insertGiaoDich(giaoDich);
            giaoDichLiveData.setValue(giaoDich);
        } catch (Exception e) {
            errorMessage.setValue(e.getMessage());
        }
    }

    public LiveData<List<GiaoDich>> getGiaoDichByLoai(String loai) {
        if (giaoDichList == null) {
            giaoDichList = giaoDichRepository.getGiaoDichByLoai(loai);
        }
        return giaoDichList;
    }

    public LiveData<GiaoDich> getGiaoDich() {
        return giaoDichLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<List<TaiKhoan>> getAccounts() {
        return accounts;
    }
}