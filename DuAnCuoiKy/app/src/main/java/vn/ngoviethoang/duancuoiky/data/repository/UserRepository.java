package vn.ngoviethoang.duancuoiky.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import vn.ngoviethoang.duancuoiky.data.dao.UserDao;
import vn.ngoviethoang.duancuoiky.data.database.AppDatabase;
import vn.ngoviethoang.duancuoiky.data.entity.User;

public class UserRepository {
    private final UserDao userDao;
    private final ExecutorService executorService;

    // Constructor khởi tạo UserRepository với AppDatabase
    public UserRepository(Context context) {
        AppDatabase database = AppDatabase.getDatabase(context);
        userDao = database.userDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    // Thêm người dùng mới vào cơ sở dữ liệu (Đăng ký)
    public void registerUser(User user, RepositoryCallback callback) {
        executorService.execute(() -> {
            try {
                userDao.insertUser(user);
                callback.onSuccess("Đăng ký thành công!");
            } catch (Exception e) {
                callback.onFailure(e.getMessage());
            }
        });
    }

    // Kiểm tra thông tin đăng nhập (Đăng nhập)
    public LiveData<User> loginUser(String email, String password) {
        return userDao.getUserByEmailAndPassword(email, password);
    }

    // Kiểm tra người dùng đã tồn tại hay chưa
    public LiveData<User> checkUserExists(String email) {
        return userDao.getUserByEmail(email);
    }

    // Callback để xử lý kết quả bất đồng bộ
    public interface RepositoryCallback {
        void onSuccess(String message);

        void onFailure(String errorMessage);
    }
}

