package vn.ngoviethoang.duancuoiky.Data.Database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import vn.ngoviethoang.duancuoiky.Data.Dao.ChiTieuDao;
import vn.ngoviethoang.duancuoiky.Data.Dao.DanhMucDao;
import vn.ngoviethoang.duancuoiky.Data.Dao.ThuNhapDao;
import vn.ngoviethoang.duancuoiky.Data.Entity.ChiTieu;
import vn.ngoviethoang.duancuoiky.Data.Entity.DanhMuc;
import vn.ngoviethoang.duancuoiky.Data.Entity.ThuNhap;

@Database(entities = {ChiTieu.class, ThuNhap.class, DanhMuc.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
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
                            .allowMainThreadQueries() // Dành cho test, nên chuyển sang background thread khi triển khai thực tế
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

