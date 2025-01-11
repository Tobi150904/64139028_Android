package vn.ngoviethoang.duancuoiky.Ui.Account;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import java.util.List;
import vn.ngoviethoang.duancuoiky.R;
import vn.ngoviethoang.duancuoiky.data.entity.GiaoDich;
import vn.ngoviethoang.duancuoiky.data.entity.TaiKhoan;

public class TransferActivity extends AppCompatActivity {

    private AccountViewModel accountViewModel;
    private LinearLayout transferListLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        transferListLayout = findViewById(R.id.transfer_list);

        accountViewModel.getAccounts().observe(this, this::updateAccountsUI);
        accountViewModel.loadAccounts();
    }

    private void updateAccountsUI(List<TaiKhoan> accounts) {
        transferListLayout.removeAllViews();
        for (TaiKhoan account : accounts) {
            LinearLayout accountLayout = new LinearLayout(this);
            accountLayout.setOrientation(LinearLayout.HORIZONTAL);
            accountLayout.setPadding(10, 10, 10, 10);
            accountLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            ImageView accountIcon = new ImageView(this);
            Bitmap bitmap = BitmapFactory.decodeByteArray(account.getIcon(), 0, account.getIcon().length);
            accountIcon.setImageBitmap(bitmap);
            LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(64, 64);
            iconParams.setMargins(0, 0, 16, 0);
            accountIcon.setLayoutParams(iconParams);
            accountIcon.setContentDescription("Account Icon");

            LinearLayout textContainer = new LinearLayout(this);
            textContainer.setOrientation(LinearLayout.VERTICAL);
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

            accountLayout.addView(accountIcon);
            accountLayout.addView(textContainer);

            transferListLayout.addView(accountLayout);
        }
    }
}