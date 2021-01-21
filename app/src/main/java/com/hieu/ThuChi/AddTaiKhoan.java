package com.hieu.ThuChi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddTaiKhoan extends AppCompatActivity {
    EditText edt_ten_tai_khoan;
    Button btn_add_tai_khoan, btn_close_add_tai_khoan;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tai_khoan);
        edt_ten_tai_khoan = findViewById(R.id.edt_ten_tai_khoan);
        btn_add_tai_khoan = findViewById(R.id.btn_add_tai_khoan);
        btn_close_add_tai_khoan = findViewById(R.id.btn_close_add_tai_khoan);
        db = FirebaseFirestore.getInstance();
        final String id_tai_khoan = getIntent().getStringExtra("id");
        btn_close_add_tai_khoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_add_tai_khoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> taiKhoan = new HashMap<>();
                taiKhoan.put("id", id_tai_khoan);
                taiKhoan.put("tenTaiKhoan", edt_ten_tai_khoan.getText().toString());

                db.collection("TaiKhoan").document(id_tai_khoan).set(taiKhoan)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Đã thêm tài khoản mới thành công", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Đã xảy ra lỗi khi thêm tài khoản mới", Toast.LENGTH_LONG).show();
                            }
                        });
                finish();
            }
        });
    }
}
