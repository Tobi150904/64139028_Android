package vn.ngoviethoang.duancuoiky.Ui.Category;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import vn.ngoviethoang.duancuoiky.R;
import vn.ngoviethoang.duancuoiky.Ui.Account.AccountActivity;
import vn.ngoviethoang.duancuoiky.Ui.Dashboard.DashboardActivity;
import vn.ngoviethoang.duancuoiky.Ui.Transaction.TransactionDetailActivity;
import vn.ngoviethoang.duancuoiky.data.entity.DanhMuc;
import vn.ngoviethoang.duancuoiky.data.repository.DanhMucRepository;

public class CategoryActivity extends AppCompatActivity {
    private TextView tabExpense, tabIncome;
    private ImageView iconMenu;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private GridLayout categoryContainer;
    private DanhMucRepository danhMucRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        danhMucRepository = new DanhMucRepository(this);
        danhMucRepository.insertPredefinedCategories(this);

        tabExpense = findViewById(R.id.tab_expense);
        tabIncome = findViewById(R.id.tab_income);
        iconMenu = findViewById(R.id.ic_menu);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        categoryContainer = findViewById(R.id.category_container);

        loadCategories("chi_phi");

        tabExpense.setOnClickListener(v -> loadCategories("chi_phi"));
        tabIncome.setOnClickListener(v -> loadCategories("thu_nhap"));
        iconMenu.setOnClickListener(v -> drawerLayout.openDrawer(navigationView));

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
                startActivity(new Intent(this, TransactionDetailActivity.class));
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

    private void loadCategories(String loai) {
        danhMucRepository.getDanhMucByLoai(loai).observe(this, danhMucList -> {
            categoryContainer.removeAllViews();
            for (DanhMuc danhMuc : danhMucList) {
                LinearLayout categoryLayout = createCategoryLayout(danhMuc);
                categoryContainer.addView(categoryLayout);
            }
        });
    }

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
        circleDrawable.setColor(Color.parseColor(danhMuc.getMauSac())); // Đặt màu nền từ danhMuc
        categoryIcon.setBackground(circleDrawable);
        categoryIcon.setClipToOutline(true);

        TextView categoryName = new TextView(this);
        categoryName.setText(danhMuc.getTenDanhMuc());
        categoryName.setTextSize(14);
        categoryName.setTextColor(getResources().getColor(R.color.Black));
        categoryName.setGravity(Gravity.CENTER);

        categoryLayout.addView(categoryIcon);
        categoryLayout.addView(categoryName);
        categoryLayout.setOnClickListener(v -> highlightSelectedCategory(categoryLayout));

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
}