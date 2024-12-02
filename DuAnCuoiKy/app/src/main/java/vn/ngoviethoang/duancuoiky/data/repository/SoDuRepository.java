package vn.ngoviethoang.duancuoiky.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import vn.ngoviethoang.duancuoiky.data.database.AppDatabase;
import vn.ngoviethoang.duancuoiky.data.entity.SoDu;

public class SoDuRepository {
    private final AppDatabase database;

    public SoDuRepository(Context context) {
        this.database = AppDatabase.getDatabase(context);
    }

    public LiveData<SoDu> getBalance() {
        return database.soDuDao().getBalance();
    }

    public void updateBalance(SoDu soDu) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            database.soDuDao().update(soDu);
        });
    }
}

