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

    public LiveData<User> getUserByEmailAndPassword(String email, String password) {
        return userDao.getUserByEmailAndPassword(email, password);
    }

    public LiveData<User> getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    public void insertUser(User user) {
        executorService.execute(() -> userDao.insertUser(user));
    }
}
