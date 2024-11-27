package vn.ngoviethoang.duancuoiky.data.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import android.content.Context;

import vn.ngoviethoang.duancuoiky.data.dao.ChiTieuDao;
import vn.ngoviethoang.duancuoiky.data.dao.DanhMucDao;
import vn.ngoviethoang.duancuoiky.data.dao.ThuNhapDao;
import vn.ngoviethoang.duancuoiky.data.dao.UserDao;
import vn.ngoviethoang.duancuoiky.data.entity.ChiTieu;
import vn.ngoviethoang.duancuoiky.data.entity.DanhMuc;
import vn.ngoviethoang.duancuoiky.data.entity.ThuNhap;

@Database(entities = {ChiTieu.class, ThuNhap.class, DanhMuc.class}, version = 1)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract ChiTieuDao chiTieuDao();
    public abstract ThuNhapDao thuNhapDao();
    public abstract DanhMucDao danhMucDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "money_manager_database")
                                    .fallbackToDestructiveMigration()
                                    .build();
                }
            }
        }
        return INSTANCE;
    }
}


