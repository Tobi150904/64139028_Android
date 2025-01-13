package vn.ngoviethoang.duancuoiky.Ui.Transaction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.Calendar;
import java.util.List;

import vn.ngoviethoang.duancuoiky.R;
import vn.ngoviethoang.duancuoiky.data.entity.GiaoDich;
import vn.ngoviethoang.duancuoiky.Ui.Dashboard.DashboardActivity;

public class TransactionDetailActivity extends AppCompatActivity {

    private TransactionViewModel transactionViewModel;
    private TextView dateRange, totalText;
    private TextView tabExpenses, tabIncome, tabDay, tabWeek, tabMonth, tabYear;
    private ImageView iconPrevious, iconNext, iconBack;
    private LinearLayout transactionListLayout;
    private String selectedRange = "day";
    private Calendar currentCalendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);

        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        dateRange = findViewById(R.id.date_range);
        totalText = findViewById(R.id.total_text);
        tabExpenses = findViewById(R.id.tab_expenses);
        tabIncome = findViewById(R.id.tab_income);
        tabDay = findViewById(R.id.tab_day);
        tabWeek = findViewById(R.id.tab_week);
        tabMonth = findViewById(R.id.tab_month);
        tabYear = findViewById(R.id.tab_year);
        iconPrevious = findViewById(R.id.icon_previous);
        iconNext = findViewById(R.id.icon_next);
        iconBack = findViewById(R.id.ic_back);
        transactionListLayout = findViewById(R.id.transaction_list);

        setupListeners();
        observeTransactions();

        transactionViewModel.getDateRange().observe(this, date -> dateRange.setText(date));

        selectTab(tabExpenses, "chi_phi");
        selectDateRangeTab(tabDay, "day");
    }

    // Thiết lập các sự kiện
    private void setupListeners() {
        iconBack.setOnClickListener(v -> {
            Intent intent = new Intent(TransactionDetailActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        });

        tabExpenses.setOnClickListener(v -> {
            selectTab(tabExpenses, "chi_phi");
            filterTransactions("chi_phi", getSelectedDateRange());
        });

        tabIncome.setOnClickListener(v -> {
            selectTab(tabIncome, "thu_nhap");
            filterTransactions("thu_nhap", getSelectedDateRange());
        });

        tabDay.setOnClickListener(v -> {
            selectDateRangeTab(tabDay, "day");
            transactionViewModel.updateTransactions(getSelectedTransactionType(), "day");
        });

        tabWeek.setOnClickListener(v -> {
            selectDateRangeTab(tabWeek, "week");
            transactionViewModel.updateTransactions(getSelectedTransactionType(), "week");
        });

        tabMonth.setOnClickListener(v -> {
            selectDateRangeTab(tabMonth, "month");
            transactionViewModel.updateTransactions(getSelectedTransactionType(), "month");
        });

        tabYear.setOnClickListener(v -> {
            selectDateRangeTab(tabYear, "year");
            transactionViewModel.updateTransactions(getSelectedTransactionType(), "year");
        });

        iconPrevious.setOnClickListener(v -> navigateDateRange(-1));
        iconNext.setOnClickListener(v -> navigateDateRange(1));
    }

    // Lấy loại giao dịch được chọn
    private String getSelectedTransactionType() {
        if (tabExpenses.isSelected()) {
            return "chi_phi";
        } else if (tabIncome.isSelected()) {
            return "thu_nhap";
        } else {
            return "chi_phi";
        }
    }

    // Lấy phạm vi ngày được chọn
    private String getSelectedDateRange() {
        return selectedRange;
    }

    // Lọc giao dịch
    private void filterTransactions(String type, String range) {
        transactionViewModel.updateTransactions(type, range);
    }

    // Quan sát danh sách giao dịch
    private void observeTransactions() {
        transactionViewModel.getGiaoDichList().observe(this, this::updateTransactionList);

        transactionViewModel.getTotalAmountLiveData().observe(this, totalAmount -> {
            if (totalAmount != null) {
                totalText.setText(String.format("%,.0f đ", totalAmount));
            } else {
                totalText.setText("0 đ");
            }
        });
    }

    // Chọn tab
    private void selectTab(TextView selectedTab, String tab) {
        resetTab(tabExpenses);
        resetTab(tabIncome);
        highlightTab(selectedTab);
        selectedTab.setSelected(true);
        filterTransactions(tab, getSelectedDateRange());
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
        filterTransactions(getSelectedTransactionType(), range);
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
        transactionViewModel.navigateDateRange(direction);
    }

    // Cập nhật danh sách giao dịch
    private void updateTransactionList(List<GiaoDich> giaoDichList) {
        transactionListLayout.removeAllViews();
        double totalAmount = 0;

        for (GiaoDich giaoDich : giaoDichList) {
            addTransactionToUI(giaoDich);
            totalAmount += giaoDich.getSoTien();
        }

        totalText.setText(String.format("Tổng cộng: %,.0f đ", totalAmount));
    }

    // Thêm giao dịch vào UI
    private void addTransactionToUI(GiaoDich transaction) {
        LinearLayout transactionLayout = new LinearLayout(this);
        transactionLayout.setOrientation(LinearLayout.VERTICAL);
        transactionLayout.setPadding(16, 16, 16, 16);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 8, 0, 0);
        transactionLayout.setLayoutParams(layoutParams);

        TextView dateTextView = new TextView(this);
        dateTextView.setText(transaction.getNgay());
        dateTextView.setTextColor(getResources().getColor(R.color.Black));
        dateTextView.setTextSize(14);
        transactionLayout.addView(dateTextView);

        LinearLayout innerLayout = new LinearLayout(this);
        innerLayout.setOrientation(LinearLayout.HORIZONTAL);
        innerLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        innerLayout.setPadding(10, 10, 10, 10);

        ImageView transactionIcon = new ImageView(this);
        transactionIcon.setLayoutParams(new LinearLayout.LayoutParams(48, 48));
        transactionViewModel.getCategoryIconById(transaction.getDanhMucId(), danhMuc -> {
            if (danhMuc != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(danhMuc.getIcon(), 0, danhMuc.getIcon().length);
                transactionIcon.setImageBitmap(bitmap);
                GradientDrawable circleDrawable = new GradientDrawable();
                circleDrawable.setShape(GradientDrawable.OVAL);
                circleDrawable.setColor(Color.parseColor(danhMuc.getMauSac()));
                transactionIcon.setBackground(circleDrawable);
                transactionIcon.setClipToOutline(true);
            } else {
                transactionIcon.setImageResource(R.drawable.ic_unknown);
            }
        });
        innerLayout.addView(transactionIcon);

        TextView transactionName = new TextView(this);
        transactionName.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        transactionName.setTextColor(getResources().getColor(R.color.Black));
        transactionName.setTextSize(14);
        transactionName.setPadding(8, 0, 0, 0);
        transactionViewModel.getCategoryById(transaction.getDanhMucId()).observe(this, danhMuc -> {
            if (danhMuc != null) {
                transactionName.setText(danhMuc.getTenDanhMuc());
            } else {
                transactionName.setText("Unknown");
            }
        });

        innerLayout.addView(transactionName);

        TextView transactionAmount = new TextView(this);
        transactionAmount.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        transactionAmount.setText(String.format("%,.0f đ", transaction.getSoTien()));
        transactionAmount.setTextColor(getResources().getColor(R.color.Gray));
        transactionAmount.setTextSize(14);
        innerLayout.addView(transactionAmount);

        transactionLayout.addView(innerLayout);
        transactionListLayout.addView(transactionLayout);
    }
}