package com.example.lehoanggiang.traicaythanhsang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lehoanggiang.traicaythanhsang.R;
import com.example.lehoanggiang.traicaythanhsang.ultil.Server;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    EditText edtTenKhachHang, edtEmail, edtMatKhau, edtXacNhanMK;
    Button btnDangKy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtTenKhachHang = (EditText) findViewById(R.id.edtUsername);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtMatKhau = (EditText) findViewById(R.id.edtPassword);
        edtXacNhanMK = (EditText) findViewById(R.id.edtConfirmPassword);
        btnDangKy = (Button) findViewById(R.id.btnRegister);

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        final String tenKhachHang = edtTenKhachHang.getText().toString().trim();
        final String email = edtEmail.getText().toString().trim();
        final String matKhau = edtMatKhau.getText().toString().trim();
        final String confirmMatKhau = edtXacNhanMK.getText().toString().trim();

        if (tenKhachHang.isEmpty() || email.isEmpty() || matKhau.isEmpty() || confirmMatKhau.isEmpty()) {
            Toast.makeText(Register.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!email.matches("^[a-zA-Z0-9._%+-]+@gmail\\.com$")) {
            Toast.makeText(Register.this, "Email phải có định dạng @gmail.com", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!matKhau.equals(confirmMatKhau)) {
            Toast.makeText(Register.this, "Mật khẩu xác nhận không khớp!", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = Server.DuongdanDangKy;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        response = response.trim();
                        switch (response) {
                            case "SUCCESS":
                                Toast.makeText(Register.this, "Đăng ký thành công! Mời bạn đăng nhập.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Register.this, LoginActivity.class));
                                finish();
                                break;
                            case "EXISTS":
                                Toast.makeText(Register.this, "Email đã tồn tại. Vui lòng dùng email khác!", Toast.LENGTH_SHORT).show();
                                break;
                            case "EMAIL_INVALID":
                                Toast.makeText(Register.this, "Email không hợp lệ! Chỉ chấp nhận @gmail.com.", Toast.LENGTH_SHORT).show();
                                break;
                            case "PASSWORD_MISMATCH":
                                Toast.makeText(Register.this, "Mật khẩu xác nhận không khớp!", Toast.LENGTH_SHORT).show();
                                break;
                            case "EMPTY":
                                Toast.makeText(Register.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(Register.this, "Đăng ký thất bại. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Register.this, "Lỗi kết nối! Vui lòng kiểm tra mạng.", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tenkhachhang", tenKhachHang);
                params.put("email", email);
                params.put("matkhau", matKhau);
                params.put("confirm_password", confirmMatKhau);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}
