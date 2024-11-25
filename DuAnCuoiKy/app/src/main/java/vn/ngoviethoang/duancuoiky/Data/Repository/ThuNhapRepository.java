package vn.ngoviethoang.duancuoiky.Data.Repository;

import android.content.Context;
import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import vn.ngoviethoang.duancuoiky.Data.Dao.ThuNhapDao;
import vn.ngoviethoang.duancuoiky.Data.Database.AppDatabase;
import vn.ngoviethoang.duancuoiky.Data.Entity.ThuNhap;

public class ThuNhapRepository {
    private ThuNhapDao thuNhapDao;
    private ExecutorService executorService;

    public ThuNhapRepository(Context context) {
        AppDatabase database = AppDatabase.getDatabase(context);
        thuNhapDao = database.thuNhapDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<ThuNhap>> getThuNhapByDateRange(Date startDate, Date endDate) {
        return thuNhapDao.getThuNhapByDateRange(startDate, endDate);
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
