package com.hieu.ThuChi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hieu.ThuChi.R;
import com.hieu.ThuChi.model.TaiKhoan;

import java.util.List;

public class TaiKhoanAdapter extends ArrayAdapter<TaiKhoan> {
    private Context context;
    private int resoure;
    private List<TaiKhoan> listTaiKhoan;

    public TaiKhoanAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<TaiKhoan> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resoure = resource;
        this.listTaiKhoan = objects;
    }

    public class ViewHolder {
        private TextView txt_ten_tai_khoan;
        private ImageView img_hinh;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_tai_khoan, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txt_ten_tai_khoan = (TextView) convertView.findViewById(R.id.txt_ten_tai_khoan);
            viewHolder.img_hinh = (ImageView) convertView.findViewById(R.id.img_hinh);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        TaiKhoan taiKhoan = listTaiKhoan.get(position);
        viewHolder.txt_ten_tai_khoan.setText(taiKhoan.getTenTaiKhoan());
        viewHolder.img_hinh.setImageDrawable(context.getResources().getDrawable(R.drawable.account01));
        return convertView;
    }
}
