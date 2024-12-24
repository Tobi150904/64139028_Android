package vn.ngoviethoang.duancuoiky.Ui.Transaction;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import vn.ngoviethoang.duancuoiky.Fragment_ChiPhi;
import vn.ngoviethoang.duancuoiky.Fragment_ThuNhap;
import vn.ngoviethoang.duancuoiky.R;
import vn.ngoviethoang.duancuoiky.Ui.Dashboard.DashboardActivity;
import vn.ngoviethoang.duancuoiky.Ui.Dashboard.DashboardViewModel;
import vn.ngoviethoang.duancuoiky.data.entity.GiaoDich;
import vn.ngoviethoang.duancuoiky.data.entity.TaiKhoan;

public class AddTransactionActivity extends AppCompatActivity {
    private ImageView backButton, addPhotoButton, calendarButton;
    private TextView accountName, categoryName, todayDate, yesterdayDate, twoDaysAgoDate;
    private EditText amountEditText, noteEditText;
    private TransactionViewModel viewModel;
    private DashboardViewModel dashboardViewModel;
    private String transactionType = "expense"; // "expense" or "income"
    private RadioButton lastCheckedRadioButton;
    private Date selectedDate;
    private int selectedCategoryId = -1; // Variable to store selected category ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        // Initialize views
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

        viewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        // Set click listeners
        backButton.setOnClickListener(v -> finish());
        accountName.setOnClickListener(v -> showAccountsDialog());
        saveButton.setOnClickListener(v -> saveTransaction());
        tabExpense.setOnClickListener(v -> switchToExpense());
        tabIncome.setOnClickListener(v -> switchToIncome());
        addCardButton.setOnClickListener(v -> showAddCardDialog());
        calendarButton.setOnClickListener(v -> showDatePickerDialog());

        // Replace fragment with default category
        replaceFragment(new Fragment_ChiPhi());

        // Set up date fields
        setupDateFields();
    }

    private void switchToExpense() {
        transactionType = "expense";
        replaceFragment(new Fragment_ChiPhi());
    }

    private void switchToIncome() {
        transactionType = "income";
        replaceFragment(new Fragment_ThuNhap());
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    private void showAccountsDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_accounts);

        RadioGroup accountsContainer = dialog.findViewById(R.id.accounts_container);
        TextView btnCancel = dialog.findViewById(R.id.btn_cancel);
        TextView btnChoice = dialog.findViewById(R.id.btn_choice);

        // Theo dõi danh sách tài khoản từ ViewModel
        dashboardViewModel.getAccounts().observe(this, accounts -> {
            accountsContainer.removeAllViews(); // Xóa các tài khoản cũ trước khi thêm mới

            for (TaiKhoan account : accounts) {
                // Tạo layout chứa thông tin tài khoản
                LinearLayout accountLayout = createAccountLayout(account, dialog);

                // Tìm RadioButton từ layout được tạo
                RadioButton radioButton = (RadioButton) accountLayout.getChildAt(0);

                // Thiết lập sự kiện để xử lý chọn một tài khoản duy nhất
                radioButton.setOnClickListener(v -> {
                    if (lastCheckedRadioButton != null) {
                        lastCheckedRadioButton.setChecked(false); // Bỏ chọn RadioButton trước đó
                    }
                    lastCheckedRadioButton = radioButton; // Cập nhật RadioButton đang được chọn
                    radioButton.setChecked(true); // Đảm bảo RadioButton này luôn được chọn
                });

                accountsContainer.addView(accountLayout); // Thêm layout tài khoản vào container
            }
        });

        // Hủy dialog khi nhấn nút "Hủy"
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        // Xử lý logic khi nhấn nút "Chọn"
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

    private void addCardView(String cardName) {
        LinearLayout cardContainer = findViewById(R.id.card_container); // Assuming you have a LinearLayout with this ID
        Button addCardButton = findViewById(R.id.add_card_button);

        TextView cardTextView = new TextView(this);
        cardTextView.setText(cardName);
        cardTextView.setTextSize(16);
        cardTextView.setPadding(16, 16, 16, 16);
        cardTextView.setBackgroundResource(R.drawable.card_background); // Assuming you have a drawable for card background

        // Add click listener to change background color when selected
        cardTextView.setOnClickListener(v -> {
            // Reset background color for all cards
            for (int i = 0; i < cardContainer.getChildCount(); i++) {
                View child = cardContainer.getChildAt(i);
                if (child instanceof TextView) {
                    child.setBackgroundResource(R.drawable.card_background); // Reset to default background
                }
            }
            // Set background color for selected card
            cardTextView.setBackgroundColor(getResources().getColor(R.color.Green));
        });

        // Add the new card view before the "Add Card" button
        cardContainer.addView(cardTextView, cardContainer.indexOfChild(addCardButton));
    }

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

    private void highlightSelectedDate(TextView selectedDateView) {
        todayDate.setTextColor(getResources().getColor(R.color.Gray));
        yesterdayDate.setTextColor(getResources().getColor(R.color.Gray));
        twoDaysAgoDate.setTextColor(getResources().getColor(R.color.Gray));

        selectedDateView.setTextColor(getResources().getColor(R.color.Green));
    }

    private void saveTransaction() {
        String amountString = amountEditText.getText().toString().trim();
        String note = noteEditText.getText().toString().trim();

        if (amountString.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập số tiền", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountString);
        int taiKhoanId = getSelectedTaiKhoanId(); // Implement this method to get the selected account ID
        int danhMucId = getSelectedDanhMucId(); // Implement this method to get the selected category ID
        Date currentDate = selectedDate != null ? selectedDate : new Date(); // Use the selected date or current date

        GiaoDich giaoDich = new GiaoDich(taiKhoanId, danhMucId, amount, currentDate, note, transactionType);

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

    private int getSelectedTaiKhoanId() {
        // Implement logic to get the selected account ID
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
        return -1; // Return -1 if no account is selected
    }

    private int getSelectedDanhMucId() {
        // Implement logic to get the selected category ID
        return selectedCategoryId;
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    selectedDate = calendar.getTime();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
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