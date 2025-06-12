package com.example.lehoanggiang.traicaythanhsang.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lehoanggiang.traicaythanhsang.R;
import com.example.lehoanggiang.traicaythanhsang.adapter.LichSuAdapter;
import com.example.lehoanggiang.traicaythanhsang.model.LichSu;
import com.example.lehoanggiang.traicaythanhsang.ultil.CheckConnection;
import com.example.lehoanggiang.traicaythanhsang.ultil.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LichSuActivity extends AppCompatActivity {
    Toolbar toolbarLichSu;
    ListView lvLichSu;
    LichSuAdapter lichSuAdapter;
    ArrayList<LichSu> mangLichSu;
    int page = 1;
    View footerview;
    boolean isLoading = false;
    boolean limitadata = false;
    mHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_su);
        Anhxa();
        if (CheckConnection.isInternetAvailable(getApplicationContext())) {
            ActionToolbar();
            GetData(page);
            LoadMoreData();
        } else {
            CheckConnection.ShowToast_Short(getApplicationContext(), "Bạn hãy kiểm tra lại Internet");
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menugiohang:
                Intent intent =  new Intent(getApplicationContext(),GioHangActivity.class);
                startActivity(intent);
            case R.id.dangxuat:
                SharedPreferences preferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                Toast.makeText(LichSuActivity.this, "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();

                // Quay về màn hình đăng nhập
                Intent intent1 = new Intent(LichSuActivity.this, LoginActivity.class);
                startActivity(intent1);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void GetData(int Page) {
        if (Page == 1) {
            mangLichSu.clear(); // ✅ Xóa dữ liệu cũ khi load lại
        }

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String duongDan = Server.Duongdanlichsu;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, duongDan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null && response.length() > 2) {
                            lvLichSu.removeFooterView(footerview);
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String tenSanPham = jsonObject.getString("ten_san_pham");
                                    int giaSanPham = jsonObject.getInt("gia_san_pham");
                                    int soLuong = jsonObject.getInt("so_luong");
                                    String ngayDat = jsonObject.getString("ngay_dat");
                                    LichSu lichSu = new LichSu(ngayDat, tenSanPham, giaSanPham, soLuong);
                                    mangLichSu.add(lichSu);
                                }
                                lichSuAdapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            limitadata = true;
                            lvLichSu.removeFooterView(footerview);
                            CheckConnection.ShowToast_Short(getApplicationContext(), "Đã hết dữ liệu");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        CheckConnection.ShowToast_Short(getApplicationContext(), "Lỗi kết nối đến server");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SharedPreferences preferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
                String id_khachhang = preferences.getString("id_khachhang", "0");

                HashMap<String, String> params = new HashMap<>();
                params.put("id_khachhang", id_khachhang);
                params.put("page", String.valueOf(page)); // ✅ đưa page vào POST
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void ActionToolbar() {
        setSupportActionBar(toolbarLichSu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarLichSu.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void LoadMoreData() {
        lvLichSu.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int FirstItem, int VisibleItem, int TotalItem) {
                if (FirstItem + VisibleItem == TotalItem && TotalItem != 0 && !isLoading && !limitadata) {
                    isLoading = true;
                    ThreadData threadData = new ThreadData();
                    threadData.start();
                }
            }
        });
    }

    private void Anhxa() {
        toolbarLichSu = (Toolbar) findViewById(R.id.toolbarlichsu);
        lvLichSu = (ListView) findViewById(R.id.listviewlichsu);
        mangLichSu = new ArrayList<>();
        lichSuAdapter = new LichSuAdapter(getApplicationContext(), R.layout.dong_lichsu, mangLichSu); // ✅ đảm bảo truyền đúng layout ID
        lvLichSu.setAdapter(lichSuAdapter);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerview = inflater.inflate(R.layout.progressbar, null);
        mHandler = new mHandler();
    }

    public class mHandler extends android.os.Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    lvLichSu.addFooterView(footerview);
                    break;
                case 1:
                    GetData(++page);
                    isLoading = false;
                    break;
            }
            super.handleMessage(msg);
        }
    }

    public class ThreadData extends Thread {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(0);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message = mHandler.obtainMessage(1);
            mHandler.sendMessage(message);
            super.run();
        }
    }
}
