package vn.ngoviethoang.duancuoiky.Data.Repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import vn.ngoviethoang.duancuoiky.Data.Dao.ChiTieuDao;
import vn.ngoviethoang.duancuoiky.Data.Database.AppDatabase;
import vn.ngoviethoang.duancuoiky.Data.Entity.ChiTieu;

public class ChiTieuRepository {
    private ChiTieuDao chiTieuDao;
    private LiveData<List<ChiTieu>> allChiTieu;
    private ExecutorService executorService;

    public ChiTieuRepository(Context context) {
        AppDatabase database = AppDatabase.getDatabase(context);
        chiTieuDao = database.chiTieuDao();
        allChiTieu = chiTieuDao.getAllChiTieu();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<ChiTieu>> getAllChiTieu() {
        return allChiTieu;
    }

    public void insertChiTieu(ChiTieu chiTieu) {
        executorService.execute(() -> chiTieuDao.insertChiTieu(chiTieu));
    }
}

