package com.example.lehoanggiang.traicaythanhsang.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;

import com.example.lehoanggiang.traicaythanhsang.R;
import com.example.lehoanggiang.traicaythanhsang.ultil.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends Activity {

    EditText edtUsername, edtPassword;
    Button btnLogin, btnGotoRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnGotoRegister = (Button) findViewById(R.id.btnGotoRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                if (username.equals("") || password.equals("")) {
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    new LoginUser().execute(username, password);
                }
            }
        });
    }

    // Nút "Đăng ký"
    public void ClickGotoRegister(View view) {
        Intent intent = new Intent(LoginActivity.this, Register.class);
        startActivity(intent);
    }

    private class LoginUser extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setMessage("Đang đăng nhập...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(Server.DuongdanDangNhap);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                String postData = "email=" + params[0] + "&matkhau=" + params[1];
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(postData);
                writer.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                writer.close();
                reader.close();

                return result.toString().trim();

            } catch (Exception e) {
                return "error: " + e.getMessage();
            }
        }
        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();

            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString("status");

                if (status.equalsIgnoreCase("success")) {
                    String tenkhachhang = jsonObject.getString("tenkhachhang");
                    String email = jsonObject.getString("email");
                    String matkhau = jsonObject.getString("matkhau");

                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                    // Lưu thông tin vào SharedPreferences
                    SharedPreferences preferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("isLoggedIn", true);
                    editor.putString("tenkhachhang", tenkhachhang);
                    editor.putString("email", email);
                    editor.putString("matkhau", matkhau);
                    editor.apply();

                    // Chuyển sang MainActivity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    String message = jsonObject.has("message") ? jsonObject.getString("message") : "Đăng nhập thất bại";
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                Toast.makeText(LoginActivity.this, "Lỗi đăng nhập: " + result, Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }

    }
}
