package vn.ngoviethoang.duancuoiky.Ui.Account;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.Calendar;
import java.util.List;

import vn.ngoviethoang.duancuoiky.R;
import vn.ngoviethoang.duancuoiky.data.entity.TaiKhoan;

public class TransferHistoryActivity extends AppCompatActivity {

    private AccountViewModel accountViewModel;
    private TextView dateRange;
    private TextView tabDay, tabWeek, tabMonth, tabYear;
    private ImageView iconPrevious, iconNext, iconBack;
    private LinearLayout transferListLayout;
    private String selectedRange = "day";
    private Calendar currentCalendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_transfer);

        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);

        dateRange = findViewById(R.id.date_range);
        tabDay = findViewById(R.id.tab_day);
        tabWeek = findViewById(R.id.tab_week);
        tabMonth = findViewById(R.id.tab_month);
        tabYear = findViewById(R.id.tab_year);
        iconPrevious = findViewById(R.id.icon_previous);
        iconNext = findViewById(R.id.icon_next);
        iconBack = findViewById(R.id.ic_back);
        transferListLayout = findViewById(R.id.transfer_list);

        setupListeners();
        observeTransfers();

        accountViewModel.getDateRange().observe(this, date -> dateRange.setText(date));

        // Mặc định chọn tab "Ngày"
        selectDateRangeTab(tabDay, "day");
    }

    // Thiết lập các sự kiện
    private void setupListeners() {
        iconBack.setOnClickListener(v -> {
            Intent intent = new Intent(TransferHistoryActivity.this, AccountActivity.class);
            startActivity(intent);
            finish();
        });

        tabDay.setOnClickListener(v -> {
            selectDateRangeTab(tabDay, "day");
            accountViewModel.updateTransfer("day");
        });

        tabWeek.setOnClickListener(v -> {
            selectDateRangeTab(tabWeek, "week");
            accountViewModel.updateTransfer("week");
        });

        tabMonth.setOnClickListener(v -> {
            selectDateRangeTab(tabMonth, "month");
            accountViewModel.updateTransfer("month");
        });

        tabYear.setOnClickListener(v -> {
            selectDateRangeTab(tabYear, "year");
            accountViewModel.updateTransfer("year");
        });

        iconPrevious.setOnClickListener(v -> navigateDateRange(-1));
        iconNext.setOnClickListener(v -> navigateDateRange(1));
    }

    // Lấy phạm vi ngày được chọn
    private String getSelectedDateRange() {
        return selectedRange;
    }

    // Quan sát danh sách chuyển khoản
    private void observeTransfers() {
        accountViewModel.getTransferHistory().observe(this, this::updateTransferList);
    }

    // Chọn tab phạm vi ngày
    private void selectDateRangeTab(TextView selectedTab, String range) {
        resetTab(tabDay);
        resetTab(tabWeek);
        resetTab(tabMonth);
        resetTab(tabYear);
        highlightTab(selectedTab);
        selectedRange = range;
        if (!range.equals("year") && !range.equals("month") && !range.equals("week")) {
            currentCalendar = Calendar.getInstance();
        }
        accountViewModel.updateTransfer(range);
    }

    // Đặt lại tab
    private void resetTab(TextView tab) {
        tab.setTextColor(getResources().getColor(R.color.Gray));
        tab.setTypeface(null, Typeface.NORMAL);
        tab.setPaintFlags(tab.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
        tab.setSelected(false);
    }

    // Làm nổi bật tab
    private void highlightTab(TextView tab) {
        tab.setTextColor(getResources().getColor(R.color.Red));
        tab.setTypeface(null, Typeface.BOLD);
        tab.setPaintFlags(tab.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tab.setSelected(true);
    }

    // Điều hướng phạm vi ngày
    private void navigateDateRange(int direction) {
        accountViewModel.navigateDateRange(direction);
    }

    // Cập nhật danh sách chuyển khoản
    private void updateTransferList(List<TaiKhoan> transferList) {
        transferListLayout.removeAllViews();
        if (transferList == null || transferList.isEmpty()) {
            // Hiển thị thông báo nếu không có dữ liệu
            TextView noDataTextView = new TextView(this);
            noDataTextView.setText("Không có dữ liệu");
            noDataTextView.setTextColor(getResources().getColor(R.color.Gray));
            noDataTextView.setTextSize(16);
            noDataTextView.setPadding(16, 16, 16, 16);
            transferListLayout.addView(noDataTextView);
            return;
        }

        for (TaiKhoan transfer : transferList) {
            addTransferToUI(transfer);
        }
    }

    // Thêm chuyển khoản vào UI
    private void addTransferToUI(TaiKhoan transfer) {
        LinearLayout transferLayout = new LinearLayout(this);
        transferLayout.setOrientation(LinearLayout.VERTICAL);
        transferLayout.setPadding(16, 16, 16, 16);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 8, 0, 0);
        transferLayout.setLayoutParams(layoutParams);

        TextView dateTextView = new TextView(this);
        dateTextView.setText(transfer.getNgay());
        dateTextView.setTextColor(getResources().getColor(R.color.Black));
        dateTextView.setTextSize(14);
        transferLayout.addView(dateTextView);

        LinearLayout innerLayout = new LinearLayout(this);
        innerLayout.setOrientation(LinearLayout.HORIZONTAL);
        innerLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        innerLayout.setPadding(10, 10, 10, 10);

        TextView accountName = new TextView(this);
        accountName.setText(transfer.getTen());
        accountName.setTextColor(getResources().getColor(R.color.Black));
        accountName.setTextSize(14);
        accountName.setPadding(8, 0, 0, 0);
        innerLayout.addView(accountName);

        TextView transferAmount = new TextView(this);
        transferAmount.setText(String.format("%,.0f đ", transfer.getSoTienBanDau()));
        transferAmount.setTextColor(getResources().getColor(R.color.Gray));
        transferAmount.setTextSize(14);
        innerLayout.addView(transferAmount);

        transferLayout.addView(innerLayout);
        transferListLayout.addView(transferLayout);
    }
}