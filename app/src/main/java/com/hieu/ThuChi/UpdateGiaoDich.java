package com.hieu.ThuChi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hieu.ThuChi.model.GiaoDich;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class UpdateGiaoDich extends AppCompatActivity {
    EditText edt_ten_giao_dich, edt_so_tien, edt_ngay_giao_dich;
    RadioGroup radioGroup;
    RadioButton rdoThu, rdoChi;
    Button btn_update, btn_close;
    FirebaseFirestore db;
    String id_tai_khoan, id_giao_dich;
    Calendar calendar;
    SimpleDateFormat dinhdangngay;
    GiaoDich giaoDich;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_giao_dich);

        db = FirebaseFirestore.getInstance();
        edt_ten_giao_dich = findViewById(R.id.edt_ten_giao_dich);
        edt_so_tien = findViewById(R.id.edt_so_tien);
        edt_ngay_giao_dich = findViewById(R.id.edt_ngay_giao_dich);
        radioGroup = findViewById(R.id.radioGroup);
        rdoThu = findViewById(R.id.rdoThu);
        rdoChi = findViewById(R.id.rdoChi);
        btn_update = findViewById(R.id.btn_update_giao_dich);
        btn_close = findViewById(R.id.btn_close_update_giao_dich);
        calendar = Calendar.getInstance();
        dinhdangngay = new SimpleDateFormat("dd/MM/yyyy");

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
                new DatePickerDialog(UpdateGiaoDich.this, dateHT, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        id_tai_khoan = getIntent().getStringExtra("id_tai_khoan");
        id_giao_dich = getIntent().getStringExtra("id_giao_dich");

        DocumentReference document = db.collection("TaiKhoan").document(id_tai_khoan).collection("GiaoDich").document(id_giao_dich);
        document.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        DecimalFormat decim = new DecimalFormat("####", new DecimalFormatSymbols(Locale.US));

                        giaoDich = document.toObject(GiaoDich.class);
                        edt_ten_giao_dich.setText(giaoDich.getTenGiaoDich());
                        edt_so_tien.setText(decim.format(giaoDich.getSoTien()));
                        edt_ngay_giao_dich.setText(giaoDich.getNgayGiaoDich());

                        if (giaoDich.getLoaiGiaoDich() == 1)
                            rdoThu.setChecked(true);
                        else
                            rdoChi.setChecked(true);
                    } else {
                        Log.d("Cập nhật giao dịch: ", "Không có giao dịch");
                    }
                } else {
                    Log.d("Cập nhật giao dịch: ", "Lỗi đọc giao dịch", task.getException());
                }
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int lgd;
                if (radioGroup.getCheckedRadioButtonId() == R.id.rdoThu)
                    lgd = 1;
                else
                    lgd = 0;

                db.collection("TaiKhoan").document(id_tai_khoan).collection("GiaoDich").document(id_giao_dich)
                        .update("tenGiaoDich", edt_ten_giao_dich.getText().toString(),
                                "soTien", Float.parseFloat(edt_so_tien.getText().toString()),
                                "ngayGiaoDich", edt_ngay_giao_dich.getText().toString(),
                                "loaiGiaoDich", lgd)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Đã cập nhật thông tin giao dịch thành công", Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Đã xảy ra lỗi khi cập nhật: " + e.toString(), Toast.LENGTH_LONG).show();
                            }
                        });
                finish();
            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
