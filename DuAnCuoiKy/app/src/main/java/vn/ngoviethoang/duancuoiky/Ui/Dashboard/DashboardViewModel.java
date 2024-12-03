// DashboardViewModel.java
package vn.ngoviethoang.duancuoiky.Ui.Dashboard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import vn.ngoviethoang.duancuoiky.data.entity.SoDu;
import vn.ngoviethoang.duancuoiky.data.repository.SoDuRepository;

public class DashboardViewModel extends AndroidViewModel {
    private final MutableLiveData<String> dateRange = new MutableLiveData<>();
    private final MutableLiveData<String> tabSelected = new MutableLiveData<>();

    private final SoDuRepository soDuRepository;
    private final LiveData<SoDu> balance;

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        soDuRepository = new SoDuRepository(application);
        balance = soDuRepository.getBalance();
    }

    public LiveData<SoDu> getBalance() {
        return balance;
    }

    public void updateBalance(SoDu soDu) {
        soDuRepository.updateBalance(soDu);
    }

    public LiveData<String> getDateRange() {
        return dateRange;
    }

    public LiveData<String> getTabSelected() {
        return tabSelected;
    }

    public void updateDateRange(String range) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();

        switch (range) {
            case "day":
                String today = formatter.format(calendar.getTime());
                dateRange.setValue("Hôm nay: " + today);
                break;

            case "week":
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                String weekStart = formatter.format(calendar.getTime());
                calendar.add(Calendar.DAY_OF_WEEK, 6);
                String weekEnd = formatter.format(calendar.getTime());
                dateRange.setValue(weekStart + " - " + weekEnd);
                break;

            case "month":
                int month = calendar.get(Calendar.MONTH) + 1;
                int year = calendar.get(Calendar.YEAR);
                dateRange.setValue(month + "/" + year);
                break;

            case "year":
                int currentYear = calendar.get(Calendar.YEAR);
                dateRange.setValue("Năm: " + currentYear);
                break;

            default:
                dateRange.setValue("Chọn thời gian");
        }
    }

    public void switchTab(String tab) {
        tabSelected.setValue(tab);
    }
}