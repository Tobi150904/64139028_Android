package vn.ngoviethoang.duancuoiky.Ui.Dashboard;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.ngoviethoang.duancuoiky.R;
import vn.ngoviethoang.duancuoiky.Ui.Account.AccountActivity;
import vn.ngoviethoang.duancuoiky.Ui.Category.CategoryActivity;
import vn.ngoviethoang.duancuoiky.Ui.Components.PieChartComponent;
import vn.ngoviethoang.duancuoiky.Ui.Transaction.AddTransactionActivity;
import vn.ngoviethoang.duancuoiky.Ui.Transaction.TransactionDetailActivity;
import vn.ngoviethoang.duancuoiky.data.entity.GiaoDich;
import vn.ngoviethoang.duancuoiky.data.entity.TaiKhoan;

public class DashboardActivity extends AppCompatActivity {
    private DashboardViewModel dashboardViewModel;
    private TextView totalBalanceAmount, dateRange, tabExpenses, tabIncome, tabDay, tabWeek, tabMonth, tabYear, tabCustom;
    private ImageView iconMenu, iconList, iconAdd, iconEditBalance, iconDown, iconPrevious, iconNext;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private int userId;
    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initializeViews();
        setupViewModel();
        setupListeners();
        initializeDashboard();

        GiaoDich newTransaction = (GiaoDich) getIntent().getSerializableExtra("NEW_TRANSACTION");
        if (newTransaction != null) {
            List<GiaoDich> currentTransactions = dashboardViewModel.getTransactions().getValue();
            if (currentTransactions == null) {
                currentTransactions = new ArrayList<>();
            }

            double totalAmount = currentTransactions.stream().mapToDouble(GiaoDich::getSoTien).sum();
            currentTransactions.add(newTransaction);
            totalAmount += newTransaction.getSoTien();
            addTransactionToUI(newTransaction, totalAmount);
        }
    }

    private void initializeViews() {
        totalBalanceAmount = findViewById(R.id.total_balance_amount);
        dateRange = findViewById(R.id.date_range);
        iconMenu = findViewById(R.id.icon_menu);
        iconList = findViewById(R.id.icon_list);
        iconAdd = findViewById(R.id.icon_add_transaction);
        iconEditBalance = findViewById(R.id.icon_edit_balance);
        iconDown = findViewById(R.id.icon_down);
        iconPrevious = findViewById(R.id.icon_previous);
        iconNext = findViewById(R.id.icon_next);
        tabExpenses = findViewById(R.id.tab_expenses);
        tabIncome = findViewById(R.id.tab_income);
        tabDay = findViewById(R.id.tab_day);
        tabWeek = findViewById(R.id.tab_week);
        tabMonth = findViewById(R.id.tab_month);
        tabYear = findViewById(R.id.tab_year);
        tabCustom = findViewById(R.id.tab_custom);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        pieChart = findViewById(R.id.pieChart);
        setupPieChart();
    }

    private void setupPieChart() {
        pieChart.setUsePercentValues(true); // Hiển thị giá trị phần trăm
        pieChart.getDescription().setEnabled(false); // Tắt mô tả
        pieChart.setExtraOffsets(5, 10, 5, 10); // Đặt lề
        pieChart.setDragDecelerationFrictionCoef(0.95f); // Độ trượt
        pieChart.setDrawHoleEnabled(true); // Cho phép vẽ lỗ ở giữa
        pieChart.setHoleColor(Color.WHITE); // Màu lỗ
        pieChart.setTransparentCircleColor(Color.WHITE); // Màu vòng tròn trong suốt
        pieChart.setTransparentCircleAlpha(110); // Độ trong suốt
        pieChart.setHoleRadius(58f); // Bán kính lỗ
        pieChart.setTransparentCircleRadius(61f); // Bán kính vòng tròn trong suốt
        pieChart.setDrawCenterText(true); // Cho phép vẽ văn bản ở giữa
        pieChart.setRotationAngle(0); // Góc xoay
        pieChart.setRotationEnabled(true); // Cho phép xoay
        pieChart.setHighlightPerTapEnabled(true); // Cho phép highlight khi chạm

        // Tắt hiển thị nhãn (labels) và giá trị (values) bên trong biểu đồ
        pieChart.setDrawEntryLabels(false); // Tắt nhãn bên trong biểu đồ

        // Thiết lập đường kẻ từ nhãn đến phần biểu đồ
        PieDataSet dataSet = new PieDataSet(new ArrayList<>(), ""); // Tạo một dataset rỗng để thiết lập thuộc tính
        dataSet.setValueLinePart1Length(0.4f); // Độ dài phần đầu của đường kẻ
        dataSet.setValueLinePart2Length(0.4f); // Độ dài phần thứ hai của đường kẻ
        dataSet.setValueLineColor(Color.BLACK); // Màu đường kẻ
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE); // Hiển thị giá trị bên ngoài biểu đồ
        dataSet.setValueLinePart1OffsetPercentage(80f); // Đặt khoảng cách từ phần biểu đồ đến đường kẻ
        dataSet.setValueLineVariableLength(true); // Cho phép đường kẻ có độ dài thay đổi

        // Gán dataset vào biểu đồ
        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
    }

    private void updatePieChart(List<GiaoDich> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            // Nếu không có giao dịch, hiển thị biểu đồ với một phần tử duy nhất
            List<PieEntry> entries = new ArrayList<>();
            entries.add(new PieEntry(100f, "Không có giao dịch")); // Phần tử duy nhất chiếm 100%

            // Tạo dataset cho biểu đồ
            PieDataSet dataSet = new PieDataSet(entries, "");
            dataSet.setColors(Color.GRAY); // Màu xám mặc định
            dataSet.setValueTextSize(12f); // Kích thước giá trị
            dataSet.setValueTextColor(Color.WHITE); // Màu giá trị

            // Tạo đối tượng PieData và gán vào biểu đồ
            PieData pieData = new PieData(dataSet);
            pieChart.setData(pieData);
            pieChart.setCenterText("Không có giao dịch"); // Hiển thị thông báo ở giữa
            pieChart.invalidate(); // Vẽ lại biểu đồ
            return;
        }

        // Tạo danh sách các mục (entries) cho biểu đồ
        List<PieEntry> entries = new ArrayList<>();
        double totalAmount = transactions.stream().mapToDouble(GiaoDich::getSoTien).sum();

        // Danh sách màu sắc tương ứng với từng danh mục
        List<Integer> colors = new ArrayList<>();

        for (GiaoDich transaction : transactions) {
            dashboardViewModel.getCategoryById(transaction.getDanhMucId()).observe(this, danhMuc -> {
                if (danhMuc != null) {
                    float percentage = (float) ((transaction.getSoTien() / totalAmount) * 100);
                    entries.add(new PieEntry(percentage, danhMuc.getTenDanhMuc() + " " + String.format("%.1f%%", percentage)));

                    // Lấy màu từ danh mục và thêm vào danh sách màu sắc
                    int color = Color.parseColor(danhMuc.getMauSac());
                    colors.add(color);
                } else {
                    entries.add(new PieEntry((float) ((transaction.getSoTien() / totalAmount) * 100), "Unknown"));
                    colors.add(Color.GRAY); // Màu mặc định nếu không có danh mục
                }

                // Tạo dataset cho biểu đồ
                PieDataSet dataSet = new PieDataSet(entries, "");
                dataSet.setColors(colors); // Sử dụng màu từ danh mục
                dataSet.setValueTextSize(12f); // Kích thước giá trị
                dataSet.setValueTextColor(Color.BLACK); // Màu giá trị
                dataSet.setValueTypeface(Typeface.DEFAULT_BOLD); // Đặt kiểu chữ in đậm
                dataSet.setValueLinePart1Length(0.4f); // Độ dài phần đầu của đường kẻ
                dataSet.setValueLinePart2Length(0.4f); // Độ dài phần thứ hai của đường kẻ
                dataSet.setValueLineColor(Color.BLACK); // Màu đường kẻ
                dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE); // Hiển thị giá trị bên ngoài biểu đồ
                dataSet.setValueLinePart1OffsetPercentage(80f); // Đặt khoảng cách từ phần biểu đồ đến đường kẻ
                dataSet.setValueLineVariableLength(true); // Cho phép đường kẻ có độ dài thay đổi

                // Tắt hiển thị giá trị (phần trăm) bên trong biểu đồ
                dataSet.setDrawValues(false); // Tắt giá trị bên trong biểu đồ

                // Tạo đối tượng PieData và gán vào biểu đồ
                PieData pieData = new PieData(dataSet);
                pieChart.setData(pieData);
                pieChart.setCenterText(String.format("%,.0f đ", totalAmount)); // Hiển thị tổng số tiền
                pieChart.invalidate(); // Vẽ lại biểu đồ
            });
        }
    }

    private void setupViewModel() {
        userId = getIntent().getIntExtra("USER_ID", -1);

        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        dashboardViewModel.getDateRange().observe(this, dateRange::setText);
        dashboardViewModel.getTotalBalance().observe(this, balance -> totalBalanceAmount.setText(String.format("%,.2f VND", balance)));
        dashboardViewModel.getTransactions().observe(this, this::updateTransactionUI);
    }

    private String currentTab = "expenses";
    private void setupListeners() {
        iconMenu.setOnClickListener(v -> drawerLayout.openDrawer(navigationView));
        iconList.setOnClickListener(v -> startActivity(new Intent(this, TransactionDetailActivity.class)));
        iconAdd.setOnClickListener(v -> startActivity(new Intent(this, AddTransactionActivity.class)));
        iconEditBalance.setOnClickListener(v -> showEditBalanceDialog());
        iconDown.setOnClickListener(v -> showAccountsDialog());
        iconPrevious.setOnClickListener(v -> navigateDateRange(-1));
        iconNext.setOnClickListener(v -> navigateDateRange(1));

        tabExpenses.setOnClickListener(v -> {
            selectTab(tabExpenses, "expenses");
            currentTab = "expenses";
            updateFilteredTransactions("chi_phi", dashboardViewModel.getDateRange().getValue());
        });

        tabIncome.setOnClickListener(v -> {
            selectTab(tabIncome, "income");
            currentTab = "income";
            updateFilteredTransactions("thu_nhap", dashboardViewModel.getDateRange().getValue());
        });

        tabDay.setOnClickListener(v -> {
            selectDateRangeTab(tabDay, "day");
            if (currentTab.equals("expenses")) {
                updateFilteredTransactions("chi_phi", "day");
            } else {
                updateFilteredTransactions("thu_nhap", "day");
            }
        });

        tabWeek.setOnClickListener(v -> {
            selectDateRangeTab(tabWeek, "week");
            if (currentTab.equals("expenses")) {
                updateFilteredTransactions("chi_phi", "week");
            } else {
                updateFilteredTransactions("thu_nhap", "week");
            }
        });

        tabMonth.setOnClickListener(v -> {
            selectDateRangeTab(tabMonth, "month");
            if (currentTab.equals("expenses")) {
                updateFilteredTransactions("chi_phi", "month");
            } else {
                updateFilteredTransactions("thu_nhap", "month");
            }
        });

        tabYear.setOnClickListener(v -> {
            selectDateRangeTab(tabYear, "year");
            if (currentTab.equals("expenses")) {
                updateFilteredTransactions("chi_phi", "year");
            } else {
                updateFilteredTransactions("thu_nhap", "year");
            }
        });

        tabCustom.setOnClickListener(v -> showDatePickerDialog());

        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_dashboard) {
                startActivity(new Intent(this, DashboardActivity.class));
                return true;
            }
            if (item.getItemId() == R.id.nav_account) {
                startActivity(new Intent(this, AccountActivity.class));
                return true;
            }
            if (item.getItemId() == R.id.nav_chart) {
                startActivity(new Intent(this, TransactionDetailActivity.class));
                return true;
            }
            if (item.getItemId() == R.id.nav_category) {
                startActivity(new Intent(this, CategoryActivity.class));
                return true;
            }
            if (item.getItemId() == R.id.nav_settings) {
                startActivity(new Intent(this, TransactionDetailActivity.class));
                return true;
            }

            drawerLayout.closeDrawer(navigationView);
            return false;
        });
    }

    private void initializeDashboard() {
        dashboardViewModel.initializeDashboard();
        selectTab(tabExpenses, "expenses");
        selectDateRangeTab(tabDay, "day");
        updateFilteredTransactions("chi_phi", "day");

        Double totalBalance = dashboardViewModel.getTotalBalance().getValue();
        updateMainUI("Tổng cộng", totalBalance != null ? totalBalance : 0);
    }

    private void selectDateRangeTab(TextView selectedTab, String range) {
        resetTab(tabDay);
        resetTab(tabWeek);
        resetTab(tabMonth);
        resetTab(tabYear);
        resetTab(tabCustom);

        highlightTab(selectedTab);
        dashboardViewModel.updateDateRange(range, 0);
        String dateRange = dashboardViewModel.getDateRange().getValue();
        updateFilteredTransactions(currentTab.equals("expenses") ? "chi_phi" : "thu_nhap", dateRange);
    }

    private void selectTab(TextView selectedTab, String tab) {
        resetTab(tabExpenses);
        resetTab(tabIncome);

        highlightTab(selectedTab);
        dashboardViewModel.switchTab(tab);
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
        dashboardViewModel.navigateDateRange(direction);
        String dateRange = dashboardViewModel.getDateRange().getValue();
        if (currentTab.equals("expenses")) {
            updateFilteredTransactions("chi_phi", dateRange);
        } else {
            updateFilteredTransactions("thu_nhap", dateRange);
        }
    }

    private void updateFilteredTransactions(String loai, String range) {
        dashboardViewModel.getFilteredTransactions(loai, range).observe(this, transactions -> {
            updateTransactionUI(transactions);
        });
    }

    private void updateMainUI(String accountName, double balance) {
        TextView mainAccountName = findViewById(R.id.total_balance_label);
        TextView mainAccountBalance = findViewById(R.id.total_balance_amount);
        ImageView iconEditBalance = findViewById(R.id.icon_edit_balance);

        mainAccountName.setText(accountName);
        mainAccountBalance.setText(String.format("%,.0f đ", balance));

        if ("Tổng cộng".equals(accountName)) {
            iconEditBalance.setVisibility(View.GONE);
            totalBalanceAmount.setTag(null);
        } else {
            iconEditBalance.setVisibility(View.VISIBLE);
            TaiKhoan selectedAccount = dashboardViewModel.getAccountByName(accountName);
            totalBalanceAmount.setTag(selectedAccount);
        }
    }

    private void showAccountsDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_accounts);

        RadioGroup accountsContainer = dialog.findViewById(R.id.accounts_container);
        LinearLayout totalContainer = dialog.findViewById(R.id.total_container);
        TextView totalBalanceAmount = dialog.findViewById(R.id.text_total_amt);
        TextView btnCancel = dialog.findViewById(R.id.btn_cancel);
        TextView btnChoice = dialog.findViewById(R.id.btn_choice);
        RadioButton radioTotal = dialog.findViewById(R.id.radio_total);

        radioTotal.setChecked(true);
        final RadioButton[] lastCheckedRadioButton = {radioTotal};

        dashboardViewModel.getAccounts().observe(this, accounts -> {
            accountsContainer.removeAllViews();
            accountsContainer.addView(totalContainer);

            double total = 0;
            for (TaiKhoan account : accounts) {
                LinearLayout accountLayout = createAccountLayout(account, dialog);
                RadioButton radioButton = (RadioButton) accountLayout.getChildAt(0);

                radioButton.setOnClickListener(v -> {
                    if (lastCheckedRadioButton[0] != null) {
                        lastCheckedRadioButton[0].setChecked(false);
                    }
                    lastCheckedRadioButton[0] = radioButton;
                    radioButton.setChecked(true);
                });

                accountsContainer.addView(accountLayout);
                total += account.getSodu();
            }

            totalBalanceAmount.setText(String.format("%,.0f đ", total));
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnChoice.setOnClickListener(v -> {
            if (lastCheckedRadioButton[0] != null) {
                if (lastCheckedRadioButton[0] == radioTotal) {
                    updateMainUI("Tổng cộng", dashboardViewModel.getTotalBalance().getValue());
                    Toast.makeText(this, "Bạn đã chọn: Tổng cộng", Toast.LENGTH_SHORT).show();
                } else {
                    LinearLayout selectedLayout = (LinearLayout) lastCheckedRadioButton[0].getParent();
                    LinearLayout textContainer = (LinearLayout) selectedLayout.getChildAt(2);
                    TextView selectedAccountName = (TextView) textContainer.getChildAt(0);
                    TextView selectedAccountAmount = (TextView) textContainer.getChildAt(1);
                    updateMainUI(selectedAccountName.getText().toString(), Double.parseDouble(selectedAccountAmount.getText().toString().replace(" đ", "").replace(",", "")));
                    Toast.makeText(this, "Bạn đã chọn: " + selectedAccountName.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Vui lòng chọn tài khoản", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });

        radioTotal.setOnClickListener(v -> {
            if (lastCheckedRadioButton[0] != null) {
                lastCheckedRadioButton[0].setChecked(false);
            }
            lastCheckedRadioButton[0] = radioTotal;
            radioTotal.setChecked(true);
        });

        dialog.show();
    }

    private LinearLayout createAccountLayout(TaiKhoan account, Dialog dialog) {
        LinearLayout accountLayout = new LinearLayout(this);
        accountLayout.setOrientation(LinearLayout.HORIZONTAL);
        accountLayout.setPadding(10, 10, 10, 10);
        accountLayout.setGravity(Gravity.CENTER_VERTICAL);
        accountLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        RadioButton radioButton = new RadioButton(this);
        LinearLayout.LayoutParams radioParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        radioParams.setMargins(0, 0, 16, 0);
        radioButton.setLayoutParams(radioParams);

        ImageView accountIcon = new ImageView(this);
        Bitmap bitmap = BitmapFactory.decodeByteArray(account.getIcon(), 0, account.getIcon().length);
        accountIcon.setImageBitmap(bitmap);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(
                80, 80
        );
        iconParams.setMargins(16, 0, 16, 0);
        accountIcon.setLayoutParams(iconParams);
        accountIcon.setContentDescription("Account Icon");

        LinearLayout textContainer = new LinearLayout(this);
        textContainer.setOrientation(LinearLayout.VERTICAL);
        textContainer.setPadding(32, 0, 0, 0);
        textContainer.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1
        ));

        TextView accountName = new TextView(this);
        accountName.setText(account.getTen());
        accountName.setTextSize(16);
        accountName.setTextColor(getResources().getColor(R.color.Black));
        accountName.setPadding(8, 0, 0, 0);

        TextView accountAmount = new TextView(this);
        accountAmount.setText(String.format("%,.0f đ", account.getSodu()));
        accountAmount.setTextSize(14);
        accountAmount.setTextColor(getResources().getColor(R.color.Gray));
        accountAmount.setPadding(8, 0, 0, 0);

        textContainer.addView(accountName);
        textContainer.addView(accountAmount);

        accountLayout.addView(radioButton);
        accountLayout.addView(accountIcon);
        accountLayout.addView(textContainer);

        return accountLayout;
    }

    private void showEditBalanceDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_edit_balance);

        TextView title = dialog.findViewById(R.id.dialog_title);
        EditText editBalance = dialog.findViewById(R.id.edit_balance_input);
        TextView btnCancel = dialog.findViewById(R.id.btn_cancel);
        TextView btnSave = dialog.findViewById(R.id.btn_save);

        title.setText("Chỉnh sửa số dư");

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnSave.setOnClickListener(v -> {
            try {
                double newBalance = Double.parseDouble(editBalance.getText().toString());
                updateAccountBalance(newBalance);
                dialog.dismiss();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Số dư không hợp lệ!", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void updateAccountBalance(double newBalance) {
        TaiKhoan selectedAccount = (TaiKhoan) totalBalanceAmount.getTag();
        if (selectedAccount != null) {
            selectedAccount.setSodu(newBalance);
            dashboardViewModel.updateBalance(selectedAccount);
            totalBalanceAmount.setText(String.format("%,.0f đ", newBalance));
            Toast.makeText(this, "Số dư đã được cập nhật!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDatePickerDialog() {
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener endDateListener = (view, year, month, dayOfMonth) -> {
            endDate.set(year, month, dayOfMonth);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String start = formatter.format(startDate.getTime());
            String end = formatter.format(endDate.getTime());
            dashboardViewModel.updateCustomDateRange(start, end);
            updateFilteredTransactions(dashboardViewModel.getTabSelected().getValue(), "custom");
        };

        DatePickerDialog.OnDateSetListener startDateListener = (view, year, month, dayOfMonth) -> {
            startDate.set(year, month, dayOfMonth);
            new DatePickerDialog(this, endDateListener, endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH), endDate.get(Calendar.DAY_OF_MONTH)).show();
        };

        new DatePickerDialog(this, startDateListener, startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateTransactionUI(List<GiaoDich> transactions) {
        LinearLayout transactionContainer = findViewById(R.id.transaction_list);
        transactionContainer.removeAllViews();

        // Gộp các giao dịch giống nhau dựa trên danh mục
        Map<Integer, GiaoDich> mergedTransactions = new HashMap<>();
        double totalAmount = 0;

        for (GiaoDich transaction : transactions) {
            int danhMucId = transaction.getDanhMucId();
            if (mergedTransactions.containsKey(danhMucId)) {
                // Nếu đã có giao dịch cùng danh mục, cộng dồn số tiền
                GiaoDich existingTransaction = mergedTransactions.get(danhMucId);
                existingTransaction.setSoTien(existingTransaction.getSoTien() + transaction.getSoTien());
            } else {
                // Nếu chưa có, thêm giao dịch vào map
                mergedTransactions.put(danhMucId, new GiaoDich(transaction));
            }
            totalAmount += transaction.getSoTien();
        }

        // Hiển thị các giao dịch đã gộp
        for (GiaoDich mergedTransaction : mergedTransactions.values()) {
            addTransactionToUI(mergedTransaction, totalAmount);
        }

        // Cập nhật biểu đồ với các giao dịch đã gộp
        updatePieChart(new ArrayList<>(mergedTransactions.values()));
    }

    private void addTransactionToUI(GiaoDich transaction, double totalAmount) {
        LinearLayout transactionLayout = new LinearLayout(this);
        transactionLayout.setOrientation(LinearLayout.HORIZONTAL);
        transactionLayout.setPadding(10, 10, 10, 10);

        ImageView transactionIcon = new ImageView(this);
        transactionIcon.setLayoutParams(new LinearLayout.LayoutParams(48, 48));

        dashboardViewModel.getCategoryIconById(transaction.getDanhMucId(), danhMuc -> {
            if (danhMuc != null) {
                byte[] iconBytes = danhMuc.getIcon();
                Bitmap categoryIcon = BitmapFactory.decodeByteArray(iconBytes, 0, iconBytes.length);
                transactionIcon.setImageBitmap(categoryIcon);

                // Tạo hình tròn cho biểu tượng danh mục
                GradientDrawable circleDrawable = new GradientDrawable();
                circleDrawable.setShape(GradientDrawable.OVAL);
                circleDrawable.setColor(Color.parseColor(danhMuc.getMauSac()));
                transactionIcon.setBackground(circleDrawable);
                transactionIcon.setClipToOutline(true);
            } else {
                transactionIcon.setImageResource(R.drawable.ic_unknown);
            }
        });

        transactionLayout.addView(transactionIcon);

        TextView transactionName = new TextView(this);
        transactionName.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        transactionName.setTextColor(getResources().getColor(R.color.Black));
        transactionName.setTextSize(14);
        transactionName.setPadding(8, 0, 0, 0);
        dashboardViewModel.getCategoryById(transaction.getDanhMucId()).observe(this, danhMuc -> {
            if (danhMuc != null) {
                transactionName.setText(danhMuc.getTenDanhMuc());
            } else {
                transactionName.setText("Unknown");
            }
        });

        transactionLayout.addView(transactionName);

        // Tính phần trăm của giao dịch
        double percentage = (transaction.getSoTien() / totalAmount) * 100;

        // Hiển thị phần trăm
        TextView transactionPercentage = new TextView(this);
        transactionPercentage.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        transactionPercentage.setText(String.format("%.1f%%", percentage));
        transactionPercentage.setTextColor(getResources().getColor(R.color.Gray));
        transactionPercentage.setTextSize(14);
        transactionPercentage.setPadding(8, 0, 8, 0);
        transactionLayout.addView(transactionPercentage);

        // Hiển thị số tiền
        TextView transactionAmount = new TextView(this);
        transactionAmount.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        transactionAmount.setText(String.format("%,.0f đ", transaction.getSoTien()));
        transactionAmount.setTextColor(getResources().getColor(R.color.Gray));
        transactionAmount.setTextSize(14);
        transactionLayout.addView(transactionAmount);

        // Thêm giao dịch vào container
        LinearLayout transactionContainer = findViewById(R.id.transaction_list);
        transactionContainer.addView(transactionLayout);
    }
}