package vn.ngoviethoang.duancuoiky.Ui.Transaction;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import vn.ngoviethoang.duancuoiky.R;
import vn.ngoviethoang.duancuoiky.Ui.Dashboard.DashboardActivity;
import vn.ngoviethoang.duancuoiky.Ui.Dashboard.DashboardViewModel;
import vn.ngoviethoang.duancuoiky.Ui.Category.CategoryActivity;
import vn.ngoviethoang.duancuoiky.data.entity.DanhMuc;
import vn.ngoviethoang.duancuoiky.data.entity.GiaoDich;
import vn.ngoviethoang.duancuoiky.data.entity.TaiKhoan;
import vn.ngoviethoang.duancuoiky.data.repository.DanhMucRepository;

public class AddTransactionActivity extends AppCompatActivity {
    private ImageView backButton, calendarButton;
    private TextView accountName, categoryName, todayDate, yesterdayDate, twoDaysAgoDate;
    private EditText amountEditText, noteEditText;
    private TransactionViewModel viewModel;
    private DashboardViewModel dashboardViewModel;
    private String transactionType = "chi_phi";
    private RadioButton lastCheckedRadioButton;
    private Date selectedDate;
    private int selectedCategoryId = -1; // Biến lưu trữ ID danh mục được chọn
    private GridLayout categoryContainer;
    private DanhMucRepository danhMucRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        // Khởi tạo các view
        backButton = findViewById(R.id.ic_back);
        accountName = findViewById(R.id.account_name);
        categoryName = findViewById(R.id.category_name);
        amountEditText = findViewById(R.id.amount);
        noteEditText = findViewById(R.id.note_edit_text);
        Button saveButton = findViewById(R.id.save_button);
        TextView tabExpense = findViewById(R.id.tab_expense);
        TextView tabIncome = findViewById(R.id.tab_income);
        Button addCardButton = findViewById(R.id.add_card_button);
        todayDate = findViewById(R.id.today_date);
        yesterdayDate = findViewById(R.id.yesterday_date);
        twoDaysAgoDate = findViewById(R.id.two_days_ago_date);
        calendarButton = findViewById(R.id.ic_calendar);
        categoryContainer = findViewById(R.id.category_container);

        viewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        danhMucRepository = new DanhMucRepository(this);

        // Đặt các sự kiện click
        backButton.setOnClickListener(v -> finish());
        accountName.setOnClickListener(v -> showAccountsDialog());
        saveButton.setOnClickListener(v -> saveTransaction());
        tabExpense.setOnClickListener(v -> switchToExpense());
        tabIncome.setOnClickListener(v -> switchToIncome());
        addCardButton.setOnClickListener(v -> showAddCardDialog());
        calendarButton.setOnClickListener(v -> showDatePickerDialog());

        loadCategories(transactionType);
        // Thiết lập các trường ngày
        setupDateFields();
    }

    // Chuyển sang tab chi phí
    private void switchToExpense() {
        transactionType = "chi_phi";
        loadCategories(transactionType); // Reload categories when switching tabs
    }

    // Chuyển sang tab thu nhập
    private void switchToIncome() {
        transactionType = "thu_nhap";
        loadCategories(transactionType); // Reload categories when switching tabs
    }

    private void loadCategories(String loai) {
        danhMucRepository.getDanhMucByLoai(loai).observe(this, danhMucList -> {
            categoryContainer.removeAllViews();
            for (DanhMuc danhMuc : danhMucList) {
                LinearLayout categoryLayout = createCategoryLayout(danhMuc);
                categoryContainer.addView(categoryLayout);
            }
        });
    }

    // Hiển thị dialog chọn tài khoản
    private void showAccountsDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_accounts);

        RadioGroup accountsContainer = dialog.findViewById(R.id.accounts_container);
        TextView btnCancel = dialog.findViewById(R.id.btn_cancel);
        TextView btnChoice = dialog.findViewById(R.id.btn_choice);

        // Quan sát danh sách tài khoản từ ViewModel
        dashboardViewModel.getAccounts().observe(this, accounts -> {
            accountsContainer.removeAllViews(); // Xóa các tài khoản cũ trước khi thêm mới

            for (TaiKhoan account : accounts) {
                // Tạo layout chứa thông tin tài khoản
                LinearLayout accountLayout = createAccountLayout(account, dialog);

                // Tìm RadioButton từ layout đã tạo
                RadioButton radioButton = (RadioButton) accountLayout.getChildAt(0);

                // Đặt sự kiện để xử lý việc chọn một tài khoản duy nhất
                radioButton.setOnClickListener(v -> {
                    if (lastCheckedRadioButton != null) {
                        lastCheckedRadioButton.setChecked(false); // Bỏ chọn RadioButton trước đó
                    }
                    lastCheckedRadioButton = radioButton; // Cập nhật RadioButton hiện tại được chọn
                    radioButton.setChecked(true); // Đảm bảo RadioButton này luôn được chọn
                });

                accountsContainer.addView(accountLayout); // Thêm layout tài khoản vào container
            }
        });

        // Hủy dialog khi nhấn nút "Cancel"
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        // Xử lý logic khi nhấn nút "Choose"
        btnChoice.setOnClickListener(v -> {
            if (lastCheckedRadioButton != null) {
                // Lấy tài khoản được chọn từ RadioButton
                LinearLayout selectedLayout = (LinearLayout) lastCheckedRadioButton.getParent();
                LinearLayout textContainer = (LinearLayout) selectedLayout.getChildAt(2);
                TextView selectedAccountName = (TextView) textContainer.getChildAt(0);
                accountName.setText(selectedAccountName.getText().toString());
                Toast.makeText(this, "Bạn đã chọn: " + selectedAccountName.getText().toString(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Vui lòng chọn tài khoản", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });

        dialog.show();
    }

    // Tạo layout chứa thông tin tài khoản
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

    // Tạo layout chứa thông tin danh mục
    private LinearLayout createCategoryLayout(DanhMuc danhMuc) {
        LinearLayout categoryLayout = new LinearLayout(this);
        categoryLayout.setOrientation(LinearLayout.VERTICAL);
        categoryLayout.setPadding(20, 10, 20, 10);
        categoryLayout.setGravity(Gravity.CENTER);

        // Tạo ImageView để hiển thị ảnh danh mục trong hình tròn
        ImageView categoryIcon = new ImageView(this);
        Bitmap bitmap = BitmapFactory.decodeByteArray(danhMuc.getIcon(), 0, danhMuc.getIcon().length);
        categoryIcon.setImageBitmap(bitmap);
        categoryIcon.setLayoutParams(new LinearLayout.LayoutParams(200, 200));

        // Tạo hình tròn cho ImageView
        GradientDrawable circleDrawable = new GradientDrawable();
        circleDrawable.setShape(GradientDrawable.OVAL);
        circleDrawable.setColor(Color.parseColor(danhMuc.getMauSac()));
        categoryIcon.setBackground(circleDrawable);
        categoryIcon.setClipToOutline(true);

        TextView categoryName = new TextView(this);
        categoryName.setText(danhMuc.getTenDanhMuc());
        categoryName.setTextSize(14);
        categoryName.setTextColor(getResources().getColor(R.color.Black));
        categoryName.setGravity(Gravity.CENTER);

        categoryLayout.addView(categoryIcon);
        categoryLayout.addView(categoryName);

        categoryLayout.setOnClickListener(v -> {
            highlightSelectedCategory(categoryLayout);
            selectedCategoryId = danhMuc.getId(); // Cập nhật ID danh mục được chọn
        });

        return categoryLayout;
    }

    // Làm nổi bật danh mục được chọn
    private void highlightSelectedCategory(LinearLayout selectedCategoryLayout) {
        // Đặt lại màu nền cho tất cả các danh mục
        for (int i = 0; i < categoryContainer.getChildCount(); i++) {
            View child = categoryContainer.getChildAt(i);
            if (child instanceof LinearLayout) {
                child.setBackgroundColor(getResources().getColor(R.color.White)); // Đặt lại nền mặc định
            }
        }
        // Đặt màu nền cho danh mục được chọn
        selectedCategoryLayout.setBackgroundColor(getResources().getColor(R.color.Gray));
    }

    // Hiển thị dialog thêm thẻ
    private void showAddCardDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_card);
        EditText cardNameEditText = dialog.findViewById(R.id.card_name_edit_text);
        TextView btnCancel = dialog.findViewById(R.id.btn_cancel);
        TextView btnChoice = dialog.findViewById(R.id.btn_choice);

        btnChoice.setOnClickListener(v -> {
            String cardName = cardNameEditText.getText().toString().trim();
            if (!cardName.isEmpty()) {
                addCardView(cardName);
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Vui lòng nhập tên thẻ", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    // Thêm view thẻ mới vào giao diện
    private void addCardView(String cardName) {
        LinearLayout cardContainer = findViewById(R.id.card_container);
        Button addCardButton = findViewById(R.id.add_card_button);

        TextView cardTextView = new TextView(this);
        cardTextView.setText(cardName);
        cardTextView.setTextSize(16);
        cardTextView.setPadding(16, 16, 16, 16);
        cardTextView.setBackgroundResource(R.drawable.card_background);

        cardTextView.setOnClickListener(v -> {
            // Đặt lại màu nền cho tất cả các thẻ
            for (int i = 0; i < cardContainer.getChildCount(); i++) {
                View child = cardContainer.getChildAt(i);
                if (child instanceof TextView) {
                    child.setBackgroundResource(R.drawable.card_background); // Đặt lại nền mặc định
                }
            }
            // Đặt màu nền cho thẻ được chọn
            cardTextView.setBackgroundColor(getResources().getColor(R.color.Green));
        });

        // Thêm view thẻ mới trước nút "Add Card"
        cardContainer.addView(cardTextView, cardContainer.indexOfChild(addCardButton));
    }

    // Thiết lập các trường ngày
    private void setupDateFields() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();

        todayDate.setText(dateFormat.format(calendar.getTime()));
        calendar.add(Calendar.DATE, -1);
        yesterdayDate.setText(dateFormat.format(calendar.getTime()));
        calendar.add(Calendar.DATE, -1);
        twoDaysAgoDate.setText(dateFormat.format(calendar.getTime()));

        todayDate.setOnClickListener(v -> {
            selectedDate = new Date();
            highlightSelectedDate(todayDate);
        });
        yesterdayDate.setOnClickListener(v -> {
            calendar.add(Calendar.DATE, -1);
            selectedDate = calendar.getTime();
            highlightSelectedDate(yesterdayDate);
        });
        twoDaysAgoDate.setOnClickListener(v -> {
            calendar.add(Calendar.DATE, -2);
            selectedDate = calendar.getTime();
            highlightSelectedDate(twoDaysAgoDate);
        });
    }

    // Làm nổi bật ngày được chọn
    private void highlightSelectedDate(TextView selectedDateView) {
        todayDate.setTextColor(getResources().getColor(R.color.Gray));
        yesterdayDate.setTextColor(getResources().getColor(R.color.Gray));
        twoDaysAgoDate.setTextColor(getResources().getColor(R.color.Gray));

        selectedDateView.setTextColor(getResources().getColor(R.color.Green));
    }

    // Lưu giao dịch
    private void saveTransaction() {
        String amountString = amountEditText.getText().toString().trim();
        String note = noteEditText.getText().toString().trim();

        if (amountString.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập số tiền", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountString);
        int taiKhoanId = getSelectedTaiKhoanId();
        int danhMucId = selectedCategoryId;
        Date currentDate = selectedDate != null ? selectedDate : new Date();

        if (taiKhoanId == -1) {
            Toast.makeText(this, "Vui lòng chọn tài khoản hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (danhMucId == -1) {
            Toast.makeText(this, "Vui lòng chọn danh mục hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(currentDate);

        GiaoDich giaoDich = new GiaoDich(taiKhoanId, danhMucId, amount, formattedDate, note, transactionType);
        viewModel.addTransaction(giaoDich);

        viewModel.getGiaoDich().observe(this, giaoDich1 -> {
            if (giaoDich1 != null) {
                Toast.makeText(AddTransactionActivity.this, "Giao dịch đã được lưu", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, DashboardActivity.class);
                intent.putExtra("NEW_TRANSACTION", giaoDich1);
                startActivity(intent);
                finish();
            }
        });

        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Toast.makeText(AddTransactionActivity.this, "Lỗi: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Lấy ID tài khoản được chọn
    private int getSelectedTaiKhoanId() {
        if (lastCheckedRadioButton != null) {
            LinearLayout selectedLayout = (LinearLayout) lastCheckedRadioButton.getParent();
            TextView selectedAccountName = (TextView) ((LinearLayout) selectedLayout.getChildAt(2)).getChildAt(0);
            String accountName = selectedAccountName.getText().toString();
            List<TaiKhoan> accounts = dashboardViewModel.getAccounts().getValue();
            if (accounts != null) {
                for (TaiKhoan account : accounts) {
                    if (account.getTen().equals(accountName)) {
                        return account.getId();
                    }
                }
            }
        }
        return -1; // Trả về -1 nếu không có tài khoản nào được chọn
    }

    // Hiển thị dialog chọn ngày
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    selectedDate = calendar.getTime();
                    java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy");
                    todayDate.setText(dateFormat.format(selectedDate));
                    highlightSelectedDate(todayDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
}