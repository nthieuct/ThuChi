package com.hieu.ThuChi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hieu.ThuChi.R;
import com.hieu.ThuChi.model.GiaoDich;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class GiaoDichAdapter extends ArrayAdapter<GiaoDich> {
    private Context context;
    private int resoure;
    private List<GiaoDich> listGiaoDiches;

    public GiaoDichAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<GiaoDich> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resoure = resource;
        this.listGiaoDiches = objects;
    }

    public class ViewHolder {
        private TextView txt_ten_giao_dich;
        private TextView txt_so_tien;
        private TextView txt_so_tien_truoc;
        private TextView txt_so_tien_sau;
        private TextView txt_ngay_giao_dich;
        private TextView txt_loai_giao_dich;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        GiaoDichAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_giao_dich, parent, false);
            viewHolder = new GiaoDichAdapter.ViewHolder();
            viewHolder.txt_ten_giao_dich = (TextView) convertView.findViewById(R.id.txt_ten_giao_dich);
            viewHolder.txt_so_tien = (TextView) convertView.findViewById(R.id.txt_so_tien);
            viewHolder.txt_so_tien_truoc = (TextView) convertView.findViewById(R.id.txt_so_tien_truoc);
            viewHolder.txt_so_tien_sau = (TextView) convertView.findViewById(R.id.txt_so_tien_sau);
            viewHolder.txt_ngay_giao_dich = (TextView) convertView.findViewById(R.id.txt_ngay_giao_dich);
            viewHolder.txt_loai_giao_dich = (TextView) convertView.findViewById(R.id.txt_loai_giao_dich);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GiaoDichAdapter.ViewHolder) convertView.getTag();
        }

        GiaoDich giaoDich = listGiaoDiches.get(position);
        int loaiGiaoDich = giaoDich.getLoaiGiaoDich();
        DecimalFormat decim = new DecimalFormat("#,###", new DecimalFormatSymbols(Locale.ITALY));

        viewHolder.txt_ten_giao_dich.setText(giaoDich.getTenGiaoDich());
        viewHolder.txt_so_tien.setText("GD: " + decim.format(giaoDich.getSoTien()));
        viewHolder.txt_so_tien_truoc.setText("Trước GD: " + decim.format(giaoDich.getSoTienTruoc()));
        viewHolder.txt_so_tien_sau.setText("Sau GD: " + decim.format(giaoDich.getSoTienSau()));
        viewHolder.txt_ngay_giao_dich.setText(giaoDich.getNgayGiaoDich());

        if (loaiGiaoDich == 1)
            viewHolder.txt_loai_giao_dich.setText("thu");
        else
            viewHolder.txt_loai_giao_dich.setText("chi");

        return convertView;
    }
}
