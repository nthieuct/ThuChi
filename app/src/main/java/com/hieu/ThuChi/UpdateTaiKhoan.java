package com.hieu.ThuChi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hieu.ThuChi.model.TaiKhoan;

public class UpdateTaiKhoan extends AppCompatActivity {
    EditText edt_ten_tai_khoan;
    Button btn_update_tai_khoan, btn_close_update_tai_khoan;
    FirebaseFirestore db;
    TaiKhoan taiKhoan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_tai_khoan);
        edt_ten_tai_khoan = findViewById(R.id.edt_ten_tai_khoan);
        btn_update_tai_khoan = findViewById(R.id.btn_update_tai_khoan);
        btn_close_update_tai_khoan = findViewById(R.id.btn_close_update_tai_khoan);
        db = FirebaseFirestore.getInstance();
        final String id_tai_khoan = getIntent().getStringExtra("id");

        DocumentReference document = db.collection("TaiKhoan").document(id_tai_khoan);
        document.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        taiKhoan = document.toObject(TaiKhoan.class);
                        edt_ten_tai_khoan.setText(taiKhoan.getTenTaiKhoan().toString());
                    } else {
                        Log.d("Cập nhật tài khoản: ", "Không có tài khoản");
                    }
                } else {
                    Log.d("Cập nhật tài khoản: ", "Lỗi đọc tài khoản", task.getException());
                }
            }
        });

        btn_close_update_tai_khoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_update_tai_khoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("TaiKhoan").document(id_tai_khoan)
                        .update("tenTaiKhoan", edt_ten_tai_khoan.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Đã cập nhật thông tin tài khoản thành công", Toast.LENGTH_LONG).show();
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
    }
}
