package vn.ngoviethoang.duancuoiky.Ui.Dashboard;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

import vn.ngoviethoang.duancuoiky.Data.Entity.ChiTieu;
import vn.ngoviethoang.duancuoiky.Data.Repository.ChiTieuRepository;

public class ChiTieuViewModel extends AndroidViewModel {
    private ChiTieuRepository repository;
    private LiveData<List<ChiTieu>> allChiTieu;

    public ChiTieuViewModel(Application application) {
        super(application);
        repository = new ChiTieuRepository(application);
        allChiTieu = repository.getAllChiTieu();
    }

    public LiveData<List<ChiTieu>> getAllChiTieu() {
        return allChiTieu;
    }

    public void insertChiTieu(ChiTieu chiTieu) {
        repository.insertChiTieu(chiTieu);
    }
}
