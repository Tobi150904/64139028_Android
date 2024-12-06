package vn.ngoviethoang.duancuoiky.data.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import vn.ngoviethoang.duancuoiky.data.dao.ChiTieuDao;
import vn.ngoviethoang.duancuoiky.data.database.AppDatabase;
import vn.ngoviethoang.duancuoiky.data.entity.ChiTieu;

public class ChiTieuRepository {
    private ChiTieuDao chiTieuDao;
    private ExecutorService executorService;

    public ChiTieuRepository(Context context) {
        AppDatabase database = AppDatabase.getDatabase(context);
        chiTieuDao = database.chiTieuDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<ChiTieu>> getChiTieuByDateRange(Date startDate, Date endDate) {
        long startTimestamp = startDate.getTime();
        long endTimestamp = endDate.getTime();
        return chiTieuDao.getChiTieuByDateRange(startTimestamp, endTimestamp);
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