package vn.ngoviethoang.duancuoiky.data.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import vn.ngoviethoang.duancuoiky.data.dao.ChiTieuDao;
import vn.ngoviethoang.duancuoiky.data.dao.DanhMucDao;
import vn.ngoviethoang.duancuoiky.data.dao.GiaoDichDao;
import vn.ngoviethoang.duancuoiky.data.dao.SoDuDao;
import vn.ngoviethoang.duancuoiky.data.dao.ThuNhapDao;
import vn.ngoviethoang.duancuoiky.data.dao.UserDao;
import vn.ngoviethoang.duancuoiky.data.entity.ChiTieu;
import vn.ngoviethoang.duancuoiky.data.entity.DanhMuc;
import vn.ngoviethoang.duancuoiky.data.entity.GiaoDich;
import vn.ngoviethoang.duancuoiky.data.entity.SoDu;
import vn.ngoviethoang.duancuoiky.data.entity.ThuNhap;
import vn.ngoviethoang.duancuoiky.data.entity.User;

@Database(entities = {ChiTieu.class, ThuNhap.class, DanhMuc.class, User.class, SoDu.class, GiaoDich.class}, version = 1)
@TypeConverters({DateConverter.class, Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract ChiTieuDao chiTieuDao();
    public abstract ThuNhapDao thuNhapDao();
    public abstract DanhMucDao danhMucDao();
    public abstract SoDuDao soDuDao();
    public abstract GiaoDichDao giaoDichDao();

    private static volatile AppDatabase INSTANCE;

    // Executor để thực hiện các thao tác cơ sở dữ liệu không đồng bộ
    private static final int NUMBER_OF_THREADS = 4; // Số lượng luồng
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

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