package vn.ngoviethoang.duancuoiky.data.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import vn.ngoviethoang.duancuoiky.data.dao.ThuNhapDao;
import vn.ngoviethoang.duancuoiky.data.database.AppDatabase;
import vn.ngoviethoang.duancuoiky.data.entity.ThuNhap;

public class ThuNhapRepository {
    private ThuNhapDao thuNhapDao;
    private ExecutorService executorService;

    public ThuNhapRepository(Context context) {
        AppDatabase database = AppDatabase.getDatabase(context);
        thuNhapDao = database.thuNhapDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<ThuNhap>> getThuNhapByDateRange(Date startDate, Date endDate) {
        long startTimestamp = startDate.getTime();
        long endTimestamp = endDate.getTime();
        return thuNhapDao.getThuNhapByDateRange(startTimestamp, endTimestamp);
    }

    public void insertThuNhap(ThuNhap thuNhap) {
        executorService.execute(() -> thuNhapDao.insertThuNhap(thuNhap));
    }

    public void updateThuNhap(ThuNhap thuNhap) {
        executorService.execute(() -> thuNhapDao.updateThuNhap(thuNhap));
    }

    public void deleteThuNhap(ThuNhap thuNhap) {
        executorService.execute(() -> thuNhapDao.deleteThuNhap(thuNhap));
    }
}