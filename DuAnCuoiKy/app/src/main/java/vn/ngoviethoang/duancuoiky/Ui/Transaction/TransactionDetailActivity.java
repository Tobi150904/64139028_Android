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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import java.util.List;
import vn.ngoviethoang.duancuoiky.R;
import vn.ngoviethoang.duancuoiky.data.entity.GiaoDich;
import vn.ngoviethoang.duancuoiky.Ui.Dashboard.DashboardActivity;

public class TransactionDetailActivity extends AppCompatActivity {

    private TransactionViewModel transactionViewModel;
    private TextView dateRange;
    private TextView tabExpenses, tabIncome, tabDay, tabWeek, tabMonth, tabYear;
    private ImageView iconPrevious, iconNext, iconBack;
    private LinearLayout transactionListLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);

        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        dateRange = findViewById(R.id.date_range);
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

        transactionViewModel.getDateRange().observe(this, date -> dateRange.setText(date));

        selectTab(tabExpenses, "chi_phi");
        selectDateRangeTab(tabDay, "day");
    }

    private void setupListeners() {
        iconBack.setOnClickListener(v -> {
            Intent intent = new Intent(TransactionDetailActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        });

        tabExpenses.setOnClickListener(v -> selectTab(tabExpenses, "chi_phi"));
        tabIncome.setOnClickListener(v -> selectTab(tabIncome, "thu_nhap"));

        tabDay.setOnClickListener(v -> selectDateRangeTab(tabDay, "day"));
        tabWeek.setOnClickListener(v -> selectDateRangeTab(tabWeek, "week"));
        tabMonth.setOnClickListener(v -> selectDateRangeTab(tabMonth, "month"));
        tabYear.setOnClickListener(v -> selectDateRangeTab(tabYear, "year"));

        iconPrevious.setOnClickListener(v -> navigateDateRange(-1));
        iconNext.setOnClickListener(v -> navigateDateRange(1));
    }

    private void selectTab(TextView selectedTab, String tab) {
        resetTab(tabExpenses);
        resetTab(tabIncome);
        highlightTab(selectedTab);
        transactionViewModel.updateTransactions(tab);
        transactionViewModel.getGiaoDichList().observe(this, this::updateTransactionList);
    }

    private void selectDateRangeTab(TextView selectedTab, String range) {
        resetTab(tabDay);
        resetTab(tabWeek);
        resetTab(tabMonth);
        resetTab(tabYear);
        highlightTab(selectedTab);
        transactionViewModel.updateDateRange(range, 0);
    }

    private void resetTab(TextView tab) {
        tab.setTextColor(getResources().getColor(R.color.Gray));
        tab.setTypeface(null, Typeface.NORMAL);
        tab.setPaintFlags(tab.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
    }

    private void highlightTab(TextView tab) {
        tab.setTextColor(getResources().getColor(R.color.Red));
        tab.setTypeface(null, Typeface.BOLD);
        tab.setPaintFlags(tab.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    private void navigateDateRange(int direction) {
        transactionViewModel.navigateDateRange(direction);
    }

    private void updateTransactionList(List<GiaoDich> giaoDichList) {
        transactionListLayout.removeAllViews();
        for (GiaoDich giaoDich : giaoDichList) {
            addTransactionToUI(giaoDich);
        }
    }

    private void addTransactionToUI(GiaoDich transaction) {
        LinearLayout transactionLayout = new LinearLayout(this);
        transactionLayout.setOrientation(LinearLayout.VERTICAL);
        transactionLayout.setPadding(16, 16, 16, 16);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 8, 0, 0); // Set top margin to 8dp
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