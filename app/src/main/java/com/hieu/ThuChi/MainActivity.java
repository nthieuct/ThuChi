package com.hieu.ThuChi;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hieu.ThuChi.adapter.TaiKhoanAdapter;
import com.hieu.ThuChi.model.GiaoDich;
import com.hieu.ThuChi.model.TaiKhoan;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<TaiKhoan> listTaiKhoan = new ArrayList<TaiKhoan>();
    TaiKhoanAdapter taiKhoanAdapter;
    ListView lst_tai_khoan;
    Button btn_add_tai_khoan;
    String id_tai_khoan;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();

        lst_tai_khoan = findViewById(R.id.lst_tai_khoan);
        btn_add_tai_khoan = findViewById(R.id.btn_add_tai_khoan);
        taiKhoanAdapter = new TaiKhoanAdapter(this, R.layout.item_tai_khoan, listTaiKhoan);
        lst_tai_khoan.setAdapter(taiKhoanAdapter);

        btn_add_tai_khoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listTaiKhoan.size() == 0) {
                    id_tai_khoan = "1";
                } else {
                    id_tai_khoan = String.valueOf(Integer.valueOf(listTaiKhoan.get(listTaiKhoan.size() - 1).getId()) + 1);
                }
                Intent add_tai_khoan = new Intent(getApplicationContext(), AddTaiKhoan.class);
                add_tai_khoan.putExtra("id", id_tai_khoan);
                startActivity(add_tai_khoan);
            }
        });
        lst_tai_khoan.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Integer id = i;
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.menu_tai_khoan);
                Button btn_sua = dialog.findViewById(R.id.btn_sua);
                Button btn_xoa = dialog.findViewById(R.id.btn_xoa);
                Button btn_dong = dialog.findViewById(R.id.btn_dong);
                Button btn_giaodich = dialog.findViewById(R.id.btn_giao_dich);
                btn_xoa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.collection("/TaiKhoan").document(listTaiKhoan.get(id).getId())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(MainActivity.this, "Đã xóa tài khoản thành công", Toast.LENGTH_LONG).show();
                                        load_tai_khoan();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MainActivity.this, "Lỗi khi xóa tài khoản: " + e.toString(), Toast.LENGTH_LONG).show();
                                    }
                                });
                        dialog.cancel();
                    }
                });
                btn_dong.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                btn_sua.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent update_tai_khoan = new Intent(MainActivity.this, UpdateTaiKhoan.class);
                        update_tai_khoan.putExtra("id", listTaiKhoan.get(id).getId());
                        startActivity(update_tai_khoan);
                        dialog.cancel();
                    }
                });
                btn_giaodich.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent giaodich = new Intent(MainActivity.this, GiaoDichActivity.class);
                        giaodich.putExtra("id", listTaiKhoan.get(id).getId());
                        startActivity(giaodich);
                        dialog.cancel();
                    }
                });
                dialog.show();
                return false;
            }
        });
    }

    public void load_tai_khoan() {
        listTaiKhoan.clear();
        db.collection("/TaiKhoan")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                TaiKhoan taiKhoan = document.toObject(TaiKhoan.class);
                                taiKhoan.setId(document.getId());
                                listTaiKhoan.add(taiKhoan);
                            }
                            taiKhoanAdapter.notifyDataSetChanged();
                        } else {
                            Log.w("TAG", "Error getting documents", task.getException());
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        load_tai_khoan();
    }
}
