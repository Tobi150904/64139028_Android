package vn.ngoviethoang.duancuoiky.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import vn.ngoviethoang.duancuoiky.data.database.AppDatabase;
import vn.ngoviethoang.duancuoiky.data.entity.GiaoDich;

public class GiaoDichRepository {
    private final AppDatabase database;

    public GiaoDichRepository(Context context) {
        this.database = AppDatabase.getDatabase(context);
    }

    public void insertGiaoDich(GiaoDich giaoDich, RepositoryCallback callback) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                database.giaoDichDao().insert (giaoDich);
                callback.onSuccess();
            } catch (Exception e) {
                callback.onFailure(e.getMessage());
            }
        });
    }

    public void updateGiaoDich(GiaoDich giaoDich, RepositoryCallback callback) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                database.giaoDichDao().update(giaoDich);
                callback.onSuccess();
            } catch (Exception e) {
                callback.onFailure(e.getMessage());
            }
        });
    }

    public interface RepositoryCallback {
        void onSuccess();
        void onFailure(String error);
    }
}