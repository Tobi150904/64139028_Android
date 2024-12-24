package vn.ngoviethoang.duancuoiky;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Fragment_ThuNhap extends Fragment {
    private int selectedCategoryId = -1; // Variable to store selected category ID
    private ImageView lastSelectedCategoryIcon;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thu_nhap, container, false);

        // Add click listeners to category icons
        GridLayout categoryContainer = view.findViewById(R.id.category_container);
        for (int i = 0; i < categoryContainer.getChildCount(); i++) {
            LinearLayout categoryLayout = (LinearLayout) categoryContainer.getChildAt(i);
            ImageView categoryIcon = (ImageView) categoryLayout.getChildAt(0);
            int categoryId = i; // Example category ID, replace with actual logic

            categoryIcon.setOnClickListener(v -> {
                selectedCategoryId = categoryId;
                highlightSelectedCategoryIcon(categoryIcon);
            });
        }

        return view;
    }

    private void highlightSelectedCategoryIcon(ImageView selectedIcon) {
        if (lastSelectedCategoryIcon != null) {
            lastSelectedCategoryIcon.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        }
        selectedIcon.setBackgroundColor(getResources().getColor(R.color.Gray));
        lastSelectedCategoryIcon = selectedIcon;
    }

    public int getSelectedCategoryId() {
        return selectedCategoryId;
    }
}