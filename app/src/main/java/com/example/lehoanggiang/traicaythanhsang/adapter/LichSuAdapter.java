package com.example.lehoanggiang.traicaythanhsang.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lehoanggiang.traicaythanhsang.R;
import com.example.lehoanggiang.traicaythanhsang.model.LichSu;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class LichSuAdapter extends BaseAdapter {
    Context context;
    ArrayList<LichSu> arrayLichSu;

    public LichSuAdapter(Context context, int layout, ArrayList<LichSu> arrayLichSu) {
        this.context = context;
        this.arrayLichSu = arrayLichSu;
    }


    @Override
    public int getCount() {
        return arrayLichSu.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayLichSu.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private static class ViewHolder {
        TextView txtNgayDat, txtTenSanPham, txtGiaSanPham, txtSoLuong, txtTongTien;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        if(view == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dong_lichsu, null);

            viewHolder.txtNgayDat = (TextView) view.findViewById(R.id.txtNgayDat);
            viewHolder.txtTenSanPham = (TextView) view.findViewById(R.id.txtTenSanPham);
            viewHolder.txtGiaSanPham = (TextView) view.findViewById(R.id.txtGiaSanPham);
            viewHolder.txtSoLuong = (TextView) view.findViewById(R.id.txtSoLuong);
            viewHolder.txtTongTien = (TextView) view.findViewById(R.id.txtTongTien);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        LichSu lichSu = (LichSu) getItem(i);

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

        viewHolder.txtNgayDat.setText("Ngày đặt: " + lichSu.getNgaydat());
        viewHolder.txtTenSanPham.setText("Sản phẩm: " + lichSu.getTensanpham());
        viewHolder.txtGiaSanPham.setText("Giá: " + decimalFormat.format(lichSu.getGiasanpham()) + "Đ");
        viewHolder.txtSoLuong.setText("Số lượng: " + lichSu.getSoluong());
        viewHolder.txtTongTien.setText("Tổng tiền: " + decimalFormat.format(lichSu.getTongtien()) + "Đ");

        return view;
    }
}
