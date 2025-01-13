package vn.ngoviethoang.duancuoiky.Ui.Statistics;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import vn.ngoviethoang.duancuoiky.data.entity.GiaoDich;
import vn.ngoviethoang.duancuoiky.data.repository.GiaoDichRepository;

public class StatisticsViewModel extends AndroidViewModel {
    private final GiaoDichRepository giaoDichRepository;
    private final MutableLiveData<List<GiaoDich>> transactions;
    private final MutableLiveData<String> dateRange;
    private final MutableLiveData<List<GiaoDich>> filteredTransactions;
    private final MutableLiveData<Float> totalIncome = new MutableLiveData<>();
    private final MutableLiveData<Float> totalExpenses = new MutableLiveData<>();
    private final MutableLiveData<Float> profitLoss = new MutableLiveData<>();
    private Calendar currentCalendar;
    private String selectedType;
    private String selectedRange;
    private Date startDate;
    private Date endDate;
    private List<GiaoDich> allTransactions;

    public StatisticsViewModel(@NonNull Application application) {
        super(application);
        giaoDichRepository = new GiaoDichRepository(application);
        transactions = new MutableLiveData<>();
        dateRange = new MutableLiveData<>();
        filteredTransactions = new MutableLiveData<>();
        currentCalendar = Calendar.getInstance();
        allTransactions = new ArrayList<>();
        selectedType = "chung"; // Mặc định là tab "Chung"
        selectedRange = "day"; // Mặc định là tab "Ngày"
        loadTransactions();
    }

    public LiveData<List<GiaoDich>> getTransactions() {
        return transactions;
    }

    public LiveData<String> getDateRange() {
        return dateRange;
    }

    public LiveData<List<GiaoDich>> getFilteredTransactions() {
        return filteredTransactions;
    }

    public LiveData<Float> getTotalIncome() {
        return totalIncome;
    }

    public LiveData<Float> getTotalExpenses() {
        return totalExpenses;
    }

    public LiveData<Float> getProfitLoss() {
        return profitLoss;
    }

    public void loadTransactions() {
        giaoDichRepository.getAllGiaoDich().observeForever(giaoDichList -> {
            if (giaoDichList != null) {
                allTransactions = giaoDichList;
                loadFilteredTransactions();
            }
        });
    }

    public void updateDateRange(String range, int direction) {
        SimpleDateFormat formatter;
        switch (range) {
            case "day":
                formatter = new SimpleDateFormat("dd/MM/yyyy");
                if (direction == 0) {
                    currentCalendar = Calendar.getInstance();
                } else {
                    currentCalendar.add(Calendar.DATE, direction);
                }
                startDate = currentCalendar.getTime();
                endDate = startDate;
                dateRange.setValue(formatter.format(startDate));
                break;

            case "week":
                formatter = new SimpleDateFormat("dd/MM/yyyy");
                if (direction == 0) {
                    currentCalendar = Calendar.getInstance();
                    currentCalendar.set(Calendar.DAY_OF_WEEK, currentCalendar.getFirstDayOfWeek());
                } else {
                    currentCalendar.add(Calendar.WEEK_OF_YEAR, direction);
                    currentCalendar.set(Calendar.DAY_OF_WEEK, currentCalendar.getFirstDayOfWeek());
                }
                startDate = currentCalendar.getTime();
                currentCalendar.add(Calendar.DATE, 6);
                endDate = currentCalendar.getTime();
                dateRange.setValue(formatter.format(startDate) + " - " + formatter.format(endDate));
                break;

            case "month":
                formatter = new SimpleDateFormat("MM/yyyy");
                if (direction == 0) {
                    currentCalendar = Calendar.getInstance();
                } else {
                    currentCalendar.add(Calendar.MONTH, direction);
                }
                startDate = getFirstDateOfMonth(currentCalendar);
                endDate = getLastDateOfMonth(currentCalendar);
                dateRange.setValue(formatter.format(startDate));
                break;

            case "year":
                formatter = new SimpleDateFormat("yyyy");
                if (direction == 0) {
                    currentCalendar = Calendar.getInstance();
                } else {
                    currentCalendar.add(Calendar.YEAR, direction);
                }
                startDate = getFirstDateOfYear(currentCalendar);
                endDate = getLastDateOfYear(currentCalendar);
                dateRange.setValue(formatter.format(startDate));
                break;
        }
        loadFilteredTransactions();
    }

    private Date getFirstDateOfMonth(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    private Date getLastDateOfMonth(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    private Date getFirstDateOfYear(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        return calendar.getTime();
    }

    private Date getLastDateOfYear(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
        return calendar.getTime();
    }

    public void navigateDateRange(int direction) {
        String currentRange = dateRange.getValue();
        if (currentRange != null) {
            if (currentRange.contains("-")) {
                updateDateRange("week", direction); // Xử lý tuần
            } else if (currentRange.contains("/")) {
                if (currentRange.length() == 10) {
                    updateDateRange("day", direction); // Xử lý ngày
                } else if (currentRange.length() == 7) {
                    updateDateRange("month", direction); // Xử lý tháng
                }
            } else if (currentRange.length() == 4) {
                updateDateRange("year", direction); // Xử lý năm
            }
        }
    }

    public void updateTransactions(String type, String range) {
        selectedType = type;
        selectedRange = range;
        updateDateRange(range, 0);
        loadFilteredTransactions();
    }

    private void calculateTotals(List<GiaoDich> transactions) {
        float income = 0;
        float expenses = 0;

        for (GiaoDich giaoDich : transactions) {
            if (giaoDich.getLoai().equals("thu_nhap")) {
                income += giaoDich.getSoTien();
            } else if (giaoDich.getLoai().equals("chi_phi")) {
                expenses += giaoDich.getSoTien();
            }
        }

        totalIncome.setValue(income);
        totalExpenses.setValue(expenses);
        profitLoss.setValue(income - expenses);
    }

    private void loadFilteredTransactions() {
        List<GiaoDich> filteredList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (GiaoDich giaoDich : allTransactions) {
            try {
                Date giaoDichDate = sdf.parse(giaoDich.getNgay());
                if (giaoDichDate != null) {
                    boolean isDateInRange = false;
                    switch (selectedRange) {
                        case "day":
                            isDateInRange = isSameDay(giaoDichDate, startDate);
                            break;
                        case "week":
                            isDateInRange = isSameWeek(giaoDichDate, startDate, endDate);
                            break;
                        case "month":
                            isDateInRange = isSameMonth(giaoDichDate, startDate);
                            break;
                        case "year":
                            isDateInRange = isSameYear(giaoDichDate, startDate);
                            break;
                    }

                    if (isDateInRange) {
                        filteredList.add(giaoDich);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        filteredTransactions.setValue(filteredList);
        calculateTotals(filteredList);
    }

    private boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }

    private boolean isSameWeek(Date date, Date startDate, Date endDate) {
        if (date == null || startDate == null || endDate == null) {
            return false;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Calendar calStart = Calendar.getInstance();
        calStart.setTime(startDate);
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(endDate);

        // Kiểm tra cùng năm và cùng tuần
        return cal.get(Calendar.YEAR) == calStart.get(Calendar.YEAR) &&
                cal.get(Calendar.WEEK_OF_YEAR) == calStart.get(Calendar.WEEK_OF_YEAR);
    }

    private boolean isSameMonth(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
    }

    private boolean isSameYear(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
    }
}