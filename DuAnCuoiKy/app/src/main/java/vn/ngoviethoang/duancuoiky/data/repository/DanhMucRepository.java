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

// DanhMucRepository.java
import android.os.AsyncTask;

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
            insertCategoryIfNotExists(context, "Sức khỏe", R.drawable.ic_health, "chi_phi");
            insertCategoryIfNotExists(context, "Giải trí", R.drawable.ic_entertain, "chi_phi");
            insertCategoryIfNotExists(context, "Trang chủ", R.drawable.ic_home, "chi_phi");
            insertCategoryIfNotExists(context, "Cafe", R.drawable.ic_cafe, "chi_phi");
            insertCategoryIfNotExists(context, "Mua sắm", R.drawable.ic_shopping, "chi_phi");
            insertCategoryIfNotExists(context, "Du lịch", R.drawable.ic_travel, "chi_phi");
            insertCategoryIfNotExists(context, "Giáo dục", R.drawable.ic_education, "chi_phi");
            insertCategoryIfNotExists(context, "Phiếu lương", R.drawable.ic_payslip, "thu_nhap");
            insertCategoryIfNotExists(context, "Quà tặng", R.drawable.ic_giftbox, "thu_nhap");
            insertCategoryIfNotExists(context, "Ngân hàng", R.drawable.ic_account3, "thu_nhap");
            insertCategoryIfNotExists(context, "Đầu tư", R.drawable.ic_account1, "thu_nhap");
        });
    }

    private void insertCategoryIfNotExists(Context context, String name, int drawableId, String loai) {
        if (danhMucDao.getDanhMucByName(name) == null) {
            danhMucDao.insertDanhMuc(new DanhMuc(name, getImageAsByteArray(context, drawableId), loai));
        }
    }

    private byte[] getImageAsByteArray(Context context, int drawableId) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableId);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public void getDanhMucById(int id, OnDanhMucLoadedListener listener) {
        new AsyncTask<Integer, Void, DanhMuc>() {
            @Override
            protected DanhMuc doInBackground(Integer... params) {
                return danhMucDao.getDanhMucById(params[0]);
            }

            @Override
            protected void onPostExecute(DanhMuc danhMuc) {
                listener.onDanhMucLoaded(danhMuc);
            }
        }.execute(id);
    }

    public interface OnDanhMucLoadedListener {
        void onDanhMucLoaded(DanhMuc danhMuc);
    }
}