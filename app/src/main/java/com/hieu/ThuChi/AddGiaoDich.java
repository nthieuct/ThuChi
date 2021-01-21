package com.hieu.ThuChi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddGiaoDich extends AppCompatActivity {
    EditText edt_ten_giao_dich, edt_so_tien, edt_ngay_giao_dich;
    RadioGroup radioGroup;
    Button btn_add, btn_close;
    FirebaseFirestore db;
    String id_tai_khoan, id_giao_dich;
    Calendar calendar;
    SimpleDateFormat dinhdangngay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_giao_dich);
        db = FirebaseFirestore.getInstance();
        edt_ten_giao_dich = findViewById(R.id.edt_ten_giao_dich);
        edt_so_tien = findViewById(R.id.edt_so_tien);
        edt_ngay_giao_dich = findViewById(R.id.edt_ngay_giao_dich);
        radioGroup = findViewById(R.id.radioGroup);
        btn_add = findViewById(R.id.btn_add_giao_dich);
        btn_close = findViewById(R.id.btn_close_add_giao_dich);
        calendar = Calendar.getInstance();
        dinhdangngay = new SimpleDateFormat("dd/MM/yyyy");
        edt_ngay_giao_dich.setText(dinhdangngay.format(calendar.getTime()));
        final DatePickerDialog.OnDateSetListener dateHT = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                edt_ngay_giao_dich.setText(dinhdangngay.format(calendar.getTime()));
            }
        };
        edt_ngay_giao_dich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddGiaoDich.this, dateHT, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        id_tai_khoan = getIntent().getStringExtra("id_tai_khoan");
        id_giao_dich = getIntent().getStringExtra("id_giao_dich");
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> giao_dich = new HashMap<>();
                giao_dich.put("id", id_giao_dich);
                giao_dich.put("tenGiaoDich", edt_ten_giao_dich.getText().toString());
                giao_dich.put("soTien", Float.parseFloat(edt_so_tien.getText().toString()));
                giao_dich.put("ngayGiaoDich", edt_ngay_giao_dich.getText().toString());

                if (radioGroup.getCheckedRadioButtonId() == R.id.rdoThu)
                    giao_dich.put("loaiGiaoDich", 1);
                else
                    giao_dich.put("loaiGiaoDich", 0);

                db.collection(id_tai_khoan).document(id_giao_dich).set(giao_dich)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Đã thêm mới giao dịch thành công", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Đã xảy ra lỗi khi thêm mới giao dịch: ", Toast.LENGTH_LONG).show();
                            }
                        });
                finish();
            }
        });
    }
}
