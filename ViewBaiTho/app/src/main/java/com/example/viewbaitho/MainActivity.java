package com.example.viewbaitho;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // Nguon du lieu
    ArrayList<String> dsBaiTho;
    ArrayAdapter<String> adapterBaiTho;    // Đem từ OnCreate ra
    //
    ListView lvBaiTho;
    Button btnThemBaiTho;
    EditText editTextTenBaiTho;
    void getControl() {
        lvBaiTho = findViewById(R.id.lvBT);
        btnThemBaiTho = findViewById(R.id.btnThem);
        editTextTenBaiTho = findViewById(R.id.edtTenBT);
    }
    // Ham doc du lieu tu mot nguon bat ky
    ArrayList<String> getData()
    {
        ArrayList<String> lstTenBaiTho= new ArrayList<String>();
        lstTenBaiTho.add("Bánh trôi nước");
        lstTenBaiTho.add("Sóng");
        lstTenBaiTho.add("Đất nước");
        lstTenBaiTho.add("Tây Tiến");
        return lstTenBaiTho;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getControl();
        dsBaiTho = getData();

        adapterBaiTho = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                dsBaiTho
        );
        lvBaiTho.setAdapter(adapterBaiTho);

        lvBaiTho.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                XuLyKhiNhanVaoViTriThu(position);
                //Ta đưa code xử lý ra ngoài cho gọn
            }

        });
        btnThemBaiTho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy tên bài
                String tenBai = editTextTenBaiTho.getText().toString();
                //Thêm vào nguồn
                dsBaiTho.add(tenBai);
                // Báo adapter cập nhật lại ListView
                adapterBaiTho.notifyDataSetChanged();
            }
        });

    }// end Create
    void XuLyKhiNhanVaoViTriThu(int pos) {
        // Lay phan tu
        String item = dsBaiTho.get(pos);
        Toast.makeText(getApplicationContext(),item, Toast.LENGTH_SHORT).show();
    }
}