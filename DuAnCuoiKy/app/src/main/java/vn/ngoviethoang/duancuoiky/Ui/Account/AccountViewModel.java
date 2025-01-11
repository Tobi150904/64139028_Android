package vn.ngoviethoang.duancuoiky.Ui.Account;

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

import vn.ngoviethoang.duancuoiky.data.entity.TaiKhoan;
import vn.ngoviethoang.duancuoiky.data.repository.TaiKhoanRepository;

public class AccountViewModel extends AndroidViewModel {
    private final TaiKhoanRepository repository;
    private final MutableLiveData<Integer> totalBalance;
    private final MutableLiveData<List<TaiKhoan>> accounts;
    private final MutableLiveData<List<TaiKhoan>> transferHistory;
    private final MutableLiveData<String> dateRange;
    private Calendar currentCalendar;
    private String selectedRange;
    private Date startDate;
    private Date endDate;
    private List<TaiKhoan> allTransfers;
    private List<TaiKhoan> filteredTransfers;

    public AccountViewModel(@NonNull Application application) {
        super(application);
        repository = new TaiKhoanRepository(application);
        totalBalance = new MutableLiveData<>();
        accounts = new MutableLiveData<>();
        transferHistory = new MutableLiveData<>();
        dateRange = new MutableLiveData<>();
        currentCalendar = Calendar.getInstance();
        allTransfers = new ArrayList<>();
        filteredTransfers = new ArrayList<>();
        selectedRange = "day";
        loadAccountsWithInitialAmounts();
    }

    public LiveData<Integer> getTotalBalance() {
        return totalBalance;
    }

    public LiveData<List<TaiKhoan>> getAccounts() {
        return accounts;
    }

    public LiveData<List<TaiKhoan>> getTransferHistory() {
        return transferHistory;
    }

    public LiveData<String> getDateRange() {
        return dateRange;
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

    public void loadAccountsWithInitialAmounts() {
        repository.getAllTaiKhoan().observeForever(accounts -> {
            List<TaiKhoan> accountsWithInitialAmount = new ArrayList<>();
            for (TaiKhoan account : accounts) {
                if (account.getSoTienBanDau() == 0) {
                    account.setSoTienBanDau(account.getSodu());
                }
                accountsWithInitialAmount.add(account);
            }
            allTransfers = accountsWithInitialAmount;
            loadFilteredTransfers();
        });
    }

    public void addAccount(TaiKhoan account) {
        account.setSoTienBanDau(account.getSodu());
        repository.insertTaiKhoan(account);
        loadAccounts();
    }

    public void updateAccount(TaiKhoan account) {
        repository.updateTaiKhoan(account);
        loadAccounts();
    }

    private void updateDateRange(String range, int direction) {
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

        loadFilteredTransfers();
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

    // Điều hướng phạm vi ngày
    public void navigateDateRange(int direction) {
        String currentRange = dateRange.getValue();
        if (currentRange != null) {
            if (currentRange.contains("-")) {
                updateDateRange("week", direction);
            } else if (currentRange.contains("/")) {
                if (currentRange.length() == 10) {
                    updateDateRange("day", direction);
                } else if (currentRange.length() == 7) {
                    updateDateRange("month", direction);
                } else if (currentRange.length() == 4) {
                    updateDateRange("year", direction);
                }
            }
        }
    }

    // Cập nhật giao dịch
    public void updateTransfer(String range) {
        selectedRange = range;
        updateDateRange(range, 0);
        loadFilteredTransfers();
    }

    private void loadFilteredTransfers() {
        filteredTransfers.clear();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (TaiKhoan transfer : allTransfers) {
            if (transfer.getNgay() != null && !transfer.getNgay().isEmpty()) {
                try {
                    Date transferDate = sdf.parse(transfer.getNgay());
                    if (transferDate != null) {
                        if (selectedRange.equals("day") && isSameDay(transferDate, startDate)) {
                            filteredTransfers.add(transfer);
                        } else if (selectedRange.equals("week") && isSameWeek(transferDate, startDate, endDate)) {
                            filteredTransfers.add(transfer);
                        } else if (selectedRange.equals("month") && isSameMonth(transferDate, startDate)) {
                            filteredTransfers.add(transfer);
                        } else if (selectedRange.equals("year") && isSameYear(transferDate, startDate)) {
                            filteredTransfers.add(transfer);
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        transferHistory.setValue(filteredTransfers);
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
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Calendar calStart = Calendar.getInstance();
        calStart.setTime(startDate);
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(endDate);
        return cal.get(Calendar.YEAR) == calStart.get(Calendar.YEAR) &&
                cal.get(Calendar.WEEK_OF_YEAR) == calStart.get(Calendar.WEEK_OF_YEAR);
    }

    private boolean isSameMonth(Date date, Date startDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        cal.setTime(startDate);
        return month == cal.get(Calendar.MONTH) && year == cal.get(Calendar.YEAR);
    }

    private boolean isSameYear(Date date, Date startDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        cal.setTime(startDate);
        return year == cal.get(Calendar.YEAR);
    }
}