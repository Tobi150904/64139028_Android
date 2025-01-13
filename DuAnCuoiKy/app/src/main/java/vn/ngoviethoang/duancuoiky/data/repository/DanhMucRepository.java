package vn.ngoviethoang.duancuoiky.data.repository;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.lifecycle.LiveData;

import java.io.ByteArrayOutputStream;
import java.util.List;

import vn.ngoviethoang.duancuoiky.R;
import vn.ngoviethoang.duancuoiky.data.dao.DanhMucDao;
import vn.ngoviethoang.duancuoiky.data.database.AppDatabase;
import vn.ngoviethoang.duancuoiky.data.entity.DanhMuc;

public class DanhMucRepository {
    private final DanhMucDao danhMucDao;

    public DanhMucRepository(Context context) {
        AppDatabase database = AppDatabase.getDatabase(context);
        this.danhMucDao = database.danhMucDao();
    }

    public LiveData<List<DanhMuc>> getDanhMucByLoai(String loai) {
        return danhMucDao.getDanhMucByLoai(loai);
    }

    public void insertDanhMuc(DanhMuc danhMuc) {
        AppDatabase.databaseWriteExecutor.execute(() -> danhMucDao.insertDanhMuc(danhMuc));
    }

    public void insertPredefinedCategories(Context context) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            insertCategoryIfNotExists(context, "Sức khỏe", R.drawable.ic_health, "chi_phi", "#FF5733");
            insertCategoryIfNotExists(context, "Giải trí", R.drawable.ic_entertain, "chi_phi", "#33FF57");
            insertCategoryIfNotExists(context, "Trang chủ", R.drawable.ic_home, "chi_phi", "#3357FF");
            insertCategoryIfNotExists(context, "Cafe", R.drawable.ic_cafe, "chi_phi", "#FF33A1");
            insertCategoryIfNotExists(context, "Mua sắm", R.drawable.ic_shopping, "chi_phi", "#A133FF");
            insertCategoryIfNotExists(context, "Du lịch", R.drawable.ic_travel, "chi_phi", "#33FFF5");
            insertCategoryIfNotExists(context, "Giáo dục", R.drawable.ic_education, "chi_phi", "#F5FF33");
            insertCategoryIfNotExists(context, "Phiếu lương", R.drawable.ic_payslip, "thu_nhap", "#FF3333");
            insertCategoryIfNotExists(context, "Quà tặng", R.drawable.ic_giftbox, "thu_nhap", "#33FF33");
            insertCategoryIfNotExists(context, "Ngân hàng", R.drawable.ic_account3, "thu_nhap", "#3333FF");
            insertCategoryIfNotExists(context, "Đầu tư", R.drawable.ic_account1, "thu_nhap", "#FF33FF");
        });
    }

    private void insertCategoryIfNotExists(Context context, String name, int drawableId, String loai, String mauSac) {
        if (danhMucDao.getDanhMucByName(name) == null) {
            danhMucDao.insertDanhMuc(new DanhMuc(name, getImageAsByteArray(context, drawableId), loai, mauSac));
        }
    }

    private byte[] getImageAsByteArray(Context context, int drawableId) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableId);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public void getDanhMucById(int id, OnDanhMucLoadedListener listener) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            DanhMuc danhMuc = danhMucDao.getDanhMucById(id);
            android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());
            mainHandler.post(() -> listener.onDanhMucLoaded(danhMuc));
        });
    }

    public interface OnDanhMucLoadedListener {
        void onDanhMucLoaded(DanhMuc danhMuc);
    }
}