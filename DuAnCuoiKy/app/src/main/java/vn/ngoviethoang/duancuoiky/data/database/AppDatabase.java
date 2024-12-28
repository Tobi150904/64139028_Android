package vn.ngoviethoang.duancuoiky.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import vn.ngoviethoang.duancuoiky.data.dao.DanhMucDao;
import vn.ngoviethoang.duancuoiky.data.dao.GiaoDichDao;
import vn.ngoviethoang.duancuoiky.data.dao.TaiKhoanDao;
import vn.ngoviethoang.duancuoiky.data.dao.UserDao;
import vn.ngoviethoang.duancuoiky.data.entity.DanhMuc;
import vn.ngoviethoang.duancuoiky.data.entity.GiaoDich;
import vn.ngoviethoang.duancuoiky.data.entity.TaiKhoan;
import vn.ngoviethoang.duancuoiky.data.entity.User;

@Database(entities = {GiaoDich.class, DanhMuc.class, TaiKhoan.class, User.class}, version = 3, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract GiaoDichDao giaoDichDao();
    public abstract DanhMucDao danhMucDao();
    public abstract TaiKhoanDao taiKhoanDao();
    public abstract UserDao userDao();

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
                                    AppDatabase.class, "app_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

