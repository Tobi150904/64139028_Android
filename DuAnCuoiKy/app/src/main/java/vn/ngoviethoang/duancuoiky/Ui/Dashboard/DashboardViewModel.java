package vn.ngoviethoang.duancuoiky.Ui.Dashboard;

import android.app.Application;
import android.content.ClipData;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import vn.ngoviethoang.duancuoiky.Ui.Components.PieChartComponent;
import vn.ngoviethoang.duancuoiky.data.entity.SoDu;
import vn.ngoviethoang.duancuoiky.data.repository.SoDuRepository;

public class DashboardViewModel extends AndroidViewModel {
    private final MutableLiveData<String> dateRange = new MutableLiveData<>();
    private final MutableLiveData<String> tabSelected = new MutableLiveData<>();
    private final MutableLiveData<List<ClipData.Item>> items = new MutableLiveData<>();
    private final MutableLiveData<PieChartComponent> pieChartData = new MutableLiveData<>();

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

    public LiveData<List<ClipData.Item>> getItems() {
        return items;
    }

    public LiveData<PieChartComponent> getPieChartData() {
        return pieChartData;
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

    public void updateCustomDateRange(String start, String end) {
        dateRange.setValue(start + " - " + end);
    }

    public void switchTab(String tab) {
        tabSelected.setValue(tab);
//        updateDataForTab(tab);
    }

//    private void updateDataForTab(String tab) {
//        // Fetch and update items and pie chart data based on the selected tab
//        List<ClipData.Item> updatedItems = soDuRepository.getItemsForTab(tab);
//        PieChartComponent updatedPieChartData = soDuRepository.getPieChartDataForTab(tab);
//        items.setValue(updatedItems);
//        pieChartData.setValue(updatedPieChartData);
//    }
}