package vn.ngoviethoang.duancuoiky.Ui.Account;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import vn.ngoviethoang.duancuoiky.data.entity.TaiKhoan;
import vn.ngoviethoang.duancuoiky.data.repository.TaiKhoanRepository;

public class AccountViewModel extends AndroidViewModel {
    private final TaiKhoanRepository repository;
    private final MutableLiveData<Integer> totalBalance;
    private final MutableLiveData<List<TaiKhoan>> accounts;

    public AccountViewModel(@NonNull Application application) {
        super(application);
        repository = new TaiKhoanRepository(application);
        totalBalance = new MutableLiveData<>();
        accounts = new MutableLiveData<>();
    }

    public LiveData<Integer> getTotalBalance() {
        return totalBalance;
    }

    public LiveData<List<TaiKhoan>> getAccounts() {
        return accounts;
    }

    public void loadAccounts() {
        repository.getAllTaiKhoan().observeForever(accounts -> {
            this.accounts.setValue(accounts);
            int total = 0;
            for (TaiKhoan account : accounts) {
                total += account.getSodu();
            }
            totalBalance.setValue(total);
        });
    }

    public void addAccount(TaiKhoan account) {
        repository.insertTaiKhoan(account);
        loadAccounts();
    }
}