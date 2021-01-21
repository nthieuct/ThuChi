package com.hieu.ThuChi;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hieu.ThuChi.adapter.GiaoDichAdapter;
import com.hieu.ThuChi.model.TaiKhoan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GiaoDichActivity extends AppCompatActivity {
    ArrayList<com.hieu.ThuChi.model.GiaoDich> listGiaoDiches = new ArrayList<com.hieu.ThuChi.model.GiaoDich>();
    GiaoDichAdapter giaoDichAdapter;
    ListView lst_giao_dich;
    Button btn_add_giao_dich;
    String id_tai_khoan, id_giao_dich;
    FirebaseFirestore db;
    TextView txt_ten_tai_khoan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giao_dich);
        db = FirebaseFirestore.getInstance();

        lst_giao_dich = findViewById(R.id.lst_giao_dich);
        btn_add_giao_dich = findViewById(R.id.btn_add_giao_dich);
        giaoDichAdapter = new GiaoDichAdapter(this, R.layout.item_giao_dich, listGiaoDiches);
        lst_giao_dich.setAdapter(giaoDichAdapter);

        id_tai_khoan = getIntent().getStringExtra("id");

        txt_ten_tai_khoan = findViewById(R.id.txt_ten_tai_khoan);
        DocumentReference document = db.collection("TaiKhoan").document(id_tai_khoan);
        document.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        TaiKhoan taiKhoan = document.toObject(TaiKhoan.class);
                        txt_ten_tai_khoan.setText(taiKhoan.getTenTaiKhoan());
                    } else {
                        Log.d("Giao dịch: ", "Không có tài khoản");
                    }
                } else {
                    Log.d("Giao dịch: ", "Lỗi đọc tài khoản", task.getException());
                }
            }
        });

        btn_add_giao_dich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listGiaoDiches.size() == 0) {
                    id_giao_dich = "1";
                } else {
                    id_giao_dich = String.valueOf(Integer.valueOf(listGiaoDiches.get(listGiaoDiches.size() - 1).getId()) + 1);
                }
                Intent add_giao_dich = new Intent(getApplicationContext(), AddGiaoDich.class);
                add_giao_dich.putExtra("id_tai_khoan", "/TaiKhoan/" + id_tai_khoan + "/GiaoDich");
                add_giao_dich.putExtra("id_giao_dich", id_giao_dich);
                startActivity(add_giao_dich);
            }
        });

        lst_giao_dich.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Integer id = i;
                final Dialog dialog = new Dialog(GiaoDichActivity.this);
                dialog.setContentView(R.layout.menu_giao_dich);
                Button btn_sua = dialog.findViewById(R.id.btn_sua);
                Button btn_xoa = dialog.findViewById(R.id.btn_xoa);
                Button btn_dong = dialog.findViewById(R.id.btn_dong);
                btn_xoa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.collection("/TaiKhoan/" + id_tai_khoan + "/GiaoDich").document(listGiaoDiches.get(id).getId())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(GiaoDichActivity.this, "Đã xóa giao dịch thành công", Toast.LENGTH_LONG).show();
                                        load_giao_dich();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(GiaoDichActivity.this, "Lỗi khi xóa giao dịch: " + e.toString(), Toast.LENGTH_LONG).show();
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

                        Intent update_giao_dich = new Intent(GiaoDichActivity.this, UpdateGiaoDich.class);
                        update_giao_dich.putExtra("id_giao_dich", listGiaoDiches.get(id).getId());
                        update_giao_dich.putExtra("id_tai_khoan", id_tai_khoan);
                        startActivity(update_giao_dich);
                        dialog.cancel();
                    }
                });
                dialog.show();
                return false;
            }
        });
    }

    public void load_giao_dich() {
        listGiaoDiches.clear();

        db.collection("/TaiKhoan/" + id_tai_khoan + "/GiaoDich")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            float tienHienTai = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                com.hieu.ThuChi.model.GiaoDich giaoDich = document.toObject(com.hieu.ThuChi.model.GiaoDich.class);
                                giaoDich.setId(document.getId());
                                giaoDich.setSoTienTruoc(tienHienTai);
                                if (giaoDich.getLoaiGiaoDich() == 1) {
                                    tienHienTai = tienHienTai + giaoDich.getSoTien();
                                    giaoDich.setSoTienSau(tienHienTai);
                                } else {
                                    tienHienTai = tienHienTai - giaoDich.getSoTien();
                                    giaoDich.setSoTienSau(tienHienTai);
                                }

                                listGiaoDiches.add(giaoDich);
                            }
                            giaoDichAdapter.notifyDataSetChanged();
                        } else {
                            Log.w("TAG", "Error getting documents", task.getException());
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        load_giao_dich();
    }
}
