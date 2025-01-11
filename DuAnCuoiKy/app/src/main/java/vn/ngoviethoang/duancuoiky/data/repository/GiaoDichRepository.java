// GiaoDichRepository.java
package vn.ngoviethoang.duancuoiky.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Date;
import java.util.List;

import vn.ngoviethoang.duancuoiky.data.dao.GiaoDichDao;
import vn.ngoviethoang.duancuoiky.data.database.AppDatabase;
import vn.ngoviethoang.duancuoiky.data.entity.GiaoDich;

public class GiaoDichRepository {
    private final GiaoDichDao giaoDichDao;

    public GiaoDichRepository(Context context) {
        AppDatabase database = AppDatabase.getDatabase(context);
        this.giaoDichDao = database.giaoDichDao();
    }

    public void insertGiaoDich(GiaoDich giaoDich, MutableLiveData<GiaoDich> giaoDichLiveData, MutableLiveData<String> errorMessage) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                giaoDichDao.insertGiaoDich(giaoDich);
                giaoDichLiveData.postValue(giaoDich);
            } catch (Exception e) {
                errorMessage.postValue(e.getMessage());
            }
        });
    }

    public LiveData<List<GiaoDich>> getGiaoDichByLoai(String loai) {
        return giaoDichDao.getGiaoDichByLoai(loai);
    }

    public LiveData<List<GiaoDich>> getGiaoDichByLoaiAndDateRange(String loai, Date startDate, Date endDate) {
        return giaoDichDao.getGiaoDichByLoaiAndDateRange(loai, startDate, endDate);
    }

    public LiveData<List<GiaoDich>> getAllGiaoDich() {
        return giaoDichDao.getAllGiaoDich();
    }
}