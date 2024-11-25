package vn.ngoviethoang.duancuoiky.Data.Repository;

import android.content.Context;
import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import vn.ngoviethoang.duancuoiky.Data.Dao.ChiTieuDao;
import vn.ngoviethoang.duancuoiky.Data.Database.AppDatabase;
import vn.ngoviethoang.duancuoiky.Data.Entity.ChiTieu;

public class ChiTieuRepository {
    private ChiTieuDao chiTieuDao;
    private ExecutorService executorService;

    public ChiTieuRepository(Context context) {
        AppDatabase database = AppDatabase.getDatabase(context);
        chiTieuDao = database.chiTieuDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<ChiTieu>> getChiTieuByDateRange(Date startDate, Date endDate) {
        return chiTieuDao.getChiTieuByDateRange(startDate, endDate);
    }

    public void insertChiTieu(ChiTieu chiTieu) {
        executorService.execute(() -> chiTieuDao.insertChiTieu(chiTieu));
    }

    public void updateChiTieu(ChiTieu chiTieu) {
        executorService.execute(() -> chiTieuDao.updateChiTieu(chiTieu));
    }

    public void deleteChiTieu(ChiTieu chiTieu) {
        executorService.execute(() -> chiTieuDao.deleteChiTieu(chiTieu));
    }
}


