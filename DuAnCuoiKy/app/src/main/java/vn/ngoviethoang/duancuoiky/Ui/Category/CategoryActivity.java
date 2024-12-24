package vn.ngoviethoang.duancuoiky.Ui.Category;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import vn.ngoviethoang.duancuoiky.Fragment_ChiPhi;
import vn.ngoviethoang.duancuoiky.Fragment_ThuNhap;
import vn.ngoviethoang.duancuoiky.R;

public class CategoryActivity extends AppCompatActivity {

    private TextView tabExpense, tabIncome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        tabExpense = findViewById(R.id.tab_expense);
        tabIncome = findViewById(R.id.tab_income);

        replaceFragment(new Fragment_ChiPhi());

        tabExpense.setOnClickListener(v -> replaceFragment(new Fragment_ChiPhi()));
        tabIncome.setOnClickListener(v -> replaceFragment(new Fragment_ThuNhap()));
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}