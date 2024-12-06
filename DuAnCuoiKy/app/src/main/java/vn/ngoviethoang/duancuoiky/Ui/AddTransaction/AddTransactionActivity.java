package vn.ngoviethoang.duancuoiky.Ui.AddTransaction;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.Calendar;

import vn.ngoviethoang.duancuoiky.R;
import vn.ngoviethoang.duancuoiky.data.entity.GiaoDich;

public class AddTransactionActivity extends AppCompatActivity {
    private ImageView backButton, addPhotoButton, calendarButton;
    private TextView accountName, categoryName, todayDate, yesterdayDate, twoDaysAgoDate;
    private EditText amountEditText, noteEditText;
    private AddTransactionViewModel viewModel;
    private String transactionType = "expense"; // "expense" or "income"
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        // Initialize views
        backButton = findViewById(R.id.ic_back);
        addPhotoButton = findViewById(R.id.ic_add_photo);
        calendarButton = findViewById(R.id.ic_calendar);
        accountName = findViewById(R.id.account_name);
        categoryName = findViewById(R.id.category_name);
        amountEditText = findViewById(R.id.amount);
        noteEditText = findViewById(R.id.note_edit_text);
        todayDate = findViewById(R.id.today_date);
        yesterdayDate = findViewById(R.id.yesterday_date);
        twoDaysAgoDate = findViewById(R.id.two_days_ago_date);
        Button saveButton = findViewById(R.id.save_button);
        TextView tabExpense = findViewById(R.id.tab_expense);
        TextView tabIncome = findViewById(R.id.tab_income);
        Button addCardButton = findViewById(R.id.add_card_button);

        viewModel = new ViewModelProvider(this).get(AddTransactionViewModel.class);

        // Set click listeners
        backButton.setOnClickListener(v -> finish());
        accountName.setOnClickListener(v -> showAccountDialog());
        addPhotoButton.setOnClickListener(v -> selectImageFromGallery());
        calendarButton.setOnClickListener(v -> showDatePickerDialog());
        saveButton.setOnClickListener(v -> saveTransaction());
        tabExpense.setOnClickListener(v -> switchToExpense());
        tabIncome.setOnClickListener(v -> switchToIncome());
        addCardButton.setOnClickListener(v -> showAddCardDialog());

        // Update date text views
        updateDateTextViews();

        // Observe ViewModel LiveData
        viewModel.getSelectedAccount().observe(this, account -> accountName.setText(account));
        viewModel.getSelectedCategory().observe(this, category -> categoryName.setText(category));
        viewModel.getSelectedDate().observe(this, date -> todayDate.setText(date));
        viewModel.getSelectedCard().observe(this, card -> addCardButton.setText(card));
    }

    private void switchToExpense() {
        transactionType = "expense";
        // Update UI to reflect expense tab selected
    }

    private void switchToIncome() {
        transactionType = "income";
        // Update UI to reflect income tab selected
    }

    private void showAccountDialog() {
        // Show dialog to select account
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_select_account);
        // Implement account selection logic here
        dialog.show();
    }

    private void selectImageFromGallery() {
        // Intent to select image from gallery
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    viewModel.setSelectedDate(selectedDate);
                    updateDateTextViews();
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            addPhotoButton.setImageURI(selectedImageUri);
        }
    }

    private void showAddCardDialog() {
        // Show dialog to add a card
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_card);
        EditText cardNameEditText = dialog.findViewById(R.id.card_name_edit_text);
        Button addCardButton = dialog.findViewById(R.id.add_card_button);
        addCardButton.setOnClickListener(v -> {
            String cardName = cardNameEditText.getText().toString().trim();
            if (!cardName.isEmpty()) {
                viewModel.setSelectedCard(cardName);
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Vui lòng nhập tên thẻ", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    public void saveTransaction() {
        String amountString = amountEditText.getText().toString().trim();
        String note = noteEditText.getText().toString().trim();

        if (amountString.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập số tiền", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountString);
        GiaoDich giaoDich = new GiaoDich(amount, accountName.getText().toString(), categoryName.getText().toString(), getCurrentDate(), note, transactionType, null);

        viewModel.addTransaction(giaoDich);
        viewModel.getGiaoDich().observe(this, giaoDich1 -> {
            if (giaoDich1 != null) {
                Toast.makeText(AddTransactionActivity.this, "Giao dịch đã được lưu", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Toast.makeText(AddTransactionActivity.this, "Lỗi: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDateTextViews() {
        Calendar calendar = Calendar.getInstance();
        todayDate.setText("Hôm nay: " + getCurrentDate());
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        yesterdayDate.setText("Hôm qua: " + getCurrentDate(calendar));
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        twoDaysAgoDate.setText("2 ngày trước: " + getCurrentDate(calendar));
    }

    private String getCurrentDate() {
        return getCurrentDate(Calendar.getInstance());
    }

    private String getCurrentDate(Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
    }
}