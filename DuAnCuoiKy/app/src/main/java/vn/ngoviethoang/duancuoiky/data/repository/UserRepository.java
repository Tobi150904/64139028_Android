package vn.ngoviethoang.duancuoiky.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.concurrent.ExecutorService;

import vn.ngoviethoang.duancuoiky.data.dao.UserDao;
import vn.ngoviethoang.duancuoiky.data.database.AppDatabase;
import vn.ngoviethoang.duancuoiky.data.entity.User;

public class UserRepository {
    private final UserDao userDao;
    private final ExecutorService executorService;

    public UserRepository(Context context) {
        AppDatabase database = AppDatabase.getDatabase(context);
        userDao = database.userDao();
        executorService = AppDatabase.databaseWriteExecutor;
    }

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

    public LiveData<User> loginUser(String email, String password) {
        return userDao.getUserByEmailAndPassword(email, password);
    }

    public LiveData<User> checkUserExists(String email) {
        return userDao.getUserByEmail(email);
    }

    public interface RepositoryCallback {
        void onSuccess(String message);
        void onFailure(String errorMessage);
    }
}